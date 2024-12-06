package me.sanao1006.core.ui.modifier

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Modifier.bottomBorder(
  width: Dp = 3.dp,
  color: Color = MaterialTheme.colorScheme.primary
): Modifier = this.then(
  Modifier.drawBehind {
    val strokeWidth = width.toPx()
    val y = size.height - strokeWidth / 2
    drawLine(
      color = color,
      start = Offset(0f, y),
      end = Offset(size.width, y),
      strokeWidth = strokeWidth
    )
  }
)
