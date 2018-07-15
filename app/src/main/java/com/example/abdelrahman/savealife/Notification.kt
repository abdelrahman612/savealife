package com.example.abdelrahman.savealife

/**
 * Created by Abdelrahman on 4/29/2018.
 */
class  Notification{
    var notificationID:String?=null
    var notificationText:String?=null
    var userImageURL:String?=null
    var notificationDate:String?=null
    var userName:String?=null
    // var personImage:String?=null
    constructor(notificationID:String,notificationText:String,userImageURL:String,
                notificationDate:String,userName:String){
        this.notificationID=notificationID
        this.notificationText=notificationText
        this.userImageURL=userImageURL
        this.notificationDate=notificationDate
        this.userName=userName
    }

}