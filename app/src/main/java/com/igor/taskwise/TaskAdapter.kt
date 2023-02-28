package com.igor.taskwise

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

class TaskAdapter(val taskList:List<Task>) :RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val projectTitle = itemView.findViewById<TextView>(R.id.tv_project_description)
        val date = itemView.findViewById<TextView>(R.id.tv_date)
        val taskPriority = itemView.findViewById<TextView>(R.id.tv_task_priority)
        val taskStart = itemView.findViewById<TextView>(R.id.tv_start_time)
        val endTime = itemView.findViewById<TextView>(R.id.tv_end_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_task,parent,false)
        return TaskViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList[position]
        holder.projectTitle.text = task.projectTitle
        holder.date.text = task.date
        holder.taskPriority.text = task.projectPriority
        holder.taskStart.text = task.startTime
        holder.endTime.text = task.endTime
    }

    override fun getItemCount(): Int {
        return taskList.size
    }
}