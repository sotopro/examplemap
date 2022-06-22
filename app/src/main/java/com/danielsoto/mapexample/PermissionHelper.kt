package com.danielsoto.mapexample

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionHelper {
    fun askForLocationPermission(activity: Activity){
        ActivityCompat.requestPermissions(activity, PERMISSION, REQUEST_CODE_FOR_PERMISSION )
    }
    fun arePermissionsGranted(context: Context) =
        PERMISSION.all{
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    const val REQUEST_CODE_FOR_PERMISSION = 0x1001
    private val PERMISSION = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
}