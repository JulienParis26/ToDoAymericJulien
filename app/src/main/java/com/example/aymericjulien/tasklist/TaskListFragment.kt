package com.example.aymericjulien.tasklist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.aymericjulien.R
import com.example.aymericjulien.network.Api
import com.example.aymericjulien.task.TaskActivity
import com.example.aymericjulien.userinfo.UserInfoActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class TaskListFragment : Fragment() {
    private val tasksRepository = TasksRepository()
    val viewModel: TaskListViewModel by viewModels()
    private val taskList = mutableListOf(
            Task(id = "id_1", title = "Task 1"),
            Task(id = "id_2", title = "Task 2"),
            Task(id = "id_3", title = "Task 3")
    )
    private val adapter: TaskListAdapter = TaskListAdapter(taskList)

    companion object {
        const val ADD_TASK_REQUEST_CODE = 123
        const val AVATAR_TASK_REQUEST_CODE = 456
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_task_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)

        val openAddTask = view.findViewById<FloatingActionButton>(R.id.openAddTask)
        openAddTask.setOnClickListener {
            val intent = Intent(activity, TaskActivity::class.java)
            startActivityForResult(intent, ADD_TASK_REQUEST_CODE)
        }

        val imageView = view?.findViewById<ImageView>(R.id.image_view)
        imageView.setOnClickListener {
            val intent = Intent(activity, UserInfoActivity::class.java)
            startActivityForResult(intent, ADD_TASK_REQUEST_CODE)
        }

        viewModel.taskList.observe(viewLifecycleOwner) { newList ->
            taskList.clear()
            taskList.addAll(newList)
            adapter.notifyDataSetChanged()
        }

        recyclerView.adapter = adapter

        adapter.onEditTask = { task ->
            val intent = Intent(activity, TaskActivity::class.java)
            intent.putExtra(TaskActivity.TASK_KEY, task)
            startActivityForResult(intent, ADD_TASK_REQUEST_CODE)
        }

        adapter.onDeleteTask = { task ->
//            taskList.remove(task)
            lifecycleScope.launch {
                viewModel.deleteTask(task)
            }
            adapter.notifyDataSetChanged()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_TASK_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val task = data?.getSerializableExtra(TaskActivity.TASK_KEY) as? Task ?: return
            val indexTask = taskList.indexOfFirst{it.id == task.id}
            if (indexTask < 0) {
                lifecycleScope.launch {
                    viewModel.addTask(task)
                }
            }
            else {
                lifecycleScope.launch {
                    viewModel.editTask(task)
                }
            }
        }

        adapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        val textView = view?.findViewById<TextView>(R.id.userInfo)
        val imageView = view?.findViewById<ImageView>(R.id.image_view)
        lifecycleScope.launch {
            val userInfo = Api.userService.getInfo().body()!!
            textView?.text = "${userInfo.firstName} ${userInfo.lastName}"
            imageView?.load("${userInfo.avatar}")
            viewModel.loadTasks()
        }
    }
}


