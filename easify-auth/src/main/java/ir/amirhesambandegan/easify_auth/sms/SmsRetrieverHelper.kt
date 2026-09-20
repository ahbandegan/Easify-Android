package ir.amirhesambandegan.easify_auth.sms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.core.content.ContextCompat
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import ir.amirhesambandegan.easify_auth.model.AuthResult
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.concurrent.TimeoutException

internal class SmsRetrieverHelper(private val context: Context) {

    /**
     * Starts the SMS Retriever client in Google Play Services.
     */
    fun startListening(
        onStarted: () -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        val client = SmsRetriever.getClient(context)
        client.startSmsRetriever()
            .addOnSuccessListener { onStarted() }
            .addOnFailureListener { onFailure(it) }
    }

    /**
     * Starts the SMS User Consent client in Google Play Services.
     */
    fun startUserConsent(
        senderPhoneNumber: String? = null,
        onStarted: () -> Unit = {},
        onFailure: (Throwable) -> Unit = {}
    ) {
        val client = SmsRetriever.getClient(context)
        client.startSmsUserConsent(senderPhoneNumber)
            .addOnSuccessListener { onStarted() }
            .addOnFailureListener { onFailure(it) }
    }

    /**
     * Listens for incoming SMS via SMS Retriever API, extracts the OTP code within [otpLengthRange],
     * and emits the result. Automatically registers and unregisters the BroadcastReceiver.
     */
    fun listenForOtp(
        otpLengthRange: IntRange = 4..8
    ): Flow<AuthResult<String>> = callbackFlow {
        val client = SmsRetriever.getClient(context)
        client.startSmsRetriever()
            .addOnFailureListener { e ->
                trySend(AuthResult.Error(e))
                close(e)
            }

        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action != SmsRetriever.SMS_RETRIEVED_ACTION) return

                val extras = intent.extras ?: return
                @Suppress("DEPRECATION")
                val status = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    extras.getParcelable(SmsRetriever.EXTRA_STATUS, Status::class.java)
                } else {
                    extras.get(SmsRetriever.EXTRA_STATUS) as? Status
                }

                when (status?.statusCode) {
                    CommonStatusCodes.SUCCESS -> {
                        val message = extras.getString(SmsRetriever.EXTRA_SMS_MESSAGE)
                        if (message != null) {
                            val otp = OtpExtractor.extractOtp(message, otpLengthRange)
                            if (otp != null) {
                                trySend(AuthResult.Success(otp))
                            } else {
                                trySend(AuthResult.Error(IllegalStateException("No OTP code matched in message: $message")))
                            }
                        } else {
                            trySend(AuthResult.Error(IllegalStateException("Empty SMS message received")))
                        }
                    }
                    CommonStatusCodes.TIMEOUT -> {
                        trySend(AuthResult.Error(TimeoutException("SMS Retriever timed out waiting for SMS")))
                    }
                    else -> {
                        trySend(AuthResult.Error(IllegalStateException("SMS Retriever status: ${status?.statusMessage ?: status?.statusCode}")))
                    }
                }
            }
        }

        val filter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        ContextCompat.registerReceiver(
            context,
            receiver,
            filter,
            ContextCompat.RECEIVER_EXPORTED
        )

        awaitClose {
            try {
                context.unregisterReceiver(receiver)
            } catch (_: Exception) {
            }
        }
    }

    /**
     * Listens for SMS User Consent intent when an SMS arrives.
     * Emits the consent [Intent] which can be launched with [androidx.activity.result.ActivityResultLauncher]
     * to prompt the user to allow reading the SMS.
     */
    fun listenForUserConsentIntent(
        senderPhoneNumber: String? = null
    ): Flow<AuthResult<Intent>> = callbackFlow {
        val client = SmsRetriever.getClient(context)
        client.startSmsUserConsent(senderPhoneNumber)
            .addOnFailureListener { e ->
                trySend(AuthResult.Error(e))
                close(e)
            }

        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action != SmsRetriever.SMS_RETRIEVED_ACTION) return

                val extras = intent.extras ?: return
                @Suppress("DEPRECATION")
                val status = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    extras.getParcelable(SmsRetriever.EXTRA_STATUS, Status::class.java)
                } else {
                    extras.get(SmsRetriever.EXTRA_STATUS) as? Status
                }

                when (status?.statusCode) {
                    CommonStatusCodes.SUCCESS -> {
                        @Suppress("DEPRECATION")
                        val consentIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            extras.getParcelable(SmsRetriever.EXTRA_CONSENT_INTENT, Intent::class.java)
                        } else {
                            extras.getParcelable(SmsRetriever.EXTRA_CONSENT_INTENT) as? Intent
                        }

                        if (consentIntent != null) {
                            trySend(AuthResult.Success(consentIntent))
                        } else {
                            trySend(AuthResult.Error(IllegalStateException("Consent intent is null")))
                        }
                    }
                    CommonStatusCodes.TIMEOUT -> {
                        trySend(AuthResult.Error(TimeoutException("SMS User Consent timed out")))
                    }
                }
            }
        }

        val filter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        ContextCompat.registerReceiver(
            context,
            receiver,
            filter,
            ContextCompat.RECEIVER_EXPORTED
        )

        awaitClose {
            try {
                context.unregisterReceiver(receiver)
            } catch (_: Exception) {
            }
        }
    }
}