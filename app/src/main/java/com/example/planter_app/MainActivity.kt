package com.example.planter_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import com.example.planter_app.firebase_login.sign_in.GoogleAuthUiClient
import com.example.planter_app.navigation_drawer.AppBar
import com.example.planter_app.navigation_drawer.NavigationDrawer
import com.example.planter_app.firebase_login.sign_in.SignInScreen
import com.example.planter_app.screens.home.HomeScreen
import com.example.planter_app.screens.settings.SettingsViewModel
import com.example.planter_app.ui.theme.Planter_appTheme
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch




class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Planter_appTheme(
                darkTheme = SettingsViewModel.darkMode.value,
                dynamicColor = SettingsViewModel.dynamicTheme.value
            ) {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val settingsViewModel = viewModel<SettingsViewModel>()

                    // if user is logged in, then go to home screen. else sign in screen
                    val isUserLoggedIn =  settingsViewModel.isUserLoggedIn()

                    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                    val scope = rememberCoroutineScope()

                    //scrollBehaviour is to implement app bar color change once user starts scrolling the screen items
                    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

                    Scaffold(
                        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                        topBar = {
                            AppBar(
                                onNavigationIconClick = {
                                    scope.launch {
                                        if (drawerState.isClosed) {
                                            drawerState.open()
                                        } else drawerState.close()
                                    }
                                },
                                scrollBehavior
                            )
                        }
                    ) { paddingVales ->
                        //Initial screen -> SignIn screen
                        Navigator(
                            screen = if (isUserLoggedIn) HomeScreen else SignInScreen)
                        { navigator ->
                            NavigationDrawer(
                                drawerState = drawerState,
                                scope = scope,
                                paddingValues = paddingVales,
                                navigator = navigator
                            )
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Planter_appTheme {

    }
}