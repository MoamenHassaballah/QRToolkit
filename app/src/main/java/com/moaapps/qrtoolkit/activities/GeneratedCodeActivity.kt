package com.moaapps.qrtoolkit.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.moaapps.qrtoolkit.R
import com.moaapps.qrtoolkit.databinding.ActivityGeneratedCodeBinding


class GeneratedCodeActivity : AppCompatActivity() {
    companion object{
        @JvmStatic
        fun start(context: Context, qr: String) {
            val starter = Intent(context, GeneratedCodeActivity::class.java)
                .putExtra("qr", qr)
            context.startActivity(starter)
        }
    }
    private lateinit var binding: ActivityGeneratedCodeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGeneratedCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        try {
            val content = intent.getStringExtra("qr")
            val barcodeEncoder = BarcodeEncoder()
            val bitmap = barcodeEncoder.encodeBitmap(content, BarcodeFormat.QR_CODE, 500, 500)
            binding.qrImage.setImageBitmap(bitmap)
        } catch (e: Exception) {
            Toast.makeText(this, R.string.error_generating_the_code , Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }
}