package com.example.weba.presentation.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weba.databinding.ListItemBinding
import com.example.weba.domain.models.AppInfo

class MyAdapter(private val values: List<AppInfo>, private val onClick: (Int) -> Unit) :
    RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context))
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = values.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = values[position]
        holder.binding.nameApp.text = item.name
        holder.binding.moreButton.setOnClickListener {
            onClick(position)
        }

    }
}