package com.example.tinytrades

import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tinytrades.database.Item

class SellerItemPageRecyclerViewAdapter(private var items: MutableList<Item>) : RecyclerView.Adapter<SellerItemPageRecyclerViewAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val itemImage: ImageView = itemView.findViewById(R.id.item_image)
        val itemTitle: TextView = itemView.findViewById(R.id.item_title)
        val itemSize: TextView = itemView.findViewById(R.id.item_size)
        val itemPrice: TextView = itemView.findViewById(R.id.item_price)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.seller_item_page_layout, parent, false)
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

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, SellerItemDetailsPage::class.java).apply {
                putExtra("itemImage", item.image)
                putExtra("itemTitle", item.title)
                putExtra("itemQuantity", item.quantity)
                putExtra("itemUsername", item.username)
                putExtra("itemPrice", item.price)
                putExtra("emailId", item.emailid)
                putExtra("itemSize", item.size)
            }
            context.startActivity(intent)
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
