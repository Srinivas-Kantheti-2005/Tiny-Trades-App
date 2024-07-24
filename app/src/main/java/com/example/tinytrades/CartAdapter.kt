package com.example.tinytrades

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tinytrades.database.Cart

class CartAdapter(private val context: Context, private val CartItems: List<Cart>): RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.add_to_cart_layout, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return CartItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = CartItems[position]
        currentItem.image?.let {
            holder.image.setImageBitmap(BitmapFactory.decodeByteArray(it, 0, it.size))
        }
        holder.title.text = currentItem.title
        holder.size.text = currentItem.size
        holder.price.text = currentItem.price.toString()

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, CartItemDetails::class.java)
        }
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.cartImage)
        val title: TextView = itemView.findViewById(R.id.cartTitle)
        val size: TextView = itemView.findViewById(R.id.cartSize)
        val price: TextView = itemView.findViewById(R.id.cartPrice)
    }

//    fun updateCart(newCart: List<Cart>) {
//        CartItems.clear()
//        CartItems.addAll(newCart)
//        notifyDataSetChanged()
//    }
}
