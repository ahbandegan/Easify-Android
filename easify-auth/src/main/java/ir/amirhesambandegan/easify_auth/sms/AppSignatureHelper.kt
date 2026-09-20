package ir.amirhesambandegan.easify_auth.sms

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Base64
import android.util.Log
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.Arrays

/**
 * Utility helper to compute application signature hashes for Google Play Services SMS Retriever API.
 * The resulting 11-character hash must be included at the end of the SMS message so Google Play Services
 * can route the incoming SMS directly to this app without requiring android.permission.READ_SMS.
 */
object AppSignatureHelper {

    private const val TAG = "AppSignatureHelper"
    private const val HASH_TYPE = "SHA-256"
    private const val NUM_HASHED_BYTES = 9
    private const val NUM_BASE64_CHARS = 11

    /**
     * Gets all valid 11-character signature hashes for this application.
     */
    fun getAppSignatures(context: Context): List<String> {
        val appCodes = ArrayList<String>()
        try {
            val packageName = context.packageName
            val packageManager = context.packageManager
            val signatures = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val packageInfo = packageManager.getPackageInfo(
                    packageName,
                    PackageManager.GET_SIGNING_CERTIFICATES
                )
                val signingInfo = packageInfo.signingInfo
                if (signingInfo != null) {
                    if (signingInfo.hasMultipleSigners()) {
                        signingInfo.apkContentsSigners
                    } else {
                        signingInfo.signingCertificateHistory
                    }
                } else null
            } else {
                @Suppress("DEPRECATION")
                val packageInfo = packageManager.getPackageInfo(
                    packageName,
                    PackageManager.GET_SIGNATURES
                )
                @Suppress("DEPRECATION")
                packageInfo.signatures
            }

            if (signatures != null) {
                for (sig in signatures) {
                    val hash = hash(packageName, sig.toCharsString())
                    if (hash != null) {
                        appCodes.add(hash)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Unable to get package signatures", e)
        }
        return appCodes
    }

    /**
     * Generates an 11-character hash for a package name and signature string.
     */
    internal fun hash(packageName: String, signature: String): String? {
        val appInfo = "$packageName $signature"
        return try {
            val messageDigest = MessageDigest.getInstance(HASH_TYPE)
            messageDigest.update(appInfo.toByteArray(StandardCharsets.UTF_8))
            val hashSignature = messageDigest.digest()

            // Truncated to NUM_HASHED_BYTES (9 bytes)
            val truncatedHash = Arrays.copyOfRange(hashSignature, 0, NUM_HASHED_BYTES)
            var base64Hash = Base64.encodeToString(
                truncatedHash,
                Base64.NO_PADDING or Base64.NO_WRAP
            )
            base64Hash = base64Hash.substring(0, NUM_BASE64_CHARS.coerceAtMost(base64Hash.length))
            base64Hash
        } catch (e: NoSuchAlgorithmException) {
            Log.e(TAG, "Hash algorithm not found", e)
            null
        }
    }
}
