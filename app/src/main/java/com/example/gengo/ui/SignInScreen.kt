package com.example.gengo.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gengo.R
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    auth: FirebaseAuth? = null,
    onSignInSuccess: () -> Unit = {},
    onSignInFailure: () -> Unit = {},
    onSignUpButtonClicked: () -> Unit = {},
) {
    var emailInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .statusBarsPadding()
            .padding(horizontal = 40.dp)
            .safeDrawingPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        TextField(
            value = emailInput,
            onValueChange = { emailInput = it },
            label = { Text(stringResource(R.string.email_label)) },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
            ),
            modifier = Modifier
                .padding(bottom = 32.dp, top = 40.dp),
        )
        TextField(
            value = passwordInput,
            onValueChange = { passwordInput = it },
            label = { Text(stringResource(R.string.password_label)) },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .padding(bottom = 32.dp),
        )
        Button(
            onClick = {
                auth?.signInWithEmailAndPassword(
                    emailInput,
                    passwordInput
                )?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        onSignInSuccess()
                    }
                }?.addOnFailureListener {
                    onSignInFailure()
                }
            },
            modifier = Modifier
                .padding(bottom = 16.dp)
        ) {
            Text(stringResource(R.string.sign_in_label))
        }
        OutlinedButton(
            onClick = onSignUpButtonClicked
        ) {
            Text(stringResource(R.string.sign_up_label))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignInPreview() {
    SignInScreen()
}
