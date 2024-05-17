package com.diego.dlauncher.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.diego.dlauncher.model.AppInfo
import com.diego.dlauncher.viewModel.AppUIState
import com.diego.dlauncher.viewModel.AppViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MyItem(appInfo: AppInfo, onPress: (String) -> Unit) {
    val appViewModel = AppViewModel(LocalContext.current)
    val sheetState = rememberModalBottomSheetState()

    var showBottomSheet by remember { mutableStateOf(false) }

    fun handlePress() {
        onPress(appInfo.name.toString())
    }

    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
        .fillMaxWidth()
        //.clickable { handlePress() }
        .combinedClickable(
            onClick = { handlePress() },
            onLongClick = {
                showBottomSheet = true
            },
        )
    ) {
        Image(
            bitmap = appInfo.icon!!,
            contentDescription = "Content description for visually impaired",
            modifier = Modifier
                .size(50.dp)
                .padding(horizontal = 5.dp)
        )
        Text(text = appInfo.label.toString())
    }


    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            },
            sheetState = sheetState
        ) {
            // Sheet content
            Column {
                Row (modifier = Modifier.padding(16.dp)) {
                    TextButton(
                        onClick = {
                            appViewModel.uninstallApp(appInfo = appInfo)
                        }
                    ) {
                        Text(
                            text = "Uninstall App"
                        )
                    }
                }
                HorizontalDivider()
                Row (modifier = Modifier.padding(16.dp)) {
                    TextButton(
                        onClick = {
                            appViewModel.openConfiguration(appInfo = appInfo)
                        }
                    ) {
                        Text(
                            text = "App Info"
                        )
                    }
                }
            }
        }
    }
}