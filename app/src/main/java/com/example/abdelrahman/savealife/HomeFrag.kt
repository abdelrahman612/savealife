package com.example.abdelrahman.savealife

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_content.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.add_post.view.*
import kotlinx.android.synthetic.main.home_frag.*
import kotlinx.android.synthetic.main.post_ticket.*
import kotlinx.android.synthetic.main.post_ticket.view.*
import kotlinx.android.synthetic.main.ticket_loading_post.*
import java.io.ByteArrayOutputStream
import java.util.*


class HomeFrag:Fragment(){
    private var database= FirebaseDatabase.getInstance()
    private var myRef=database.reference
    lateinit var ref: DatabaseReference
    val TAG = "homeFrag"
    var listOfPost = ArrayList<Post>()
    var myAuth = FirebaseAuth.getInstance()
    var PICK_IMAGE_CODE=123
    var sp: SharedPreferences ?=null
    var logInEmail:String?=null
    var loginuid:String?=null
    var firstname:String?=null
    var lastname:String?=null
    var adapter: HomeFrag.PostAdapter?=null

    override fun onAttach(context: Context?) {
        Log.d(TAG,"onAttach")
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG,"onCreate")
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG,"onCreateView")
        var view:View =inflater!!.inflate(R.layout.home_frag,container,false);
        //user_email = intent.getStringExtra("ID_email")

        var lv :ListView = view.findViewById(R.id.LV_postsfrag)
        sp=activity.getSharedPreferences("splogin", Context.MODE_PRIVATE)
        logInEmail= sp!!.getString("loginEmail","default")
        loginuid=sp!!.getString("loginuid","default")
       // firstname =sp!!.getString("firstnameLogin","default")
        //lastname =sp!!.getString("lastnameLogin","default")
        ref = FirebaseDatabase.getInstance().getReference("RTDB_register")

        ref.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onDataChange(p0: DataSnapshot?) {
                if(p0!!.exists()){
                    for (e in p0.children) {
                        var userdata = e!!.getValue(Db_Register::class.java)
                        if(userdata!!.Email==logInEmail) {
                            firstname=userdata.firstname
                            lastname=userdata.lastname
                        }
                    }
                }
            }

        })
        //loadPosts()
        adapter = PostAdapter(activity,listOfPost)
        lv.adapter =  adapter
        //Toast.makeText(activity,loginuid.toString(),Toast.LENGTH_LONG).show()
        LoadPostFromFireBase()
        myAuth.addAuthStateListener{//this to do log out from this activity to last activity
            if(myAuth.currentUser==null){
                //activity.finish()
            }
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d(TAG,"onActivityCreated")
        super.onActivityCreated(savedInstanceState)
    }

    override fun onStart() {
        Log.d(TAG,"onStart")
        super.onStart()
    }

    override fun onResume() {
        Log.d(TAG,"onResume")
        super.onResume()

    }

    override fun onPause() {
        Log.d(TAG,"onPause")
        super.onPause()
    }

    override fun onStop() {
        Log.d(TAG,"onStop")
        super.onStop()
    }

    override fun onDestroyView() {
        Log.d(TAG,"onDestroyView")
        super.onDestroyView()
    }

    override fun onDestroy() {
        Log.d(TAG,"onDestroy")
        super.onDestroy()
    }

    override fun onDetach() {
        Log.d(TAG,"onDetach")
        super.onDetach()
    }
    //home fire base code start
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
                //myView.etPost.setText(user_email)
                //to load image from galary
                myView.iv_attach.setOnClickListener(View.OnClickListener {
                    loadImage()

                })
                myView.iv_post.setOnClickListener(View.OnClickListener {
                    //upload to server
                    Toast.makeText(activity,"Posting ...", Toast.LENGTH_LONG).show()

                    myRef.child("posts").push().setValue(
                            Post(loginuid!!.toString(), myView.etPost.text.toString(), DownloadURL!!, Date().toString(),firstname +" "+lastname,loginuid!!.toString()))
                    listOfPost.removeAt(0)
                    adapter!!.notifyDataSetChanged()


                })
                myView.txtUserName.setText(firstname!!+ " " + lastname!!)
                myRef.child("RTDB_register").child(loginuid)
                        .addValueEventListener(object :ValueEventListener{

                            override fun onDataChange(dataSnapshot: DataSnapshot?) {

                                try {
                                    var firstname:String?=null
                                    var td= dataSnapshot!!.value as HashMap<String,Any>

                                    for(key in td.keys){

                                        var userInfo= td[key] as String
                                        if(key.equals("photoUrl")){
                                            Picasso.get().load(userInfo).into(myView.picture_path)
                                        }/*else if (key.equals("firstname")){
                                            firstname = userInfo
                                        }
                                        else if (key.equals("lastname")){
                                            myView.txtUserName.text = firstname+" "+ userInfo
                                        }*/



                                    }

                                }catch (ex:Exception){}
                                //Toast.makeText(activity,"dc d d d",Toast.LENGTH_LONG).show()

                            }

                            override fun onCancelled(p0: DatabaseError?) {

                            }
                        })
                myView.txtUserName.setOnClickListener {
                    var intent = Intent(context,profile::class.java)
                    //intent.putExtra("useremail",user_email)
                    startActivity(intent)
                }
                return myView
            }
            else if (post.personID.equals("Loading")){
                //add posts in list view
                var inflator = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                var myView = inflator.inflate(R.layout.ticket_loading_post, null)
                //myView.txtUserNamePost.setText(post.personID)
                return  myView
            }
            else{
                //add posts in list view
                var inflator = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                var myView = inflator.inflate(R.layout.post_ticket, null)
                myView.txtUserNamePost.setText(post.personName)
                myView.txt_post_date.setText(post.postDate)
                myView.txt_post_text.setText(post.postText)
                if (post.postImageURL!="") {
                    Picasso.get().load(post.postImageURL).into(myView.post_picture)
                }else{
                    myView.post_picture.setImageDrawable(null)
                }
                myView.txtUserNamePost.setOnClickListener {
                    ref.addValueEventListener(object :ValueEventListener{
                        override fun onCancelled(p0: DatabaseError?) {

                        }

                        override fun onDataChange(p0: DataSnapshot?) {
                            if(p0!!.exists()){
                                for (e in p0.children) {
                                    var userdata = e!!.getValue(Db_Register::class.java)
                                    if(userdata!!.firstname + " " +userdata.lastname==post.personName ) {
                                        Toast.makeText(activity,post.personName,Toast.LENGTH_LONG).show()
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
                myRef.child("RTDB_register").child(post.personID)
                        .addValueEventListener(object :ValueEventListener{

                            override fun onDataChange(dataSnapshot: DataSnapshot?) {

                                try {
                                    var firstname:String?=null
                                    var td= dataSnapshot!!.value as HashMap<String,Any>

                                    for(key in td.keys){

                                        var userInfo= td[key] as String
                                        if(key.equals("photoUrl")){
                                            Picasso.get().load(userInfo).into(myView.picture_path_profile)
                                        }else if (key.equals("firstname")){
                                            firstname = userInfo
                                        }
                                        else if (key.equals("lastname")){
                                            myView.txtUserName.text = firstname + userInfo
                                        }



                                    }

                                }catch (ex:Exception){}
                                //Toast.makeText(activity,"dc d d d",Toast.LENGTH_LONG).show()

                            }

                            override fun onCancelled(p0: DatabaseError?) {

                            }
                        })

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

    fun loadImage(){

        var intent= Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent,PICK_IMAGE_CODE)

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==PICK_IMAGE_CODE  && data!=null && resultCode == AppCompatActivity.RESULT_OK){

            val selectedImage=data.data
            val filePathColum= arrayOf(MediaStore.Images.Media.DATA)
            val cursor= activity.contentResolver.query(selectedImage,filePathColum,null,null,null)
            cursor.moveToFirst()
            val coulomIndex=cursor.getColumnIndex(filePathColum[0])
            val picturePath=cursor.getString(coulomIndex)
            cursor.close()
            UploadImage(BitmapFactory.decodeFile(picturePath))
            // IV_Loading.setImageBitmap(BitmapFactory.decodeFile(picturePath))
            //this is the path of the image in th phone
        }
    }
    var DownloadURL:String?=""
    fun UploadImage(bitmap:Bitmap){
        listOfPost.add(0,Post("0","him","url","loading",logInEmail.toString(),"Loading"))
        adapter!!.notifyDataSetChanged()

        val storage= FirebaseStorage.getInstance()
        val storgaRef=storage.getReferenceFromUrl("gs://save-a-life-add56.appspot.com/imagePost")
        val df= java.text.SimpleDateFormat("ddMMyyHHmmss")
        val dataobj= Date()
        val imagePath= SplitString(logInEmail!!) + "."+ df.format(dataobj)+ ".jpg"
        val ImageRef=storgaRef.child("imagePost/"+imagePath )
        val baos= ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos)
        val data= baos.toByteArray()
        val uploadTask=ImageRef.putBytes(data)
        uploadTask.addOnFailureListener{
            Toast.makeText(activity,"fail to upload", Toast.LENGTH_LONG).show()
        }.addOnSuccessListener { taskSnapshot ->

            DownloadURL= taskSnapshot.downloadUrl!!.toString()
            Toast.makeText(activity,"the image is uploaded", Toast.LENGTH_LONG).show()
            TV_loading.setText("the image is loaded")
            //this to load image before posting
            //we should know how to get photo from url
            //IV_Loading.setImageURI(taskSnapshot.downloadUrl!!)



        }
    }
    fun SplitString(email:String):String{
        val split= email.split("@")
        return split[0]
    }

    //load posts from firebase
    fun LoadPostFromFireBase(){

        myRef.child("posts")
                .addValueEventListener(object : ValueEventListener {

                    override fun onDataChange(dataSnapshot: DataSnapshot?) {

                        try {

                            listOfPost.clear()
                            listOfPost.add(Post("0","him","url"," ","add","addPost"))

                            var td= dataSnapshot!!.value as HashMap<String,Any>

                            for(key in td.keys){

                                var post= td[key] as HashMap<String,Any>

                                listOfPost.add(Post(key,
                                        post["postText"] as String,
                                        post["postImageURL"] as String
                                        ,post["postDate"] as String
                                        ,post["personName"] as String
                                        ,post["personID"] as String))


                            }


                            adapter!!.notifyDataSetChanged()
                        }catch (ex:Exception){}


                    }

                    override fun onCancelled(p0: DatabaseError?) {

                    }
                })
    }


}