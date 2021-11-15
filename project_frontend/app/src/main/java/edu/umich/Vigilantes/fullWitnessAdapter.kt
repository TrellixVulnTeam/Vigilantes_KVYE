package edu.umich.Vigilantes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.witness_row.view.*
import kotlinx.android.synthetic.main.witness_section_row.view.*

class fullWitnessAdapter(
    private val witnessList: MutableList<WitnessInfo>) : RecyclerView.Adapter<fullWitnessAdapter.fullWitnessViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): fullWitnessAdapter.fullWitnessViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.witness_section_row, parent, false)

        return fullWitnessViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: fullWitnessAdapter.fullWitnessViewHolder, position: Int) {
        val currentItem = witnessList[position]

        holder.name.text = currentItem.name
        holder.phone.text = currentItem.phone
        holder.desc.text = currentItem.description
    }

    override fun getItemCount() = witnessList.size

    inner class fullWitnessViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)/*, View.OnClickListener*/ {
        val name: TextView = itemView.namePreview
        val phone: TextView = itemView.phonePreview
        val desc: TextView = itemView.descriptionPreview
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