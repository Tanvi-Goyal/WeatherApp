package com.example.weatherapp

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.utils.setupWithNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var currentNavController: LiveData<NavController>? = null
    private lateinit var binding: ActivityMainBinding

    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var lastLocation: Location? = null

    @Inject
    lateinit var preferences: SharedPreferences

    companion object {
        private const val TAG = "LocationProvider"
        private const val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }
        binding.welcomeText.text = "Welcome " + preferences.getString("username", "")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setupBottomNavigationBar()
    }

    private fun setupBottomNavigationBar() {
        val bottomNavigationView = binding.bottomNav
        val navGraphIds = listOf(
            R.navigation.nav_graph_current,
            R.navigation.nav_graph_date_wise,
            R.navigation.nav_graph_seven_days,
            R.navigation.nav_graph_settings
        )

        val controller = bottomNavigationView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_fragment,
            intent = intent
        )

        currentNavController = controller
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }

    override fun onStart() {
        super.onStart()
        if (!checkPermissions()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions()
            }
        } else {
            getLastLocation()
        }
    }

    private fun getLastLocation() {
        fusedLocationClient?.lastLocation!!.addOnCompleteListener(this) { task ->
            if (task.isSuccessful && task.result != null) {
                lastLocation = task.result

                preferences.edit().putString("latitude", lastLocation!!.latitude.toString()).apply()
                preferences.edit().putString("longitude", lastLocation!!.longitude.toString())
                    .apply()

                if (preferences.getString("username", "").isNullOrEmpty()) {
                    showDialog()
                } else {
                    binding.welcomeText.text = "Welcome " + preferences.getString("username", "")
                }
            } else {
                Log.w(TAG, "getLastLocation:exception", task.exception)
                showMessage("No location detected. Make sure location is enabled on the device.")
            }
        }
    }

    private fun showDialog() {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_username, null, false)
        val materialAlertDialogBuilder = MaterialAlertDialogBuilder(this)
        val usernameField = mDialogView.findViewById<TextInputLayout>(R.id.text_username)

        materialAlertDialogBuilder.setView(mDialogView)
            .setTitle("Set Username")
            .setPositiveButton("Submit") { dialog, _ ->
                val username = usernameField.editText?.text.toString()

                preferences.edit().putString("username", username).apply()
                binding.welcomeText.text = "Welcome $username"
                setupBottomNavigationBar()
                dialog.dismiss()
            }
            .setCancelable(false)
            .show()
    }

    private fun showMessage(string: String) {
        Toast.makeText(this@MainActivity, string, Toast.LENGTH_LONG).show()
    }

    private fun showSnackbar(
        mainTextStringId: String, actionStringId: String,
        listener: View.OnClickListener
    ) {
        Toast.makeText(this@MainActivity, mainTextStringId, Toast.LENGTH_LONG).show()
    }

    private fun checkPermissions(): Boolean {
        val permissionState = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        return permissionState == PackageManager.PERMISSION_GRANTED
    }

    private fun startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(
            this@MainActivity,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
            REQUEST_PERMISSIONS_REQUEST_CODE
        )
    }

    private fun requestPermissions() {
        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.")
            showSnackbar("Location permission is needed for core functionality", "Okay",
                View.OnClickListener {
                    startLocationPermissionRequest()
                })
        } else {
            Log.i(TAG, "Requesting permission")
            startLocationPermissionRequest()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        Log.i(TAG, "onRequestPermissionResult")
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            when {
                grantResults.isEmpty() -> {
                    // If user interaction was interrupted, the permission request is cancelled and you
                    // receive empty arrays.
                    Log.i(TAG, "User interaction was cancelled.")
                }
                grantResults[0] == PackageManager.PERMISSION_GRANTED -> {
                    // Permission granted.
                    getLastLocation()
                }
                else -> {
                    showSnackbar("Permission was denied", "Settings",
                        View.OnClickListener {
                            // Build intent that displays the App settings screen.
                            val intent = Intent()
                            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            val uri = Uri.fromParts(
                                "package",
                                Build.DISPLAY, null
                            )
                            intent.data = uri
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }
                    )
                }
            }
        }
    }

}