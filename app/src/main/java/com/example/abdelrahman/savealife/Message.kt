package com.example.abdelrahman.savealife

/**
 * Created by Abdelrahman on 4/27/2018.
 */
class  Message{
    var messageID:String?=null
    var messageText:String?=null
    var userImageURL:String?=null
    var messageDate:String?=null
    var userName:String?=null
    // var personImage:String?=null
    constructor(messageID:String,messageText:String,userImageURL:String,
                messageDate:String,userName:String){
        this.messageID=messageID
        this.messageText=messageText
        this.userImageURL=userImageURL
        this.messageDate=messageDate
        this.userName=userName
    }

}