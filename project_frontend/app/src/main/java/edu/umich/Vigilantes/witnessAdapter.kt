package edu.umich.Vigilantes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.witness_row.view.*

class witnessAdapter(
    private val witnessList: MutableList<WitnessInfo>,
    private val listener: OnItemClickListener) : RecyclerView.Adapter<witnessAdapter.witnessViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): witnessViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.witness_row, parent, false)

        return witnessViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: witnessViewHolder, position: Int) {
        val currentItem = witnessList[position]

        holder.name.text = currentItem.name
        holder.phone.text = currentItem.phone
    }

    override fun getItemCount() = witnessList.size

    inner class witnessViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val name: TextView = itemView.witness_name
        val phone: TextView = itemView.witness_phone

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