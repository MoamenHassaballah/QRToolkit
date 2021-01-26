package com.moaapps.qrtoolkit.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.moaapps.qrtoolkit.R
import com.moaapps.qrtoolkit.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    companion object{
        @JvmStatic
        fun start(context: Context, result:String) {
            val starter = Intent(context, ResultActivity::class.java)
                .putExtra("result", result)
            context.startActivity(starter)
        }
    }
    private lateinit var binding: ActivityResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.qrResults.text = intent.getStringExtra("result")

        binding.goHome.setOnClickListener { MainActivity.start(this) }
    }
}