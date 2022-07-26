package com.example.magnapp.ui.screens.detalle_topico

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset

@Composable
fun ChipVerticalGrid(
    modifier: Modifier = Modifier,
    spacing: Dp,
    //moreItemsView: @Composable (Int) -> Unit,
    content: @Composable () -> Unit,
) {
    SubcomposeLayout(
        modifier = modifier
    ) { constraints ->
        val contentConstraints = constraints.copy(minWidth = 0, minHeight = 0)
        var currentRow = 0
        var currentOrigin = IntOffset.Zero
        val spacingValue = spacing.toPx().toInt()
        val mainMeasurables = subcompose("content", content)
        val placeables = mutableListOf<Pair<Placeable, IntOffset>>()
        for (i in mainMeasurables.indices) {
            val measurable = mainMeasurables[i]
            val placeable = measurable.measure(contentConstraints)

            fun Placeable.didOverflowWidth() =
                currentOrigin.x > 0f && currentOrigin.x + width > contentConstraints.maxWidth

            if (placeable.didOverflowWidth()) {
                currentRow += 1
                val nextRowOffset = currentOrigin.y + placeable.height + spacingValue

                if (nextRowOffset + placeable.height > contentConstraints.maxHeight) {
                    var morePlaceable: Placeable
                    do {
                        val itemsLeft = mainMeasurables.count() - placeables.count()

                        morePlaceable = subcompose(itemsLeft) {
                            //moreItemsView(itemsLeft)
                        }[0].measure(contentConstraints)
                        val didOverflowWidth = morePlaceable.didOverflowWidth()
                        if (didOverflowWidth) {
                            val removed = placeables.removeLast()
                            currentOrigin = removed.second
                        }
                    } while (didOverflowWidth)
                    placeables.add(morePlaceable to currentOrigin)
                    break
                }
                currentOrigin = currentOrigin.copy(x = 0, y = nextRowOffset)
            }

            placeables.add(placeable to currentOrigin)
            currentOrigin = currentOrigin.copy(x = currentOrigin.x + placeable.width + spacingValue)
        }
        layout(
            width = maxOf(constraints.minWidth, placeables.maxOfOrNull { it.first.width + it.second.x } ?: 0),
            height = maxOf(constraints.minHeight, placeables.lastOrNull()?.run { first.height + second.y } ?: 0),
        ) {
            placeables.forEach {
                val (placeable, origin) = it
                placeable.place(origin.x, origin.y)
            }
        }
    }
}