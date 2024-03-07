package com.example.incidenciasparkingpmdm.ui.incidencia

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.incidenciasparkingpmdm.databinding.IncidentItemBinding


class IncidentAdapter(private val onShowEdit:(incident:Incident)->Unit,
                      private val onDeleteIncident:(incident:Incident, position:Int, adapter:IncidentAdapter)->Unit)
    : ListAdapter<Incident,IncidentAdapter.IncidentItemViewHolder>(IncidentDiffCallback) {

    //private var incidentList: List<Incident> = emptyList()

    inner class IncidentItemViewHolder(private val binding: IncidentItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(i: Incident, p: Int) {

            binding.title.text = i.title
            binding.description.text = i.description
            binding.date.text = i.date
            binding.dropButton.setOnClickListener {
                onDeleteIncident(i, p, this@IncidentAdapter)
            }
            binding.incident.setOnClickListener() {
                if (i.idInc != null)
                    onShowEdit(i)
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
        //val incident = incidentList[position]
        holder.bind(getItem(position), position)
    }

    private object IncidentDiffCallback:DiffUtil.ItemCallback<Incident>() {
        override fun areItemsTheSame(oldItem: Incident, newItem: Incident): Boolean = oldItem.idInc == newItem.idInc

        override fun areContentsTheSame(oldItem: Incident, newItem: Incident): Boolean = oldItem == newItem

    }

    /*override fun getItemCount(): Int {
        return incidentList.size
    }

    fun submitList(list: List<Incident>) {
        incidentList = list
        notifyDataSetChanged()
    }*/

}