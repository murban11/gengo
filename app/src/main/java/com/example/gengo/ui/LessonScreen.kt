package com.example.gengo.ui

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

const val TAG = "LessonScreen"

data class QuizItem(
    val question: String,
    val answers: Array<String>,
    val indexOfCorrect: Int,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as QuizItem

        if (question != other.question) return false
        if (!answers.contentEquals(other.answers)) return false
        if (indexOfCorrect != other.indexOfCorrect) return false

        return true
    }

    override fun hashCode(): Int {
        var result = question.hashCode()
        result = 31 * result + answers.contentHashCode()
        result = 31 * result + indexOfCorrect
        return result
    }
}

@Composable
fun LessonScreen(
    fetchFields: (callback: (List<Pair<String, String>>) -> Unit) -> Unit,
    modifier: Modifier = Modifier,
) {
    val quizItems = remember { mutableStateListOf<QuizItem>() }
    var current by remember { mutableIntStateOf(0) }

    if (quizItems.isEmpty()) {
        fetchFields { fields ->
            assert(fields.size >= 4)

            val indexes = IntArray(fields.size) { it }
            val items: MutableList<QuizItem> = mutableListOf()

            fields.forEachIndexed { index, field ->
                indexes.shuffle()

                val selected = IntArray(4) { indexes[it] }
                if (!selected.contains(index)) {
                    selected[(0..3).random()] = index
                }
                val correct = selected.indexOfFirst { it == index }

                val item = QuizItem(field.first, Array(4) {
                    fields[selected[it]].second
                }, correct)
                items.add(item)
            }

            items.forEach {
                Log.i(
                    TAG,
                    """
                    Question: ${it.question}
                    Answers: ${it.answers.joinToString(", ")}
                    Correct: ${it.answers[it.indexOfCorrect]}
                    """.trimIndent(),
                )
                quizItems.add(it)
            }
        }
    }

    if (quizItems.isNotEmpty() && current < quizItems.size) {
        CardView(
            quizItem = quizItems[current],
            modifier = modifier,
        ) {
            current += 1
        }
    } else if (quizItems.isNotEmpty()) {
        /* TODO: Show summary */
    }
}

@Preview(showBackground = true)
@Composable
fun LessonScreenPreview() {
    LessonScreen(
        fetchFields = { callback ->
            callback(
                listOf(
                    Pair("a", "あ"),
                    Pair("i", "い"),
                    Pair("u", "う"),
                    Pair("e", "え"),
                    Pair("o", "お"),
                )
            )
        }
    )
}