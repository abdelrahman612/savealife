package com.example.abdelrahman.savealife

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.widget.Toast
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.post_ticket.view.*
import java.util.*
import com.example.abdelrahman.savealife.messagingchat.ProfileActivity
import com.google.firebase.auth.FirebaseAuth


class profile : AppCompatActivity() {
    lateinit var ref:DatabaseReference
    lateinit var ref_chat:DatabaseReference
    var sp: SharedPreferences?=null
    var user_email:String?=null
    var custem_email:String?=null
    var email_profile:String?=null
    //var check_notify:Boolean?=false
    var first_notify:Boolean?=true

    var userid:String?=""
    var userImageurl:String?=""
    var userfirstandLastname:String?=""

    private var database= FirebaseDatabase.getInstance()
    private var myRef=database.reference

    var user_id_profileChat:String?=null
    var myauth = FirebaseAuth.getInstance()
    var usernae_inchat:String?=null
    //var loginuid_chatid:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        ref =FirebaseDatabase.getInstance().getReference("RTDB_register")
        ref_chat=FirebaseDatabase.getInstance().getReference("Users")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        sp=this.getSharedPreferences("splogin", Context.MODE_PRIVATE)
        user_email= sp!!.getString("loginEmail","default")
        //loginuid_chatid=sp!!.getString("loginuid","default")
        custem_email = intent.getStringExtra("custem_email")
        //Toast.makeText(this,"custem email is  " + custem_email , Toast.LENGTH_LONG).show()
        //whenDataChange()
        if(custem_email!=null){
            email_profile=custem_email
        }
        else {
            email_profile=user_email
        }
        //user_email = intent.getStringExtra("useremail")
        ref.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onDataChange(p0: DataSnapshot?) {
                if(p0!!.exists()){
                    for (e in p0.children) {
                        var userdata = e!!.getValue(Db_Register::class.java)
                        if(userdata!!.Email==email_profile) {
                            usernae_inchat=userdata!!.firstname+ " " + userdata!!.lastname
                            //user_id_profileChat=userdata!!.id
                            Profile_firstname.setText("firstname: " + userdata!!.firstname)
                            Profile_lastname.setText("lastname: " + userdata!!.lastname)
                            user_profile_name.setText( userdata!!.firstname +" "+ userdata!!.lastname)
                            user_profile_short_bio.setText(" groub blood is -> "+ userdata!!.bloodGroub)
                            Profile_Email.setText("Email :" + userdata!!.Email)
                            Profile_Birthday.setText("born in :" + userdata!!.Birthday)
                            Profile_Age.setText("age :" + userdata!!.age)
                            profile_phone.setText("phone number : " + userdata!!.phone)
                            if (userdata!!.photoUrl!="") {
                                Picasso.get().load(userdata!!.photoUrl).into(header_cover_image)
                            }
                            else
                            {
                                Picasso.get().load("https://www.thealthbenefitsof.com/wp-content/uploads/2018/04/Health-Benefits-Of-Donating-Blood.jpg").into(header_cover_image)
                            }
                        }
                    }
            }
            }

        })
        ref_chat.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {
            }

            override fun onDataChange(p0: DataSnapshot?) {
                if(p0!!.exists()){
                    for (e in p0.children) {
                        var userdata = e!!.getValue(com.example.abdelrahman.savealife.messagingchat.Users::class.java)
                        if(userdata!!.name == usernae_inchat) {
                            user_id_profileChat=e.key
                        }
                    }
                }
            }

        })
        IV_user_profile_photo.setOnClickListener {
            checkPermission()
        }

        add_friend.setOnClickListener {
            //check_notify=true
            if(first_notify == true && custem_email != user_email) {

                add_friend.setBackgroundDrawable(getResources().getDrawable(R.drawable.baseline_notifications_black_24))

                myRef.child("Users_Notification").child(SplitString(custem_email!!)).child("Request").push().setValue(user_email)
               //to get info from user in register db in firebase
                ref.addValueEventListener(object :ValueEventListener{
                    override fun onCancelled(p0: DatabaseError?) {
                    }

                    override fun onDataChange(p0: DataSnapshot?) {
                        if(p0!!.exists()){
                            for (e in p0.children) {
                                var userdata = e!!.getValue(Db_Register::class.java)
                                if(userdata!!.Email==user_email) {
                                    userid =userdata!!.id
                                    userImageurl = userdata!!.photoUrl
                                    userfirstandLastname= userdata!!.firstname + " " + userdata!!.lastname
                                    //Toast.makeText(applicationContext,userid + " " + userImageurl +"" +userfirstandLastname  ,Toast.LENGTH_LONG).show()
                                    myRef.child("Users_Notification").child(SplitString(custem_email!!)).child("emails").push().setValue(Notification(userid!!.toString(),"need your help",userImageurl.toString(),Date().toString(),userfirstandLastname.toString()))
                                    Toast.makeText(applicationContext,"now you send notification " ,Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    }

                })

                first_notify=false
            }
            else{
                Toast.makeText(this,"you alredy sent help request" ,Toast.LENGTH_LONG).show()
            }
            /*val notifyme=Notifications()
            notifyme.Notify(applicationContext,custem_email + " need your help",number)
            number++*/
        }
        drop_down_option_menu.setOnClickListener {
            drop_down_option_menu.setBackgroundDrawable(getResources().getDrawable(R.drawable.baseline_email_black_24))
            val profileIntent = Intent(this, ProfileActivity::class.java)
            profileIntent.putExtra("user_id", user_id_profileChat)
            startActivity(profileIntent)
        }
    }
    var number :Int=0
    fun  SplitString(str:String):String{
        var split=str.split("@")
        return split[0]
    }
    fun whenDataChange(){
        myRef.child("Users_Notification").child(SplitString(user_email!!)).child("Request")
                .addValueEventListener(object:ValueEventListener{

                    override fun onDataChange(dataSnapshot: DataSnapshot?) {
                        try{
                            val td=dataSnapshot!!.value as HashMap<String,Any>
                            if(td!=null){

                                var value:String
                                for (key in td.keys){
                                    value= td[key] as String
                                    //etEmail.setText(value)

                                        val notifyme = Notifications()
                                        notifyme.Notify(applicationContext, value + " want you to help him ", number)
                                        number++
                                        //check_notify=false
                                        myRef.child("Users_Notification").child(SplitString(user_email!!)).child("Request").setValue(true)

                                    break

                                }

                            }

                        }catch (ex:Exception){}
                    }

                    override fun onCancelled(p0: DatabaseError?) {

                    }

                })
    }
    val READIMAGE:Int=253
    fun checkPermission(){

        if(Build.VERSION.SDK_INT>=23){
            if(ActivityCompat.checkSelfPermission(this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)!=
                    PackageManager.PERMISSION_GRANTED){

                requestPermissions(arrayOf( android.Manifest.permission.READ_EXTERNAL_STORAGE),READIMAGE)
                return
            }
        }

        loadImage()
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        when(requestCode){
            READIMAGE->{
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    loadImage()
                }else{
                    Toast.makeText(applicationContext,"Cannot access your images", Toast.LENGTH_LONG).show()
                }
            }
            else-> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
    val PICK_IMAGE_CODE=123
    fun loadImage(){

        var intent= Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent,PICK_IMAGE_CODE)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==PICK_IMAGE_CODE  && data!=null && resultCode == RESULT_OK){

            val selectedImage=data.data
            val filePathColum= arrayOf(MediaStore.Images.Media.DATA)
            val cursor= contentResolver.query(selectedImage,filePathColum,null,null,null)
            cursor.moveToFirst()
            val coulomIndex=cursor.getColumnIndex(filePathColum[0])
            val picturePath=cursor.getString(coulomIndex)
            cursor.close()
            header_cover_image.setImageBitmap(BitmapFactory.decodeFile(picturePath))
        }

    }

}
