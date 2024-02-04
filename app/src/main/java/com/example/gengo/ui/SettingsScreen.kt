package com.example.gengo.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.gengo.R
import com.example.gengo.ui.theme.FontSizePrefs

@Composable
fun SettingsScreen(
    isDarkTheme: Boolean,
    fontSizePrefs: FontSizePrefs,
    modifier: Modifier = Modifier,
    onThemeSwitch: () -> Unit = {},
    onFontSizeChange: (FontSizePrefs) -> Unit = {},
) {
    var theme by remember { mutableStateOf(isDarkTheme) }
    var fontSize by remember { mutableStateOf(fontSizePrefs) }

    var isFontSizeMenuExtended by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth(1f),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 10.dp
                ),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.nightlight),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
            )
            Text(
                text = stringResource(R.string.dark_mode),
            )
            Spacer(Modifier.weight(1f))
            Switch(
                checked = theme,
                onCheckedChange = {
                    theme = it
                    onThemeSwitch()
                }
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 10.dp
                ),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.format_size),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
            )
            Text(
                text = stringResource(R.string.font_size),
            )
            Spacer(Modifier.weight(1f))
            ExposedDropdownMenuBox(
                expanded = isFontSizeMenuExtended,
                onExpandedChange = {
                    isFontSizeMenuExtended = it
                },
                modifier = Modifier
                    .fillMaxWidth(0.75f),
            ) {
                TextField(
                    value = fontSize.name,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = isFontSizeMenuExtended)
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                    modifier = Modifier.menuAnchor(),
                )

                ExposedDropdownMenu(
                    expanded = isFontSizeMenuExtended,
                    onDismissRequest = { isFontSizeMenuExtended = false }) {
                    for (fs in FontSizePrefs.values()) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    // TODO: Localize the font size name
                                    text = fs.name,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                )
                            },
                            onClick = {
                                fontSize = fs
                                onFontSizeChange(fs)
                                isFontSizeMenuExtended = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(
        isDarkTheme = true,
        fontSizePrefs = FontSizePrefs.DEFAULT,
    )
}