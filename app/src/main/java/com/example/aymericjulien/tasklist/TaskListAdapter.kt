package com.example.aymericjulien.tasklist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aymericjulien.R

class TaskListAdapter(val taskList: List<Task>) : RecyclerView.Adapter<TaskListAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskListAdapter.TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList[position]
        holder.bind(task)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleView = itemView.findViewById<TextView>(R.id.task_title)
        private val descriptionView = itemView.findViewById<TextView>(R.id.task_description)
        private val deleteButton = itemView.findViewById<ImageButton>(R.id.DeleteButton)
        private val editButton = itemView.findViewById<ImageButton>(R.id.EditButton)
        fun bind(task: Task) {
            titleView.text = task.title
            descriptionView.text = task.description
            deleteButton.setOnClickListener{
                onDeleteTask?.invoke(task)
            }
            editButton.setOnClickListener {
                onEditTask?.invoke(task)
            }
        }
    }

    var onDeleteTask: ((Task) -> Unit)? = null

    var onEditTask: ((Task) -> Unit)? = null

}