package com.example.abdelrahman.savealife

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.*

/**
 * Created by Abdelrahman on 4/23/2018.
 */
class Map: android.support.v4.app.Fragment(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    var boolfindLocate:Boolean?=false
    lateinit var ref: DatabaseReference
    var sp: SharedPreferences?=null
    var user_email:String?=null
    var lattude:String?=""
    var longatude:String?=""
    private var currentMarker: Marker? = null
    var mybloodGroub:String?=null
    //this on map redy is run when open map
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        Toast.makeText(activity,"please wait until find your location",Toast.LENGTH_LONG).show()
        // Add a marker in Sydney and move the camera
    }

    val TAG = "mapFrag"
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
        var view: View = inflater!!.inflate(R.layout.mapfrage, container, false);
        super.onCreate(savedInstanceState)
        activity.setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = activity.supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        checkPermmison()
        //to get latlong from db firebase
        ref = FirebaseDatabase.getInstance().getReference("RTDB_register")
        sp=activity.getSharedPreferences("splogin", Context.MODE_PRIVATE)
        user_email= sp!!.getString("loginEmail","default")

        ref.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onDataChange(p0: DataSnapshot?) {
                if(p0!!.exists()){
                    for (e in p0.children) {
                        var userdata = e!!.getValue(Db_Register::class.java)
                        if(userdata!!.Email==user_email) {
                            mybloodGroub=userdata!!.bloodGroub
                            //user_id_profileChat=userdata!!.id
                        }
                    }
                }
            }

        })

        ref.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onDataChange(p0: DataSnapshot?) {
                if(p0!!.exists()){
                    for (e in p0.children) {
                        var userdata = e!!.getValue(Db_Register::class.java)
                        lattude=userdata!!.latetude
                        longatude=userdata!!.longtude
                        val loctionusers = LatLng(lattude!!.toDouble(), longatude!!.toDouble())
                        /*if(userdata!!.Email==user_email) {
                            mybloodGroub=userdata.bloodGroub}
                        else{
                            mybloodGroub=" "
                        }*/

                        if(mybloodGroub==userdata.bloodGroub){
                            mMap!!.addMarker(MarkerOptions()
                                    .position(loctionusers)
                                    .icon( BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                    .title(userdata!!.Email)
                                    .snippet(userdata!!.firstname +"  " +userdata!!.lastname + " "+ userdata.bloodGroub))

                        }
                        else{
                            mMap!!.addMarker(MarkerOptions()
                                    .position(loctionusers)
                                    .title(userdata!!.Email)
                                    .snippet(userdata!!.firstname +"  " +userdata!!.lastname + " "+ userdata.bloodGroub))

                        }
                        //mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocationNow, 10f))
                        //trying when click
                        mMap!!.setOnMarkerClickListener(object : GoogleMap.OnMarkerClickListener {
                            override fun onMarkerClick(marker: Marker): Boolean {
                                currentMarker=marker
                                var intent = Intent(activity,profile::class.java)
                                intent.putExtra("custem_email",currentMarker!!.title)
                                startActivity(intent)
                                return false
                            }
                        })
                    }

                }
            }

        })
        ref.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onDataChange(p0: DataSnapshot?) {
                if(p0!!.exists()){
                    for (e in p0.children) {
                        var userdata = e!!.getValue(Db_Register::class.java)
                        if(userdata!!.Email==user_email) {
                            mybloodGroub=userdata.bloodGroub
                            lattude=userdata!!.latetude
                            longatude=userdata!!.longtude
                            val myLocationNow = LatLng(lattude!!.toDouble(), longatude!!.toDouble())
                            mMap!!.addMarker(MarkerOptions()
                                    .position(myLocationNow)
                                    .icon( BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                                    .title("Me")
                                    .snippet("my last register location"))
                            mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocationNow, 14f))
                        }
                    }
                }
            }

        })

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
    var ACCESSLOCATION=123
    fun checkPermmison(){

        if(Build.VERSION.SDK_INT>=23){

            if(ActivityCompat.
                    checkSelfPermission(activity,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){

                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),ACCESSLOCATION)
                return
            }
        }

        GetUserLocation()
    }
    fun GetUserLocation(){
        Toast.makeText(activity,"User location access on",Toast.LENGTH_LONG).show()
        //TODO: Will implement later

        var myLocation= MylocationListener()
        var mythread=myThread()

        var locationManager=activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3, 3f, myLocation)

        mythread.start()
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        when(requestCode){

            ACCESSLOCATION->{

                if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    GetUserLocation()
                }else{
                    Toast.makeText(activity,"We cannot access to your location",Toast.LENGTH_LONG).show()
                }
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    var location:Location?=null
    inner class MylocationListener: LocationListener {


        constructor(){
            location= Location("Start")
            location!!.longitude=0.0
            location!!.longitude=0.0
        }
        override fun onLocationChanged(p0: Location?) {
            location=p0
        }

        override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onProviderEnabled(p0: String?) {
            // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onProviderDisabled(p0: String?) {
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

    }
    var oldLocation:Location?=null
    inner class myThread:Thread{

        constructor():super(){
            oldLocation= Location("Start")
            oldLocation!!.longitude=0.0
            oldLocation!!.longitude=0.0
        }

        override fun run(){

            while (true){
                try {

                    if(oldLocation!!.distanceTo(location)==0f){
                       continue
                    }

                    oldLocation=location


                    activity.runOnUiThread {


                        mMap!!.clear()

                        // show me
                        val myLocationNow = LatLng(location!!.latitude, location!!.longitude)
                        mMap!!.addMarker(MarkerOptions()
                                .position(myLocationNow)
                                .icon( BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                                .title(user_email.toString())
                                .snippet(" here is my location"))
                        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocationNow, 12f))
                        Toast.makeText(activity,"thread",Toast.LENGTH_LONG).show()
                       //get location from firebase
                        ref.addValueEventListener(object :ValueEventListener{
                            override fun onCancelled(p0: DatabaseError?) {

                            }

                            override fun onDataChange(p0: DataSnapshot?) {
                                if(p0!!.exists()){
                                    for (e in p0.children) {
                                        var userdata = e!!.getValue(Db_Register::class.java)
                                            lattude=userdata!!.latetude
                                            longatude=userdata!!.longtude
                                            val loctionusers = LatLng(lattude!!.toDouble(), longatude!!.toDouble())

                                        if(mybloodGroub==userdata.bloodGroub){
                                            mMap!!.addMarker(MarkerOptions()
                                                    .position(loctionusers)
                                                    .icon( BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                                    .title(userdata!!.Email)
                                                    .snippet(userdata!!.firstname +"  " +userdata!!.lastname + " "+ userdata.bloodGroub))

                                        }
                                        else{
                                            mMap!!.addMarker(MarkerOptions()
                                                    .position(loctionusers)
                                                    .title(userdata!!.Email)
                                                    .snippet(userdata!!.firstname +"  " +userdata!!.lastname + " "+ userdata.bloodGroub))

                                        }
                                        //mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocationNow, 10f))
                                        //trying when click
                                        mMap!!.setOnMarkerClickListener(object : GoogleMap.OnMarkerClickListener {
                                            override fun onMarkerClick(marker: Marker): Boolean {
                                                currentMarker=marker
                                                var intent = Intent(activity,profile::class.java)
                                                intent.putExtra("custem_email",currentMarker!!.title)
                                                startActivity(intent)
                                                return false
                                            }
                                        })
                                    }

                                }
                            }

                        })

                    }
                    Thread.sleep(1000)
                    break
                }catch (ex:Exception){}
            }

        }

    }

}
/*
color of marker
   HUE_AZURE
   HUE_BLUE
   HUE_CYAN
   HUE_GREEN
   HUE_MAGENTA
  HUE_ORANGE
   HUE_RED
  HUE_ROSE
 HUE_VIOLET
   HUE_YELLOW*/
