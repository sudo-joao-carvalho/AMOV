package pt.isec.ans.amov.ui.Screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isec.ans.amov.R
import pt.isec.ans.amov.ui.Components.Buttons.GradientButton
import pt.isec.ans.amov.ui.Components.Buttons.RoundIconButton
import pt.isec.ans.amov.ui.theme.BlueHighlight
import pt.isec.ans.amov.ui.theme.BlueLighter
import pt.isec.ans.amov.ui.theme.BlueSoft

data class AttractionFormState(
    var name: String = "",
    var description: String = "",
    var coordinates: String = ""
)

@Preview
@Composable
fun AddAttraction(){
    var attractionFormState by remember { mutableStateOf(AttractionFormState()) }

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
                            //fontFamily = FontFamily(Font(R.font.inter)), esta linha da erro porque nao tem o ficheiro inter
                            fontWeight = FontWeight(600),
                            color = BlueHighlight,
                        )
                    )

                    Image(
                        modifier = Modifier
                            .padding(1.dp)
                            .width(16.dp)
                            .height(16.dp),
                        painter = painterResource(id = R.drawable.cancellationx1), //falta este resource
                        contentDescription = "Cancel",
                        contentScale = ContentScale.None
                    )

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
                    SecondInputs()

                }

                GradientButton(
                    _text = "Submit",
                    _gradient = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF0B374B),
                            Color(0xFF00B6DE)
                        )
                    ),
                ){ //TODO implement lambda when things work on firebase
                    Log.d("D", "submit")
                }

            }



        }
    }
}

@Composable
fun TextInputs(attractionFormState: AttractionFormState, onAttractionFormStateChange: (AttractionFormState) -> Unit) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var coordinates by remember { mutableStateOf("") }

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

            
            OutlinedTextField(
                value = name,
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
                              },
                onValueChange = { name = it }
            )
        }

        //Description
        Row(
            modifier = Modifier
                .width(300.dp)
                .height(60.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ){


            OutlinedTextField(
                value = description,
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
                onValueChange = { description = it }
            )
        }

        //Coordinates
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically,
        ){


            OutlinedTextField(
                modifier = Modifier.width(252.dp),
                value = coordinates,
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
                onValueChange = { coordinates = it },
            )

            //Icon container
            RoundIconButton(
                drawableId = R.drawable.vector,
                modifier = Modifier
                    .size(40.dp)
            )

        }
    }

}

@Composable
fun SecondInputs(){

    Column(
        verticalArrangement = Arrangement.spacedBy(40.dp, Alignment.Top),
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

            Text(
                text = "Category",
                style = TextStyle(
                    fontSize = 16.sp,
                    //fontFamily = FontFamily(Font(R.font.inter)),
                    fontWeight = FontWeight(500),
                    color = BlueSoft,
                )
            )
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

            Text(
                text = "Location",
                style = TextStyle(
                    fontSize = 16.sp,
                    //fontFamily = FontFamily(Font(R.font.inter)),
                    fontWeight = FontWeight(500),
                    color = BlueSoft,
                )
            )
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

            Text(
                text = "Upload Images",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.inter)),
                    fontWeight = FontWeight(500),
                    color = BlueSoft,
                )
            )
        }

    }

}