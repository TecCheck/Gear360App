package io.github.teccheck.gear360app.bluetooth2

import io.github.teccheck.gear360app.bluetooth.BTMessages

private const val TAG = "MessageHandler"

class MessageHandler {

    private val listeners = mutableListOf<MessageListener>()

    fun onReceive(channelId: Int, data: ByteArray?) {
        if (data == null)
            return
    }

    fun addMessageListener(messageListener: MessageListener) {
        listeners.add(messageListener)
    }

    fun removeMessageListener(messageListener: MessageListener) {
        listeners.remove(messageListener)
    }

    interface MessageListener {
        fun onMessageReceive(message: BTMessages.BTMessage)
    }
}