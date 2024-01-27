package com.example.gengo

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.gengo.ui.MainScreen
import com.example.gengo.ui.SignInScreen
import com.example.gengo.ui.SignUpScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

enum class GengoScreen(@StringRes val title: Int) {
    SignUp(title = R.string.sign_up_label),
    SignIn(title = R.string.sign_in_label),
    Main(title = R.string.app_name),
}

@Composable
fun GengoAppBar(
    currentScreen: GengoScreen,
    modifier: Modifier = Modifier,
    showMenuIcon: Boolean = false,
    onMenuButtonClicked: () -> Unit = {},
) {
    TopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
        navigationIcon = {
            if (showMenuIcon) {
                IconButton(onClick = {
                    onMenuButtonClicked()
                }) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "Localized description", // TODO: Change description
                    )
                }
            }
        },
        modifier = modifier,
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun GengoApp(
    auth: FirebaseAuth,
    db: FirebaseFirestore,
    navController: NavHostController = rememberNavController(),
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = GengoScreen.valueOf(
        backStackEntry?.destination?.route ?: GengoScreen.SignUp.name
    )
    var enableMenu by remember { mutableStateOf(false) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                NavigationDrawerItem(
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.List,
                            contentDescription = "Localized description", // TODO: Change description
                        )
                    },
                    label = {
                        Text(text = "Lessons") // TODO: Localize the string
                    },
                    selected = false,
                    onClick = { /*TODO*/ }
                )
                NavigationDrawerItem(
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.AccountCircle,
                            contentDescription = "Localized description", // TODO: Change description
                        )
                    },
                    label = {
                        Text(text = "Profile") // TODO: Localize the string
                    },
                    selected = false,
                    onClick = { /*TODO*/ }
                )
                NavigationDrawerItem(
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "Localized description", // TODO: Change description
                        )
                    },
                    label = {
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text(text = "Settings") // TODO: Localize the string
                    },
                    selected = false,
                    onClick = { /*TODO*/ }
                )
            }
        },
    ) {
        Scaffold(
            topBar = {
                GengoAppBar(currentScreen, showMenuIcon = enableMenu) {
                    scope.launch {
                        drawerState.apply {
                            if (isClosed) open() else close()
                        }
                    }
                }
            },
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },
        ) { innerPadding ->
            NavHost(
                navController = navController,
                // TODO: Change the start destination so that when user have successfully logged before then
                //       go straight to the dashboard. Otherwise, go to the sign in screen.
                startDestination = GengoScreen.SignUp.name,
                modifier = Modifier.padding(innerPadding),
            ) {
                composable(route = GengoScreen.SignUp.name) {
                    SignUpScreen(
                        auth = auth,
                        db = db,
                        onSignUpSuccess = {
                            scope.launch {
                                snackbarHostState.showSnackbar("Successfully signed up!")
                            }
                            navController.navigate(GengoScreen.Main.name)
                        },
                        onSignUpFailure = {
                            scope.launch {
                                snackbarHostState.showSnackbar("Sign up failure!")
                            }
                        },
                    ) { navController.navigate(GengoScreen.SignIn.name) }
                    enableMenu = false
                }
                composable(route = GengoScreen.SignIn.name) {
                    SignInScreen(
                        auth = auth,
                        onSignInSuccess = {
                            scope.launch {
                                snackbarHostState.showSnackbar("Successfully signed in!")
                            }
                            navController.navigate(GengoScreen.Main.name)
                        },
                        onSignInFailure = {
                            scope.launch {
                                snackbarHostState.showSnackbar("Sign in failure!")
                            }
                        },
                        onSignUpButtonClicked = { navController.navigate(GengoScreen.SignUp.name) },
                    )
                    enableMenu = false
                }
                composable(route = GengoScreen.Main.name) {
                    MainScreen()
                    enableMenu = true
                }
            }
        }
    }
}