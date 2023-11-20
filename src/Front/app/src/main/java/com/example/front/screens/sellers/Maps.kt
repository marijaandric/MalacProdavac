package com.example.front.screens.sellers

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.location.LocationManager
import android.util.Log
import android.view.ViewGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.front.R
import com.example.front.viewmodels.shops.ShopsViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker


@Composable
fun Osm(shopsViewModel: ShopsViewModel) {
    val context = LocalContext.current
    val yourUserAgent = "YourUserAgentName"
    Configuration.getInstance().userAgentValue = yourUserAgent
    Configuration.getInstance().load(context, context.getSharedPreferences("osmdroid", 0))

    AndroidView(
        modifier = Modifier,
        factory = { mapView(context, shopsViewModel) },
    )
}

private fun mapView(context: Context, shopsViewModel: ShopsViewModel): MapView {
    val mapView = MapView(context)
    val geoPoint = getCurrentLocation(context)
    mapView.controller.setZoom(18.0)
    mapView.controller.setCenter(geoPoint)
    mapView.layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
    )

    val btmp = BitmapFactory.decodeResource(context.resources, R.drawable.marker)
    val mapMarker = Bitmap.createScaledBitmap(btmp, 100, 100, false)

    if(shopsViewModel.state.value.shops!!.isNotEmpty())
    {
        for (geoPoint in shopsViewModel.state.value.shops!!) {
            val marker = Marker(mapView)
            marker.position = GeoPoint(geoPoint.latitude.toDouble(), geoPoint.longitude.toDouble())
            marker.icon = BitmapDrawable(context.resources, mapMarker)
            val title = geoPoint.name
            marker.title = title
            val address = geoPoint.address
            marker.snippet = address
            mapView.overlays.add(marker)
        }
    }

    val marker = Marker(mapView)
    val originalBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.currentlocation)
    val currPic = Bitmap.createScaledBitmap(originalBitmap, 100, 100, false)

    marker.icon = BitmapDrawable(context.resources, currPic)
    marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
    marker.position = geoPoint
    val title = "Your Current Position!"
    marker.title = title
    mapView.overlays.add(marker)

    return mapView
}


private fun getCurrentLocation(context: Context): GeoPoint {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    var location = GeoPoint(44.018813, 20.906027)
    try {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val lastKnownLocation =
                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            lastKnownLocation?.let {
                location = GeoPoint(it.latitude, it.longitude)
            }
        }
        else{
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                MY_PERMISSIONS_REQUEST_LOCATION
            )
            Log.d("Permisija", "Pronasao je lokaciju")
            val lastKnownLocation =
                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            lastKnownLocation?.let {
                location = GeoPoint(it.latitude, it.longitude)
            }
        }
    } catch (e: SecurityException) {
        e.printStackTrace()
    }

    return location
}

private const val MY_PERMISSIONS_REQUEST_LOCATION = 123

fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<out String>,
    grantResults: IntArray
) {
    when (requestCode) {
        MY_PERMISSIONS_REQUEST_LOCATION -> {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("LOKACIJA", "Permission granted, re-running location retrieval")
            } else {
                Log.d("LOKACIJA", "Permission denied")
            }
        }
    }
}

