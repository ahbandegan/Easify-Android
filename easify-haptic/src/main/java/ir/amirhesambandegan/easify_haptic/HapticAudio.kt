package ir.amirhesambandegan.easify_haptic

import android.content.Context
import android.media.MediaPlayer
import android.media.audiofx.HapticGenerator
import android.os.Build

/**
 * Plays an audio file and generates matching haptic feedback in real-time (Android 12+).
 */
class AudioHapticPlayer(private val context: Context, private val audioResId: Int) {
    private var mediaPlayer: MediaPlayer? = null
    private var hapticGenerator: HapticGenerator? = null

    fun play() {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(context, audioResId)
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && HapticGenerator.isAvailable()) {
            mediaPlayer?.audioSessionId?.let { sessionId ->
                try {
                    hapticGenerator = HapticGenerator.create(sessionId)
                    hapticGenerator?.enabled = true
                } catch (e: Exception) {
                    // Haptic generator not supported on this specific hardware
                }
            }
        }
        
        mediaPlayer?.start()
    }

    fun release() {
        hapticGenerator?.release()
        mediaPlayer?.release()
        hapticGenerator = null
        mediaPlayer = null
    }
}
