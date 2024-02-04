package com.example.gengo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.gengo.ui.theme.FontSizePrefs
import com.example.gengo.ui.theme.GengoTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        db = Firebase.firestore
        setContent {
            // TODO: Load and save the user's preferred theme and font size using DataStore
            val theme: Boolean = isSystemInDarkTheme()
            var isDarkTheme by remember { mutableStateOf(theme) }
            var fontSizePrefs by remember { mutableStateOf(FontSizePrefs.DEFAULT)}

            GengoTheme(
                darkTheme = isDarkTheme,
                fontSizePrefs = fontSizePrefs,
            ) {
                GengoApp(
                    auth = auth,
                    db = db,
                    isDarkTheme = isDarkTheme,
                    fontSizePrefs = fontSizePrefs,
                    onThemeSwitch = {
                        isDarkTheme = !isDarkTheme
                    },
                    onFontSizeChange = {
                        fontSizePrefs = it
                    }
                )
            }
        }
    }
}