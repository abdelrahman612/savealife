package com.example.abdelrahman.savealife

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_log_in.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_profile.*

class LogIn : AppCompatActivity() {
    var myauth = FirebaseAuth.getInstance()
    var sp:SharedPreferences ?=null
   var editor:SharedPreferences.Editor ?=null
    var userID:String?=null
    var firstname:String?=null
    var lastname:String?=null
    lateinit var ref: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        ref =FirebaseDatabase.getInstance().getReference("RTDB_register")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
        sp= getSharedPreferences("splogin",Context.MODE_PRIVATE)
        editor= sp!!.edit()
        TV_forgotPassword.setOnClickListener {
            TV_forgotPassword.setTextColor(Color.RED)
        }
        BU_login.setOnClickListener {
                if(ET_Email.text.trim().toString().isEmpty()){
                    Toast.makeText(this,"please enter your email",Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
                if(ET_password.text.toString().isEmpty()){
                    Toast.makeText(this,"please enter your password",Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
                signIn(ET_Email.text.toString(),ET_password.text.toString())
            }

        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
            }

            override fun onDataChange(p0: DataSnapshot?) {
                if(p0!!.exists()){
                    for (e in p0.children) {
                        var userdata = e!!.getValue(Db_Register::class.java)
                        if(userdata!!.Email==myauth.currentUser?.email) {
                            userID = userdata!!.id
                            firstname =userdata!!.firstname
                            lastname = userdata!!.lastname
                        }
                    }
                }
            }

        })
        }

    fun TV_Register_clickEvent(view:View) //to move the login activity to register
    {
        var intent = Intent(this,Register::class.java)
        startActivity(intent)
    }//ended moved to log in

    fun TV_HelpClickEvent(view: View) //to move the login activity to help
    {
        var intent=Intent(this,Help::class.java)
        startActivity(intent)
    } //endeed moved to help
    private fun signIn(email:String,password:String){
        Toast.makeText(this,"Authontication ...",Toast.LENGTH_LONG).show()

        myauth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, OnCompleteListener { task ->
                    if(task.isSuccessful){
                        Toast.makeText(this,"login successfuly",Toast.LENGTH_LONG).show()
                        var intent=Intent(this,content::class.java)
                        //intent.putExtra("ID_email",myauth.currentUser?.email)
                        editor!!.putString("loginEmail",myauth.currentUser?.email)
                        editor!!.putString("loginuid",userID)

                        editor!!.putString("firstnameLogin",firstname)
                        editor!!.putString("lastnameLogin",lastname)
                        //Toast.makeText(this,firstname +lastname,Toast.LENGTH_LONG ).show()
                        editor!!.commit()
                        startActivity(intent)
                    }
                    else{
                        Toast.makeText(this,"email or password is not correct",Toast.LENGTH_LONG).show()
                    }
                })
    }
    fun  LoadMain(){
        var currentUser =myauth!!.currentUser

        if(currentUser!=null) {


            var intent=Intent(this,content::class.java)
            //intent.putExtra("ID_email",myauth.currentUser?.email)
            editor!!.putString("loginEmail",myauth.currentUser?.email)
            editor!!.putString("loginuid",userID)

            //get name forom firebase

            editor!!.putString("firstnameLogin",firstname)
            editor!!.putString("lastnameLogin",lastname)
            editor!!.commit()
            startActivity(intent)
            finish()
        }
    }
   /* override fun onStart() {
        super.onStart()
        LoadMain()
    }*/
}
