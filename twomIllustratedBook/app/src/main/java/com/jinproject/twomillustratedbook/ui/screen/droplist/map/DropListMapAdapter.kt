package com.jinproject.twomillustratedbook.ui.screen.droplist.map

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jinproject.twomillustratedbook.databinding.DroplistmapItemBinding
import com.jinproject.twomillustratedbook.listener.OnItemClickListener
import com.jinproject.twomillustratedbook.ui.screen.droplist.map.item.MapState
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class DropListMapAdapter @Inject constructor(@ActivityContext val context: Context) :
    RecyclerView.Adapter<DropListMapAdapter.ViewHolder>(), OnItemClickListener {
    var items = ArrayList<MapState>()
    var mlistener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(DroplistmapItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItems(item: List<MapState>) {
        items.addAll(item)
    }

    fun getItem(pos: Int): String {
        return items[pos].name
    }

    inner class ViewHolder(private val binding: DroplistmapItemBinding) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("DiscouragedApi")
        fun bind(item: MapState) {
            binding.dropMapName.text = item.name
            val res = context.resources.getIdentifier(item.imgName, "drawable", context.packageName)
            binding.dropMapImg.setImageResource(res)
        }

        init {
            binding.root.setOnClickListener {
                mlistener?.OnHomeItemClick(it, adapterPosition)
            }
        }
    }

    override fun OnHomeItemClick(v: View, pos: Int) {
        mlistener?.OnHomeItemClick(v, pos)
    }

    fun setItemClickListener(listener: OnItemClickListener) {
        mlistener = listener
    }
}