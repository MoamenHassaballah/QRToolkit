package com.moaapps.qrtoolkit.adapters

import android.app.Activity
import android.app.AlertDialog
import android.content.*
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.os.StrictMode
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.moaapps.qrtoolkit.R
import com.moaapps.qrtoolkit.modules.QRCode
import com.moaapps.qrtoolkit.room.AppDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.nio.file.Files.delete


class HistoryAdapter(private val activity:Activity, private val list: ArrayList<QRCode>) : RecyclerView.Adapter<HistoryAdapter.Holder>() {


    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title:TextView = itemView.findViewById(R.id.history_title)
        val time:TextView = itemView.findViewById(R.id.history_time)
        val copy:ImageView = itemView.findViewById(R.id.copy)
        val delete:ImageView = itemView.findViewById(R.id.delete)
        val share:ImageView = itemView.findViewById(R.id.share)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return if (viewType == 1){
            Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_scanned_history, parent, false))
        }else{
            Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_generated_history, parent, false))
        }
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val qrCode = list[position]
        holder.title.text = qrCode.value
        holder.time.text = qrCode.time

        holder.copy.setOnClickListener {
            val clipboardManager = activity.getSystemService(Activity.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("primary", qrCode.value)
            clipboardManager.setPrimaryClip(clipData)
            Toast.makeText(activity, R.string.qr_title_copied, Toast.LENGTH_SHORT).show()
        }

        holder.delete.setOnClickListener {
            AlertDialog.Builder(activity)
                    .setTitle(activity.getString(R.string.delete))
                    .setMessage(activity.getString(R.string.delete_qr_msg))
                    .setPositiveButton(R.string.yes) { _: DialogInterface, _: Int ->
                        GlobalScope.launch {
                            val historyDao = AppDatabase.getInstance(activity).historyDao()
                            historyDao.deleteQRCode(qrCode)
                            list.remove(qrCode)
                            activity.runOnUiThread {
                                notifyDataSetChanged()
                            }
                        }
                    }
                    .setNegativeButton(R.string.no,null)
                    .show()
        }

        val isGenerated = qrCode.type == "generated"
        holder.share.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            if (isGenerated){
                val builder = StrictMode.VmPolicy.Builder().build()
                StrictMode.setVmPolicy(builder)
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.type = "image/*"
                intent.putExtra(Intent.EXTRA_STREAM, saveScannedCodeToStorage(qrCode))
            }else{
                intent.type = "text/plain"
            }
            intent.putExtra(Intent.EXTRA_TEXT, qrCode.value)
            activity.startActivity(Intent.createChooser(intent, activity.getString(R.string.share_code)))
        }

        if (isGenerated){
            val qrImage:ImageView = holder.itemView.findViewById(R.id.qr_image)
            qrImage.setImageBitmap(getQrImage(qrCode.value))
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (list[position].type == "scanned"){
            1
        }else{
            2
        }
    }

    private fun getQrImage(content:String):Bitmap{
        val barcodeEncoder = BarcodeEncoder()
        return  barcodeEncoder.encodeBitmap(content, BarcodeFormat.QR_CODE, 500, 500)
    }

    private fun isFileSaved(fileName:String):Boolean{
        val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "${activity.getString(R.string.app_name)}/${fileName}")
        Log.d("TAG", "isFileSaved: ${file.absolutePath}, ${file.exists()}")
        return file.exists()
    }

    private fun saveScannedCodeToStorage(qrCode: QRCode): Uri {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, qrCode.name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, "${Environment.DIRECTORY_PICTURES}/${activity.getString(R.string.app_name)}")
        }

        var imageUri:Uri? = null
        if (!isFileSaved(qrCode.name!!)){
            imageUri = activity.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)!!
            activity.contentResolver.openOutputStream(imageUri).use { out ->
                getQrImage(qrCode.value).compress(Bitmap.CompressFormat.PNG, 100, out)
            }
        }else{
            val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "${activity.getString(R.string.app_name)}/${qrCode.name}")
            imageUri = Uri.fromFile(file)
        }
        return imageUri!!
    }
}
