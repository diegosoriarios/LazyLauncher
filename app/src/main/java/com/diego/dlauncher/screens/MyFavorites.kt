package com.diego.dlauncher.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.diego.dlauncher.viewModel.AppViewModel

@Composable
fun MyFavorites(appViewModel: AppViewModel = AppViewModel(LocalContext.current), modifier: Modifier) {
    val appUiState by appViewModel.uiState.collectAsState()
    val favorites = appUiState.favorites

    fun handleOnPress(className: String) {
        appViewModel.openItem(className)
    }

    Column {
        if (favorites == null || favorites.size == 0) {
            Text(text = "None")
        }

        MyClock()

        favorites!!.forEach { app ->
            MyItem(appInfo = app, ::handleOnPress)
        }
    }
}