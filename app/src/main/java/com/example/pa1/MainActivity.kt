package com.example.pa1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pa1.ui.theme.PA1Theme
import java.text.DecimalFormat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PA1Theme {
                // Define a tela principal
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Chama o composable Conversor
                    Conversor()
                }
            }
        }
    }
}

@Composable
fun Conversor() {
    // Estados para armazenar o valor de entrada, resultado, unidade de origem e destino
    var value by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    var fromUnit by remember { mutableStateOf("Metros") }
    var toUnit by remember { mutableStateOf("Quilómetros") }

    // Lista de unidades de medida
    val units = listOf("Metros", "Quilómetros", "Milhas", "Jardas")

    // Coluna centralizada para os componentes da UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Dropdown menu para selecionar a unidade de origem
        UnitDropdownMenu(
            label = "De",
            selectedUnit = fromUnit,
            onUnitSelected = { fromUnit = it },
            units = units
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Dropdown menu para selecionar a unidade de destino
        UnitDropdownMenu(
            label = "Para",
            selectedUnit = toUnit,
            onUnitSelected = { toUnit = it },
            units = units
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo de texto para entrada do valor a ser convertido
        OutlinedTextField(
            value = value,
            onValueChange = { value = it },
            label = { Text("Escreve o valor") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botão para realizar a conversão
        Button(onClick = {
            result = formatResult(convertLength(value.toDoubleOrNull(), fromUnit, toUnit))
        }) {
            Text("Converter")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Exibe o resultado da conversão
        Text(text = "Resultado: $result", style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun UnitDropdownMenu(
    label: String,
    selectedUnit: String,
    onUnitSelected: (String) -> Unit,
    units: List<String>
) {
    // Estado para controlar a expansão do dropdown
    var expanded by remember { mutableStateOf(false) }

    // Botão para abrir o dropdown
    Column {
        OutlinedButton(onClick = { expanded = true }) {
            Text(text = "$label: $selectedUnit")
        }

        // Menu dropdown
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            units.forEach { unit ->
                DropdownMenuItem(
                    text = { Text(text = unit) },
                    onClick = {
                        onUnitSelected(unit)
                        expanded = false
                    }
                )
            }
        }
    }
}

// Função para converter os valores entre as unidades
fun convertLength(value: Double?, fromUnit: String, toUnit: String): Double {
    if (value == null) return 0.0

    // Converte o valor de entrada para metros
    val meters = when (fromUnit) {
        "Metros" -> value
        "Quilómetros" -> value * 1000
        "Milhas" -> value * 1609.34
        "Jardas" -> value * 0.9144
        else -> value
    }

    // Converte o valor de metros para a unidade de destino
    return when (toUnit) {
        "Metros" -> meters
        "Quilómetros" -> meters / 1000
        "Milhas" -> meters / 1609.34
        "Jardas" -> meters / 0.9144
        else -> meters
    }
}


// Função para formatar o resultado com precisão
fun formatResult(result: Double): String {
    val decimalFormat = DecimalFormat("#.######")
    return decimalFormat.format(result)
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PA1Theme {
        Conversor()
    }
}

