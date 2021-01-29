package com.moaapps.qrtoolkit.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.moaapps.qrtoolkit.R
import com.moaapps.qrtoolkit.databinding.ActivityGenerateQrBinding
import pub.devrel.easypermissions.EasyPermissions

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
                if (permsGranted()){
                    GeneratedCodeActivity.start(this, qrText)
                }else{
                    Toast.makeText(this, R.string.accept_perms, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun permsGranted():Boolean{
        val granted = EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
        if (!granted){
            EasyPermissions.requestPermissions(this, getString(R.string.perms_rational), 123, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        return granted
    }
}