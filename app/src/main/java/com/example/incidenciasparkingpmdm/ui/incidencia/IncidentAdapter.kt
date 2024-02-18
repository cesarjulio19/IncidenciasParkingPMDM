package com.example.incidenciasparkingpmdm.ui.incidencia

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.incidenciasparkingpmdm.databinding.IncidentItemBinding


class IncidentAdapter(private val context: Context, private val onShowEdit:(id:Int, v: View)->Unit)
    : RecyclerView.Adapter<IncidentAdapter.IncidentItemViewHolder>() {

    private var incidentList: List<Incident> = emptyList()

    inner class IncidentItemViewHolder(private val binding: IncidentItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(i: Incident) {

            binding.title.text = i.title
            binding.description.text = i.description
            binding.date.text = i.date
            if(!i.state){
                binding.state2.text = ""
            }


            binding.incident.setOnClickListener(){
                onShowEdit(i.id,binding.root)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IncidentItemViewHolder {
        val binding = IncidentItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return IncidentItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IncidentItemViewHolder, position: Int) {
        val incident = incidentList[position]
        holder.bind(incident)
    }

    override fun getItemCount(): Int {
        return incidentList.size
    }

    fun submitList(list: List<Incident>) {
        incidentList = list
        notifyDataSetChanged()
    }
}