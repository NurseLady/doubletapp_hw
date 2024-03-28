package com.doubletapp_hw

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import java.io.Serializable
import java.util.UUID

data class Habit(
    val id: String = UUID.randomUUID().toString(),
    var name: String = "NewHabit",
    var description: String = "",
    var priority: Int = 0,
    var type: String = "",
    var frequency: String = "",
    var period: String = "",
    var color: Color = Color(70, 70,70, 255)
): Serializable
