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
import com.vr.superexambro.helper.convertStringToDate
import com.vr.superexambro.helper.formatDateToIndonesian
import com.vr.superexambro.model.PaketModel
import java.util.Locale


class UjianAdapter(
    private var dataList: MutableList<PaketModel>,
    val context: Context,
    private val onEditClickListener: (PaketModel) -> Unit,
) : RecyclerView.Adapter<UjianAdapter.DataViewHolder>() {
    public var filteredDataList: MutableList<PaketModel> = mutableListOf()
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
                val nam = data.namaUjian?.toLowerCase(Locale.getDefault())?.contains(lowerCaseQuery)
                Log.d("Kunci ", lowerCaseQuery)
                if (nam == true) {
                    filteredDataList.add(data)
                    Log.d("Ada ", data.namaUjian.toString())
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
        holder.tvNamaUjian.text = currentData.namaUjian
        holder.tvMapel.text = currentData.mapel
        holder.tvKodeUjian.text = currentData.shortUrl
        holder.tvTanggal.text = formatDateToIndonesian(convertStringToDate(currentData.created_at!!,"dd MMMM yyyy")!!)
        holder.tvDurasi.text = currentData.durasi
        holder.tvKelas.text = currentData.kelas

        holder.btnUbah.setOnClickListener { onEditClickListener(currentData) }
    }

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNamaUjian: TextView = itemView.findViewById(R.id.tvNamaUjian)
        val tvMapel: TextView = itemView.findViewById(R.id.tvMapel)
        val tvKodeUjian: TextView = itemView.findViewById(R.id.tvKodeUjian)
        val tvTanggal: TextView = itemView.findViewById(R.id.tvTanggal)
        val tvDurasi: TextView = itemView.findViewById(R.id.tvDurasi)
        val tvKelas: TextView = itemView.findViewById(R.id.tvKelas)
        val btnUbah: LinearLayout = itemView.findViewById(R.id.btnUbah)
    }
}
