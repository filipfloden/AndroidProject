package se.ju.student.group16.androidproject

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class GoogleMapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

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
        var marker=""
        val doneButton = findViewById<Button>(R.id.mapDone)

        doneButton.setOnClickListener {
            if (marker.isEmpty()){
                Toast.makeText(this,"@string/location_toast", Toast.LENGTH_LONG).show()
            }
            val data = Intent()
            Log.d("koordinater", marker.toString())

            data.putExtra("eventLocation", marker)
            setResult(Activity.RESULT_OK,data)
            finish()
        }

        mMap.setOnMapLongClickListener {    latlng->
            mMap.addMarker(MarkerOptions().position(latlng))
            //val latitude = latlng.latitude
            //val longitude =latlng.longitude
            marker = latlng.toString()
            Log.d("koordinater", marker)

        }
        val defaultMapLocation = LatLng(57.5, 14.0)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(defaultMapLocation))
    }
}