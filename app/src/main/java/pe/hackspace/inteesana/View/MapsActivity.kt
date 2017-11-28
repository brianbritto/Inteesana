package pe.hackspace.inteesana.View

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.beust.klaxon.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import pe.hackspace.inteesana.R
import pe.hackspace.inteesana.Session.Prefs
import java.net.URL


class MapsActivity : AppCompatActivity(),
                     OnMapReadyCallback{

    private lateinit var mMap: GoogleMap
    private var origen : LatLng = LatLng(-8.095225,-79.049303)// Location UPN
    private var mFusedLocationClient: FusedLocationProviderClient? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
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

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient!!.getLastLocation()
                .addOnSuccessListener(this) { location ->
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        origen = LatLng(location.latitude, location.longitude)
                        Log.i("MyLocation -> ",""+origen.latitude+" "+origen.longitude)
                        drawMap()
                    }
                }
                .addOnFailureListener { Log.i("Location -> ","sin resultado") }

        // drawMap()// Solo temporal
    }

    private fun drawMap(){
        val LatLongB = LatLngBounds.Builder()
        val options = PolylineOptions()
        options.color(Color.RED)
        options.width(5f)

        // val origen = LatLng(-8.095225,-79.049303)// Solo temporal
        val centro = Prefs.centroMedico
        val destino = LatLng(centro!!.latitud, centro.longitud)
        // mMap.addMarker(MarkerOptions().position(origen).title("Mi posición actual"))
        mMap.addMarker(MarkerOptions().position(destino).title(centro.nombre))

        val url = getURL(origen, destino)
        doAsync {
            val result = URL(url).readText()
            Log.i("resultURL --> ",result.toString())
            uiThread {
                val parser: Parser = Parser()
                val stringBuilder: StringBuilder = StringBuilder(result)
                val json: JsonObject = parser.parse(stringBuilder) as JsonObject

                val routes = json.array<JsonObject>("routes")
                val points : JsonArray<JsonObject> = routes!!["legs"]["steps"][0] as JsonArray<JsonObject>

                val polypts = points.flatMap { decodePoly(it.obj("polyline")?.string("points")!!)  }

                options.add(origen)
                LatLongB.include(origen)
                for (point in polypts) options.add(point)
                options.add(destino)
                LatLongB.include(destino)
                mMap.addPolyline(options)
                val bounds = LatLongB.build()
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
            }
        }
    }

    private fun getURL(from : LatLng, to : LatLng) : String {
        val origin = "origin=" + from.latitude + "," + from.longitude
        val dest = "destination=" + to.latitude + "," + to.longitude
        val sensor = "sensor=false"
        val params = "$origin&$dest&$sensor"

        return "https://maps.googleapis.com/maps/api/directions/json?$params"
    }

    private fun decodePoly(encoded: String): List<LatLng> {
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            shift = 0
            result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng

            val p = LatLng(lat.toDouble() / 1E5,
                    lng.toDouble() / 1E5)
            poly.add(p)
        }

        return poly
    }
}
