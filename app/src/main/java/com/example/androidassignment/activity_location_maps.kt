package com.example.androidassignment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class activity_location_maps : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    //private lateinit var auth: FirebaseAuth // Authentication
    //private lateinit var database: DatabaseReference

    private var latitude : Double = 0.0
    private var longitude : Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_maps)



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        intent = intent
        var position = intent.getIntExtra("key",-1 )
        when (position) {
            1 -> {latitude = 3.208010; longitude = 101.731312 }
            2 -> {latitude = 3.073363; longitude = 101.610343 }
            3 -> {latitude = 3.102420; longitude = 101.729095 }
            4 -> {latitude = 3.148766 ; longitude = 101.712586 }
            6 -> {latitude = 3.040488; longitude = 101.587092}
            7 -> {latitude = 3.079307; longitude = 101.415282 }
            8 -> {latitude = 2.803324; longitude =  101.494589}
            9 -> {latitude = 2.993034; longitude = 101.788897 }
            11 -> {latitude = 1.477694; longitude = 103.955542 }
            12 -> {latitude = 1.461745; longitude = 103.763639 }
            13 -> {latitude = 1.461970 ; longitude = 103.583913 }
            14 -> {latitude = 1.472735 ; longitude = 103.940809 }
            16 -> {latitude = 2.238794; longitude = 102.259446 }
            17 -> {latitude = 2.248372; longitude = 102.158178 }
            18-> {latitude = 2.228376; longitude = 102.295694 }
            19-> {latitude = 2.219073 ; longitude = 102.249457 }
            else -> { // Note the block
                latitude = 0.0; longitude = 0.0
            }
        }
        Log.i("long1111", "MyClass.getView() — get item number $position")

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap ) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(latitude, longitude)
        mMap.addMarker(MarkerOptions().position(sydney).title("location"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}