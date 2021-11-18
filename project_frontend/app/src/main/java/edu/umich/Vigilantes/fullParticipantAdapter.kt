package edu.umich.Vigilantes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.participant_row.view.*
import kotlinx.android.synthetic.main.participant_section_row.view.*

class fullParticipantAdapter(
    private val participantList: MutableList<ParticipantInfo>) : RecyclerView.Adapter<fullParticipantAdapter.fullParticipantViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): fullParticipantViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.participant_section_row, parent, false)

        return fullParticipantViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: fullParticipantViewHolder, position: Int) {
        val currentItem = participantList[position]

        holder.name.text = currentItem.name
        holder.addr.text = currentItem.addr
        holder.zip.text = currentItem.zip
        holder.city.text = currentItem.city
        holder.license.text = currentItem.license
        holder.phone.text = currentItem.phone
        holder.insurance.text = currentItem.insurance
        holder.policy.text = currentItem.policy
        holder.expiration.text = currentItem.expiration
        holder.agentNumber.text = currentItem.agentNumber
    }

    override fun getItemCount() = participantList.size

    inner class fullParticipantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)/*, View.OnClickListener*/ {
        val name: TextView = itemView.namePreview
        val addr: TextView = itemView.AddrPreview
        val zip: TextView = itemView.zipPreview
        val city: TextView = itemView.cityPreview
        val state: TextView = itemView.statePreview
        val license: TextView = itemView.licensePreview
        val phone: TextView = itemView.phonePreview
        val insurance: TextView = itemView.insurancePreview
        val policy: TextView = itemView.policyPreview
        val expiration: TextView = itemView.expirationPreview
        val agentNumber: TextView = itemView.agentNumberPreview
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