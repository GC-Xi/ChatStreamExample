package com.xizz.chatstreamtesting

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import io.getstream.android.push.firebase.FirebasePushDeviceGenerator
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.logger.ChatLogLevel
import io.getstream.chat.android.client.notifications.handler.NotificationConfig
import io.getstream.chat.android.client.notifications.handler.NotificationHandlerFactory
import io.getstream.chat.android.models.UploadAttachmentsNetworkType
import io.getstream.chat.android.models.User
import io.getstream.chat.android.offline.plugin.factory.StreamOfflinePluginFactory
import io.getstream.chat.android.state.plugin.config.StatePluginConfig
import io.getstream.chat.android.state.plugin.factory.StreamStatePluginFactory
import io.getstream.result.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(applicationContext)
        Log.e("xizz", "FirebaseApp.initializeApp done")

        val notificationConfig = NotificationConfig(
            pushDeviceGenerators = listOf(FirebasePushDeviceGenerator(providerName = ""))
        )
        val notificationHandler = NotificationHandlerFactory.createNotificationHandler(
            context = applicationContext,
            notificationConfig = notificationConfig,
        )
        val offlinePluginFactory = StreamOfflinePluginFactory(applicationContext)
        val statePluginFactory = StreamStatePluginFactory(
            appContext = applicationContext,
            config = StatePluginConfig(
                backgroundSyncEnabled = true,
                userPresence = true,
            ),
        )
        ChatClient.Builder("xch5df6zf9me", applicationContext)
            .notifications(notificationConfig, notificationHandler)
            .withPlugins(offlinePluginFactory, statePluginFactory)
            .uploadAttachmentsNetworkType(UploadAttachmentsNetworkType.CONNECTED)
            .disableDistinctApiCalls()
            .logLevel(ChatLogLevel.ALL)
            .build()

        runBlocking {
            withContext(Dispatchers.IO) {
                val firebaseToken = FirebaseMessaging.getInstance().token.await()
                Log.e("xizz", "Firebase token: $firebaseToken")

                val user = User(
                    id = "user_001",
                    name = "User One",
                    image= "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQN9NVBIOk_blWPFbW7lJfwX3FNO6jMIsDdZg&s",
                )
                // Use the following link to generate user JWT
                // https://getstream.io/chat/docs/javascript/token_generator/
                val userToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoidXNlcl8wMDEifQ.e-FQ61a5QYBkzKAKt28LNvtK4-5vTzhsQ4uy8-3chss"

                when (val userResult = ChatClient.instance().connectUser(user, userToken).await()) {
                    is Result.Failure -> {
                        Log.e("xizz", "connectUser failed: ${userResult.errorOrNull()}")
                    }
                    is Result.Success -> {
                        Log.e("xizz", "connectUser success: ${userResult.value}")
                    }
                }
            }
        }
    }
}
