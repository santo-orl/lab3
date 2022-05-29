package it.polito.lab4

import java.text.SimpleDateFormat
import java.util.*

class Message {
    var message: String? = null
    var senderId: String? = null
    var receiverId: String? = null
    var sentTime: String? = null

    constructor(){}

    constructor(message:String?, senderId:String?,receiverId:String?, sentTime:String?){
        this.message = message
        this.senderId = senderId
        this.sentTime = sentTime
        this.receiverId = receiverId
    }
}