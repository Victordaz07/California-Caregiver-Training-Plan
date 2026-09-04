package com.caregiverproca.app.ui.components

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.caregiverproca.app.audio.VoiceRecorderController
import com.caregiverproca.app.content.Disclaimers
import java.io.File

/**
 * Self-contained record/play/delete control for voice practice. One instance
 * owns one recording slot (a screen with several drills — role-play, handoff
 * — instantiates one per drill, keyed by `remember(promptId)` at the call
 * site so switching prompts starts a fresh slot).
 *
 * Permission flow matches Fase 6: an in-app rationale (content/Disclaimers.kt
 * `voice` text) is shown before the system permission dialog, not after a
 * denial.
 */
@Composable
fun VoiceRecordCard(promptLabel: String) {
    val context = LocalContext.current
    val controller = remember { VoiceRecorderController(context) }

    var isRecording by remember { mutableStateOf(false) }
    var pendingFile by remember { mutableStateOf<File?>(null) }
    var recordedFile by remember { mutableStateOf<File?>(null) }
    var isPlaying by remember { mutableStateOf(false) }
    var showRationale by remember { mutableStateOf(false) }
    var permissionDenied by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        onDispose { controller.release() }
    }

    fun beginRecording() {
        recordedFile = null
        val file = controller.startRecording()
        pendingFile = file
        isRecording = file != null
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { granted ->
        if (granted) beginRecording() else permissionDenied = true
    }

    fun hasRecordPermission(): Boolean =
        ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED

    SectionCard {
        Text(promptLabel, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)

        if (showRationale) {
            DisclaimerBanner(text = Disclaimers.Voice)
            Button(
                onClick = {
                    showRationale = false
                    permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ),
            ) {
                Text("Entiendo, permitir micrófono")
            }
        } else {
            if (permissionDenied) {
                Text(
                    "No se concedió el permiso de micrófono. Puedes habilitarlo en Ajustes del sistema para practicar con grabación.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (isRecording) {
                    Button(
                        onClick = {
                            controller.stopRecording()
                            recordedFile = pendingFile
                            isRecording = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError,
                        ),
                    ) {
                        Icon(Icons.Outlined.Stop, contentDescription = null)
                        Spacer(Modifier.width(6.dp))
                        Text("Detener")
                    }
                } else {
                    Button(
                        onClick = {
                            if (hasRecordPermission()) beginRecording() else showRationale = true
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSecondary,
                        ),
                    ) {
                        Icon(Icons.Outlined.Mic, contentDescription = null)
                        Spacer(Modifier.width(6.dp))
                        Text("Grabar")
                    }
                }

                val currentFile = recordedFile
                if (currentFile != null && !isRecording) {
                    OutlinedButton(
                        onClick = {
                            isPlaying = true
                            controller.play(currentFile) { isPlaying = false }
                        },
                    ) {
                        Icon(Icons.Outlined.PlayArrow, contentDescription = null)
                        Spacer(Modifier.width(6.dp))
                        Text(if (isPlaying) "Reproduciendo…" else "Escuchar")
                    }
                    OutlinedButton(
                        onClick = {
                            controller.delete(currentFile)
                            recordedFile = null
                        },
                    ) {
                        Icon(Icons.Outlined.Delete, contentDescription = null)
                        Spacer(Modifier.width(6.dp))
                        Text("Borrar")
                    }
                }
            }
        }
    }
}
