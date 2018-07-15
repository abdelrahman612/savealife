package com.example.abdelrahman.savealife

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.abdelrahman.savealife.messagingchat.ChatsFragment
import com.example.abdelrahman.savealife.messagingchat.FriendsFragment
import com.example.abdelrahman.savealife.messagingchat.UsersActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_content.*
import java.util.HashMap

class content : AppCompatActivity() {
    var manger = supportFragmentManager
    var sp: SharedPreferences ?=null
    var editor:SharedPreferences.Editor?=null
    var logInEmail:String?=null
    var myAuth = FirebaseAuth.getInstance()

    private var database= FirebaseDatabase.getInstance()
    private var myRef=database.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content)
        //made home in first load
        var transaction = manger.beginTransaction()
        var frag1=HomeFrag()
        Tap_home.setBackgroundDrawable(getResources().getDrawable(R.drawable.baseline_home_white_24))
        Tap_message.setBackgroundDrawable(getResources().getDrawable(R.drawable.baseline_email_black_24))
        Tap_Notification.setBackgroundDrawable(getResources().getDrawable(R.drawable.baseline_notifications_black_24))
        Tap_map.setBackgroundDrawable(getResources().getDrawable(R.drawable.baseline_location_on_black_24))
        tap_friend.setBackgroundDrawable(getResources().getDrawable(R.drawable.outline_person_outline_black_24))
        transaction.replace(R.id.fragment_holder,frag1)
        //      transaction.addToBackStack(null) // adef el transaction dh le a5er el stack m3nah eny 3melt stack
        // w 3mal adef feh kol transaction 7asal wlma ad8at zorar el return fe el mobile bfdal arg3 l7d ma yfda el stack
        transaction.commit()
        //end hom in first load
        sp=getSharedPreferences("splogin", Context.MODE_PRIVATE)
        logInEmail= sp!!.getString("loginEmail","default")
        //Toast.makeText(this,"the email is " + logInEmail ,Toast.LENGTH_LONG).show()
        editor= sp!!.edit()
        whenDataChange()
    }
    fun ShowHomeFrag(v:View){
        var transaction = manger.beginTransaction()
        var frag1=HomeFrag()
        Tap_home.setBackgroundDrawable(getResources().getDrawable(R.drawable.baseline_home_white_24))
        Tap_message.setBackgroundDrawable(getResources().getDrawable(R.drawable.baseline_email_black_24))
        tap_friend.setBackgroundDrawable(getResources().getDrawable(R.drawable.outline_person_outline_black_24))
        Tap_Notification.setBackgroundDrawable(getResources().getDrawable(R.drawable.baseline_notifications_black_24))
        Tap_map.setBackgroundDrawable(getResources().getDrawable(R.drawable.baseline_location_on_black_24))
        transaction.replace(R.id.fragment_holder,frag1)
        //      transaction.addToBackStack(null) // adef el transaction dh le a5er el stack m3nah eny 3melt stack
        // w 3mal adef feh kol transaction 7asal wlma ad8at zorar el return fe el mobile bfdal arg3 l7d ma yfda el stack
        transaction.commit()
        //  isFragmentOneLoaded = true
    }

    fun ShowMessageFrage(v: View){
        var transaction = manger.beginTransaction()
        var frag2 = ChatsFragment()
        Tap_home.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_home_black_24dp))
        Tap_message.setBackgroundDrawable(getResources().getDrawable(R.drawable.baseline_email_white_24))
        tap_friend.setBackgroundDrawable(getResources().getDrawable(R.drawable.outline_person_outline_black_24))
        Tap_Notification.setBackgroundDrawable(getResources().getDrawable(R.drawable.baseline_notifications_black_24))
        Tap_map.setBackgroundDrawable(getResources().getDrawable(R.drawable.baseline_location_on_black_24))
        transaction.replace(R.id.fragment_holder,frag2)
        //  transaction.addToBackStack(null)
        transaction.commit()
        // isFragmentOneLoaded = false
    }
    fun Shownotficationfrage(v: View){
        var transaction = manger.beginTransaction()
        var frag3 = NotificationFrage()
        Tap_home.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_home_black_24dp))
        Tap_message.setBackgroundDrawable(getResources().getDrawable(R.drawable.baseline_email_black_24))
        Tap_Notification.setBackgroundDrawable(getResources().getDrawable(R.drawable.baseline_notifications_white_24))
        tap_friend.setBackgroundDrawable(getResources().getDrawable(R.drawable.outline_person_outline_black_24))
        Tap_map.setBackgroundDrawable(getResources().getDrawable(R.drawable.baseline_location_on_black_24))
        transaction.replace(R.id.fragment_holder,frag3)
        //  transaction.addToBackStack(null)
        transaction.commit()
        // isFragmentOneLoaded = false
    }

    fun ShowMapFrage(v:View){
        var transaction = manger.beginTransaction()
        var frag4 = Map()
        Tap_home.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_home_black_24dp))
        Tap_message.setBackgroundDrawable(getResources().getDrawable(R.drawable.baseline_email_black_24))
        Tap_Notification.setBackgroundDrawable(getResources().getDrawable(R.drawable.baseline_notifications_black_24))
        tap_friend.setBackgroundDrawable(getResources().getDrawable(R.drawable.outline_person_outline_black_24))
        Tap_map.setBackgroundDrawable(getResources().getDrawable(R.drawable.baseline_location_on_white_24))
        transaction.replace(R.id.fragment_holder,frag4)
        //   transaction.addToBackStack(null)
        transaction.commit()
        // isFragmentOneLoaded = true
    }
    fun Showfreindfrage(v:View){
        var transaction = manger.beginTransaction()
        var frag4 = FriendsFragment()
        Tap_home.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_home_black_24dp))
        Tap_message.setBackgroundDrawable(getResources().getDrawable(R.drawable.baseline_email_black_24))
        Tap_Notification.setBackgroundDrawable(getResources().getDrawable(R.drawable.baseline_notifications_black_24))
        Tap_map.setBackgroundDrawable(getResources().getDrawable(R.drawable.baseline_location_on_black_24))
        tap_friend.setBackgroundDrawable(getResources().getDrawable(R.drawable.outline_person_outline_white_24))
        transaction.replace(R.id.fragment_holder,frag4)
        //   transaction.addToBackStack(null)
        transaction.commit()
        // isFragmentOneLoaded = true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var inflater = menuInflater
        inflater.inflate(R.menu.main_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
            R.id.mi_profile->{
                var intent = Intent(this,profile::class.java)
                startActivity(intent)
            }
            R.id.mi_help->{
                var intent = Intent(this,Help::class.java)
                startActivity(intent)
            }
            R.id.mi_item3->{
                var intent = Intent(this,com.example.abdelrahman.savealife.messagingchat.MainActivity2::class.java)
                startActivity(intent)
            }
            R.id.mi_item4->{
                Toast.makeText(this,"log out success", Toast.LENGTH_LONG).show()
                //not sign out alredy
                myAuth.signOut()
                this.finish()

            }
            R.id.all_users_item->{
                val settingsIntent = Intent(this, UsersActivity::class.java)
                startActivity(settingsIntent)
            }
           /* R.id.myFriends_item->{
                *//*val settingsIntent = Intent(this, com.example.abdelrahman.savealife.messagingchat.FriendsFragment::class.java)
                startActivity(settingsIntent)*//*
            }*/
            else->{
                return super.onOptionsItemSelected(item)
            }
        }
       return true
    }
    fun  SplitString(str:String):String{
        var split=str.split("@")
        return split[0]
    }
    var number :Int=0
    fun whenDataChange(){
        myRef.child("Users_Notification").child(SplitString(logInEmail!!)).child("Request")
                .addValueEventListener(object: ValueEventListener {

                    override fun onDataChange(dataSnapshot: DataSnapshot?) {
                        try{
                            val td=dataSnapshot!!.value as HashMap<String, Any>
                            if(td!=null){

                                var value:String
                                for (key in td.keys){
                                    value= td[key] as String
                                    //etEmail.setText(value)

                                    val notifyme = Notifications()
                                    notifyme.Notify(applicationContext, value + " want you to help him ", number)
                                    number++
                                    //check_notify=false
                                    myRef.child("Users_Notification").child(SplitString(logInEmail!!)).child("Request").setValue(true)

                                    break

                                }

                            }

                        }catch (ex:Exception){}
                    }

                    override fun onCancelled(p0: DatabaseError?) {

                    }

                })
    }
}
