package ir.amirhesambandegan.easify_auth.google

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CreatePasswordRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetPasswordOption
import androidx.credentials.PasswordCredential
import androidx.credentials.exceptions.ClearCredentialException
import androidx.credentials.exceptions.CreateCredentialCancellationException
import androidx.credentials.exceptions.CreateCredentialException
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import ir.amirhesambandegan.easify_auth.model.AuthResult
import ir.amirhesambandegan.easify_auth.model.AuthUser

internal class GoogleAuthHelper(private val context: Context) {

    private val credentialManager = CredentialManager.create(context)

    /**
     * Signs in with Google using Android's Credential Manager.
     *
     * @param serverClientId The Web Client ID configured in Google Cloud Console / Firebase.
     * @param filterByAuthorizedAccounts If true, only shows accounts that have previously signed in to this app.
     * @param autoSelectEnabled If true, automatically signs in without prompt if only one account exists.
     * @param nonce Optional nonce to prevent replay attacks.
     */
    suspend fun signIn(
        serverClientId: String,
        filterByAuthorizedAccounts: Boolean = false,
        autoSelectEnabled: Boolean = false,
        nonce: String? = null
    ): AuthResult<AuthUser> {
        val googleIdOptionBuilder = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(filterByAuthorizedAccounts)
            .setServerClientId(serverClientId)
            .setAutoSelectEnabled(autoSelectEnabled)

        if (nonce != null) {
            googleIdOptionBuilder.setNonce(nonce)
        }

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOptionBuilder.build())
            .build()

        return try {
            val result = credentialManager.getCredential(context = context, request = request)
            when (val credential = result.credential) {
                is GoogleIdTokenCredential -> {
                    AuthResult.Success(
                        AuthUser(
                            id = credential.id,
                            email = credential.id,
                            displayName = credential.displayName,
                            givenName = credential.givenName,
                            familyName = credential.familyName,
                            profilePictureUrl = credential.profilePictureUri?.toString(),
                            phoneNumber = credential.phoneNumber,
                            idToken = credential.idToken
                        )
                    )
                }
                else -> AuthResult.Error(IllegalStateException("Unsupported credential type: ${credential.type}"))
            }
        } catch (_: GetCredentialCancellationException) {
            AuthResult.Cancelled
        } catch (e: GetCredentialException) {
            AuthResult.Error(e)
        } catch (e: Exception) {
            AuthResult.Error(e)
        }
    }

    /**
     * Clears the user's credential state from Credential Manager (Sign-Out).
     */
    suspend fun signOut(): AuthResult<Unit> {
        return try {
            credentialManager.clearCredentialState(ClearCredentialStateRequest())
            AuthResult.Success(Unit)
        } catch (e: ClearCredentialException) {
            AuthResult.Error(e)
        } catch (e: Exception) {
            AuthResult.Error(e)
        }
    }

    /**
     * Saves a username and password into Google Password Manager via Credential Manager.
     */
    suspend fun savePassword(username: String, password: String): AuthResult<Unit> {
        return try {
            val request = CreatePasswordRequest(id = username, password = password)
            credentialManager.createCredential(context = context, request = request)
            AuthResult.Success(Unit)
        } catch (_: CreateCredentialCancellationException) {
            AuthResult.Cancelled
        } catch (e: CreateCredentialException) {
            AuthResult.Error(e)
        } catch (e: Exception) {
            AuthResult.Error(e)
        }
    }

    /**
     * Retrieves saved username/password credentials from Credential Manager.
     */
    suspend fun getSavedPassword(): AuthResult<Pair<String, String>> {
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(GetPasswordOption())
            .build()

        return try {
            val result = credentialManager.getCredential(context = context, request = request)
            when (val credential = result.credential) {
                is PasswordCredential -> {
                    AuthResult.Success(Pair(credential.id, credential.password))
                }
                else -> AuthResult.Error(IllegalStateException("Unsupported credential type: ${credential.type}"))
            }
        } catch (_: GetCredentialCancellationException) {
            AuthResult.Cancelled
        } catch (e: GetCredentialException) {
            AuthResult.Error(e)
        } catch (e: Exception) {
            AuthResult.Error(e)
        }
    }
}