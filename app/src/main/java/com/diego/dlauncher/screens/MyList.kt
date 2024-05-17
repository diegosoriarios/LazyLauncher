package com.diego.dlauncher.screens

import android.util.Log
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.diego.dlauncher.viewModel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyList(appViewModel: AppViewModel = AppViewModel(), onPress: (String) -> Unit, modifier: Modifier, hideAppList: (Boolean) -> Unit) {
    val appUiState by appViewModel.uiState.collectAsState()
    val apps = appUiState.apps

    val scrollState = rememberScrollState()
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    fun handleOnPress(className: String) {
        onPress(className)
    }

    if (scrollState.isScrollInProgress){
        if (scrollState.value == 0) {
            hideAppList(true)
        }

    }
        Column(
            modifier = modifier
                .verticalScroll(scrollState).fillMaxSize()
        ) {
            if (apps == null || apps.size == 0) {
                Text(text = "None")
            }

            apps!!.forEach { app ->
                MyItem(appInfo = app, ::handleOnPress)
            }
        }

}