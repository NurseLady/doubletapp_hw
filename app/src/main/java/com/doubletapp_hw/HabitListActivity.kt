package com.doubletapp_hw

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.doubletapp_hw.ui.theme.Dobletapp_hwTheme

class HabitListActivity : ComponentActivity() {
    private val viewModel by viewModels<HabitListViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val habits = viewModel.habits.collectAsState(initial = listOf())

            Dobletapp_hwTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    //Toast.makeText(this, "ggggg", Toast.LENGTH_LONG).show()
                    HabitListScreen(viewModel)
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            val habit = data.extras?.getSerializable("habit", Habit::class.java) ?: Habit()
            if (!viewModel.updateHabit(habit)) viewModel.addHabit(habit)
        }
    }

    @Composable
    fun HabitListScreen(viewModel: HabitListViewModel) {
        val habits by viewModel.habits.collectAsState()
        val lazyListState: LazyListState = rememberLazyListState()

        Scaffold(
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    val intent = Intent(this, HabitEditActivity::class.java).apply {
                        putExtra("habit", Habit())
                    }
                    startActivityForResult(intent, 100)
                }) {
                    Icon(Icons.Filled.Add, contentDescription = "Add")
                }
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                state = LazyListState()
            ) {
                items(habits) { habit ->
                    HabitItem(habit = habit)
                }
            }
            if (habits.isEmpty()) {
                Text("Добавьте привычку", style = MaterialTheme.typography.headlineSmall)
            }
        }
    }

    @Composable
    fun HabitItem(habit: Habit) {
        Card(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = Color(habit.color.value)
            ),
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .clickable {
                    val intent = Intent(this, HabitEditActivity::class.java).apply {
                        putExtra("habit", habit)
                    }
                    startActivityForResult(intent, 100)
                }
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = habit.name,
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = habit.description,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
