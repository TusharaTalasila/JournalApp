package com.example.journalapp.feature.auth

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.journalapp.R
import com.example.journalapp.ui.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel
) {
    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    var confirmPassword by remember {
        mutableStateOf("")
    }

    val authState = authViewModel.uiState.observeAsState()//observe the uiState as a live object
    val context = LocalContext.current
    LaunchedEffect(authState) {//navigation to either home or error is shown
        when (authState.value) {
            is AuthState.Authenticated -> navController.navigate("landing")
            is AuthState.Error -> Toast.makeText(
                context,
                (authState.value as AuthState.Error).message,
                Toast.LENGTH_SHORT
            ).show()

            else -> Unit
        }

    }
    Column(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.tertiary,
                    shape = RoundedCornerShape(8.dp)
                )
                .weight(1.5f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.authimage),
                contentDescription = "Illustration"
            )
        }
        Column(
            modifier = Modifier
                .padding(MaterialTheme.spacing.large)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .weight(2f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Sign Up", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

            TextField(value = email, onValueChange = { email = it }, label = {
                Text(text = "Email                                 ")
            }, shape = RoundedCornerShape(8.dp), colors = TextFieldDefaults.textFieldColors(
                containerColor = MaterialTheme.colorScheme.tertiary
            )
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.xMedium))

            TextField(
                value = password,
                onValueChange = { password = it },
                label = {
                    Text(text = "Password")
                },
                visualTransformation = PasswordVisualTransformation(),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = MaterialTheme.colorScheme.tertiary
                )
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.xMedium))

            TextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = {
                    Text(text = "Confirm Password")
                },
                visualTransformation = PasswordVisualTransformation(),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = MaterialTheme.colorScheme.tertiary
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                if ((email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) && (password.equals(confirmPassword))) {
                    authViewModel.signUp(email, password)
                }
                else if(!password.equals(confirmPassword)){
                    Toast.makeText(
                        context,
                        "Passwords don't match",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else {
                    Toast.makeText(
                        context,
                        "Email and password cannot be empty.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }) {
                Text(text = "Sign Up",
                    color = MaterialTheme.colorScheme.onPrimaryContainer)
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(onClick = { /*lambda to transport user ot sign up page*/

                navController.navigate("login")//pass in the route to move to the sign up pg
            }) {
                Text(
                    text = "Already have an account?",
                )

            }
        }
    }
}