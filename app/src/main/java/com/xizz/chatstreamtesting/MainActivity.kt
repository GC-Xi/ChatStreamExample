package com.xizz.chatstreamtesting

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.compose.ui.channels.ChannelsScreen
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.compose.viewmodel.channels.ChannelListViewModel
import io.getstream.chat.android.compose.viewmodel.channels.ChannelViewModelFactory
import io.getstream.chat.android.models.querysort.QuerySortByField

class MainActivity : ComponentActivity() {

    private val factory by lazy {
        ChannelViewModelFactory(
            chatClient = ChatClient.instance(),
            querySort = QuerySortByField.descByName("last_updated"),
            filters = null
        )
    }

    private val listViewModel: ChannelListViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            setContent {
                ChatTheme {
                    ChannelsScreen(
                        viewModelFactory = factory,
                        onChannelClick = { channel ->
                            val myIntent: Intent = Intent(this, MessageActivity::class.java)
                            myIntent.putExtra("channelId", channel.cid)
                            startActivity(myIntent)
                        },
                        onViewChannelInfoAction = { channel ->
                            listViewModel.selectChannel(channel)
                        }
                    )
                }
            }
        }
    }
}
