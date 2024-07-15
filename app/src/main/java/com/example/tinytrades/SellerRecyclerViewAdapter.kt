package com.example.tinytrades

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tinytrades.database.Item

class SellerRecyclerViewAdapter(private var items: MutableList<Item>) : RecyclerView.Adapter<SellerRecyclerViewAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemImage: ImageView = itemView.findViewById(R.id.image)
        val itemTitle: TextView = itemView.findViewById(R.id.title)
        val itemSize: TextView = itemView.findViewById(R.id.size)
        val itemPrice: TextView = itemView.findViewById(R.id.price)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.seller_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.itemTitle.text = item.title
        holder.itemSize.text = item.size
        holder.itemPrice.text = item.price.toString()
        item.image?.let {
            holder.itemImage.setImageBitmap(BitmapFactory.decodeByteArray(it, 0, it.size))
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateItems(newItems: List<Item>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}
