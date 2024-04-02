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

class MainApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(applicationContext)
        Log.e("xizz", "FirebaseApp.initializeApp done")

        val notificationConfig = NotificationConfig(
            pushNotificationsEnabled = true,
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
    }
}