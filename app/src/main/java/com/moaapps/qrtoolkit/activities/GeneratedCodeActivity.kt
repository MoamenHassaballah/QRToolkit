package com.moaapps.qrtoolkit.activities

import android.Manifest
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.provider.MediaStore
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
    private lateinit var fileName:String
    private lateinit var imageUri:Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGeneratedCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        val content = intent.getStringExtra("qr")
        fileName = "${Calendar.getInstance().timeInMillis}.jpeg"
        try {
            val barcodeEncoder = BarcodeEncoder()
            bitmap = barcodeEncoder.encodeBitmap(content, BarcodeFormat.QR_CODE, 500, 500)
            binding.qrImage.setImageBitmap(bitmap)
        } catch (e: Exception) {
            Toast.makeText(this, R.string.error_generating_the_code, Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }

        binding.save.setOnClickListener {
            try {
                saveScannedCodeToStorage()
                Toast.makeText(this, R.string.qr_saved, Toast.LENGTH_SHORT).show()
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(this, R.string.error_qr_saved, Toast.LENGTH_SHORT).show()
            }
        }

        binding.share.setOnClickListener {
            try {
                val builder = StrictMode.VmPolicy.Builder().build()
                StrictMode.setVmPolicy(builder)
                val i = Intent(Intent.ACTION_SEND)
                i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                i.type = "image/*"
                i.putExtra(Intent.EXTRA_STREAM, getFileForShare())
                startActivity(Intent.createChooser(i, getString(R.string.share_code)))
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        binding.goHome.setOnClickListener { MainActivity.start(this) }

//        saveScannedCode()

        GlobalScope.launch {
            val time = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault()).format(Calendar.getInstance().time)
            val appDatabase = AppDatabase.getInstance(applicationContext).historyDao()
            appDatabase.addQRCode(QRCode(0, content!!, time, "generated", fileName))
        }

    }

//    private fun saveScannedCode() {
//        try {
//            val directory = File(filesDir, getString(R.string.app_name))
//            if (!directory.exists()) {
//                directory.mkdirs()
//            }
//            file = File(directory, "${Calendar.getInstance().timeInMillis}.jpeg")
//            FileOutputStream(file).use { out ->
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out) // bmp is your Bitmap instance
//            }
//
//        }catch (e:IOException){
//            e.printStackTrace()
//        }
//    }


    private fun saveScannedCodeToStorage():Uri {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, "${Environment.DIRECTORY_PICTURES}/${getString(R.string.app_name)}")
        }

        if (!isFileSaved()){
            imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)!!
            contentResolver.openOutputStream(imageUri).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
        }
        return imageUri
    }

    private fun getFileForShare():Uri {
        val dir = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), getString(R.string.app_name))
        if (!dir.exists()){
            dir.mkdirs()
        }
        val file = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "${getString(R.string.app_name)}/${fileName}")
        if (!file.exists()){
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        }
        return Uri.fromFile(file)
    }

    private fun isFileSaved():Boolean{
        val projection = arrayOf(
                MediaStore.MediaColumns._ID
        )

        val selection = "${MediaStore.MediaColumns.RELATIVE_PATH}='${Environment.DIRECTORY_PICTURES}/${getString(R.string.app_name)}/' AND " +
                "${MediaStore.MediaColumns.DISPLAY_NAME}='$fileName' "

        contentResolver.query( MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection, selection, null, null ).use { c ->
            if (c != null && c.count >= 1){
                c.moveToFirst().let {
                    val id = c.getLong(c.getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
                    imageUri = ContentUris.withAppendedId(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,  id)
                }
            }
            Log.d("TAG", "isFileSaved: ${c != null && c.count >= 1}")
            return c != null && c.count >= 1
        }
    }

}