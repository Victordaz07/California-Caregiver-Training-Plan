package com.caregiverproca.app.audio

import android.content.Context
import android.media.MediaPlayer
import android.media.MediaRecorder
import java.io.File
import java.util.UUID

/**
 * Records and plays back the learner's own voice for practice (role-play,
 * shift handoff), per Fase 6 and content/Disclaimers.kt's `voice` notice:
 * only the learner's voice, private app storage, opaque filename, delete any
 * time. No upload path exists — recordings never leave the device.
 *
 * Uses the platform `android.media.MediaRecorder`/`MediaPlayer` rather than
 * Media3, because the only playback need here is a short local self-recording,
 * not streaming or downloads — see docs/caregiver_upgrade/DECISIONS.md ADR-006
 * for why Media3 itself isn't added in this batch (there is no narrated audio
 * content in the source pack to play).
 */
class VoiceRecorderController(private val context: Context) {
    private var recorder: MediaRecorder? = null
    private var player: MediaPlayer? = null

    private val storageDir: File
        get() = File(context.filesDir, "voice_practice").apply { mkdirs() }

    /** Returns the created file on success, or null if recording could not start (e.g. no space). */
    fun startRecording(): File? {
        val file = File(storageDir, "practice_${UUID.randomUUID()}.m4a")
        return runCatching {
            @Suppress("DEPRECATION") // MediaRecorder(Context) needs API 31; this app supports minSdk 26.
            val newRecorder = MediaRecorder()
            newRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
            newRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            newRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            newRecorder.setOutputFile(file.absolutePath)
            newRecorder.prepare()
            newRecorder.start()
            recorder = newRecorder
            file
        }.getOrNull()
    }

    /** Stops the active recording. Safe to call even if nothing is recording. */
    fun stopRecording() {
        runCatching {
            recorder?.stop()
        }
        recorder?.release()
        recorder = null
    }

    fun play(file: File, onCompletion: () -> Unit) {
        stopPlayback()
        runCatching {
            val newPlayer = MediaPlayer()
            newPlayer.setDataSource(file.absolutePath)
            newPlayer.setOnCompletionListener {
                it.release()
                if (player === it) player = null
                onCompletion()
            }
            newPlayer.prepare()
            newPlayer.start()
            player = newPlayer
        }.onFailure { onCompletion() }
    }

    fun stopPlayback() {
        player?.let { runCatching { it.stop() } }
        player?.release()
        player = null
    }

    fun delete(file: File) {
        stopPlayback()
        file.delete()
    }

    /** Call from DisposableEffect(onDispose) to avoid leaking native recorder/player resources. */
    fun release() {
        runCatching { recorder?.stop() }
        recorder?.release()
        recorder = null
        player?.release()
        player = null
    }
}
