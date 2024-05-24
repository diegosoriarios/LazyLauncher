package com.diego.lazylauncher

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.diego.lazylauncher.screens.MyList
import com.diego.lazylauncher.screens.MyPage
import com.diego.lazylauncher.ui.theme.DLauncherTheme
import com.diego.lazylauncher.viewModel.AppViewModel

class AppActivity : ComponentActivity() {
    val appViewModel = AppViewModel(this)

    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appViewModel.loadApps(packageManager)

        setContent {
            DLauncherTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyList(appViewModel, modifier = Modifier
                        .combinedClickable (
                            onClick = {  },
                            onLongClick = {
                                //showBottomSheet = true
                            },
                        ),
                        hideAppList = { newValue ->  // pass callback function to child Composable
                            //showAppList = !newValue    // set updated value received from child Composable
                        },
                        image = appViewModel.getWallpaper(this)
                    )
                }
            }
        }
    }
}