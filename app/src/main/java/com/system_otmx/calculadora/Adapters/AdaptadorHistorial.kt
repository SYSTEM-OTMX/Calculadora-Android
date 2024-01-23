package com.system_otmx.calculadora.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.system_otmx.calculadora.ModeloHistorial.ModeloHistorial
import com.system_otmx.calculadora.RvListenerHistorial
import com.system_otmx.calculadora.databinding.ItemHistorialResultadoBinding

class AdaptadorHistorial(
    private val context : Context,
    private val historialArrayList: ArrayList<ModeloHistorial>,
    private val rvListenerHistorial : RvListenerHistorial

):Adapter<AdaptadorHistorial.HolderHistorial>() {
    private lateinit var binding:ItemHistorialResultadoBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderHistorial {
        binding = ItemHistorialResultadoBinding.inflate(LayoutInflater.from(context),parent,false)
        return HolderHistorial(binding.root)
    }

    override fun getItemCount(): Int {
        return 4
    }

    override fun onBindViewHolder(holder: AdaptadorHistorial.HolderHistorial, position: Int) {
        var modeloHistorial = historialArrayList[position]
        val historial = modeloHistorial.historial
        holder.historialNumber.text = historial


    }

    inner class HolderHistorial(itemView:View):ViewHolder(itemView){
        var historialNumber = binding.IHRTVNumero
    }

}

