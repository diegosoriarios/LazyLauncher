package com.diego.dlauncher

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.R
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.diego.dlauncher.model.AppInfo
import com.diego.dlauncher.screens.MyItem
import com.diego.dlauncher.screens.MyPage
import com.diego.dlauncher.ui.theme.DLauncherTheme
import com.diego.dlauncher.viewModel.AppViewModel

class FavoriteActivity : ComponentActivity() {
    val appViewModel = AppViewModel(this)

    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appViewModel.loadApps(packageManager)
        appViewModel.getWallpaper(this)
        appViewModel.fetchFavorites(this)

        setContent {
            var selected by remember { mutableStateOf<ArrayList<AppInfo>>(arrayListOf()) }
            val scrollState = rememberScrollState()

            fun handleOnClick() {
                selected.forEach {
                    appViewModel.updateFavorites(this, it)
                }
            }

            DLauncherTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        Modifier.fillMaxSize()){
                        Column {
                            Text("Select or favorite apps!")
                            Text("select at least 2 apps")
                        }
                        Column(modifier = Modifier.verticalScroll(scrollState)) {
                            appViewModel.uiState.value.apps!!.forEach { appInfo ->
                                var isSelected by remember { mutableStateOf(false) }

                                Row(verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .combinedClickable(
                                            onClick = {
                                                var isAlreadyOnList =
                                                    selected.any { it.label == appInfo.label }
                                                if (!isAlreadyOnList) {
                                                    selected.add(appInfo)
                                                } else {
                                                    selected.remove(appInfo)
                                                }
                                                isSelected = !isAlreadyOnList
                                            }
                                        )
                                        .fillMaxWidth()
                                ) {
                                    if (isSelected) {
                                        AnimatedVisibility(
                                            visible = isSelected,
                                            exit = shrinkOut(shrinkTowards = Alignment.TopStart) + fadeOut(),
                                            modifier = Modifier
                                                .size(50.dp)
                                                //.padding(horizontal = 5.dp)
                                                .background(Color.White, shape = CircleShape)
                                        ) {
                                            //the check only (without the surrounding box)
                                            Image(
                                                painter = painterResource(id = R.drawable.btn_checkbox_checked_mtrl),
                                                contentDescription = "Checked"
                                            )
                                        }
                                    } else {
                                        Image(
                                            bitmap = appInfo.icon!!,
                                            contentDescription = "Content description for visually impaired",
                                            modifier = Modifier
                                                .size(50.dp)
                                                .padding(horizontal = 5.dp)
                                        )
                                    }
                                    Text(text = appInfo.label.toString())
                                }
                            }
                            Row {
                                Spacer(Modifier.weight(1f))
                                ExtendedFloatingActionButton(onClick = {
                                    handleOnClick()
                                }, elevation = FloatingActionButtonDefaults.elevation(
                                    defaultElevation = 4.dp
                                )
                                ) {
                                    Text("Salvar")
                                }
                            }
                        }
                    }
                    }
            }
        }
    }
}