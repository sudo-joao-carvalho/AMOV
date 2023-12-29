package pt.isec.ans.amov.ui.Screens

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.firestore.GeoPoint
import pt.isec.ans.amov.R
import pt.isec.ans.amov.ui.Components.Buttons.FilterField
import pt.isec.ans.amov.ui.Components.Buttons.FilterFields
import pt.isec.ans.amov.ui.Components.Buttons.GradientButton
import pt.isec.ans.amov.ui.Components.Buttons.RoundIconButton
import pt.isec.ans.amov.ui.Components.Buttons.SearchDropdownButton
import pt.isec.ans.amov.ui.Components.OutlinedInput
import pt.isec.ans.amov.ui.ViewModels.FireBaseViewModel
import pt.isec.ans.amov.ui.ViewModels.LocationViewModel
import pt.isec.ans.amov.ui.theme.BlueHighlight
import pt.isec.ans.amov.ui.theme.BlueLighter

data class AttractionFormState(
    var name: String = "",
    var description: String = "",
    var category: String = "",
    var location: String = "",
    var latitude : String = "",
    var longitude : String = "",
    var coordinates: GeoPoint = GeoPoint(0.0,0.0), //val coordinates = GeoPoint(latitude, longitude).
    var image: List<String> = listOf(),
    var imageUri: List<Uri> = listOf()
)


@SuppressLint("SuspiciousIndentation")
@Composable
fun AddAttraction( navController: NavHostController,viewModelL: LocationViewModel, viewModelFB: FireBaseViewModel){
    var attractionFormState by remember { mutableStateOf(AttractionFormState()) }

    val context = LocalContext.current

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        //Page Column
        Column(
            modifier = Modifier
                .width(360.dp)
                .height(804.dp)
                .padding(start = 20.dp, top = 35.dp, end = 20.dp, bottom = 20.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            //Form Column + Title + Submit Button
            Column(
                modifier = Modifier
                    .width(320.dp)
                    .height(676.dp),
                verticalArrangement = Arrangement.spacedBy(40.dp, Alignment.Top),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                //Title + Cancel button
                Row(
                    modifier = Modifier
                        .width(320.dp)
                        .height(29.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {

                    Text(
                        modifier = Modifier
                            .width(248.dp)
                            .height(29.dp),
                        text = "Contribute Attraction",
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontWeight = FontWeight(600),
                            color = BlueHighlight,
                        )
                    )

                    Box(
                        modifier = Modifier.clickable {
                            navController.popBackStack()
                        }
                    ) {
                        Image(
                            modifier = Modifier
                                .padding(1.dp)
                                .width(16.dp)
                                .height(16.dp),
                            painter = painterResource(id = R.drawable.cancellationx1),
                            contentDescription = "Cancel",
                            contentScale = ContentScale.None
                        )
                    }
                }

                //Form Column
                Column(
                    modifier = Modifier
                        .width(320.dp)
                        .height(492.dp)
                        .padding(start = 10.dp, end = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(60.dp, Alignment.Top),
                    horizontalAlignment = Alignment.Start,
                ) {

                    //First 3 inputs
                    TextInputs(attractionFormState) { updatedState ->
                        attractionFormState = updatedState
                    }

                    //Second 3 inputs
                    SecondInputs(attractionFormState, viewModelFB) { updatedState ->
                        attractionFormState = updatedState
                    }

                }

                GradientButton(
                    _text = "Submit",
                    _gradient = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF0B374B),
                            Color(0xFF00B6DE)
                        )
                    ),
                ) {
                    Log.d("VERRR------>", "category: ${attractionFormState.category}")

                    try {
                        attractionFormState.coordinates = GeoPoint(attractionFormState.latitude.toDouble(), attractionFormState.longitude.toDouble())
                    } catch (e: NumberFormatException) {
                        Toast.makeText(context, "Invalid coordinates", Toast.LENGTH_SHORT).show()
                        return@GradientButton
                    }

                    var imageUrls: List<String> = listOf()

                    attractionFormState.imageUri.let { uri ->
                        // Quando o botão de registro é clicado, faz o upload da imagem
                        viewModelFB.uploadImages(uri) { imageUrl ->
                            attractionFormState.image = imageUrl

                                viewModelFB.addAtraction(
                                    attractionFormState.name,
                                    attractionFormState.description,
                                    attractionFormState.coordinates,
                                    attractionFormState.category,
                                    attractionFormState.location,
                                    attractionFormState.image
                                ) { e ->
                                    if (e == null) {
                                        Toast.makeText(context, "Attraction added", Toast.LENGTH_SHORT)
                                            .show()
                                    } else {
                                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                }
                        }
                    }
                }

            }



        }
    }
}

@Composable
fun TextInputs(attractionFormState: AttractionFormState, onAttractionFormStateChange: (AttractionFormState) -> Unit) {

    //Name + Description + Coordinates
    Column(
        modifier = Modifier
            .width(300.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.Top),
        horizontalAlignment = Alignment.Start,
    ) {

        //Name
        Row(
            modifier = Modifier
                .width(300.dp)
                .height(60.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ){


            OutlinedInput(
                _value = attractionFormState.name,
                _label = "Name",
                _iconName = R.drawable.nameicon,
                onValueChange = { newValue ->
                    attractionFormState.name = newValue
                }
            )
        }

        /*OutlinedTextField(
                value = attractionFormState.name,
                onValueChange = { attractionFormState.name = it },
                label = { Text("Name") },
                leadingIcon = {
                    Image(
                        modifier = Modifier
                            .padding(1.dp)
                            .width(18.dp)
                            .height(18.dp),
                        painter = painterResource(id = R.drawable.nameicon),
                        contentDescription = "name icon",
                        contentScale = ContentScale.None,
                    )
                }

        }*/

        //Description
        Row(
            modifier = Modifier
                .width(300.dp)
                .height(60.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ){

            OutlinedInput(
                _value = attractionFormState.description,
                _label = "Description",
                _iconName = R.drawable.descicon,
                onValueChange = { newValue ->
                    attractionFormState.description = newValue
                }
            )
            /*OutlinedTextField(
                value = attractionFormState.description,
                label = { Text("Description") },
                leadingIcon = {
                    Image(
                        modifier = Modifier
                            .padding(1.dp)
                            .width(18.dp)
                            .height(18.dp),
                        painter = painterResource(id = R.drawable.descicon),
                        contentDescription = "description icon",
                    )
                },
                onValueChange = { attractionFormState.description = it }
            )*/
        }

        //latitude
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically,
        ){

            OutlinedInput(
                _value = attractionFormState.latitude,
                _label = "Latitude",
                _iconName = R.drawable.coordsicon,
                onValueChange = { newValue ->
                    attractionFormState.latitude = newValue
                }
            )

            /*OutlinedTextField(
                modifier = Modifier.width(252.dp),
                value = attractionFormState.latitude,
                label = { Text("Coordinates") },
                leadingIcon = {
                    Image(
                        modifier = Modifier
                            .padding(1.dp)
                            .width(18.dp)
                            .height(18.dp),
                        painter = painterResource(id = R.drawable.coordsicon),
                        contentDescription = "coordinates icon",
                        contentScale = ContentScale.None
                    )
                },
                onValueChange = { attractionFormState.latitude = it },
            )*/

            //Icon container
            RoundIconButton(
                drawableId = R.drawable.vector,
                modifier = Modifier
                    .size(40.dp)
            )

        }
        //longitude
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            OutlinedInput(
                _value = attractionFormState.longitude,
                _label = "Name",
                _iconName = R.drawable.coordsicon,
                onValueChange = { newValue ->
                    attractionFormState.longitude = newValue
                }
            )

            /*OutlinedTextField(
                modifier = Modifier.width(252.dp),
                value = attractionFormState.longitude,
                label = { Text("Coordinates") },
                leadingIcon = {
                    Image(
                        modifier = Modifier
                            .padding(1.dp)
                            .width(18.dp)
                            .height(18.dp),
                        painter = painterResource(id = R.drawable.coordsicon),
                        contentDescription = "coordinates icon",
                        contentScale = ContentScale.None
                    )
                },
                onValueChange = { attractionFormState.longitude = it },
            )*/
        }
    }

}

@Composable
fun SecondInputs(attractionFormState: AttractionFormState, viewModelFB: FireBaseViewModel, onAttractionFormStateChange: (AttractionFormState) -> Unit){

    var inputValues by remember { mutableStateOf(FilterFields()) }

    var categoriesList by remember { mutableStateOf<List<String>>(emptyList()) }

    var locationList by remember { mutableStateOf<List<String>>(emptyList()) }


    viewModelFB.getAllCategories { loadedCategories ->
        categoriesList = loadedCategories
    }

    viewModelFB.getAllLocations { loadedLocations ->
        locationList = loadedLocations
    }

    val pickImagesLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetMultipleContents()) { results ->
            val uris = results.mapNotNull { it }
            attractionFormState.imageUri = uris

        }

    /*val pickImageLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                //Log.d("VERRR------>", "result: $result") // passa aqui
                result.data?.data?.let { uri ->
                    // Aqui você tem a URI da imagem selecionada
                    // Agora você pode fazer o upload para o Firestore ou atualizar o estado conforme necessário
                    attractionFormState.imageUri = uri
                }
            }
        }*/


    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.Top),
        horizontalAlignment = Alignment.Start,
    ) {

        //Category
        Row(
            modifier = Modifier
                .border(width = 1.dp, color = BlueLighter, shape = RoundedCornerShape(size = 5.dp))
                .width(300.dp)
                .height(50.dp)
                .background(color = Color(0xCCFFFFFF), shape = RoundedCornerShape(size = 5.dp))
                .padding(start = 10.dp, end = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            SearchDropdownButton(FilterField.CATEGORY, "Category", inputValues, categoriesList){ newValue ->
                attractionFormState.category = newValue
            }
        }

        //Location
        Row(
            modifier = Modifier
                .border(width = 1.dp, color = BlueLighter, shape = RoundedCornerShape(size = 5.dp))
                .width(300.dp)
                .height(50.dp)
                .background(color = Color(0xCCFFFFFF), shape = RoundedCornerShape(size = 5.dp))
                .padding(start = 10.dp, end = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            SearchDropdownButton(FilterField.LOCATION, "Location", inputValues, locationList){newValue ->
                attractionFormState.location = newValue
            }
        }

        //Upload Images
        Row(
            modifier = Modifier
                .border(width = 1.dp, color = BlueLighter, shape = RoundedCornerShape(size = 5.dp))
                .width(300.dp)
                .height(50.dp)
                .background(color = Color(0xCCFFFFFF), shape = RoundedCornerShape(size = 5.dp))
                .padding(start = 10.dp, end = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ClickableText(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.Blue)) {
                        append("Upload Image")
                    }
                },
                onClick = { offset ->
                    // Iniciar a atividade de escolha de imagem da galeria
                    pickImagesLauncher.launch("image/*")
                },
                modifier = Modifier.clickable {
                    // por aqui a foto que deu upload
                }
            )
        }

    }

}