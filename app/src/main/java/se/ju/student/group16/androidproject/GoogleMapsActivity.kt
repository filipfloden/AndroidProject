package se.ju.student.group16.androidproject

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class GoogleMapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
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
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        var marker = emptyMap<String,Double>()
        var lat:Double = 0.0
        var lng:Double = 0.0
        val doneButton = findViewById<Button>(R.id.mapDone)

        doneButton.setOnClickListener {
            if (marker.isEmpty()){
                Toast.makeText(this,"@string/location_toast", Toast.LENGTH_LONG).show()
            }
            val data = Intent()
            data.putExtra("longitude", lng)
            data.putExtra("latitude", lat)
            setResult(Activity.RESULT_OK,data)
            finish()
        }

        mMap.setOnMapLongClickListener {    latlng->
            mMap.addMarker(MarkerOptions().position(latlng))
            lng  = latlng.longitude
            lat = latlng.latitude
            marker = mapOf("lat" to lat, "lng" to lng)
            Log.d("koordinater", marker.toString())

        }
        getLocationAccess()
        val defaultZoom = 5f
        val defaultMapLocation = LatLng(57.8, 14.2)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultMapLocation, defaultZoom))
    }


    private fun getLocationAccess(){
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            mMap.isMyLocationEnabled = true
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE){
            if (grantResults.contains(PackageManager.PERMISSION_GRANTED)){
                if(ContextCompat.checkSelfPermission(this,
                                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    mMap.isMyLocationEnabled = true
                }
            } else{
                finish()
            }
        }
    }
}