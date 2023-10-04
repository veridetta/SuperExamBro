package com.vr.superexambro.lockutils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.vr.superexambro.lockactivity.MainActivity
import com.vr.superexambro.R

class WallpaperAdapter(
    private val wallpapersList: List<Wallpaper>
) :
    RecyclerView.Adapter<WallpaperAdapter.MyViewHolder>() {
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var image: ImageView

        init {
            image = view.findViewById<ImageView>(R.id.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_rcv, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val wallpaper = wallpapersList[position]
        holder.image.layoutParams.width = ((MainActivity.width * 0.4).toInt())
        holder.image.layoutParams.height = ((MainActivity.height * 0.4).toInt())
        holder.image.requestLayout()
        //        if(position % 2 == 0){
//            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//            lp.setMargins(1, 0, 0, 0);
//            holder.image.setLayoutParams(lp);
//        }
        holder.image.setImageResource(wallpaper.url)
    }

    override fun getItemCount(): Int {
        return wallpapersList.size
    }
}