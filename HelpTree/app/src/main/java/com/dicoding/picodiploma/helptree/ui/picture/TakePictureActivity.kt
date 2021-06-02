package com.dicoding.picodiploma.helptree.ui.picture

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.dicoding.picodiploma.helptree.R
import com.dicoding.picodiploma.helptree.databinding.ActivityTakePictureBinding
import com.dicoding.picodiploma.helptree.ml.CNNBiasa2
import com.dicoding.picodiploma.helptree.ui.chatbot.ChatbotActivity
import com.dicoding.picodiploma.helptree.ui.information.InfoActivity
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.TensorProcessor
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp

class TakePictureActivity : AppCompatActivity(), View.OnClickListener {

    private var _activityTakePictureBinding:ActivityTakePictureBinding? = null
    private val activityTakePictureBinding get() = _activityTakePictureBinding
    private lateinit var bitmap: Bitmap
    private lateinit var imgView: ImageView
    private var nameDisease: String? = null
    private lateinit var button: ImageButton
    private lateinit var tvPredict: TextView
    private lateinit var tvDetail: TextView
    private lateinit var diseaselist: List<String>
    private lateinit var diseaseDetails: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityTakePictureBinding = ActivityTakePictureBinding.inflate(layoutInflater)
        setContentView(activityTakePictureBinding?.root)

        imgView = activityTakePictureBinding?.resultImg!!
        val filename = "labels.txt"
        val filenamedetail = "details.txt"
        val inputString = application.assets.open(filename).bufferedReader().use { it.readText() }
        val inputStringdetail = application.assets.open(filenamedetail).bufferedReader().use { it.readText() }
        diseaselist = inputString.split("\n")
        diseaseDetails = inputStringdetail.split("\n")

        val select = activityTakePictureBinding?.btnSelect
        val reset = activityTakePictureBinding?.btnReset
        val solusi = activityTakePictureBinding?.btnSolusi
        val info = activityTakePictureBinding?.btnInfoDetail
         button = activityTakePictureBinding?.btnCam!!

        button.isEnabled = false

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 111)
        }else{
            button.isEnabled = true
        }

        val predict = activityTakePictureBinding?.btnPredict
        tvPredict = activityTakePictureBinding?.tvPrediction!!
        tvDetail = activityTakePictureBinding?.tvDetails!!

        select?.setOnClickListener(this)
        reset?.setOnClickListener(this)
        solusi?.setOnClickListener(this)
        info?.setOnClickListener(this)
        predict?.setOnClickListener(this)
        button.setOnClickListener(this)


        predict?.setOnClickListener {
            if (activityTakePictureBinding?.resultImg?.drawable != null) {
                val imageProcessor = ImageProcessor.Builder()
                    .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.BILINEAR))
                    .build()

                var tImage = TensorImage(DataType.FLOAT32)

                tImage.load(bitmap)
                tImage = imageProcessor.process(tImage)

                val probabilityProcessor =
                    TensorProcessor.Builder().add(NormalizeOp(0f, 255f)).build()

                val model = CNNBiasa2.newInstance(this@TakePictureActivity)
                val outputs =
                    model.process(probabilityProcessor.process(tImage.tensorBuffer))
                val outputBuffer = outputs.outputFeature0AsTensorBuffer

                val max = getMax(outputBuffer.floatArray)
                tvPredict.text = diseaselist[max]
                tvDetail.text = diseaseDetails[max]
                nameDisease = diseaselist[max]
                model.close()
            } else {
                Toast.makeText(
                    applicationContext,
                    "Silahkan Pilih Gambar Terlebih dahulu",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==111 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            button.isEnabled = true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==121){
            val img = data?.getParcelableExtra<Bitmap>("data")
            imgView.setImageBitmap(img)
            if (img != null) {
                bitmap = img
            }else{
                Log.d("Error","Tidak bisa konvert bitmap")
            }
        }else{
            imgView.setImageURI(data?.data)
            val uri: Uri? = data?.data
            try {
                if (Build.VERSION.SDK_INT >= 29) {
                    val source = ImageDecoder.createSource(this.contentResolver, uri!!)
                    bitmap = ImageDecoder.decodeBitmap(source)
                    bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
                } else {
                    bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri!!)
                }
            } catch (e: Exception) {
                println("Could not convert image to BitMap")
                e.printStackTrace()
            }
        }
    }

    private fun getMax(arr: FloatArray):Int{
        var idx = 0
        var min = 0.0f

        for (i in 0..37){
            if(arr[i]>min){
                idx = i
                min = arr[i]
            }
        }
        return idx
    }

    override fun onClick(view: View) {
        if(view.id == R.id.btn_solusi){
                if (activityTakePictureBinding?.tvPrediction?.text != "Nama Penyakit") {
                    val intent = Intent(
                        this,
                        ChatbotActivity::class.java
                    )
                    intent.putExtra("DISEASE_NAME", nameDisease)
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Silahkan Pilih Gambar Terlebih dahulu",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }else if(view.id == R.id.btn_reset){
            activityTakePictureBinding?.tvPrediction?.text = getString(R.string.tvpred)
            activityTakePictureBinding?.resultImg?.setImageResource(0)
        }else if(view.id == R.id.btn_info_detail){
            val intent = Intent(this, InfoActivity::class.java)
            startActivity(intent)
        }else if(view.id == R.id.btn_select){
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"

            startActivityForResult(intent, 100)
        }else if(view.id == R.id.btn_cam){
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, 121)
        }else if(view.id == R.id.btn_predict){
            if (activityTakePictureBinding?.resultImg?.drawable != null) {
                val imageProcessor = ImageProcessor.Builder()
                    .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.BILINEAR))
                    .build()

                var tImage = TensorImage(DataType.FLOAT32)

                tImage.load(bitmap)
                tImage = imageProcessor.process(tImage)

                val probabilityProcessor =
                    TensorProcessor.Builder().add(NormalizeOp(0f, 255f)).build()

                val model = CNNBiasa2.newInstance(this@TakePictureActivity)
                val outputs =
                    model.process(probabilityProcessor.process(tImage.tensorBuffer))
                val outputBuffer = outputs.outputFeature0AsTensorBuffer

                val max = getMax(outputBuffer.floatArray)
                tvPredict.text = diseaselist[max]
                tvDetail.text = diseaseDetails[max]
                nameDisease = diseaselist[max]
                model.close()
            } else {
                Toast.makeText(
                    applicationContext,
                    "Silahkan Pilih Gambar Terlebih dahulu",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }else if(view.id == R.id.btn_back){
            onBackPressed()
        }
    }
}