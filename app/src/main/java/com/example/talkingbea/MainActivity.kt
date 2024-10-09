package com.example.talkingbea

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.talkingbea.ui.theme.TalkingBeaTheme
import kotlinx.coroutines.delay
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.talkingbea.ui.theme.Purple40
import com.example.talkingbea.ui.theme.Purple80
import com.example.talkingbea.ui.theme.PurpleGrey80


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()
        enableEdgeToEdge()
        setContent {
            TalkingBeaTheme {
                FunnyImageApp()
            }
        }
    }
}

@SuppressLint("AutoboxingStateCreation")
@Composable
fun FunnyImageApp() {
//    Condicionais
    var isMouthOpen by remember { mutableStateOf(false) }
    var isPlaying by remember { mutableStateOf(false) }
    var isBlinking by remember { mutableStateOf(false) } // Estado para a piscada

    val context = LocalContext.current
// Imagens
    val logo = painterResource(id = R.drawable.logo_img)
    val closedMouthImage = painterResource(id = R.drawable.bea_idle)
    val openMouthImage = painterResource(id = R.drawable.bea_falando)
    val blinkingImage = painterResource(id = R.drawable.bea_idle_piscando) // Imagem de piscada

//    Audios
    val audioResIds = listOf(R.raw.audio_eima, R.raw.audio_prakrlma)

    var playingCount by remember { mutableIntStateOf(0) }

    fun playRandomAudio() {
        val mediaPlayer = MediaPlayer.create(context, audioResIds.random())
        playingCount++
        isMouthOpen = true

        mediaPlayer.setOnCompletionListener {
            it.release()  // Libera o MediaPlayer após a reprodução
            playingCount--  // Decrementa a contagem de áudios tocando

            if (playingCount < 0) {
                playingCount = 0
            }

            if (playingCount == 0) {
                isMouthOpen = false
            }
        }
        mediaPlayer.start()
    }


    val scale by animateFloatAsState(
        targetValue = if (isMouthOpen) 1.2f else 1f, animationSpec = tween(
            durationMillis = 400, easing = LinearOutSlowInEasing
        ), label = ""
    )

    LaunchedEffect(isMouthOpen) {
        if (!isMouthOpen) {
            while (true) {
                val randomDelay =
                    (2000..5000).random().toLong() // Intervalo aleatório entre 1 e 3 segundos
                delay(randomDelay)
                if (!isMouthOpen) {
                    isBlinking = true
                    delay(150)
                    isBlinking = false
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Yellow)
            .padding(30.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))
//        Logo
        Image(
            painter = logo, contentDescription = null, modifier = Modifier
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(4f), contentAlignment = Alignment.Center
        ) {

            Image(
                painter = if (isBlinking) blinkingImage else if (isMouthOpen) openMouthImage else closedMouthImage,
                contentDescription = null,
                modifier = Modifier
                    .scale(scale)
                    .size(300.dp)
            )
        }


        Spacer(modifier = Modifier.height(20.dp))

        // Botão
        Button(
            onClick = { playRandomAudio() },
            modifier = Modifier
                .padding(25.dp)
                .fillMaxWidth()
                .height(60.dp)
                .background(color = PurpleGrey80, shape = RoundedCornerShape(30.dp))

        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,  // Icone de Play
                contentDescription = "Play", modifier = Modifier.size(40.dp)
            )
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewFunnyImageApp() {
    MaterialTheme {
        FunnyImageApp()
    }
}


