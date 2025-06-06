package com.mobatia.bisad.fcm

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * Override base class methods to handle any events required by the application.
 * All methods are invoked on a background thread, and may be called when the app is in the background or not open.
 *
 *  The registration token may change when:
 *  - The app deletes Instance ID
 *  - The app is restored on a new device
 *  - The user uninstalls/reinstall the app
 *  - The user clears app data.
 */
class SellerFirebaseService : FirebaseMessagingService() {

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had
     * een compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
//    override fun onNewToken(token: String) {
//        super.onNewToken(token)
//        Log.i("SellerFirebaseService ", "Refreshed token :: $token")
//        // If you want to send messages to this application instance or
//        // manage this apps subscriptions on the server side, send the
//        // Instance ID token to your app server.
//        sendRegistrationToServer(token)
//    }

    private fun sendRegistrationToServer(token: String) {
        // TODO : send token to tour server
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        //Log.i("SellerFirebaseService ", "Message :: $message")
    }
}