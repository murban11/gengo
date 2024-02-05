package com.example.gengo

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.gengo.ui.LessonScreen
import com.example.gengo.ui.LoadingScreen
import com.example.gengo.ui.MainScreen
import com.example.gengo.ui.ProfileScreen
import com.example.gengo.ui.SettingsScreen
import com.example.gengo.ui.SignInScreen
import com.example.gengo.ui.SignUpScreen
import com.example.gengo.ui.theme.FontSizePrefs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch

const val TAG = "GengoScreen"

enum class GengoScreen(@StringRes val title: Int) {
    Loading(title = R.string.loading_screen),
    SignUp(title = R.string.sign_up_label),
    SignIn(title = R.string.sign_in_label),
    Main(title = R.string.app_name),
    Profile(title = R.string.profile),
    Lesson(title = R.string.lesson),
    Settings(title = R.string.settings),
}

@Composable
fun GengoAppBar(
    title: String,
    modifier: Modifier = Modifier,
    showMenuIcon: Boolean = false,
    menuIcon: ImageVector = Icons.Filled.Menu,
    onMenuButtonClicked: () -> Unit = {},
) {
    val screenLabel = stringResource(R.string.screen)

    TopAppBar(
        title = {
            Text(
                text = title,
                modifier = modifier
                    .semantics {
                        contentDescription = "$screenLabel: $title"
                    },
                color = MaterialTheme.colorScheme.onSurface,
            )
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ),
        navigationIcon = {
            if (showMenuIcon) {
                IconButton(onClick = {
                    onMenuButtonClicked()
                }) {
                    Icon(
                        imageVector = menuIcon,
                        contentDescription = stringResource(R.string.toggle_menu),
                        tint = MaterialTheme.colorScheme.onSurface,
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
    storage: FirebaseStorage,
    isDarkTheme: Boolean,
    fontSizePrefs: FontSizePrefs,
    onThemeSwitch: () -> Unit,
    onFontSizeChange: (FontSizePrefs) -> Unit,
    navController: NavHostController = rememberNavController(),
) {
    val usernamePlaceholder: String = stringResource(R.string.username)
    val usernameFetchFailureMessage: String = stringResource(R.string.username_fetch_failure)
    val emailAddressFetchFailureMessage: String = stringResource(R.string.email_address_fetch_failure)
    val signUpSuccess: String = stringResource(R.string.sign_up_success)
    val signUpFailure: String = stringResource(R.string.sing_up_failure)
    val signInSuccess: String = stringResource(R.string.sign_in_success)
    val signInFailure: String = stringResource(R.string.sing_in_failure)
    val profilePictureUploadedSuccessfully: String = stringResource(R.string.profile_picture_uploaded_successfully)
    val profilePictureUploadFailure: String = stringResource(R.string.profile_picture_upload_failed)

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = GengoScreen.valueOf(
        backStackEntry?.destination?.route ?: GengoScreen.Loading.name
    )
    var enableMenu by remember { mutableStateOf(false) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var username by remember { mutableStateOf(usernamePlaceholder) }
    var profilePictureUri by remember { mutableStateOf(Uri.EMPTY) }

    val lessonNames = remember { mutableStateListOf<String>() }
    var lessonSelected by remember { mutableStateOf("") }

    val fetchLessonNames: () -> Unit = {
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
    }

    val fetchLessonFields: (String, (List<Pair<String, String>>) -> Unit) -> Unit = { lessonName, callback ->
        val fieldList: MutableList<Pair<String, String>> = mutableListOf()

        db.collection("Lessons")
            .document(lessonName)
            .get()
            .addOnSuccessListener { fields ->
                fields.data?.forEach { f ->
                    val newField = Pair(f.key, f.value.toString())
                    if (!fieldList.contains(newField)) {
                        fieldList.add(newField)
                    }
                }
            }
            .addOnFailureListener {
                /* TODO */
            }
            .addOnCompleteListener {
                Log.i(TAG, "Fetched ${fieldList.size} lesson fields")
                callback(fieldList)
            }
    }

    val fetchProfilePictureUri: (email: String?) -> Unit = { email ->
        if (email != null && profilePictureUri == Uri.EMPTY) {
            db.collection("Users")
                .document(email)
                .get()
                .addOnSuccessListener {
                    val data = it.data?.get("profilePictureUri")
                    profilePictureUri = if (data != null) Uri.parse(data.toString()) else Uri.EMPTY
                    Log.i(TAG, "Fetched profile picture URI: $profilePictureUri")
                }
                .addOnFailureListener {
                    // Do nothing
                }
        }
    }

    val toggleMenu: () -> Any = {
        scope.launch {
            drawerState.apply {
                if (isClosed) open() else close()
            }
        }
    }

    val updateUsername: () -> Unit = {
        val email = auth.currentUser?.email

        if (email != null) {
            db.collection("Users")
                .document(email)
                .get()
                .addOnSuccessListener {
                    username = it.data?.get("username").toString()
                }
                .addOnFailureListener {
                    scope.launch {
                        snackbarHostState.showSnackbar(usernameFetchFailureMessage)
                    }
                }
        } else {
            scope.launch {
                snackbarHostState.showSnackbar(emailAddressFetchFailureMessage)
            }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,

        // Prevent the user from accessing the menu when they are not supposed to do that.
        gesturesEnabled = enableMenu,

        drawerContent = {
            ModalDrawerSheet {
                NavigationDrawerItem(
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.List,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface,
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(R.string.lessons),
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    },
                    selected = false,
                    onClick = {
                        navController.navigate(GengoScreen.Main.name)
                        toggleMenu()
                    }
                )
                NavigationDrawerItem(
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.AccountCircle,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface,
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(R.string.profile),
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    },
                    selected = false,
                    onClick = {
                        navController.navigate(GengoScreen.Profile.name)
                        toggleMenu()
                    }
                )
                NavigationDrawerItem(
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface,
                        )
                    },
                    label = {
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text(
                            text = stringResource(R.string.settings),
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    },
                    selected = false,
                    onClick = {
                        navController.navigate(GengoScreen.Settings.name)
                        toggleMenu()
                    }
                )
            }
        },
    ) {
        Scaffold(
            topBar = {
                if (currentScreen == GengoScreen.Lesson) {
                    GengoAppBar(
                        title = lessonSelected,
                        showMenuIcon = true,
                        menuIcon = Icons.Filled.ArrowBack,
                    ) {
                        navController.navigate(GengoScreen.Main.name)
                    }
                } else {
                    GengoAppBar(
                        title = stringResource(currentScreen.title),
                        showMenuIcon = enableMenu,
                        menuIcon = Icons.Filled.Menu,
                    ) {
                        toggleMenu()
                    }
                }
            },
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = GengoScreen.Loading.name,
                modifier = Modifier.padding(innerPadding),
            ) {
                composable(route = GengoScreen.Loading.name) {
                    LoadingScreen()
                    auth.addAuthStateListener {
                        enableMenu = if (auth.currentUser != null) {
                            navController.navigate(GengoScreen.Main.name)
                            true
                        } else {
                            navController.navigate(GengoScreen.SignIn.name)
                            false
                        }
                    }
                }
                composable(route = GengoScreen.SignUp.name) {
                    SignUpScreen(
                        auth = auth,
                        db = db,
                        onSignUpSuccess = {
                            scope.launch {
                                snackbarHostState.showSnackbar(signUpSuccess)
                            }
                            navController.navigate(GengoScreen.Main.name)
                            updateUsername()
                        },
                        onSignUpFailure = {
                            scope.launch {
                                snackbarHostState.showSnackbar(signUpFailure)
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
                                snackbarHostState.showSnackbar(signInSuccess)
                            }
                            navController.navigate(GengoScreen.Main.name)
                            updateUsername()
                        },
                        onSignInFailure = {
                            scope.launch {
                                snackbarHostState.showSnackbar(signInFailure)
                            }
                        },
                        onSignUpButtonClicked = { navController.navigate(GengoScreen.SignUp.name) },
                    )
                    enableMenu = false
                }
                composable(route = GengoScreen.Main.name) {
                    fetchLessonNames()
                    MainScreen(
                        lessonNames,
                        onLessonSelect = { lessonName ->
                            lessonSelected = lessonName
                            navController.navigate(GengoScreen.Lesson.name)
                        }
                    )
                    enableMenu = true
                }
                composable(route = GengoScreen.Profile.name) {
                    val email = auth.currentUser?.email

                    fetchProfilePictureUri(email)

                    if (username == usernamePlaceholder) {
                        updateUsername()
                    }

                    // TODO: Update ProfileScreen right after receiving the uri. Now it is bugged
                    // and you need to switch to another screen and come back later to see the
                    // updated picture.
                    ProfileScreen(
                        username = username,
                        email = email ?: stringResource(R.string.email),
                        profilePictureUri = profilePictureUri,
                        onLogoutClicked = {
                            auth.signOut()
                            navController.navigate(GengoScreen.SignIn.name)
                            profilePictureUri = Uri.EMPTY
                        },
                        onProfilePictureChange = { uri ->
                            val storageRef = storage.reference
                            val imageRef = storageRef.child("images/${email}.jpg")
                            val uploadTask = uri?.let {
                                imageRef.putFile(it).addOnSuccessListener {
                                    imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                                        if (email != null) {
                                            db.collection("Users")
                                                .document(email)
                                                .update("profilePictureUri", downloadUri)
                                                .addOnSuccessListener {
                                                    // TODO
                                                }.addOnFailureListener {
                                                    // TODO
                                                }
                                        }
                                    }
                                }
                            }

                            uploadTask?.addOnSuccessListener {
                                scope.launch {
                                    snackbarHostState.showSnackbar(profilePictureUploadedSuccessfully)
                                }
                                profilePictureUri = uri
                            }?.addOnFailureListener {
                                scope.launch {
                                    snackbarHostState.showSnackbar(profilePictureUploadFailure)
                                }
                            }
                        }
                    )
                    enableMenu = true
                }
                composable(route = GengoScreen.Lesson.name) {
                    if (lessonSelected.isNotEmpty()) {
                        LessonScreen(
                            fetchFields = { callback ->
                                fetchLessonFields(lessonSelected) { fields ->
                                    callback(fields)
                                }
                            },
                            onReturnClick = {
                                navController.navigate(GengoScreen.Main.name)
                            },
                        )
                    } else {
                        /* TODO */
                    }
                    enableMenu = false
                }
                composable(route = GengoScreen.Settings.name) {
                    SettingsScreen(
                        isDarkTheme = isDarkTheme,
                        fontSizePrefs = fontSizePrefs,
                        onThemeSwitch = {
                            onThemeSwitch()
                        },
                        onFontSizeChange = {
                            onFontSizeChange(it)
                        }
                    )
                    enableMenu = true
                }
            }
        }
    }
}