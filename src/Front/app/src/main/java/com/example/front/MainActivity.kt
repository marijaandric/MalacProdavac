    package com.example.front

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.front.model.LoginDTO
import com.example.front.repository.Repository
import com.example.front.ui.theme.FrontTheme
import com.example.front.viewmodels.login.LoginViewModel
import com.example.front.viewmodels.login.MainViewModelFacotry

    class MainActivity : ComponentActivity() {
    private lateinit var viewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FrontTheme {
                //LoginScreen(viewModel = loginViewModel)
            }
            val repository = Repository()
            val viewModelFacotry = MainViewModelFacotry(repository)
            viewModel = ViewModelProvider(this,viewModelFacotry).get(LoginViewModel::class.java)
            var data = LoginDTO("marija.andric","MejoSmrdi123!")
            viewModel.getLoginnInfo(data)
            val response = viewModel.myResponse.value
            Log.e("RESPONSEEE",response.toString())
        }
    }
}


@Composable
@Preview(showBackground = true)
fun SplashScreenAndIntro() {
    SplashScreen().Navigation()
}

