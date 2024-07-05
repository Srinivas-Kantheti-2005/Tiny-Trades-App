package com.example.tinytrades

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class AdapterClass(private var dataList: ArrayList<DataClass>) : RecyclerView.Adapter<AdapterClass.ViewHolderClass>() {

    class ViewHolderClass(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rvImage: ImageView = itemView.findViewById(R.id.image)
        val rvTitle: TextView = itemView.findViewById(R.id.title)
        val rvPrice: TextView = itemView.findViewById(R.id.price)
        val cardView: CardView = itemView.findViewById(R.id.cardView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ViewHolderClass(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem = dataList[position]
        holder.rvImage.setImageResource(currentItem.dataImage)
        holder.rvTitle.text = currentItem.dataTitle
        holder.rvPrice.text = currentItem.dataPrice

        // Adjusting heights of CardView (400dp to 600dp) without affecting ImageView
        val layoutParams = holder.cardView.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.height = dpToPx(getRandomHeight())
        holder.cardView.layoutParams = layoutParams
    }

    fun updateList(filteredList: ArrayList<DataClass>) {
        dataList = filteredList
        notifyDataSetChanged()
    }

    private fun getRandomHeight(): Int {
        return (500 .. 550).random()
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }
}
