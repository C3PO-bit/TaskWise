package com.igor.taskwise

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_create_new_task.*
import kotlinx.android.synthetic.main.activity_create_new_task.tv_date
import kotlinx.android.synthetic.main.item_task.*
import java.text.DateFormatSymbols
import java.util.*
import java.util.concurrent.TimeUnit


class CreateNewTaskActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_new_task)
        startTaskTime()
        endTaskTime()
        datePicker()
        selectPriority()
        createNewTask()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun startTaskTime() {

        ll_task_start_holder.setOnClickListener {
            val view = LayoutInflater.from(this).inflate(R.layout.custom_time_picker, null)
            val timePicker = view.findViewById<TimePicker>(R.id.spinnerTimePicker)
            timePicker.setIs24HourView(true)

            val dialog = AlertDialog.Builder(this)

                .setView(view)
                .setPositiveButton("OK") { _, _ ->

                    val selectedHour = timePicker.hour
                    val selectedMinute = timePicker.minute


                    val selectedTime = "%02d:%02d".format(selectedHour, selectedMinute)
                    val tvTaskTime = findViewById<TextView>(R.id.tv_time_hour)
                    tvTaskTime.text = selectedTime

                    saveStartTaskTime(selectedHour,selectedMinute)
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.cancel()
                }
                .create()

            dialog.show()

        }

    }

    private fun saveStartTaskTime(hour: Int, minute: Int) {

        val sharedPreferences = getSharedPreferences(Constant.SHARED_PREF_VALUE,Context.MODE_PRIVATE)
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY,hour)
        calendar.set(Calendar.MINUTE,minute)
        val selectedTime = calendar.timeInMillis
        sharedPreferences.edit().putLong("time",selectedTime).apply()

        // Calculate time difference between current time and selected start time
        val currentTime = System.currentTimeMillis()
        val timeDiff = selectedTime - currentTime

        // Send notification only if time difference is greater than or equal to 15 minutes
        if (timeDiff >= 15 * 60 * 1000) {
            val reminderTime = selectedTime - 15 * 60 * 1000
            val intent = Intent(this,ReminderReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(this,0,intent,0)
            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,reminderTime,pendingIntent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun endTaskTime() {
        ll_task_end_holder.setOnClickListener {
            val view = LayoutInflater.from(this).inflate(R.layout.custom_time_picker, null)

            val timePicker = view.findViewById<TimePicker>(R.id.spinnerTimePicker)
            timePicker.setIs24HourView(true)

            val dialog = AlertDialog.Builder(this)
                .setView(view)
                .setPositiveButton("OK") { _, _ ->
                    val selectedHour = timePicker.hour
                    val selectedMinute = timePicker.minute

                    val selectedTime = "%02d:%02d".format(selectedHour, selectedMinute)
                    tv_time_hour_end.text = selectedTime
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.cancel()
                }
                .create()

            dialog.show()
        }
    }

    private fun datePicker() {
        ll_task_date_holder.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val monthNames = DateFormatSymbols().months
            DatePickerDialog(
                this,
                { view, year, month, day ->
                    val date = "$day ${monthNames[month]}"
                    tv_date.text = date
                },
                year,
                month,
                day
            ).show()
        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun selectPriority() {
        ll_task_priority.setOnClickListener {
            val priorityChoice = arrayOf("High", "Medium", "Low")
            var selectedPriority = 0
            val builder = AlertDialog.Builder(this)
                .setTitle("Select Priority")
                .setSingleChoiceItems(priorityChoice, selectedPriority) { dialog, which ->
                    selectedPriority = which
                }
                .setPositiveButton("OK") { dialog, which ->
                    val selected = priorityChoice[selectedPriority]
                    if (selectedPriority == 0) {
                        tv_priority.setTextColor(this.getColor(R.color.high_priority))
                    } else if (selectedPriority == 1) {
                        tv_priority.setTextColor(this.getColor(R.color.medium_priority))
                    } else {
                        tv_priority.setTextColor(this.getColor(R.color.low_priority))
                    }
                    tv_priority.text = selected
                }
                .setNegativeButton("Cancel") { dialog, which ->
                    dialog.dismiss()
                }
            builder.show()
        }

    }

    private fun createNewTask() {
        create_task_button.setOnClickListener {
            var isValid = true

            //validate fields
            if (et_task_title.text.isNullOrEmpty()) {
                et_task_title.background = ContextCompat.getDrawable(this, R.drawable.red_outline)
                isValid = false
            } else {
                et_task_title.background = null
            }

            if (tv_date.text.isNullOrEmpty()) {
                isValid = false
            }

            if (tv_priority.text.isNullOrEmpty()) {
                isValid = false
            }

            if (tv_time_hour.text.isNullOrEmpty()) {
                isValid = false
            }

            if (tv_time_hour_end.text.isNullOrEmpty()) {
                isValid = false
            }

            if(et_description.text.isNullOrEmpty()) {
                isValid = false
            }
            // If all fields are filled, create the task
            if (isValid) {
                Intent().apply {
                    putExtra(Constant.EXTRA_TASK_TITLE, et_task_title.text.toString())
                    putExtra(Constant.EXTRA_TASK_DATE, tv_date.text.toString())
                    putExtra(Constant.EXTRA_TASK_PRIORITY, tv_priority.text.toString())
                    putExtra(Constant.EXTRA_TASK_START, tv_time_hour.text.toString())
                    putExtra(Constant.EXTRA_TASK_END, tv_time_hour_end.text.toString())
                    putExtra(Constant.EXTRA_DESCRIPTION,et_description.text.toString())
                    setResult(RESULT_OK, this)
                }
                finish()
            } else {
                // Display a Toast message if any fields are left empty
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            }

        }
    }


}