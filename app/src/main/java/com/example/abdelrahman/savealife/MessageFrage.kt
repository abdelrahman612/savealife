package com.example.abdelrahman.savealife

import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.add_post.view.*
import kotlinx.android.synthetic.main.post_ticket.*
import kotlinx.android.synthetic.main.post_ticket.view.*

/**
 * Created by Abdelrahman on 4/23/2018.
 */
class MessageFrage: android.support.v4.app.Fragment() {
    val TAG = "messageFrag"
    var listOfMessage = ArrayList<Message>()
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
        var view: View = inflater!!.inflate(R.layout.messagefrage, container, false)
        var adapter: MessageFrage.MessageAdapter?=null
        var lv :ListView = view.findViewById(R.id.LV_messagelist)
        load_message()
        adapter = MessageAdapter(activity,listOfMessage)
        lv.adapter =  adapter
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
    inner class  MessageAdapter:BaseAdapter {
        var  listOfMessage= ArrayList<Message>()
        var context:Context?=null
        constructor(context:Context, listOfmessage: ArrayList<Message>):super(){
            this.listOfMessage=listOfmessage
            this.context=context
        }
        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            val message =  listOfMessage[p0]
            var inflator = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            var myView = inflator.inflate(R.layout.message_ticket, null)
            return myView
        }
        override fun getItem(p0: Int): Any {
            return listOfMessage[p0]
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getCount(): Int {
            return  listOfMessage.size
        }

    }
    fun load_message(){
        listOfMessage.add(
                Message("1","save a life ","http://","11:11 ","addPost" ))

        listOfMessage.add(
                Message("1","save a life ","http://","11:11 ","2" ))

        listOfMessage.add(
                Message("1","save a life ","http://","11:11 ","3" ))

        listOfMessage.add(
                Message("1","save a life ","http://","11:11 ","4" ))

        listOfMessage.add(
                Message("1","save a life ","http://","11:11 ","5" ))

        listOfMessage.add(
                Message("1","save a life ","http://","11:11 ","6" ))

        listOfMessage.add(
                Message("1","save a life ","http://","11:11 ","7" ))

        listOfMessage.add(
                Message("1","save a life ","http://","11:11 ","8" ))

        listOfMessage.add(
                Message("1","save a life ","http://","11:11 ","9" ))

    }
}