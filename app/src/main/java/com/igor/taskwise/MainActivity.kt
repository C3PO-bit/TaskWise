package com.igor.taskwise

import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieAnimationView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    private val taskList = mutableListOf<Task>()
    private lateinit var adapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addNewTask()
        greetingText()
        setupGreetingImage()
        setupAdapter()
        sortTasks()
    }

    private fun sortTasks() {
        val sortItems = arrayOf("Priority","Date")
        var selectedItem = 0
        fab_sort.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Sort tasks by")
                .setSingleChoiceItems(sortItems,selectedItem) {_,which ->
                    selectedItem = which
                }
                .setPositiveButton("Sort") { _, _ ->
                    when (selectedItem) {
                        0 -> sortTasksByPriority()
                        1 -> sortTasksByDate()
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    private fun sortTasksByPriority() {
        //TODO: Implement logic
    }

    private fun sortTasksByDate() {
        //TODO: Implement logic
    }


    private fun setupAdapter() {
        adapter = TaskAdapter(taskList)
        rv_current_tasks.adapter = adapter
        rv_current_tasks.layoutManager = LinearLayoutManager(this)
        addNewTask()
    }

    private fun addNewTask() {
        btn_new_task.setOnClickListener {
            Intent(this, CreateNewTaskActivity::class.java).also {
                startActivityForResult(it, Constant.ADD_TASK_RESULT)
            }
        }
    }

    private fun addTask(task: Task) {
        taskList.add(task)
        adapter.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constant.ADD_TASK_RESULT && resultCode == RESULT_OK) {
            val taskTitle = data?.getStringExtra(Constant.EXTRA_TASK_TITLE)
            val taskPriority = data?.getStringExtra(Constant.EXTRA_TASK_PRIORITY)
            val taskStartTime = data?.getStringExtra(Constant.EXTRA_TASK_START)
            val taskEndTime = data?.getStringExtra(Constant.EXTRA_TASK_END)
            val taskDate = data?.getStringExtra(Constant.EXTRA_TASK_DATE)
            val task = taskTitle?.let { Task(it, taskPriority, taskStartTime, taskEndTime, taskDate) }
            if (task != null) {
                    addTask(task)
            }
        }
    }

    private fun setupGreetingImage() {
        val animationView = findViewById<LottieAnimationView>(R.id.iv_greeting)
        val currentTime = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        when {
            currentTime < 6 -> {
                animationView.setAnimation(R.raw.good_night)
            }
            currentTime < 12 -> {
                animationView.setAnimation(R.raw.good_morning)
            }
            currentTime < 18 -> {
                animationView.setAnimation(R.raw.good_afternoon)
            }
            else -> {
                animationView.setAnimation(R.raw.good_evening)
            }
        }
        animationView.playAnimation()
    }

    private fun greetingText() {
        val currentTime = Calendar.getInstance()
        val currentHour = currentTime.get(Calendar.HOUR_OF_DAY)
        val currentAmPm = currentTime.get(Calendar.AM_PM)

        val message = if (DateFormat.is24HourFormat(this)) {
            when (currentHour) {
                in 0..5 -> "Good Night"
                in 6..11 -> "Good Morning"
                in 12..16 -> "Good Afternoon"
                in 17..23 -> "Good Evening"
                else -> "Hello"
            }
        } else {
            when (currentAmPm) {
                Calendar.AM -> when (currentHour) {
                    in 6..11 -> "Good Morning"
                    else -> "Good Night"
                }
                else -> when (currentHour) {
                    in 0..4 -> "Good Night"
                    in 5..11 -> "Good Morning"
                    in 12..16 -> "Good Afternoon"
                    in 17..19 -> "Good Evening"
                    else -> "Good Night"
                }
            }
        }

        tv_greeting.text = message
    }
}
