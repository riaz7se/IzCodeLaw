package com.izcode.law.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.touchlab.kermit.Logger
import com.izcode.law.getPlatform
import izcodelaw.composeapp.generated.resources.Res
import izcodelaw.composeapp.generated.resources.appLogo
import izcodelaw.composeapp.generated.resources.fb
import izcodelaw.composeapp.generated.resources.google
import izcodelaw.composeapp.generated.resources.x
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import com.izcode.law.auth.GoogleSignInManager
import com.izcode.law.auth.state.UserState
import kotlinx.coroutines.launch

@Preview
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    googleSignInManager: GoogleSignInManager? = null
) {
    val viewModel = remember { LoginViewModel() }
    val state by viewModel.state.collectAsState()

    var platform by remember { mutableStateOf(getPlatform().name) }
    var signInError by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()

    LaunchedEffect(state.isLoginSuccessful) {
        if (state.isLoginSuccessful) {
            onLoginSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painterResource(Res.drawable.appLogo), contentDescription = "Login image",
            modifier = Modifier.size(200.dp))

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "IzCodeLaw", fontSize = 28.sp, fontWeight = FontWeight.Bold)

//        TextField(
//            value = state.username,
//            onValueChange = { viewModel.onEvent(LoginEvent.OnUsernameChange(it)) },
//            label = { Text("Username") },
//            modifier = Modifier.fillMaxWidth()
//        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.username,
            onValueChange = { viewModel.onEvent(LoginEvent.OnUsernameChange(it)) },
            label = { Text("Username") }
        )

        Spacer(modifier = Modifier.height(16.dp))

//        TextField(
//            value = state.password,
//            onValueChange = { viewModel.onEvent(LoginEvent.OnPasswordChange(it)) },
//            label = { Text("Password") },
//            modifier = Modifier.fillMaxWidth(),
//            visualTransformation = PasswordVisualTransformation()
//        )

        OutlinedTextField(
            value = state.password,
            onValueChange =  { viewModel.onEvent(LoginEvent.OnPasswordChange(it)) },
            label = { Text(text = "Password")},
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.onEvent(LoginEvent.OnLoginClick) },
            //modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(color = MaterialTheme.colors.onPrimary)
            } else {
                Text("Login")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(text = "Forgot Password?", modifier = Modifier.clickable {

        })

        Spacer(modifier = Modifier.height(32.dp))

        Text(text = "Or sign in with")

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(40.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Image(painterResource(Res.drawable.fb),
                contentDescription = "Facebook",
                modifier = Modifier
                    .size(60.dp)
                    .clickable {
                        //Facebook clicked
//                        Log.i("Facebook Clicked....","Email : $email Password : $password")
                    }
            )

            Image(
                painterResource(Res.drawable.google),
                contentDescription = "Google",
                modifier = Modifier
                    .size(60.dp)
                    .clickable {
                        googleSignInManager?.let { manager ->
                            scope.launch {
                                try {
                                    manager.signIn()
                                        .onSuccess {result ->
                                            Logger.i { "onLoginSuccess: ${result}" }
//                                            onLoginSuccess()
                                            UserState.updateUser(result)
                                        }
                                        .onFailure { e ->
                                            signInError = e.message
                                        }
                                } catch (e: Exception) {
                                    signInError = e.message
                                }
                            }
                        }
                    }
            )

            Image(painterResource(Res.drawable.x),
                contentDescription = "Twitter",
                modifier = Modifier
                    .size(60.dp)
                    .clickable {
                        //Twitter clicked
                    }
            )

        }

        signInError?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colors.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
        Text("Device: $platform")
    }
} 