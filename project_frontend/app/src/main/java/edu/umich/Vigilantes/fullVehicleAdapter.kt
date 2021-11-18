package edu.umich.Vigilantes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.vehicle_row.view.*
import kotlinx.android.synthetic.main.vehicle_section_row.view.*

class fullVehicleAdapter(
    private val vehicleList: MutableList<VehicleInfo>) : RecyclerView.Adapter<fullVehicleAdapter.fullVehicleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): fullVehicleAdapter.fullVehicleViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.vehicle_section_row, parent, false)

        return fullVehicleViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: fullVehicleViewHolder, position: Int) {
        val currentItem = vehicleList[position]

        holder.makemodel.text = currentItem.makemodel
        holder.year.text = currentItem.year
        holder.plateNumber.text = currentItem.plateNumber
        holder.VIN.text = currentItem.VIN
        holder.color.text = currentItem.color
    }

    override fun getItemCount() = vehicleList.size

    inner class fullVehicleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)/*, View.OnClickListener*/ {
        val makemodel: TextView = itemView.makemodelPreview
        val year: TextView = itemView.yearPreview
        val plateNumber: TextView = itemView.plateNumberPreview
        val VIN: TextView = itemView.VINPreview
        val color: TextView = itemView.colorPreview
        /*
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position: Int = adapterPosition
            if(position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }*/
    }
    /*
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }*/
}