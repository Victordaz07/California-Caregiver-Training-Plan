package com.caregiverproca.app.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// Corner radii copied from /docs/design/DESIGN.md ("rounded" tokens).
val CaregiverShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),  // rounded.sm
    small = RoundedCornerShape(8.dp),       // rounded.DEFAULT — buttons, inputs, checklist boxes
    medium = RoundedCornerShape(12.dp),     // rounded.md
    large = RoundedCornerShape(16.dp),      // rounded.lg — cards, flashcards, progress widgets
    extraLarge = RoundedCornerShape(24.dp), // rounded.xl
)

val PillShape = RoundedCornerShape(50) // rounded.full — status pills, streak counters
