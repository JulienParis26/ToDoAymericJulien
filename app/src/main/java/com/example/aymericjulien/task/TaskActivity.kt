package com.example.aymericjulien.task

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.aymericjulien.R
import com.example.aymericjulien.tasklist.Task
import java.util.*

class TaskActivity : AppCompatActivity() {
    companion object {
        const val TASK_KEY = "TASK"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        val buttonConfirm = findViewById<Button>(R.id.editConfirm)
        val editTitle = findViewById<EditText>(R.id.editTitle)
        val editDescription = findViewById<EditText>(R.id.editDescription)
        val taskToModify = intent.getSerializableExtra(TASK_KEY) as? Task
        editTitle.setText(taskToModify?.title)
        editDescription.setText(taskToModify?.description)

        buttonConfirm.setOnClickListener{
            val task = Task(id = taskToModify?.id ?: UUID.randomUUID().toString(), title = editTitle.text.toString(), description = editDescription.text.toString())
            intent.putExtra(TASK_KEY, task)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}