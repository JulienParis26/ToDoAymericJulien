package com.example.aymericjulien.tasklist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.aymericjulien.network.Api

class TasksRepository {
    private val tasksWebService = Api.tasksWebService


    private val _taskList = MutableLiveData<List<Task>>()
    public val taskList: LiveData<List<Task>> = _taskList

    suspend fun loadTasks(): List<Task>? {
        val response = tasksWebService.getTasks()
        return if (response.isSuccessful) response.body() else null
    }

    suspend fun refresh() {
        val tasksResponse = tasksWebService.getTasks()
        if (tasksResponse.isSuccessful) {
            val fetchedTasks = tasksResponse.body()
            _taskList.value = fetchedTasks!!
        }
    }

    suspend fun createTask(task: Task): Task? {
        val createResponse = tasksWebService.createTask(task)
        return if(createResponse.isSuccessful) createResponse.body() else null
    }

    suspend fun updateTask(task: Task): Task? {
        val updateResponse = tasksWebService.updateTask(task)
        return if(updateResponse.isSuccessful) updateResponse.body() else null
    }

    suspend fun removeTask(task: Task): Unit? {
        val deleteResponse = tasksWebService.deleteTask(task.id)
        return if(deleteResponse.isSuccessful) deleteResponse.body() else null
    }
}