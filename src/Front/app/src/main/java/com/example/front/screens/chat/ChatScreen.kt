package com.example.front.screens.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.front.viewmodels.chat.ChatViewModel
import com.example.front.viewmodels.chat.Message

@Composable
fun ChatPage(viewModel: ChatViewModel) {
    val messages by viewModel.messages.collectAsState()
    val textState = remember { mutableStateOf("") }

    val user1Id = "user1"
    val user2Id = "user2"
    val chatId = viewModel.generateChatId(user1Id, user2Id)
    var currentUserId = 1

    LaunchedEffect(chatId) {
        viewModel.checkOrCreateChatSession(user1Id, user2Id) {
            viewModel.listenForMessages(it) { senderId, text ->
                viewModel.addMessageToList(senderId, text)
            }
        }
        currentUserId = viewModel.dataStoreManager.getUserIdFromToken()!!
    }

    Column(modifier = Modifier.padding(8.dp)) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            reverseLayout = true,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(messages) { message ->
                MessageRow(
                    message = message,
                    isCurrentUser = isMessageFromCurrentUser(message, currentUserId.toString()),
                    currentUserProfileImageUrl = "url_for_current_user",
                    otherUserProfileImageUrl = "url_for_other_user"
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            TextField(
                value = textState.value,
                onValueChange = { textState.value = it },
                modifier = Modifier.weight(1f).padding(end = 8.dp),
                placeholder = { Text("Type a message") }
            )
            Button(onClick = {
                viewModel.sendMessage(chatId, user1Id, textState.value)
                textState.value = ""
            }) {
                Text("Send")
            }
        }
    }
}

@Composable
fun MessageRow(message: Message, isCurrentUser: Boolean, currentUserProfileImageUrl: String, otherUserProfileImageUrl: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start
    ) {
        if (!isCurrentUser) {
            ProfileImage(url = otherUserProfileImageUrl)
        }
        Text(
            text = message.text,
            modifier = Modifier
                .background(if (isCurrentUser) Color(0xFFDCF8C6) else Color.LightGray, shape = RoundedCornerShape(8.dp))
                .padding(8.dp)
        )
        if (isCurrentUser) {
            ProfileImage(url = currentUserProfileImageUrl)
        }
    }
}

fun isMessageFromCurrentUser(message: Message, currentUserId: String): Boolean {
    return message.senderId == currentUserId
}


@Composable
fun ProfileImage(url: String) {
    Image(
        painter = rememberAsyncImagePainter(model = url),
        contentDescription = "Profile picture",
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .padding(end = 8.dp)
    )
}

