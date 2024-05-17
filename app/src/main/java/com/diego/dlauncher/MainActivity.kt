package com.diego.dlauncher

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.diego.dlauncher.screens.MyList
import com.diego.dlauncher.screens.MyPage
import com.diego.dlauncher.ui.theme.DLauncherTheme
import com.diego.dlauncher.viewModel.AppViewModel


class MainActivity : ComponentActivity() {
    val appViewModel = AppViewModel()

    fun openItem(className: String) {
        Log.d("LAUNCHER", className)
        val launchIntent: Intent? =
            packageManager.getLaunchIntentForPackage(className)

        if (launchIntent != null) {
            startActivity(launchIntent)
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appViewModel.loadApps(packageManager)

        setContent {
            DLauncherTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .combinedClickable(
                            onClick = { },
                            onLongClick = {
                            },
                        ),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyPage(appViewModel, ::openItem)
                }
            }
        }
    }
}

fun mock(v: String) {}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DLauncherTheme {
        MyList(AppViewModel(), ::mock, Modifier, {newValue -> })
    }
}