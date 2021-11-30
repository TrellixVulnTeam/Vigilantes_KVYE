package edu.umich.Vigilantes

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.report_row.view.*

class reportListAdapter(
    private val reportList: reportList,
    private val listener: pastReports
) : RecyclerView.Adapter<reportListAdapter.reportListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): reportListViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.report_row, parent, false)

        return reportListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: reportListViewHolder, position: Int) {
        val currentItem = reportList.getList()[position]

        holder.image.setImageURI(currentItem.getUri())
        holder.datetime.text = currentItem.datetime
        holder.location.text = currentItem.location
        holder.incidentDesc.text = currentItem.incidentDesc
    }

    override fun getItemCount() = reportList.getLength()

    inner class reportListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val image: ImageView = itemView.vehicleImage
        val datetime: TextView = itemView.rep_datetime
        val location: TextView = itemView.rep_location
        val incidentDesc: TextView = itemView.rep_incidentDesc

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