package com.tvolution.tvtransformer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class EndStateMomentAdapter : RecyclerView.Adapter<EndStateMomentAdapter.ItemViewHolder>() {

    var list = arrayListOf<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_captured_moments, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        list[position].run {
            Glide.with(holder.ivImage.context).load(list.get(position))
                .placeholder(R.drawable.cardone).into(holder.ivImage)
        }
    }

    override fun getItemCount(): Int = list.size


    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivImage: ImageView = itemView.findViewById(R.id.iv_image)
    }
}