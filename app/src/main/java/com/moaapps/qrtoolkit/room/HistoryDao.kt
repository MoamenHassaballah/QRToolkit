package com.moaapps.qrtoolkit.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.moaapps.qrtoolkit.modules.QRCode

@Dao
interface HistoryDao {

    @Query("SELECT * FROM history WHERE type='scanned'")
    fun getScannedCodes():List<QRCode>

    @Query("SELECT * FROM history WHERE type='generated'")
    fun getGeneratedCodes():List<QRCode>

    @Insert()
    fun addQRCode(qrCode: QRCode)

    @Delete()
    fun deleteQRCode(qrCode: QRCode)
}