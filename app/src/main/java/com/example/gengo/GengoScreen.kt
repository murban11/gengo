package com.example.gengo

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.gengo.ui.SignInScreen
import com.example.gengo.ui.SignUpScreen

enum class GengoScreen(@StringRes val title: Int) {
    SignUp(title = R.string.sign_up_label),
    SignIn(title = R.string.sign_in_label),
}

@Composable
fun GengoAppBar(
    currentScreen: GengoScreen,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
        modifier = modifier,
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun GengoApp(
    navController: NavHostController = rememberNavController(),
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = GengoScreen.valueOf(
        backStackEntry?.destination?.route ?: GengoScreen.SignUp.name
    )

    Scaffold(
        topBar = {
            GengoAppBar(currentScreen)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = GengoScreen.SignUp.name,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(route = GengoScreen.SignUp.name) {
                SignUpScreen(
                    onSignInButtonClicked = { navController.navigate(GengoScreen.SignIn.name) },
                )
            }
            composable(route = GengoScreen.SignIn.name) {
                SignInScreen(
                    onSignUpButtonClicked = { navController.navigate(GengoScreen.SignUp.name) },
                )
            }
        }
    }
}