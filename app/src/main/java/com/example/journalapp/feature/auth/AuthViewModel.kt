package com.example.journalapp.feature.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel :ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()//how we get the auth state (authenticated,unauthenticated, error)

    private val _uiState = MutableLiveData<AuthState>()
    val uiState: LiveData<AuthState> = _uiState

    init{
        checkAuthStatus() //must check upon initializing the vm
    }

    fun checkAuthStatus(){//checks if user is logged in or not
        if(auth.currentUser == null){//user is not logged in
            _uiState.value = AuthState.Unauthenticated
        }else{
            _uiState.value = AuthState.Authenticated
        }
    }

    fun login(email: String, password: String){//login page method
        _uiState.value =
            AuthState.Loading //while call to firebase is being made default state is loading
        //Use firebase methods to sign in
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener{task->//stores return value of function
            if(task.isSuccessful){
                _uiState.value = AuthState.Authenticated//user was successfully logged in
            }else{
                _uiState.value = AuthState.Error(task.exception?.message ?: "Something went wrong")
            }
        }
    }

    fun signUp(email: String, password: String){//sign up oage method
        _uiState.value =
            AuthState.Loading //while call to firebase is being made default state is loading
        //Use firebase methods to sign up
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{task->//stores return value of function
            if(task.isSuccessful){
                _uiState.value = AuthState.Authenticated//user was successfully logged in
            }else{
                _uiState.value = AuthState.Error(task.exception?.message ?: "Something went wrong")
            }
        }
    }

    fun signOut(){ //home page method
        //firebase provides this method
        auth.signOut()
        _uiState.value = AuthState.Unauthenticated //user will be redirected to login page
    }
}

sealed class AuthState{//use these states in UI to know what to display
object Authenticated: AuthState() //if so go to home page
    object Unauthenticated: AuthState() //if so go to login page
    object Loading : AuthState()
    data class Error(val message : String): AuthState() //if so show error message
}