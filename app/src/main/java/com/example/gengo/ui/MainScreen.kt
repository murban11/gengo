package com.example.gengo.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em

@Composable
fun MainScreen(
    lessonNames: MutableList<String>,
    onLessonSelect: (lessonName: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(),
    ) {
        for (lessonName in lessonNames) {
            Row(
                modifier = modifier
                    .align(Alignment.CenterHorizontally),
            ) {
                Button(
                    onClick = {
                              onLessonSelect(lessonName)
                    },
                    modifier = Modifier
                        .padding(6.dp)
                        .fillMaxWidth(1f)
                ) {
                    Text(
                        text = lessonName,
                        fontSize = 8.em,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen(
        lessonNames = mutableListOf("Foo", "Bar", "Baz"),
        onLessonSelect = {},
    )
}
