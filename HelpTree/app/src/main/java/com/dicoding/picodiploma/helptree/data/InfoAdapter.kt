package com.dicoding.picodiploma.helptree.data

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.picodiploma.helptree.R
import com.dicoding.picodiploma.helptree.ui.home.WebViewAct
import com.dicoding.picodiploma.helptree.ui.home.WebViewAct.Companion.EXTRA_ID
import java.util.*


class InfoAdapter  (private val listDev: ArrayList<News>):  RecyclerView.Adapter<InfoAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val view: View = LayoutInflater.from (parent.context).inflate(R.layout.item_list, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = listDev.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val portal=listDev[position]

        holder.nama.text = portal.name
        holder.pathL.text = portal.path
        Glide.with(holder.itemView.context)
            .load(portal.photo)
            .apply(RequestOptions())
            .into(holder.imgPhoto)

        holder.itemView.setOnClickListener {view->
            val moveIntentWithObject = Intent(view.context, WebViewAct::class.java)
            moveIntentWithObject.putExtra(EXTRA_ID,portal)
            view.context.startActivity(moveIntentWithObject)
        }

    }

    inner class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val nama:TextView=itemView.findViewById(R.id.nama)
        val pathL:TextView=itemView.findViewById(R.id.pathL)
        val imgPhoto:ImageView=itemView.findViewById(R.id.img_photo)
    }

}