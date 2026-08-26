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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project2.ui.theme.Project2Theme
import java.text.NumberFormat

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
const val DEFAULT_PERCENT_VALUE = 15.0f
// Default max percent value
const val MAX_PERCENT_VALUE = 25.0f

// Gets the dollar amount per person after applying the tip to the total
fun calculateTotalPerPerson(numberTotal: Float, tipPercent: Float, splitNumber: Int = 1): String {
    return NumberFormat.getCurrencyInstance().format(numberTotal * (1 + (tipPercent / 100.0)) / splitNumber)
}

@Composable
fun ProjectApp(modifier: Modifier = Modifier) {
    // This is the number of people splitting the bill
    var personCount: Int by remember { mutableIntStateOf(1) }
    // Increment the personCount. I add the condition so that it doesn't ever get to a 4-digit
    // number and start pushing the button off the screen.
    val personCountOnChangeIncrement: () -> Unit = {if (personCount < 998) personCount++}
    // Decrement the personCount
    val personCountOnChangeDecrement: () -> Unit = {if (personCount > 1) personCount--}
    // Text input for the total Bill field
    var totalBillInput: String by remember { mutableStateOf("") }
    // Updating the text input for the total bill field
    val totalBillOnValueChange: (String) -> Unit = {totalBillInput = it}

    // Input from the slider.
    var tipPercent: Float by remember { mutableFloatStateOf(DEFAULT_PERCENT_VALUE) }
    // Updating the slider. We don't need to round or modify the value at all because the slider
    // incremements by whole values.
    val sliderValueOnValueChange: (Float) -> Unit = {tipPercent = it}

    // Convert the string input to a float
    val numberTotal: Float = totalBillInput.toFloatOrNull() ?: 0f
    val calculatedTip = calculateTip(amount = numberTotal, tipPercent = tipPercent)

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(top = 50.dp, start = 14.dp, end = 14.dp)) {
        // Text for displaying the total per person amount
        TotalPerPersonText(
            calculateTotalPerPerson(numberTotal, tipPercent, personCount), Modifier
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
        ControlFields(totalBillInput = totalBillInput, totalBillOnValueChange = totalBillOnValueChange,
            tipPercent = tipPercent, sliderValueOnValueChange = sliderValueOnValueChange,
            tip = calculatedTip, personCount = personCount,
            personCountOnChangeIncrement = personCountOnChangeIncrement,
            personCountOnChangeDecrement = personCountOnChangeDecrement)
    }
}
@Composable
fun TipSlider(tipPercent: Float, sliderValueOnValueChange: (Float) -> Unit,tip: String, modifier: Modifier = Modifier) {
    Row(modifier) {
        // Tip text
        Text(text = stringResource(R.string.tip))
        // Separate the text with a spacer
        Spacer(modifier = Modifier.width(170.dp))
        // Text displaying the selected tip
        Text(text = tip)
    }
    // TipSlider should be called from in a column. So, we should be able to simply call these
    // without a container.
    Text("${tipPercent.toInt()}%", modifier = Modifier.fillMaxWidth().padding(top = 12.dp, bottom = 12.dp), textAlign = TextAlign.Center)
    Slider(
        value = tipPercent,
        onValueChange = sliderValueOnValueChange,
        valueRange = DEFAULT_PERCENT_VALUE..MAX_PERCENT_VALUE,
        steps = (MAX_PERCENT_VALUE - DEFAULT_PERCENT_VALUE - 1).toInt()
    )
}
@Composable
fun SplitSelector(
    personCount: Int,
    personCountOnChangeIncrement: () -> Unit,
    personCountOnChangeDecrement: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        Text("Split")
        Spacer(Modifier.width(120.dp))
        Button(onClick = personCountOnChangeDecrement) {
            Text("-", fontSize = 23.sp)
        }
        // Note: This padding extends to raise the dollar amount text field and the tip slider
        //       when the row has no modifier padding
        Text("$personCount", modifier = Modifier.padding(15.dp))
        Button(onClick = personCountOnChangeIncrement) {
            Text("+", fontSize = 23.sp)
        }

    }
}
@Composable
fun ControlFields(
    totalBillInput: String,
    totalBillOnValueChange: (String) -> Unit,
    tipPercent: Float,
    sliderValueOnValueChange: (Float) -> Unit,
    tip: String,
    personCount: Int,
    personCountOnChangeIncrement: () -> Unit,
    personCountOnChangeDecrement: () -> Unit) {

    Column(Modifier
        .border(
            width = 2.dp,
            color = Color.LightGray,
            shape = RoundedCornerShape(12.dp)
        )
        .padding(12.dp)
    ) {
        // This is the tip selection text field
        TotalBillTextField(totalBillInput, totalBillOnValueChange)
        SplitSelector(
            personCount = personCount,
            personCountOnChangeIncrement = personCountOnChangeIncrement,
            personCountOnChangeDecrement = personCountOnChangeDecrement,
            modifier = Modifier.padding(top = 10.dp, bottom = 10.dp))
        // This contains all the text in relation to the tip, and it contains the slider
        TipSlider(tipPercent, sliderValueOnValueChange, tip, Modifier.padding(10.dp))
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
        // Keyboard customization
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        colors =
            TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Blue,
                unfocusedIndicatorColor = Color.Black
            )
    )
}
internal fun calculateTip(amount: Float, tipPercent: Float = DEFAULT_PERCENT_VALUE): String {
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