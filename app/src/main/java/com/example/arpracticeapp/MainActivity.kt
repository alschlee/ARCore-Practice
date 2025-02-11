package com.example.arpracticeapp

import android.os.Bundle
import android.widget.Toast
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
import com.google.ar.core.Session
import com.google.ar.core.exceptions.UnavailableUserDeclinedInstallationException
import com.example.arpracticeapp.ui.theme.ARPracticeAppTheme
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : ComponentActivity() {

    private var mSession: Session? = null  // AR 세션 객체
    private var mUserRequestedInstall = true  // ARCore 설치 요청 여부
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
                            Button(onClick = { startArSession() }) {
                                Text("Start AR")
                            }
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
            if (availability.isSupported) {
                // ARCore 지원 시 설치 여부 확인
                checkArCoreInstallation()
            } else {
                mArButtonVisible = false  // ARCore 미지원 시 버튼 안보이기
            }
        }
    }

    // ARCore 설치 여부 확인
    private fun checkArCoreInstallation() {
        try {
            when (ArCoreApk.getInstance().requestInstall(this, mUserRequestedInstall)) {
                ArCoreApk.InstallStatus.INSTALLED -> {
                    // ARCore가 설치되어 있으면 AR 세션 시작
                    mSession = Session(this)
                    mArButtonVisible = true  // AR 버튼 보이기
                }
                ArCoreApk.InstallStatus.INSTALL_REQUESTED -> {
                    // 설치가 요청된 경우
                    mUserRequestedInstall = false
                }
            }
        } catch (e: UnavailableUserDeclinedInstallationException) {
            // 사용자가 설치를 거부한 경우
            Toast.makeText(this, "ARCore 설치 필요", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(this, "오류 발생: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    // 카메라 권한 요청
    private fun requestCameraPermission() {
        val permission = android.Manifest.permission.CAMERA
        if (ContextCompat.checkSelfPermission(this, permission) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), 1)
        } else {
            // 권한이 이미 있다면 AR 세션을 시작
            startArSession()
        }
    }

    private fun startArSession() {
        // AR 세션
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ARPracticeAppTheme {
        Text("Hello Android!")
    }
}
