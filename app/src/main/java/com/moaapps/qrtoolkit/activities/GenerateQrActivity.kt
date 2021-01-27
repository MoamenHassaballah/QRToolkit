package com.moaapps.qrtoolkit.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.moaapps.qrtoolkit.R
import com.moaapps.qrtoolkit.databinding.ActivityGenerateQrBinding

class GenerateQrActivity : AppCompatActivity() {
    companion object{
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, GenerateQrActivity::class.java)
            context.startActivity(starter)
        }
    }
    private lateinit var binding: ActivityGenerateQrBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGenerateQrBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.generateCode.setOnClickListener {
            val qrText = binding.qrText.text.toString()
            if (qrText.isEmpty()){
                Toast.makeText(this, R.string.enter_code_text, Toast.LENGTH_SHORT).show()
            }else{
                GeneratedCodeActivity.start(this, qrText)
            }
        }
    }
}