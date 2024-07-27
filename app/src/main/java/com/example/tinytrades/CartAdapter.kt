package com.example.tinytrades

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tinytrades.database.Cart

class CartAdapter(
    private val cartItems: MutableList<Cart>,
    private val onUpdateClick: (Cart, Int) -> Unit
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.add_to_cart_layout, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return cartItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = cartItems[position]

        currentItem.image?.let {
            val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            holder.image.setImageBitmap(bitmap)
        } ?: holder.image.setImageResource(android.R.color.transparent)

        holder.title.text = currentItem.title
        holder.size.text = currentItem.size
        holder.price.text = currentItem.price.toString()
        holder.quantity.text = currentItem.quantity.toString()

        holder.update.setOnClickListener {
            val updatedQuantity = holder.quantity.text.toString().toIntOrNull() ?: 0
            onUpdateClick(currentItem, updatedQuantity)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.cart_image)
        val title: TextView = itemView.findViewById(R.id.cart_title)
        val size: TextView = itemView.findViewById(R.id.cart_size)
        val price: TextView = itemView.findViewById(R.id.cart_price)
        val quantity: TextView = itemView.findViewById(R.id.cart_quantity)
        val update: Button = itemView.findViewById(R.id.update)
    }

    fun updateCart(newCart: List<Cart>) {
        cartItems.clear()
        cartItems.addAll(newCart)
        notifyDataSetChanged()
    }
}
