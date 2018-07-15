package com.example.abdelrahman.savealife

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.notification_ticket.view.*
import java.util.HashMap

/**
 * Created by Abdelrahman on 4/23/2018.
 */
class NotificationFrage: Fragment() {
    val TAG = "NotficationFrag"
    var listOfnotification = ArrayList<Notification>()
    var adapter: NotificationFrage.MessageAdapter?=null
    var sp: SharedPreferences?=null
    var user_email:String?=null

    lateinit var ref: DatabaseReference

    private var database= FirebaseDatabase.getInstance()
    private var myRef=database.reference

    override fun onAttach(context: Context?) {
        Log.d(TAG, "onAttach")
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "onCreateView")
        var view: View = inflater!!.inflate(R.layout.notficationfrage, container, false);

        var lv : ListView = view.findViewById(R.id.LV_notfication)
        sp=activity.getSharedPreferences("splogin", Context.MODE_PRIVATE)
        user_email= sp!!.getString("loginEmail","default")
        ref = FirebaseDatabase.getInstance().getReference("RTDB_register")
        //load_notfication()
        adapter = MessageAdapter(activity,listOfnotification)
        lv.adapter =  adapter
        LoadNotifivationFromFireBase()
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d(TAG, "onActivityCreated")
        super.onActivityCreated(savedInstanceState)
    }

    override fun onStart() {
        Log.d(TAG, "onStart")
        super.onStart()
    }

    override fun onResume() {
        Log.d(TAG, "onResume")
        super.onResume()
    }

    override fun onPause() {
        Log.d(TAG, "onPause")
        super.onPause()
    }

    override fun onStop() {
        Log.d(TAG, "onStop")
        super.onStop()
    }

    override fun onDestroyView() {
        Log.d(TAG, "onDestroyView")
        super.onDestroyView()
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        super.onDestroy()
    }

    override fun onDetach() {
        Log.d(TAG, "onDetach")
        super.onDetach()
    }
    inner class  MessageAdapter: BaseAdapter {
        var  listOfNotification= ArrayList<Notification>()
        var context:Context?=null
        constructor(context:Context, listOfNotification: ArrayList<Notification>):super(){
            this.listOfNotification=listOfNotification
            this.context=context
        }
        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            val notification =  listOfNotification[p0]
            var inflator = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            var myView = inflator.inflate(R.layout.notification_ticket, null)
            myView.txtDescriptionNoti.setText(notification.userName+" "+notification.notificationText)
            myView.txtDateNoti.setText(notification.notificationDate)
            if(notification.userImageURL!=null)
            {
                Picasso.get().load(notification.userImageURL).into(myView.imageView)
            }else{
                myView.imageView.setImageDrawable(null)
            }
             myView.imageView.setOnClickListener {
                    ref.addValueEventListener(object :ValueEventListener{
                        override fun onCancelled(p0: DatabaseError?) {
                        }

                        override fun onDataChange(p0: DataSnapshot?) {
                            if(p0!!.exists()){
                                for (e in p0.children) {
                                    var userdata = e!!.getValue(Db_Register::class.java)
                                    if(userdata!!.firstname + " " +userdata.lastname==notification.userName ) {
                                        Toast.makeText(activity,notification.userName,Toast.LENGTH_LONG).show()
                                        var intent = Intent(context,profile::class.java)
                                        intent.putExtra("custem_email",userdata.Email)
                                        //intent.putExtra("useremail",user_email)
                                        startActivity(intent)
                                    }
                                }
                            }
                        }

                    })

                }
            myView.txtDescriptionNoti.setOnClickListener {
                ref.addValueEventListener(object :ValueEventListener{
                    override fun onCancelled(p0: DatabaseError?) {
                    }

                    override fun onDataChange(p0: DataSnapshot?) {
                        if(p0!!.exists()){
                            for (e in p0.children) {
                                var userdata = e!!.getValue(Db_Register::class.java)
                                if(userdata!!.firstname + " " +userdata.lastname==notification.userName ) {
                                    Toast.makeText(activity,notification.userName,Toast.LENGTH_LONG).show()
                                    var intent = Intent(context,profile::class.java)
                                    intent.putExtra("custem_email",userdata.Email)
                                    //intent.putExtra("useremail",user_email)
                                    startActivity(intent)
                                }
                            }
                        }
                    }

                })
            }
            return myView
        }
        override fun getItem(p0: Int): Any {
            return listOfNotification[p0]
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getCount(): Int {
            return  listOfNotification.size
        }
    }
    fun  SplitString(str:String):String{
        var split=str.split("@")
        return split[0]
    }
    fun load_notfication(){
        listOfnotification.add(
                Notification("1","save a life ","http://","sat at 20:25 ","user" ))
        listOfnotification.add(
                Notification("2","save a life ","http://","sat at 20:25 ","user" ))

        listOfnotification.add(
                Notification("3","save a life ","http://","sat at 20:25 ","user" ))

        listOfnotification.add(
                Notification("4","save a life ","http://","sat at 20:25 ","user" ))

        listOfnotification.add(
                Notification("5","save a life ","http://","sat at 20:25 ","user" ))

        listOfnotification.add(
                Notification("6","save a life ","http://","sat at 20:25 ","user" ))

        listOfnotification.add(
                Notification("7","save a life ","http://","sat at 20:25 ","user" ))

        listOfnotification.add(
                Notification("8","save a life ","http://","sat at 20:25 ","user" ))
        listOfnotification.add(
                Notification("9","save a life ","http://","sat at 20:25 ","user" ))

    }
    fun LoadNotifivationFromFireBase(){

        myRef.child("Users_Notification").child(SplitString(user_email!!)).child("emails")
                .addValueEventListener(object : ValueEventListener {

                    override fun onDataChange(dataSnapshot: DataSnapshot?) {

                        try {

                            listOfnotification.clear()
                            //listOfPost.add(Post("0","him","url"," ","add","addPost"))

                            var td= dataSnapshot!!.value as HashMap<String, Any>

                            for(key in td.keys){

                                var noti= td[key] as HashMap<String, Any>

                                listOfnotification.add(Notification(key,
                                        noti["notificationText"] as String,
                                        noti["userImageURL"] as String
                                        ,noti["notificationDate"] as String
                                        ,noti["userName"] as String))


                            }
                            adapter!!.notifyDataSetChanged()
                        }catch (ex:Exception){}


                    }

                    override fun onCancelled(p0: DatabaseError?) {

                    }
                })
    }
}
