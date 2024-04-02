package com.xizz.chatstreamtesting

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp
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
                // Use the following link to generate user JWT
                // https://getstream.io/chat/docs/javascript/token_generator/

                val (user, token) = users[4]
                when (val userResult = ChatClient.instance().connectUser(user, token).await()) {
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

private val users: List<Pair<User, String>> = listOf(
    User(
        id = "user_001",
        name = "User One",
        image = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSiy5b6RNHED40zOsBD9cu1OA1LTaMvRLn7jA&s",
    ) to "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoidXNlcl8wMDEifQ.e-FQ61a5QYBkzKAKt28LNvtK4-5vTzhsQ4uy8-3chss",
    User(
        id = "user_002",
        name = "User Two",
        image = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRTLx_oMLbFJg1lKZQJtBwH1s6YUYn0XpmNng&s",
    ) to "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoidXNlcl8wMDIifQ.nbRwNx9G2ubYvSXraG97x7chxL-Jc8LhWONbICxm9bo",
    User(
        id = "user_003",
        name = "User Three",
        image = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRqqIoIa1qJtGBtWTd0pVZJup8E9kPL0lt4Ew&s",
    ) to "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoidXNlcl8wMDMifQ.KMXxIDKFf3soHdSdc-Ggx5kd_spdsTiscSn7Ujbh_YM",
    User(
        id = "user_004",
        name = "User Four",
        image = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTGsp6hmVJaGK0K6AzokthECLTJ4sZB7hWIzA&s",
    ) to "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoidXNlcl8wMDQifQ.-JJE19KHPEwUuTkD8WV62fqAbi4KHMaZXUR6bBN9lmM",
    User(
        id = "user_005",
        name = "User Four",
        image = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQHLydRvCC8njmoBKhrqHzn6oRZcYk3UxBEZQ&s",
    ) to "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoidXNlcl8wMDUifQ.4JrI7Uc4Sjm6q2TYaIEzSU1dJd8RN85LGWGQBUYaXlM",
)
