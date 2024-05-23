package com.diego.dlauncher.screens

import android.app.WallpaperManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import coil.compose.rememberAsyncImagePainter
import com.diego.dlauncher.AppActivity
import com.diego.dlauncher.FavoriteActivity
import com.diego.dlauncher.viewModel.AppViewModel
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MyPage(appViewModel: AppViewModel) {
    val sheetState = rememberModalBottomSheetState()
    val appListState = rememberModalBottomSheetState()
    val mWallpaperManager = WallpaperManager.getInstance(LocalContext.current)

    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    var showConfigBottomSheet by remember { mutableStateOf(false) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    var showAppList by remember { mutableStateOf(false) }
    var isOnTop by remember { mutableStateOf(true) }
    val context = LocalContext.current

    var imageUri by remember {
        mutableStateOf<Uri?>(appViewModel.wallpaper)
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                appViewModel.changeWallpaper(it)
                imageUri = it
            }
            showConfigBottomSheet = false
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
        Modifier
            .fillMaxSize()
            //.offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .combinedClickable(
                onClick = { },
                onLongClick = {
                    showConfigBottomSheet = true
                },
            )
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()

                    val (x, y) = dragAmount
                    when {
                        x > 0 -> { /* right */
                        }

                        x < 0 -> { /* left */
                        }
                    }
                    when {
                        y > 0 -> {
                            showAppList = !isOnTop
                        }

                        y < 0 -> {
                            //showAppList = true
                            appViewModel.navigateTo(AppActivity::class.java)
                        }
                    }

                    offsetX += dragAmount.x
                    offsetY += dragAmount.y
                }
            }
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = imageUri),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )


            MyFavorites(appViewModel, modifier = Modifier
                .combinedClickable (
                    onClick = {  },
                    onLongClick = {
                        showBottomSheet = true
                    },
                )
            )

        if (showConfigBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showConfigBottomSheet = false
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
                        TextButton(
                            onClick = {
                                val intent = Intent(context, FavoriteActivity::class.java)
                                context.startActivity(intent, Bundle())
                            }
                        ) {
                            Text(
                                text = "Change Favorites"
                            )
                        }
                    }
                }
            }
        }
    }
}