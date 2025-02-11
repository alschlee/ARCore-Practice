package com.example.arpracticeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.ar.core.ArCoreApk
import com.example.arpracticeapp.ui.theme.ARPracticeAppTheme

class MainActivity : ComponentActivity() {

    private var mArButtonVisible by mutableStateOf(false)  // AR 버튼

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ARPracticeAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("AR Practice App")

                        Spacer(modifier = Modifier.height(16.dp))

                        // AR 버튼이 보이는 경우 버튼 표시
                        if (mArButtonVisible) {
                            Button(onClick = { /* AR 세션 시작 로직 */ }) {
                                Text("Start AR")
                            }
                        } else {
                            // ARCore 미지원일 경우 버튼이 안 보임
                            Text("ARCore를 지원하는 기기가 아닙니다.")
                        }
                    }
                }
            }
        }

        // ARCore 지원 여부 확인
        maybeEnableArButton()
    }

    // ARCore 지원 여부를 확인하여 AR 버튼 활성화 or 비활성화
    private fun maybeEnableArButton() {
        ArCoreApk.getInstance().checkAvailabilityAsync(this) { availability ->
            mArButtonVisible = availability.isSupported
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ARPracticeAppTheme {
        Text("Hello Android!")
    }
}
