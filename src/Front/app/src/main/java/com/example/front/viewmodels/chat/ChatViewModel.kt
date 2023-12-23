package com.example.front.viewmodels.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.front.helper.DataStore.DataStoreManager
import com.google.firebase.Firebase
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    val dataStoreManager: DataStoreManager
) : ViewModel() {
    private val database = Firebase.database.reference

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    // Add message to the list
    fun addMessageToList(senderId: String, text: String) {
        val newMessage = Message(senderId, text, System.currentTimeMillis())
        _messages.value = listOf(newMessage) + _messages.value
    }

    fun checkOrCreateChatSession(user1Id: String, user2Id: String, onCompletion: (String) -> Unit) {
        val chatId = generateChatId(user1Id, user2Id)

        database.child("chats").child(chatId).get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                // Chat session exists, return the existing chatId
                onCompletion(chatId)
            } else {
                val chatData = mapOf(
                    "participants" to mapOf(
                        user1Id to true,
                        user2Id to true
                    ),
                    "messages" to listOf<String>()
                )
                database.child("chats").child(chatId).setValue(chatData).addOnCompleteListener {
                    // After creating the chat, return the new chatId
                    onCompletion(chatId)
                }
            }
        }.addOnFailureListener {
        }
    }

    fun generateChatId(user1Id: String, user2Id: String): String {
        val ids = listOf(user1Id, user2Id).sorted()
        return ids.joinToString("-")
    }
    fun sendMessage(chatId: String, senderId: String, messageText: String) {
        val messageId = database.child("chats").child(chatId).child("messages").push().key ?: return
        val messageData = mapOf(
            "sender" to senderId,
            "text" to messageText,
            "timestamp" to System.currentTimeMillis()
        )
        database.child("chats").child(chatId).child("messages").child(messageId).setValue(messageData)
    }

    fun listenForMessages(chatId: String, onNewMessage: (String, String) -> Unit) {
        database.child("chats").child(chatId).child("messages").addChildEventListener(object :
            ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue<MessageModel>()
                message?.let { onNewMessage(it.sender, it.text) }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun loadMessages(chatId: String, onMessagesLoaded: (List<Message>) -> Unit) {
        database.child("chats").child(chatId).child("messages")
            .orderByChild("timestamp") // Assuming each message has a 'timestamp' field
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val messages = dataSnapshot.children.mapNotNull { it.getValue(Message::class.java) }
                    onMessagesLoaded(messages)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle possible errors, such as a database access error
                    Log.e("loadMessages", "Database error: ${databaseError.message}")
                }
            })
    }
}

data class MessageModel(val sender: String = "", val text: String = "", val timestamp: Long = 0)

data class Message(
    val senderId: String = "",
    val text: String = "",
    val timestamp: Long = 0L
)