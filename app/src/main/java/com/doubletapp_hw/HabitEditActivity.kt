package com.doubletapp_hw


import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.doubletapp_hw.ui.theme.Dobletapp_hwTheme


class HabitEditActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val habit = intent.extras?.getSerializable("habit", Habit::class.java) ?: Habit(name = "хуй")

        setContent {
            Dobletapp_hwTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HabitEditScreen(habit)
                }
            }
        }
    }

    @Composable
    fun HabitEditScreen(habit: Habit) {
        var name by remember { mutableStateOf(habit.name) }
        var description by remember { mutableStateOf(habit.description) }
        val priorityOptions = listOf("Низкий", "Средний", "Высокий")
        var priority by remember { mutableIntStateOf(habit.priority) }
        var type by remember { mutableStateOf(habit.type) }
        var period by remember { mutableStateOf(habit.period) }
        var frequency by remember { mutableStateOf(habit.frequency) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Название привычки") }
            )

            TextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Описание") }
            )

            Text("Приоритет:")
            ExposedDropdownMenuBox(priorityOptions, priority) { selectedIndex ->
                priority = selectedIndex
            }


            Row(modifier = Modifier.padding(vertical = 8.dp)) {
                RadioButtonGroup(listOf("Положительная", "Отрицательная"), type) { selectedType ->
                    type = selectedType
                }
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = period,
                    onValueChange = { period = it },
                    label = { Text("Выполнений") },
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))


                TextField(
                    value = frequency,
                    onValueChange = { frequency = it },
                    label = { Text("Периодичность") },
                    modifier = Modifier.weight(1f)
                )
            }

            Button(
                onClick = {
                    val i = Intent()
                    habit.name = name
                    habit.description = description
                    habit.period = period
                    habit.frequency = frequency
                    habit.priority = priority
                    habit.type = type
                    i.putExtra("habit", habit)
                    setResult(RESULT_OK, i)
                    finish()
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Сохранить")
            }
        }
    }

    @Composable
    fun DropdownMenuPriority(options: List<String>, selectedIndex: Int, onSelect: (Int) -> Unit) {
        var expanded by remember { mutableStateOf(false) }
        val selectedOption = options[selectedIndex]

        Box(Modifier.fillMaxWidth()) {
            Text(selectedOption, Modifier.clickable { expanded = true })
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }) {
                options.forEachIndexed { index, option ->
                    DropdownMenuItem(onClick = {
                        onSelect(index)
                        expanded = false
                    }, text = { Text(option) })
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ExposedDropdownMenuBox(options: List<String>, selectedIndex: Int, onSelect: (Int) -> Unit) {
        var expanded by remember { mutableStateOf(false) }

        Box(modifier = Modifier.fillMaxWidth()) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                }
            ) {
                TextField(
                    value = options[selectedIndex],
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    options.forEachIndexed { index, option ->
                        DropdownMenuItem(onClick = {
                            onSelect(index)
                            expanded = false
                        }, text = { Text(option) })
                    }
                }
            }
        }
    }

    @Composable
    fun RadioButtonGroup(options: List<String>, selectedOption: String, onOptionSelected: (String) -> Unit) {
        Column {
            options.forEach { option ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = option == selectedOption,
                        onClick = { onOptionSelected(option) }
                    )
                    Text(text = option)
                }
            }
        }
    }
//
//@Composable
//fun SecondScreen(counter: Int) {
//    val context = LocalContext.current
//    Column(
//        modifier = Modifier.fillMaxSize(),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(
//            text = counter.toString(),
//            fontSize = 150.sp,
//            fontWeight = FontWeight.Bold
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//        Button(onClick = {
//            (context as? Activity)?.finish()
//        }) {
//            Text("Go to FirstActivity")
//        }
//    }
}