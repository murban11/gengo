package com.example.gengo.ui

import com.example.gengo.R

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.layout.ContentScale
import androidx.compose.material3.Text
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    username: String = stringResource(R.string.username),
    email: String = stringResource(R.string.email),
    onLogoutClicked: () -> Unit = {},
) {
    val usernameLabel = stringResource(R.string.username)
    val emailLabel = stringResource(R.string.email)

    Column(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Row(
            modifier = modifier
                .padding(32.dp)
                .align(Alignment.CenterHorizontally),
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_user),
                contentDescription = stringResource(R.string.profile_picture),
                contentScale = ContentScale.Crop,
                modifier = modifier
                    .size(128.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color.Black, CircleShape)
            )
        }
        Row(
            modifier = modifier
                .align(Alignment.CenterHorizontally),
        ) {
            Text(
                text = username,
                fontSize = 12.em,
                fontWeight = FontWeight(500),
                modifier = modifier
                    .semantics {
                        contentDescription = "$usernameLabel: $username"
                    },
            )
        }
        Row(
            modifier = modifier
                .align(Alignment.CenterHorizontally),
        ) {
            Text(
                text = email,
                fontSize = 6.em,
                modifier = modifier
                    .semantics {
                        contentDescription = "$emailLabel: $email"
                    },
            )
        }
        Divider(
            color = Color.Black,
            thickness = 1.dp,
            modifier = modifier.padding(10.dp)
        )
        Row(
            modifier = modifier
                .align(Alignment.CenterHorizontally),
        ) {
            Button(
                onClick = {
                          onLogoutClicked()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text(
                    text = stringResource(R.string.logout),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen()
}
