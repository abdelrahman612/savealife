package com.example.abdelrahman.savealife

/**
 * Created by Abdelrahman on 2/23/2018.
 */
class  Post{
    var postID:String?=null
    var postText:String?=null
    var postImageURL:String?=null
    var postDate:String?=null
    var personName:String?=null
    var personID:String?=null

    constructor(postID:String,postText:String,postImageURL:String,
                postDate:String,personName:String/*,personImage:String*/,personID:String){
        this.postID=postID
        this.postText=postText
        this.postImageURL=postImageURL
        this.postDate=postDate
        this.personName=personName
        this.personID=personID
    }
}