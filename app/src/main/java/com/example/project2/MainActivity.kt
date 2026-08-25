package com.example.project2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project2.ui.theme.Project2Theme
import java.text.NumberFormat
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Project2Theme {
                Surface ( modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,) {
                    ProjectApp()
                }
            }
        }
    }
}
// Default percent value
const val DefaultPercentValue = 15.0f

@Composable
fun ProjectApp(modifier: Modifier = Modifier) {
    // Text input for the total Bill field
    var totalBillInput: String by remember { mutableStateOf("") }
    // Updating the text input for the total bill field
    val totalBillOnValueChange: (String) -> Unit = {totalBillInput = it}

    // Input from the slider.
    var sliderValue: Float by remember { mutableFloatStateOf(DefaultPercentValue) }
    // Updating the slider.
    // Value is rounded to the nearest tenth.
    val sliderValueOnValueChange: (Float) -> Unit = {sliderValue = (it * 10).roundToInt() / 10f}

    // Convert the string input to a float
    val numberTotal: Float = totalBillInput.toFloatOrNull() ?: 0f
    val calculatedTip = calculateTip(amount = numberTotal, tipPercent = sliderValue)

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(top = 50.dp, start = 14.dp, end = 14.dp)) {
        // Text for displaying the total per person amount
        TotalPerPersonText(
            calculatedTip, Modifier
            .background(
                color = Color.LightGray,
                shape = RoundedCornerShape(8.dp),
            )
            .border(
                width = 2.dp,
                color = Color.Black,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(bottom = 40.dp, top = 40.dp)
            .fillMaxWidth())



        // This contains all the fields for input
        ControlFields(totalBillInput, totalBillOnValueChange,
            sliderValue, sliderValueOnValueChange)
    }
}
@Composable
fun TipSlider(sliderValue: Float, sliderValueOnValueChange: (Float) -> Unit, modifier: Modifier = Modifier) {
    Row(modifier) {
        // Tip text
        Text(text = stringResource(R.string.tip))
        // Separate the text with a spacer
        Spacer(modifier = Modifier.width(170.dp))
        // Text displaying the selected tip percentage
        Text(text = "$sliderValue")
    }
    Slider(
        value = sliderValue,
        onValueChange = sliderValueOnValueChange,
        valueRange = 15f..40f,
        steps = 10
    )
}
@Composable
fun ControlFields(totalBillInput: String, totalBillOnValueChange: (String) -> Unit,
                  sliderValue: Float, sliderValueOnValueChange: (Float) -> Unit) {

    Column(Modifier
        .border(
            width = 2.dp,
            color = Color.LightGray,
            shape = RoundedCornerShape(12.dp)
        )
        .padding(12.dp)
    ) {
        TotalBillTextField(totalBillInput, totalBillOnValueChange)
        TipSlider(sliderValue, sliderValueOnValueChange, Modifier.padding(10.dp))
    }
}
@Composable
fun TotalPerPersonText(
    text: String,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier) {
        // Text outputs.
        // Total per-person literal string
        Text(text = stringResource(R.string.total_per_person))
        // Total per-person price
        Text(text = text,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold)
    }
}
@Composable
fun TotalBillTextField(
    totalBillInput: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = totalBillInput,
        onValueChange = onValueChange,
        leadingIcon = {Text("$")},
        label = { Text(stringResource(R.string.dollar_amount)) },
        modifier = modifier
            .fillMaxWidth(),
        colors =
            TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Blue,
                unfocusedIndicatorColor = Color.Black
            )
    )
}
internal fun calculateTip(amount: Float, tipPercent: Float = DefaultPercentValue): String {
    val tip = tipPercent / 100.0 * amount
    return NumberFormat.getCurrencyInstance().format(tip)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Project2Theme {
        ProjectApp()
    }
}