package com.jinproject.twomillustratedbook.ui.screen.collection.item

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.jinproject.twomillustratedbook.data.Entity.Book
import com.jinproject.twomillustratedbook.data.Entity.RegisterItemToBook
import com.jinproject.twomillustratedbook.databinding.CollectionListItemBinding
import com.jinproject.twomillustratedbook.ui.screen.collection.item.item.CollectionState
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class CollectionListAdapter @Inject constructor(@ActivityContext private val context: Context) :
    RecyclerView.Adapter<CollectionListAdapter.BookViewHolder>(), Filterable {
    var items = ArrayList<CollectionState>()
    var itemsUnfiltered = ArrayList<CollectionState>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = CollectionListItemBinding.inflate(LayoutInflater.from(parent.context))
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(items[position])
    }


    override fun getItemCount(): Int {
        return this.items.size
    }


    fun setItems(items: List<CollectionState>) {
        this.items.clear()
        this.items.addAll(items)

        itemsUnfiltered = this.items
    }


    inner class BookViewHolder(private val binding: CollectionListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.containerInBook.minWidth =
                context.applicationContext.resources.displayMetrics.widthPixels
        }

        fun bind(item: CollectionState) {
            var itemInfo = ""
            item.items.forEachIndexed { index, itemState ->
                if (itemState.enchantNumber != 0)
                    itemInfo += "+${itemState.enchantNumber} "

                if (itemState.count != 1)
                    itemInfo += "${itemState.name} * ${itemState.count}"
                else
                    itemInfo += itemState.name

                if (index != item.items.lastIndex)
                    itemInfo += "\n"
            }

            binding.tvContent.text = itemInfo

            var statInfo = ""
            item.stats.forEach { statState ->
                statInfo += isValidItemStat(
                    stat = statState.value,
                    statName = statState.name,
                    isPercentValue = statState.name.contains("퍼센트")
                )
            }
            binding.tvStat.text = statInfo
        }

        private fun isValidItemStat(
            stat: Double,
            statName: String,
            isPercentValue: Boolean = false
        ): String =
            if (stat != 0.0) {
                if (isPercentValue)
                    "$statName: $stat%\n"
                else
                    "$statName: $stat\n"
            } else
                ""

    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {
                if (p0.toString().isEmpty()) {
                    items = itemsUnfiltered
                } else {
                    val contentItemsFiltering = ArrayList<CollectionState>()

                    itemsUnfiltered.forEach { collectionState ->
                        collectionState.stats.forEach { statState ->
                            if (statState.name == p0.toString() && statState.value != 0.0)
                                contentItemsFiltering.add(collectionState)
                        }
                        collectionState.items.forEach { itemState ->
                            if (itemState.name == p0.toString())
                                contentItemsFiltering.add(collectionState)
                        }
                    }

                    items = contentItemsFiltering
                }
                return FilterResults().apply { values = items }
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                items = p1!!.values as ArrayList<CollectionState>
                notifyDataSetChanged()
            }
        }
    }
}