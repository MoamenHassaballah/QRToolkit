package com.moaapps.qrtoolkit.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.moaapps.qrtoolkit.R
import com.moaapps.qrtoolkit.databinding.ActivityGeneratedCodeBinding
import com.moaapps.qrtoolkit.modules.QRCode
import com.moaapps.qrtoolkit.room.AppDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


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
    private lateinit var bitmap: Bitmap
    private lateinit var file:File
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
            bitmap = barcodeEncoder.encodeBitmap(content, BarcodeFormat.QR_CODE, 500, 500)
            binding.qrImage.setImageBitmap(bitmap)
        } catch (e: Exception) {
            Toast.makeText(this, R.string.error_generating_the_code, Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }

        binding.save.setOnClickListener {
            if (!permsGranted()){
                return@setOnClickListener
            }
            try {
                val directory = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "QRToolkit")
                if (!directory.exists()) {
                    val generated = directory.mkdir()
                    Log.d("TAG", "onCreate: $generated")
                }
                val file = File(directory, "${Calendar.getInstance().timeInMillis}.png")
                FileOutputStream(file).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out) // bmp is your Bitmap instance
                }
                Toast.makeText(this, R.string.qr_saved, Toast.LENGTH_SHORT).show()
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(this, R.string.error_qr_saved, Toast.LENGTH_SHORT).show()
            }
        }

        binding.share.setOnClickListener {
            if (!permsGranted()){
                return@setOnClickListener
            }
            try {
                val builder = StrictMode.VmPolicy.Builder().build()
                StrictMode.setVmPolicy(builder)
                val i = Intent(Intent.ACTION_SEND)
                i.type = "image/*"
                i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file))
                startActivity(Intent.createChooser(i, getString(R.string.share_code)))
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        binding.goHome.setOnClickListener { MainActivity.start(this) }

        saveScannedCode()

        GlobalScope.launch {
            val time = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault()).format(Calendar.getInstance().time)
            val appDatabase = AppDatabase.getInstance(applicationContext).historyDao
            appDatabase.addQRCode(QRCode(0, file.absolutePath, intent.getStringExtra("result")!!, time, "generated"))
        }

    }

    private fun saveScannedCode() {
        try {
            val directory = File(filesDir, "QRToolkit")
            if (!directory.exists()) {
                directory.mkdirs()
            }
            file = File(directory, "${Calendar.getInstance().timeInMillis}.png")
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out) // bmp is your Bitmap instance
            }
        }catch (e:IOException){
            e.printStackTrace()
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