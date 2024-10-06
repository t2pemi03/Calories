package com.example.calories

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.calories.ui.theme.CaloriesTheme
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CaloriesTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CalorieScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun CalorieScreen(modifier: Modifier = Modifier) {
    var weight by remember { mutableStateOf("") }
    var isMale by remember { mutableStateOf(true) }
    var intensity by remember { mutableStateOf("Moderate") }
    var result by remember { mutableStateOf("") }

    Column(modifier = modifier.padding(16.dp)) {
        Heading(title = "Calories")
        Spacer(modifier = Modifier.height(8.dp))
        WeightField(weight = weight, onWeightChange = { weight = it })
        Spacer(modifier = Modifier.height(8.dp))
        GenderChoices(isMale = isMale, onGenderChange = { isMale = it })
        Spacer(modifier = Modifier.height(8.dp))
        IntensityDropdown(intensity = intensity, onIntensityChange = { intensity = it })
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            result = calculateCalories(weight, intensity, isMale)
        }) {
            Text("Calculate")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Calories Burned: $result")
    }
}

@Composable
fun WeightField(weight: String, onWeightChange: (String) -> Unit) {
    OutlinedTextField(
        value = weight,
        onValueChange = onWeightChange,
        label = { Text("Enter your weight (kg)") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    )
}


@Composable
fun GenderChoices(isMale: Boolean, onGenderChange: (Boolean) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        RadioButton(
            selected = isMale,
            onClick = { onGenderChange(true) }
        )
        Text("Male", modifier = Modifier.padding(end = 16.dp))
        RadioButton(
            selected = !isMale,
            onClick = { onGenderChange(false) }
        )
        Text("Female")
    }
}


@Composable
fun IntensityDropdown(intensity: String, onIntensityChange: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val intensities = listOf("Low", "Moderate", "High")

    OutlinedTextField(
        value = intensity,
        onValueChange = {},
        label = { Text("Activity Level") },
        readOnly = true,
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        trailingIcon = {
            IconButton(onClick = { expanded = true }) {
                Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
            }
        }
    )

    if (expanded) {
        AlertDialog(
            onDismissRequest = { expanded = false },
            title = { Text("Select Activity Level") },
            text = {
                Column {
                    intensities.forEach { item ->
                        TextButton(onClick = {
                            onIntensityChange(item) // Notify parent of selected item
                            expanded = false // Close the dialog
                        }) {
                            Text(item)
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = { expanded = false }) {
                    Text("Done")
                }
            },
            dismissButton = {
                Button(onClick = { expanded = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}




@Composable
fun Heading(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.headlineLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        textAlign = TextAlign.Center
    )
}


private fun calculateCalories(weight: String, intensity: String, isMale: Boolean): String {
    val calorieFactor = when (intensity) {
        "Low" -> if (isMale) 1.2 else 1.1
        "Moderate" -> if (isMale) 1.55 else 1.4
        "High" -> if (isMale) 1.9 else 1.7
        else -> 1.0
    }

    return if (weight.isNotEmpty()) {
        weight.toFloatOrNull()?.let {
            (it * calorieFactor).toString()
        } ?: "Invalid weight"
    } else {
        "Enter your weight"
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewCalorieScreen() {
    CaloriesTheme {
        CalorieScreen()
    }
}