package com.dehaat.kyc.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

val Shapes = Shapes(
	small = RoundedCornerShape(4.dp),
	medium = RoundedCornerShape(8.dp),
	large = RoundedCornerShape(10.dp)
)

@Composable
fun smallShape() = MaterialTheme.shapes.small

@Composable
fun mediumShape() = MaterialTheme.shapes.medium

@Composable
fun largeShape() = MaterialTheme.shapes.large

@Composable
fun veryLargeShape() = RoundedCornerShape(12.dp)
