package com.igor.taskwise

import android.widget.EditText
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.sql.Date
import java.sql.Time
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Task(
    var projectTitle: String?,
    var projectPriority: String?,
    var startTime: String?,
    var endTime: String?,
    var date: String?,
    var description:String?
) : Parcelable
