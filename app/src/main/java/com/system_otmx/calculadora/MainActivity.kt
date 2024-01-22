package com.system_otmx.calculadora

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.system_otmx.calculadora.Adapters.AdaptadorHistorial
import com.system_otmx.calculadora.ModeloHistorial.ModeloHistorial
import com.system_otmx.calculadora.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.BtnSYSTEMOTMX.setOnClickListener {
            Toast.makeText(this, "HOLA", Toast.LENGTH_SHORT).show()
        }
        cargarHistorial()
    }

    private fun cargarHistorial(){
        val historialArrayList = ArrayList<ModeloHistorial>()
        for(i in 0  until Constantes.test_historial.size){
            val modeloHistorial = ModeloHistorial(Constantes.test_historial[i])
            historialArrayList.add(modeloHistorial)
        }

        val adaptadorHistorial = AdaptadorHistorial(
            this,
            historialArrayList,
            object  : RvListenerHistorial{
                override fun onHistorialClick(modeloHistorial: ModeloHistorial) {
                    super.onHistorialClick(modeloHistorial)
                }
            }
        )

        binding.AMRVHistorial.adapter = adaptadorHistorial
    }
}