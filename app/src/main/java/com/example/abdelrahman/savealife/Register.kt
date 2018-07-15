package com.example.abdelrahman.savealife

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.View
import android.widget.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

class Register : AppCompatActivity() {

    var myauth = FirebaseAuth.getInstance()
    var spinner: Spinner?=null
    var adapter: ArrayAdapter<CharSequence>? =null
    var adapterLastDonner: ArrayAdapter<CharSequence>? =null
    var boolLoadImage:Boolean=false
    var DownloadURL:String?=""
    val READIMAGE:Int=253
    val PICK_IMAGE_CODE=123
    var picturePath2:String?=null
    var gender:String=""
    var bloodGroub:String=""
    var lastdonner:String=""
    val LOCATION_CODE =124
    private var locationManager : LocationManager? = null
    var longtudee:String=""
    var lateitudee:String=""

    private var database=FirebaseDatabase.getInstance()
    private var myRef=database.reference

    private var mDatabase: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //spinner to opserve the selection to Blood Groub
        spinner = findViewById<View>(R.id.SP_bloodGroub) as Spinner
        adapter = ArrayAdapter.createFromResource(this, R.array.selection, android.R.layout.simple_spinner_item)
        adapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner!!.adapter = adapter
        spinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                bloodGroub= adapter!!.getItem(i).toString()
                //Toast.makeText(applicationContext,bloodGroub,Toast.LENGTH_LONG).show()
            }
            override fun onNothingSelected(adapterView: AdapterView<*>) {
            }
        }
        //endeed spinner observe groube bloood

        //spinner to opserve the selection to lastdonner
        spinner = findViewById<View>(R.id.SP_LastDonner) as Spinner
        adapterLastDonner = ArrayAdapter.createFromResource(this, R.array.select_LastDonner, android.R.layout.simple_spinner_item)
        adapterLastDonner!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner!!.adapter = adapterLastDonner
        spinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                lastdonner= adapterLastDonner!!.getItem(i).toString()
                //Toast.makeText(applicationContext,lastdonner,Toast.LENGTH_LONG).show()
            }
            override fun onNothingSelected(adapterView: AdapterView<*>) {
            }
        }
        //endeed spinner observe lastdonner

        //start take photo from gallary
        IV_UserPhoto.setOnClickListener {
            checkPermission()
        }
        //end choose the profile photo

        Bu_Register_Done.setOnClickListener {

            //validate every edite text
            checkLocationPermission()
            if (boolLoadImage !=true){
                Toast.makeText(this,"please choose your profile picture",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (ET_Register_firstName.text.toString().trim().isEmpty()){
                ET_Register_firstName.error="please enter your first name"
                return@setOnClickListener
            }
            if (ET_Register_lastName.text.toString().trim().isEmpty()){
                ET_Register_lastName.error="please enter your last name"
                return@setOnClickListener
            }
            if(ET_Register_email.text.trim().toString().isEmpty()){
                ET_Register_email.error="please enter your email"
                return@setOnClickListener
            }
            if(ET_Register_password.text.toString().isEmpty()){
                ET_Register_password.error="please enter your password"
                return@setOnClickListener
            }
            if(ET_Register_password.text.toString().length<6){
                ET_Register_password.error="password should be 6 char or more"
                return@setOnClickListener
            }
            if(ET_Register_confirmPassword.text.toString().isEmpty()){
                ET_Register_confirmPassword.error="please enter your confirm password"
                return@setOnClickListener
            }
            if(!ET_Register_confirmPassword.text.toString().equals(ET_Register_password.text.toString())) {
                Toast.makeText(this,"password and confirm password is not match",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if ( ET_Register_birthday.text.toString().trim().isEmpty()){
                ET_Register_birthday.error="please enter your birthday"
                return@setOnClickListener
            }
            if (ET_Register_age.text.toString().trim().isEmpty()){
                ET_Register_age.error="please enter your age"
                return@setOnClickListener
            }
            if (ET_Register_Phone.text.toString().trim().isEmpty()){
                ET_Register_Phone.error="please enter your phone"
                return@setOnClickListener
            }
            if(gender==""){
                Toast.makeText(this,"please choose your gender",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (bloodGroub == "select")
            {
                Toast.makeText(this,"please choose your bloodgroub",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (lastdonner == "select")
            {
                Toast.makeText(this,"please choose last donner",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (longtudee=="" || lateitudee=="")
            {
                Toast.makeText(this,"please open your location ",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            //ensed validate
            Toast.makeText(this,"Please wait ....",Toast.LENGTH_LONG).show()
            //to create email and password

            signUp(ET_Register_email.text.trim().toString(), ET_Register_password.text.toString())
        }
        //get radio button choosen

        RG_gender?.setOnCheckedChangeListener { group, checkedId ->

            if (R.id.RB_Male == checkedId) {
                gender="male"
            }
            if (R.id.RB_Female == checkedId) {
                gender="female"
            }
        }
        //to detect location
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?
        registertext.setOnClickListener { view ->
            Toast.makeText(this,"toast test ", Toast.LENGTH_LONG).show()
        }
    }

    private fun signUp(email:String,password:String){
        var currentUser =myauth!!.currentUser
        myauth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, OnCompleteListener { task ->
                    if(task.isSuccessful){
                        var Email =ET_Register_email.text.toString().trim()

                       // Toast.makeText(this,SplitString(currentUser!!.email.toString()),Toast.LENGTH_LONG).show()

                        // image take atime to save i wait it for take the saved url
                        UploadImage(BitmapFactory.decodeFile(picturePath2),Email)
                        this.finish()
                    }
                    else{
                        Toast.makeText(this,"not add error"+task.exception?.toString(),Toast.LENGTH_LONG).show()
                    }
                })
    }
    fun UploadImage(bitmap: Bitmap,email:String){
        val storage= FirebaseStorage.getInstance()
        val storgaRef=storage.getReferenceFromUrl("gs://save-a-life-add56.appspot.com/")
        val df= SimpleDateFormat("ddMMyyHHmmss")
        val dataobj= Date()
        val imagePath= SplitString(email!!) + "."+ df.format(dataobj)+ ".jpg"
        val ImageRef=storgaRef.child("imageProfile/"+imagePath )
        val baos= ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos)
        val data= baos.toByteArray()
        val uploadTask=ImageRef.putBytes(data)
        uploadTask.addOnFailureListener{
            Toast.makeText(applicationContext,"fail to upload", Toast.LENGTH_LONG).show()
        }.addOnSuccessListener { taskSnapshot ->
            DownloadURL= taskSnapshot.downloadUrl!!.toString()
            savedata()
            //Toast.makeText(this,"download before"+DownloadURL,Toast.LENGTH_LONG).show()
        }
    }
    fun savedata(){
        var mydatabase = FirebaseDatabase.getInstance().getReference("RTDB_register")
        val userID = mydatabase.push().key
        var firstname = ET_Register_firstName.text.toString().trim()
        var lasttname = ET_Register_lastName.text.toString().trim()
        var Email =ET_Register_email.text.toString().trim()
        var birthday = ET_Register_birthday.text.toString().trim()
        var age = ET_Register_age.text.toString().trim()
        var phone =ET_Register_Phone.text.toString().trim()
        var photourl= DownloadURL.toString().trim()

        var user = Db_Register(userID, firstname, lasttname, Email, birthday, age, phone,photourl,gender,bloodGroub,lastdonner,longtudee,lateitudee)
        mydatabase.child(userID).setValue(user).addOnCompleteListener {
            Toast.makeText(this, "Done successfully", Toast.LENGTH_LONG).show()
        }
        //myref here to create users notification database in firebase
        myRef.child("Users_Notification").child(SplitString(Email.toString())).child("Request").setValue(userID.toString())

        redister_chting(firstname+" "+lasttname)
    }
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
        //working with photo
        when(requestCode){
            READIMAGE->{
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    loadImage()
                }else{
                    Toast.makeText(applicationContext,"Cannot access your images",Toast.LENGTH_LONG).show()
                }
            }
            LOCATION_CODE->{
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getmylocation()
                } else {
                    Toast.makeText(this, "Cannot acces to contact ", Toast.LENGTH_LONG).show()
                }
            }
            else-> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
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
            IV_UserPhoto.setImageBitmap(BitmapFactory.decodeFile(picturePath))
            boolLoadImage=true
            picturePath2=picturePath
            //this is the path of the image in th phone
        }
    }
    fun SplitString(email:String):String{
        val split= email.split("@")
        return split[0]
    }
    //location detect

    fun checkLocationPermission(){

        if(Build.VERSION.SDK_INT>=23){

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED ){

                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_CODE)
                return
            }
        }
        getmylocation()
    }
    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            longtudee= location.longitude!!.toString()
            lateitudee=location.latitude!!.toString()
            //Toast.makeText(applicationContext,"" + location.longitude + ":" + location.latitude,Toast.LENGTH_LONG).show()
        }
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }
    fun getmylocation(){
        try {
            // Request location updates
            locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 100f, locationListener);
        } catch(ex: SecurityException) {
            Log.d("myTag", "Security Exception, no location available");
        }
    }

    fun redister_chting(display_name:String)
    {
        val current_user = FirebaseAuth.getInstance().currentUser
        val uid = current_user!!.uid

        mDatabase = FirebaseDatabase.getInstance().reference.child("Users").child(uid)

        val device_token = FirebaseInstanceId.getInstance().token

        val userMap = HashMap<String, String>()
        userMap.put("name", display_name)
        userMap.put("status", "Hi there I'm using Lapit Chat App.")
        userMap.put("image", "default")
        userMap.put("thumb_image", "default")
        userMap.put("device_token", device_token!!)

        mDatabase!!.setValue(userMap)
    }

}

