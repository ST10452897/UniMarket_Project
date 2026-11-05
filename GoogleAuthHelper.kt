package za.co.unimarket.auth

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

data class GoogleSignInUser(
    val idToken: String,
    val email: String,
    val displayName: String,
    val profilePictureUri: String?
)

class GoogleAuthHelper(private val context: Context) {

    private val credentialManager: CredentialManager = CredentialManager.create(context)

    /**
     * Launches the native Google Sign-In bottom sheet via AndroidX Credential Manager
     * and returns a verified Google ID token and user profile.
     *
     * @param webClientId The OAuth 2.0 Web Client ID from Firebase / Google Cloud Console.
     */
    suspend fun signInWithGoogle(webClientId: String): Result<GoogleSignInUser> {
        if (webClientId.isBlank() || webClientId.startsWith("YOUR_")) {
            return Result.failure(
                IllegalArgumentException("Please configure a valid Firebase Web Client ID for Google SSO.")
            )
        }

        return try {
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(webClientId)
                .setAutoSelectEnabled(false)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val result = credentialManager.getCredential(
                request = request,
                context = context
            )

            val credential = result.credential
            if (credential is CustomCredential &&
                credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
            ) {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val user = GoogleSignInUser(
                    idToken = googleIdTokenCredential.idToken,
                    email = googleIdTokenCredential.id,
                    displayName = googleIdTokenCredential.displayName
                        ?: googleIdTokenCredential.id.substringBefore("@"),
                    profilePictureUri = googleIdTokenCredential.profilePictureUri?.toString()
                )
                Log.d(TAG, "Google SSO verified for ${user.email}")
                Result.success(user)
            } else {
                Result.failure(IllegalStateException("Unexpected credential type received."))
            }
        } catch (e: GetCredentialException) {
            Log.e(TAG, "Google Sign-In failed or was cancelled by user.", e)
            Result.failure(e)
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error during Google SSO.", e)
            Result.failure(e)
        }
    }

    companion object {
        private const val TAG = "GoogleAuthHelper"
        const val NOTIFICATION_PERMISSION_REQUEST_CODE = 1001

        /**
         * Requests Android 13+ (API 33+) runtime POST_NOTIFICATIONS permission.
         */
        fun ensureNotificationPermission(activity: Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val granted = ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED

                if (!granted) {
                    ActivityCompat.requestPermissions(
                        activity,
                        arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                        NOTIFICATION_PERMISSION_REQUEST_CODE
                    )
                }
            }
        }
    }
}
