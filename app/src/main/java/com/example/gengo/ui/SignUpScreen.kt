package com.example.gengo.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gengo.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.gengo.InputError

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    auth: FirebaseAuth? = null,
    db: FirebaseFirestore? = null,
    onSignUpSuccess: () -> Unit = {},
    onSignUpFailure: () -> Unit = {},
    onSignInButtonClicked: () -> Unit = {},
) {
    var usernameInput by remember { mutableStateOf("") }
    var emailInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }

    var usernameInputError by remember { mutableStateOf(InputError.OK) }
    var emailInputError by remember { mutableStateOf(InputError.OK) }
    var passwordInputError by remember { mutableStateOf(InputError.OK) }

    fun validateUsername(text: String) {
        usernameInputError = if (text.isEmpty()) {
            InputError.Empty
        } else {
            InputError.OK
        }
    }

    fun validateEmail(text: String) {
        emailInputError = if (text.isEmpty()) {
            InputError.Empty
        } else {
            InputError.OK
        }
    }

    fun validatePassword(text: String) {
        passwordInputError = if (text.isEmpty()) {
            InputError.Empty
        } else {
            InputError.OK
        }
    }

    Column(
        modifier = modifier
            .statusBarsPadding()
            .padding(horizontal = 40.dp)
            .safeDrawingPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        TextField(
            value = usernameInput,
            onValueChange = {
                usernameInput = it
                validateUsername(it)
            },
            label = {
                Text(
                    stringResource(R.string.username_field_label),
                    color = MaterialTheme.colorScheme.background,
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
            ),
            isError = usernameInputError != InputError.OK,
            supportingText = {
                if (usernameInputError != InputError.OK) {
                    Text(
                        text = stringResource(usernameInputError.message),
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            },
            modifier = Modifier
                .padding(bottom = 32.dp, top = 40.dp),
            colors = TextFieldDefaults.textFieldColors(
                textColor = MaterialTheme.colorScheme.background,
                containerColor = MaterialTheme.colorScheme.onBackground,
                cursorColor = MaterialTheme.colorScheme.background,
                errorCursorColor = MaterialTheme.colorScheme.error,
                errorIndicatorColor = MaterialTheme.colorScheme.error,
            ),
            textStyle = TextStyle(color = MaterialTheme.colorScheme.background),
        )
        TextField(
            value = emailInput,
            onValueChange = {
                emailInput = it
                validateEmail(it)
            },
            label = {
                Text(
                    stringResource(R.string.email_field_label),
                    color = MaterialTheme.colorScheme.background,
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
            ),
            isError = emailInputError != InputError.OK,
            supportingText = {
                if (emailInputError != InputError.OK) {
                    Text(
                        text = stringResource(emailInputError.message),
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            },
            modifier = Modifier
                .padding(bottom = 32.dp),
            colors = TextFieldDefaults.textFieldColors(
                textColor = MaterialTheme.colorScheme.background,
                containerColor = MaterialTheme.colorScheme.onBackground,
                cursorColor = MaterialTheme.colorScheme.background,
                errorCursorColor = MaterialTheme.colorScheme.error,
                errorIndicatorColor = MaterialTheme.colorScheme.error,
            ),
            textStyle = TextStyle(color = MaterialTheme.colorScheme.background)
        )
        TextField(
            value = passwordInput,
            onValueChange = {
                passwordInput = it
                validatePassword(it)
            },
            label = {
                Text(
                    stringResource(R.string.password_field_label),
                    color = MaterialTheme.colorScheme.background,
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            isError = passwordInputError != InputError.OK,
            supportingText = {
                if (passwordInputError != InputError.OK) {
                    Text(
                        text = stringResource(passwordInputError.message),
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .padding(bottom = 32.dp),
            colors = TextFieldDefaults.textFieldColors(
                textColor = MaterialTheme.colorScheme.background,
                containerColor = MaterialTheme.colorScheme.onBackground,
                cursorColor = MaterialTheme.colorScheme.background,
                errorCursorColor = MaterialTheme.colorScheme.error,
                errorIndicatorColor = MaterialTheme.colorScheme.error,
            ),
            textStyle = TextStyle(color = MaterialTheme.colorScheme.background)
        )
        Button(
            onClick = {
                if (usernameInput.isNotEmpty() && emailInput.isNotEmpty() && passwordInput.isNotEmpty()) {
                    auth?.createUserWithEmailAndPassword(
                        emailInput,
                        passwordInput
                    )?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            db?.collection("Users")
                                ?.document(emailInput)?.set(
                                    hashMapOf(
                                        "username" to usernameInput
                                    )
                                )?.addOnSuccessListener {
                                    onSignUpSuccess()
                                }?.addOnFailureListener {
                                    onSignUpFailure()
                                }
                        }
                    }?.addOnFailureListener {
                        onSignUpFailure()
                    }
                } else {
                    onSignUpFailure()
                }
            },
            modifier = Modifier
                .padding(bottom = 16.dp)
        ) {
            Text(
                stringResource(R.string.sign_up_label),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
        OutlinedButton(
            onClick = onSignInButtonClicked
        ) {
            Text(stringResource(R.string.sign_in_label))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpPreview() {
    SignUpScreen()
}