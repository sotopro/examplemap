package com.danielsoto.mapexample

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.danielsoto.mapexample.databinding.ActivityMapsBinding
import com.google.android.gms.location.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val SECOND = 1000L
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    val locationCallbackRequest = LocationRequest.create().apply {
        interval = 10 * SECOND
        fastestInterval = 5 * SECOND
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }
    lateinit var locationCallback: LocationCallback

    fun startLocationTracking(context: Context){
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        locationCallback = object: LocationCallback() {
            override fun onLocationResult(currentLocation: LocationResult) {
                super.onLocationResult(currentLocation)
            }
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            PermissionHelper.askForLocationPermission(this)
            return
        }
        mFusedLocationClient.requestLocationUpdates(locationCallbackRequest, locationCallback, null)
    }

    fun stopLocationTracking(context: Context) {
        mFusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkPermissionAndSetView()
    }

    override fun onPause() {
        super.onPause()
        stopLocationTracking(this)
    }

    override fun onResume() {
        super.onResume()
        startLocationTracking(this)
    }

    private fun checkPermissionAndSetView() {
        if(PermissionHelper.arePermissionsGranted(this)) {
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this)
        } else {
            PermissionHelper.askForLocationPermission(this)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == PermissionHelper.REQUEST_CODE_FOR_PERMISSION) {
            checkPermissionAndSetView()
        }
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

//        // Add a marker in Sydney and move the camera
//        val sydney = LatLng(-34.0, 151.0)
//        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}