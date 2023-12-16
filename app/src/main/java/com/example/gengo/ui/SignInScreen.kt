package com.example.gengo.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gengo.R

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    onSignUpButtonClicked: () -> Unit = {},
    onSignInButtonClicked: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .statusBarsPadding()
            .padding(horizontal = 40.dp)
            .safeDrawingPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        TextField(
            value = "",
            onValueChange = {},
            label = { Text(stringResource(R.string.user_name_label)) },
            modifier = Modifier
                .padding(bottom = 32.dp, top = 40.dp),
        )
        TextField(
            value = "",
            onValueChange = {},
            label = { Text(stringResource(R.string.password_label)) },
            modifier = Modifier
                .padding(bottom = 32.dp),
        )
        Button(
            onClick = onSignInButtonClicked,
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
