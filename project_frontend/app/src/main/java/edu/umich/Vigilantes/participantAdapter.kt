package edu.umich.Vigilantes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.participant_row.view.*

class participantAdapter(
    private val participantList: MutableList<ParticipantInfo>,
    private val listener: OnItemClickListener) : RecyclerView.Adapter<participantAdapter.participantViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): participantViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.participant_row, parent, false)

        return participantViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: participantViewHolder, position: Int) {
        val currentItem = participantList[position]

        holder.name.text = currentItem.name
        holder.license.text = currentItem.license
        holder.phone.text = currentItem.phone
    }

    override fun getItemCount() = participantList.size

    inner class participantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val name: TextView = itemView.part_name
        val license: TextView = itemView.part_license
        val phone: TextView = itemView.part_phone

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