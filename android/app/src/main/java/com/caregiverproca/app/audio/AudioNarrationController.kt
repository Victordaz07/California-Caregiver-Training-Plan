package com.caregiverproca.app.audio

import android.content.Context
import android.media.MediaPlayer
import com.caregiverproca.app.R

/**
 * Plays the 13 bundled narrated episodes (res/raw/audio_w01.mp3..audio_w13.mp3),
 * generated with Google Cloud Text-to-Speech and committed as static app resources
 * — see docs/caregiver_upgrade/AUDIO_GENERATION_PROMPT.md and DECISIONS.md ADR-006.
 * The API key used to generate them was never part of the app: these are plain,
 * offline audio files, played with the platform MediaPlayer like any other
 * bundled resource. Only one episode plays at a time.
 */
class AudioNarrationController(private val context: Context) {
    private var player: MediaPlayer? = null

    fun play(resId: Int, onCompletion: () -> Unit) {
        stop()
        runCatching {
            val newPlayer = MediaPlayer.create(context, resId)
            newPlayer.setOnCompletionListener {
                it.release()
                if (player === it) player = null
                onCompletion()
            }
            newPlayer.start()
            player = newPlayer
        }.onFailure { onCompletion() }
    }

    fun stop() {
        player?.let { runCatching { it.stop() } }
        player?.release()
        player = null
    }

    /** Call from DisposableEffect(onDispose) to avoid leaking the native player. */
    fun release() = stop()
}

fun audioResIdFor(episodeId: String): Int? = when (episodeId) {
    "audio_w01" -> R.raw.audio_w01
    "audio_w02" -> R.raw.audio_w02
    "audio_w03" -> R.raw.audio_w03
    "audio_w04" -> R.raw.audio_w04
    "audio_w05" -> R.raw.audio_w05
    "audio_w06" -> R.raw.audio_w06
    "audio_w07" -> R.raw.audio_w07
    "audio_w08" -> R.raw.audio_w08
    "audio_w09" -> R.raw.audio_w09
    "audio_w10" -> R.raw.audio_w10
    "audio_w11" -> R.raw.audio_w11
    "audio_w12" -> R.raw.audio_w12
    "audio_w13" -> R.raw.audio_w13
    else -> null
}
