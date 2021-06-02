package com.dicoding.picodiploma.helptree.ui.home

import android.content.Intent
import android.content.res.TypedArray
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.picodiploma.helptree.R
import com.dicoding.picodiploma.helptree.data.InfoAdapter
import com.dicoding.picodiploma.helptree.data.News
import com.dicoding.picodiploma.helptree.databinding.ActivityHomeBinding
import com.dicoding.picodiploma.helptree.ui.chatbot.ChatbotActivity
import com.dicoding.picodiploma.helptree.ui.information.InfoActivity
import com.dicoding.picodiploma.helptree.ui.picture.TakePictureActivity

class HomeActivity : AppCompatActivity(), View.OnClickListener {


    private var _activityHomeBinding: ActivityHomeBinding? = null
    private val activityHomeBinding get() = _activityHomeBinding
    private lateinit var dnama:Array<String>
    private lateinit var dpath:Array<String>
    private lateinit var ddesc:Array<String>
    private lateinit var dphoto: TypedArray
    private var dev:ArrayList<News> = arrayListOf()
    private lateinit var listDevA: RecyclerView
    private lateinit var infoadapter: InfoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityHomeBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(activityHomeBinding?.root)

        activityHomeBinding?.btnCamera?.setOnClickListener(this)
        activityHomeBinding?.info?.setOnClickListener(this)
        activityHomeBinding?.btnChat?.setOnClickListener(this)

        supportActionBar?.elevation = 0f

        listDevA=findViewById(R.id.listDevA)
        listDevA.setHasFixedSize(true)

        getSource()
        addToDisplay()
        showRecyclerList()


    }
    private fun showRecyclerList() {
        listDevA.layoutManager = LinearLayoutManager(this)
        infoadapter = InfoAdapter(dev)
        listDevA.adapter = infoadapter

    }
    private fun getSource(){
        dnama=resources.getStringArray(R.array.title)
        dpath=resources.getStringArray(R.array.url_link)
        dphoto=resources.obtainTypedArray(R.array.photoArtikel)
        ddesc=resources.getStringArray(R.array.descr)

    }
    private fun addToDisplay(){
        for (position in dnama.indices) {
            dev.add(
                News(
                    name = dnama[position],
                    photo = dphoto.getResourceId(position, -1),
                    path = dpath[position],
                    desc =ddesc[position]
                )
            )
        }
    }
    override fun onClick(view: View?) {
        when ( view!!.id)
        {
            R.id.btn_camera->
            {
                val intent = Intent(this, TakePictureActivity::class.java)
                startActivity(intent)
            }
            R.id.btn_chat->
            {
                val intent = Intent(this, ChatbotActivity::class.java)
                startActivity(intent)
            }
            R.id.info->
            {
                val intent = Intent(this, InfoActivity::class.java)
                startActivity(intent)
            }
        }
    }
//    private var _activityHomeBinding: ActivityHomeBinding? = null
//    private val activityHomeBinding get() = _activityHomeBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        _activityHomeBinding = ActivityHomeBinding.inflate(layoutInflater)
//        setContentView(activityHomeBinding?.root)
//
//        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
//        activityHomeBinding?.viewPager?.adapter = sectionsPagerAdapter
//        activityHomeBinding?.tabs?.setupWithViewPager(activityHomeBinding?.viewPager)
//
//        activityHomeBinding?.btnCamera?.setOnClickListener(this)
//        activityHomeBinding?.btnChat?.setOnClickListener(this)
//        supportActionBar?.elevation = 0f
//
//    }
//
//    override fun onClick(view: View?) {
//        when ( view!!.id)
//        {
//            R.id.btn_camera->
//            {
//                val intent = Intent(this, TakePictureActivity::class.java)
//                startActivity(intent)
//            }
//            R.id.btn_chat->
//            {
//                val intent = Intent(this, ChatbotActivity::class.java)
//                startActivity(intent)
//            }
//        }
//    }
}