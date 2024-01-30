package com.example.gengo.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun MainScreen(
    db: FirebaseFirestore,
    modifier: Modifier = Modifier,
) {
    val lessonNames = remember { mutableStateListOf<String>() }

    if (lessonNames.isEmpty()) {
        db.collection("Lessons")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    if (!lessonNames.contains(document.id)) {
                        lessonNames.add(document.id)
                    }
                }
            }
    }

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
                    onClick = { /*TODO*/ },
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
    MainScreen(Firebase.firestore)
}
