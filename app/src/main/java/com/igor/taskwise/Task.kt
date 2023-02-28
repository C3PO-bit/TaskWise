package com.igor.taskwise

import android.widget.EditText
import java.sql.Date
import java.sql.Time

data class Task(
    var projectTitle:String?,
    var projectPriority:String?,
    var startTime:String?,
    var endTime:String?,
    var date:String?
)
