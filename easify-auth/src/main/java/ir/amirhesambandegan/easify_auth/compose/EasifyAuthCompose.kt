package ir.amirhesambandegan.easify_auth.compose

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.auth.api.phone.SmsRetriever
import ir.amirhesambandegan.easify_auth.EasifyAuth
import ir.amirhesambandegan.easify_auth.model.AuthResult
import ir.amirhesambandegan.easify_auth.sms.OtpExtractor

/**
 * Creates and remembers an [EasifyAuth] instance scoped to the application context.
 */
@Composable
fun rememberEasifyAuth(): EasifyAuth {
    val context = LocalContext.current.applicationContext
    return remember(context) { EasifyAuth(context) }
}

/**
 * Headless Composable effect that listens for SMS Retriever OTP broadcasts while active in composition.
 *
 * @param otpLengthRange The acceptable number of digits in the OTP (default 4..8).
 * @param onOtpReceived Callback invoked when the OTP code is parsed from incoming SMS.
 * @param onError Callback invoked when an error or timeout occurs.
 */
@Composable
fun SmsOtpEffect(
    otpLengthRange: IntRange = 4..8,
    onOtpReceived: (String) -> Unit,
    onError: (Throwable) -> Unit = {}
) {
    val easifyAuth = rememberEasifyAuth()
    val currentOnOtpReceived = rememberUpdatedState(onOtpReceived)
    val currentOnError = rememberUpdatedState(onError)

    LaunchedEffect(easifyAuth, otpLengthRange) {
        easifyAuth.listenForSmsOtp(otpLengthRange).collect { result ->
            when (result) {
                is AuthResult.Success -> currentOnOtpReceived.value(result.data)
                is AuthResult.Error -> currentOnError.value(result.throwable)
                is AuthResult.Cancelled -> {}
            }
        }
    }
}

/**
 * State and launcher for Google's SMS User Consent API in Jetpack Compose.
 */
class SmsConsentLauncher internal constructor(
    private val launchIntent: (Intent) -> Unit
) {
    fun launch(consentIntent: Intent) {
        launchIntent(consentIntent)
    }
}

/**
 * Composable launcher for Google Play SMS User Consent API.
 * Automatically listens for the consent intent, triggers the system bottom sheet dialog,
 * and passes the extracted OTP code to [onOtpReceived].
 *
 * @param senderPhoneNumber Optional filter for sender's phone number.
 * @param otpLengthRange The acceptable number of digits in the OTP (default 4..8).
 * @param onOtpReceived Callback with the extracted OTP upon user approval.
 */
@Composable
fun rememberSmsConsentLauncher(
    senderPhoneNumber: String? = null,
    otpLengthRange: IntRange = 4..8,
    onOtpReceived: (String) -> Unit
): SmsConsentLauncher {
    val easifyAuth = rememberEasifyAuth()
    val currentOnOtpReceived = rememberUpdatedState(onOtpReceived)

    val consentActivityLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            val message = result.data?.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)
            if (message != null) {
                val otp = OtpExtractor.extractOtp(message, otpLengthRange)
                if (otp != null) {
                    currentOnOtpReceived.value(otp)
                }
            }
        }
    }

    LaunchedEffect(easifyAuth, senderPhoneNumber) {
        easifyAuth.listenForSmsUserConsent(senderPhoneNumber).collect { authResult ->
            if (authResult is AuthResult.Success) {
                consentActivityLauncher.launch(authResult.data)
            }
        }
    }

    return remember(consentActivityLauncher) {
        SmsConsentLauncher { intent ->
            consentActivityLauncher.launch(intent)
        }
    }
}
