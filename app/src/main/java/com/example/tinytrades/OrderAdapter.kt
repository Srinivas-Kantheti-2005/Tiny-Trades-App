package com.example.tinytrades

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tinytrades.database.Order

class OrderAdapter(
    private val items: MutableList<Order>,
    private val onCancelClick: (Order) -> Unit
): RecyclerView.Adapter<OrderAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val orderImage: ImageView = itemView.findViewById(R.id.orderImage)
        val orderTitle: TextView = itemView.findViewById(R.id.orderTitle)
        val orderSize: TextView =  itemView.findViewById(R.id.orderSize)
        val orderQuantity: TextView = itemView.findViewById(R.id.orderQuantity)
        val orderPrice: TextView = itemView.findViewById(R.id.orderPrice)
        val cancelbtn: Button = itemView.findViewById(R.id.cancelbtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.your_order_layout, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.orderTitle.text = item.title
        holder.orderSize.text = item.size
        holder.orderQuantity.text = item.quantity.toString()
        holder.orderPrice.text = item.price.toString()
        holder.cancelbtn?.setOnClickListener {
            onCancelClick(item)
        }
        item.image?.let {
            holder.orderImage.setImageBitmap(BitmapFactory.decodeByteArray(it, 0, it.size))
        }
    }

    fun updateOrders(newOrders: List<Order>) {
        items.clear()
        items.addAll(newOrders)
        notifyDataSetChanged()
    }
}
