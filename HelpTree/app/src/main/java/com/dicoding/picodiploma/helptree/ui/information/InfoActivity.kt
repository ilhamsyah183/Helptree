package com.dicoding.picodiploma.helptree.ui.information

import android.content.res.TypedArray
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.picodiploma.helptree.R
import com.dicoding.picodiploma.helptree.data.Developer
import com.dicoding.picodiploma.helptree.data.DeveloperAdapter

class InfoActivity : AppCompatActivity() {

    private lateinit var dnama:Array<String>
    private lateinit var dpath:Array<String>
    private lateinit var dphoto:TypedArray
    private var dev:ArrayList<Developer> = arrayListOf()
    private lateinit var listDev:RecyclerView
    private lateinit var infoadapter:DeveloperAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        listDev=findViewById(R.id.listDev)
        listDev.setHasFixedSize(true)

        getSource()
        addToDisplay()
        showRecyclerList()


    }
    private fun showRecyclerList() {
        listDev.layoutManager = LinearLayoutManager(this)
        infoadapter =DeveloperAdapter(dev)
        listDev.adapter = infoadapter

    }
    private fun getSource(){
        dnama=resources.getStringArray(R.array.nama_dev)
        dpath=resources.getStringArray(R.array.path_dev)
        dphoto=resources.obtainTypedArray(R.array.photo_dev)
    }
    private fun addToDisplay(){
        for (position in dnama.indices) {
            dev.add(
                Developer(
                    name = dnama[position],
                    photo = dphoto.getResourceId(position, -1),
                    path = dpath[position]
                )
            )
        }
    }

}
