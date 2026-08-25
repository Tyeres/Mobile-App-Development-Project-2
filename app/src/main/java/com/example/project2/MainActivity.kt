package com.example.project2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project2.ui.theme.Project2Theme

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

@Composable
fun ProjectApp( modifier: Modifier = Modifier) {
    // Text input for the total Bill
    var totalBillInput by remember { mutableStateOf("") }




    Column(modifier = Modifier.padding(top = 50.dp, start = 14.dp, end = 14.dp)) {
        TotalPerPersonText("Sample Text", Modifier
            .background(
                color = Color.LightGray,
                shape = RoundedCornerShape(8.dp),
            ).border(
                width = 2.dp,
                color = Color.Blue,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(all = 82.dp)
            .fillMaxWidth())

        TotalBillTextField(totalBillInput, {totalBillInput = it})
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
        // Text outputs
        Text(
            text = stringResource(R.string.total_per_person)
        )
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
        label = { Text(stringResource(R.string.tip_amount)) },
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        colors =
            TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Blue,
                unfocusedIndicatorColor = Color.Black
            )
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Project2Theme {
        ProjectApp()
    }
}