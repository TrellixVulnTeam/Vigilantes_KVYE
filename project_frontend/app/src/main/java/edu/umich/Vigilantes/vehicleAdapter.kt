package edu.umich.Vigilantes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.vehicle_row.view.*

class vehicleAdapter(
    private val vehicleList: MutableList<VehicleInfo>,
    private val listener: OnItemClickListener) : RecyclerView.Adapter<vehicleAdapter.vehicleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): vehicleViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.vehicle_row, parent, false)

        return vehicleViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: vehicleViewHolder, position: Int) {
        val currentItem = vehicleList[position]

        holder.makemodel.text = currentItem.makemodel
        holder.plateNumber.text = currentItem.plateNumber
        holder.VIN.text = currentItem.VIN
    }

    override fun getItemCount() = vehicleList.size

    inner class vehicleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val makemodel: TextView = itemView.car_makemodel
        val plateNumber: TextView = itemView.car_plate
        val VIN: TextView = itemView.car_vin

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position: Int = adapterPosition
            if(position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}