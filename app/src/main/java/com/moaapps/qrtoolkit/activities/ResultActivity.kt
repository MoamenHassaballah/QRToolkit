package com.moaapps.qrtoolkit.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.moaapps.qrtoolkit.R
import com.moaapps.qrtoolkit.databinding.ActivityResultBinding
import com.moaapps.qrtoolkit.modules.QRCode
import com.moaapps.qrtoolkit.room.AppDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

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

        GlobalScope.launch {
            val time = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault()).format(Calendar.getInstance().time)
            val appDatabase = AppDatabase.getInstance(this@ResultActivity.applicationContext).historyDao()
            appDatabase.addQRCode(QRCode(0, null, intent.getStringExtra("result")!!, time, "scanned"))
        }
    }
}