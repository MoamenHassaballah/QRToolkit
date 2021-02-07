package com.moaapps.qrtoolkit.modules

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class QRCode(
    @PrimaryKey(autoGenerate = true)
    var id:Int,

    @ColumnInfo(name = "value")
    var value:String,

    @ColumnInfo(name = "time")
    var time:String,

    @ColumnInfo(name = "type")
    var type:String,

    @ColumnInfo(name = "name")
    var name:String?
) {
}