package com.dicoding.picodiploma.helptree.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.picodiploma.helptree.databinding.ItemListBinding
import java.util.*

class DeveloperAdapter  (private val listDev:ArrayList<Developer>):RecyclerView.Adapter<DeveloperAdapter.PortalViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PortalViewHolder {
        val itemList = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PortalViewHolder(itemList)
    }

    override fun getItemCount(): Int = listDev.size

    override fun onBindViewHolder(holder: PortalViewHolder, position: Int) {
        holder.bind(listDev[position])
    }

    inner class PortalViewHolder(private val binding: ItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(webs: Developer) {
            with(binding) {
                nama.text = webs.name
                pathL.text = webs.path
                Glide.with(itemView.context)
                    .load(webs.photo)
                    .apply(RequestOptions())
                    .into(imgPhoto)

            }
        }
    }
}