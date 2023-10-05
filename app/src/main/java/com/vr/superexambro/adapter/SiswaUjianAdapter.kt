package com.vr.superexambro.adapter
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vr.superexambro.R
import com.vr.superexambro.helper.calculateRemainingTime
import com.vr.superexambro.helper.convertStringToDate
import com.vr.superexambro.helper.formatDateToIndonesian
import com.vr.superexambro.helper.startTimerSiswa
import com.vr.superexambro.model.PaketModel
import com.vr.superexambro.model.UjianModel
import java.util.Locale


class SiswaUjianAdapter(
    private var dataList: MutableList<UjianModel>,
    val context: Context,
) : RecyclerView.Adapter<SiswaUjianAdapter.DataViewHolder>() {
    public var filteredDataList: MutableList<UjianModel> = mutableListOf()
    init {
        filteredDataList.addAll(dataList)
    }
    override fun getItemViewType(position: Int): Int {
        return if (position == 0 && filteredDataList.isEmpty()) {
            1 // Return 1 for empty state view
        } else {
            0 // Return 0 for regular product view
        }
    }
    fun filter(query: String) {
        filteredDataList.clear()
        if (query !== null || query !=="") {
            val lowerCaseQuery = query.toLowerCase(Locale.getDefault())
            for (data in dataList) {
                val nam = data.namaSiswa?.toLowerCase(Locale.getDefault())?.contains(lowerCaseQuery)
                Log.d("Kunci ", lowerCaseQuery)
                if (nam == true) {
                    filteredDataList.add(data)
                    Log.d("Ada ", data.namaSiswa.toString())
                }
            }
        } else {
            filteredDataList.addAll(dataList)
        }
        notifyDataSetChanged()
        Log.d("Data f",filteredDataList.size.toString())
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ujian, parent, false)
        return DataViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return filteredDataList.size
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val currentData = filteredDataList[position]
        holder.tvNamaSiswa.text = currentData.namaSiswa
        holder.tvStatus.text = currentData.status
        holder.tvTanggal.text = formatDateToIndonesian(convertStringToDate(currentData.waktuMulai.toString(),"dd MMMM yyyy")!!)
        if (currentData.status == "Selesai"){
            holder.tvDurasi.text = "00:00:00"
        }else{
            startTimerSiswa( calculateRemainingTime(currentData.waktuMulai!!,currentData.durasi!!.toLong()).toInt(),holder.tvDurasi)
        }
    }

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNamaSiswa: TextView = itemView.findViewById(R.id.tvNamaSiswa)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        val tvTanggal: TextView = itemView.findViewById(R.id.tvTanggal)
        val tvDurasi: TextView = itemView.findViewById(R.id.tvDurasi)
    }
}
