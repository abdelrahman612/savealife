package com.example.abdelrahman.savealife

/**
 * Created by Abdelrahman on 7/1/2018.
 */
class  Notify_ticket{
    var notifyText:String?=null
    var NotifyDate:String?=null
    var personEmail:String?=null

    constructor(notifyText:String,NotifyDate:String,
                personEmail:String){
        this.notifyText=notifyText
        this.NotifyDate=NotifyDate
        this.personEmail=personEmail
    }
}