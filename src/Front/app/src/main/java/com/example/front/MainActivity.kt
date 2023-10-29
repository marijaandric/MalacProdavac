    package com.example.front

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import com.example.front.app.PostOfficeApp
import com.example.front.repository.Repository
import com.example.front.screens.HomePage
import com.example.front.screens.RegistrationCategories
import com.example.front.viewmodels.login.LoginViewModel
import com.example.front.viewmodels.login.MainViewModelFacotry
import com.example.front.screens.SplashScreen
import com.example.front.ui.theme.FrontTheme
import com.example.front.viewmodels.categories.CategoriesMainViewModelFactory
import com.example.front.viewmodels.categories.CategoriesViewModel

    class MainActivity : ComponentActivity() {
    private lateinit var viewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
//            val repository = Repository()
//            val viewModelFactory = CategoriesMainViewModelFactory(repository)
//            viewModel = ViewModelProvider(this,viewModelFactory).get(CategoriesViewModel::class.java)
//            viewModel.getCategoriesInfo()
            //PostOfficeApp(viewModel);
            //HomePage()
            FrontTheme {
                RegistrationCategories()
            }
        }
    }
}


@Composable
fun SplashScreenAndIntro() {
    SplashScreen().Navigation()
}
