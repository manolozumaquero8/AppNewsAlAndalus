package com.example.appnewsalandalus.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.appnewsalandalus.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class EventosFragment : Fragment(R.layout.fragment_eventos), OnMapReadyCallback {

    private lateinit var map: GoogleMap

    private val locationPermissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permisos ->
            if (permisos[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permisos[Manifest.permission.ACCESS_COARSE_LOCATION] == true
            ) {
                gestionarLocalizacion()
            } else {
                Toast.makeText(requireContext(), "El usuario denegó los permisos....", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniciarFragment()
    }

    private fun iniciarFragment() {
        val fragment = SupportMapFragment()
        fragment.getMapAsync(this)
        childFragmentManager.commit {
            setReorderingAllowed(true)
            add(R.id.fm_maps, fragment)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.uiSettings.isZoomControlsEnabled = true
        ponerMarcador(LatLng(36.85022532839174, -2.465028503902572))
        gestionarLocalizacion()
    }

    private fun gestionarLocalizacion() {
        if (!::map.isInitialized) return
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map.isMyLocationEnabled = true
            map.uiSettings.isMyLocationButtonEnabled = true
        } else {
            pedirPermisos()
        }
    }

    private fun pedirPermisos() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) ||
            ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)) {
            mostrarExplicacion()
        } else {
            escogerPermisos()
        }
    }

    private fun escogerPermisos() {
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    private fun mostrarExplicacion() {
        AlertDialog.Builder(requireContext())
            .setTitle("Permisos de Ubicación")
            .setMessage("Para el uso adecuado de esta increíble aplicación necesitamos los permisos de ubicación")
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(false)
            .setPositiveButton("Aceptar") { dialog, _ ->
                startActivity(Intent(Settings.ACTION_APPLICATION_SETTINGS))
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun ponerMarcador(coordenadas: LatLng) {
        val marker = MarkerOptions().position(coordenadas).title("Sede Periodico Ies Al-Andalus")
        map.addMarker(marker)
        mostrarAnimacion(coordenadas, 15f)
    }

    private fun mostrarAnimacion(coordenadas: LatLng, zoom: Float) {
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(coordenadas, zoom),
            4500,
            null
        )
    }

    override fun onResume() {
        super.onResume()
        gestionarLocalizacion()
    }
}










