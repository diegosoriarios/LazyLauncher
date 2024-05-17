package com.diego.dlauncher.screens

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.diego.dlauncher.viewModel.AppViewModel
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MyPage(appViewModel: AppViewModel, openItem: (String) -> Unit) {
    val sheetState = rememberModalBottomSheetState()
    val appListState = rememberModalBottomSheetState()

    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    var showAppList by remember { mutableStateOf(false) }
    var isOnTop by remember { mutableStateOf(true) }

    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                imageUri = it
            }
        }
    )

    fun closeBottomSheet() {
        scope.launch { sheetState.hide() }.invokeOnCompletion {
            if (!sheetState.isVisible) {
                showBottomSheet = false
            }
        }
    }

    Box(
        Modifier.fillMaxSize()
            //.offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .pointerInput (Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()

                    val (x,y) = dragAmount
                    when {
                        x > 0 ->{ /* right */ }
                        x < 0 ->{ /* left */ }
                    }
                    when {
                        y > 0 -> {
                            showAppList = !isOnTop
                        }
                        y < 0 -> {
                            showAppList = true
                        }
                    }

                    offsetX += dragAmount.x
                    offsetY += dragAmount.y
                }
            }
    ) {
        if (imageUri != null) {
            Image(
                painter = rememberAsyncImagePainter(model = imageUri),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }

        if (showAppList) {
            MyList(appViewModel, onPress = openItem, modifier = Modifier
                .combinedClickable (
                    onClick = {  },
                    onLongClick = {
                        showBottomSheet = true
                    },
                ),
                hideAppList = { newValue ->  // pass callback function to child Composable
                    showAppList = !newValue    // set updated value received from child Composable
                }
            )
        } else {
            MyFavorites(appViewModel, onPress = openItem, modifier = Modifier
                .combinedClickable (
                    onClick = {  },
                    onLongClick = {
                        showBottomSheet = true
                    },
                )
            )
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
                                galleryLauncher.launch("image/*")
                            }
                        ) {
                            Text(
                                text = "Change Wallpaper"
                            )
                        }
                    }
                    HorizontalDivider()
                    Row (modifier = Modifier.padding(16.dp)) {
                        Text("Change Favorites")
                    }
                }
            }
        }
    }
}