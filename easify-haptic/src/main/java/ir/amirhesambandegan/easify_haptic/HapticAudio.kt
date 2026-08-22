package ir.amirhesambandegan.easify_haptic

import android.content.Context
import android.media.MediaPlayer
import android.media.audiofx.HapticGenerator
import android.os.Build

/**
 * Plays an audio file and generates matching haptic feedback in real-time (Android 12+).
 * 
 * @property context The application or activity context.
 * @property audioResId The resource ID of the audio file to play.
 */
class AudioHapticPlayer(private val context: Context, private val audioResId: Int) {
    /**
     * The internal media player instance.
     */
    private var mediaPlayer: MediaPlayer? = null
    
    /**
     * The haptic generator instance that creates haptic feedback from the audio session.
     */
    private var hapticGenerator: HapticGenerator? = null

    /**
     * Starts playing the audio file and generating the corresponding haptic feedback.
     */
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

    /**
     * Releases the media player and haptic generator resources.
     */
    fun release() {
        hapticGenerator?.release()
        mediaPlayer?.release()
        hapticGenerator = null
        mediaPlayer = null
    }
}
