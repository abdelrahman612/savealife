package com.example.abdelrahman.savealife

import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.add_post.*
import kotlinx.android.synthetic.main.add_post.view.*
import kotlinx.android.synthetic.main.post_ticket.*
import kotlinx.android.synthetic.main.post_ticket.view.*

class home : AppCompatActivity() {
    var listOfPost = ArrayList<Post>()
    var adapter:PostAdapter?=null
    var myAuth = FirebaseAuth.getInstance()
    var user_email:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        loadPosts()
         user_email = intent.getStringExtra("ID_email")
        adapter = PostAdapter(this,listOfPost)
        LV_posts.adapter =  adapter



        myAuth.addAuthStateListener{//this to do log out from this activity to last activity
            if(myAuth.currentUser==null){
                this.finish()
            }
        }

    }
    inner class  PostAdapter:BaseAdapter {
        var  listOfPost= ArrayList<Post>()
        var context:Context?=null
        constructor(context:Context, listOfAnimals: ArrayList<Post>):super(){
            this.listOfPost=listOfAnimals
            this.context=context
        }
        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            val post =  listOfPost[p0]
              if (post.personID.equals("addPost")){
                  //add first do post
                  var inflator = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                  var myView = inflator.inflate(R.layout.add_post, null)
                  myView.etPost.setText(user_email)
                  myView.txtUserName.setOnClickListener {
                      var intent = Intent(this@home,profile::class.java)
                      intent.putExtra("useremail",user_email)
                      startActivity(intent)
                  }
                  return myView
              }
            else{
                  //add posts in list view
                  var inflator = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                  var myView = inflator.inflate(R.layout.post_ticket, null)
                  myView.txtUserNamePost.setText(post.personID)
                  myView.txt_post_date.setText(post.postDate)
                  myView.txt_post_text.setText(post.postText)
                  myView.IV_Like.setOnClickListener {
                      IV_Like.setBackgroundColor(Color.RED)
                  }


                  return  myView
              }

            }
        override fun getItem(p0: Int): Any {
            return listOfPost[p0]
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getCount(): Int {

            return  listOfPost.size
        }
    }
   fun loadPosts(){
       listOfPost.add(
               Post("1","save a life ","http://","11:11 ","abdelrahman","addPost" ))

       listOfPost.add(
               Post("1","save a life ","http://","11:11 ","abdelrahman","2" ))

       listOfPost.add(
               Post("1","save a life ","http://","11:11 ","abdelrahman","3" ))

       listOfPost.add(
               Post("1","save a life ","http://","11:11 ","abdelrahman","4" ))

       listOfPost.add(
               Post("1","save a life ","http://","11:11 ","abdelrahman","5" ))

       listOfPost.add(
               Post("1","save a life ","http://","11:11 ","abdelrahman","6" ))

       listOfPost.add(
               Post("1","save a life ","http://","11:11 ","abdelrahman","7" ))

       listOfPost.add(
               Post("1","save a life ","http://","11:11 ","abdelrahman","8" ))

       listOfPost.add(
               Post("1","save a life ","http://","11:11 ","abdelrahman","9" ))

   }
     fun signOut(){
         myAuth.signOut()
     }
}
