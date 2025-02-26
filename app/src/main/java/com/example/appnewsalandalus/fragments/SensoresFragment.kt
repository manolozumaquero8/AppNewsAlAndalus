package com.example.appnewsalandalus.fragments

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.appnewsalandalus.databinding.FragmentSensoresBinding

class SensoresFragment : Fragment(), SensorEventListener {

    private lateinit var binding: FragmentSensoresBinding
    private lateinit var sensorManager: SensorManager

    private var sensorLuz: Sensor? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSensoresBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sensorManager = requireActivity().getSystemService(SensorManager::class.java)

        iniciarSensores()
        setListener()
    }

    private fun setListener() {
        binding.btnVolver.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun iniciarSensores() {
        sensorLuz = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
    }
    override fun onResume() {
        super.onResume()
        registrarListener(sensorLuz)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    private fun registrarListener(sensor: Sensor?) {
        sensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (it.sensor.type == Sensor.TYPE_LIGHT) {
                val luzActual = it.values[0]
                val progreso = ((luzActual / 10000f) * 100).toInt().coerceIn(0, 100)

                binding.progressBarLuz.progress = progreso

                binding.tvLuzNivel.text = "$progreso%"
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}



}
