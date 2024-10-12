package com.example.todoapp_sqlitestudio

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {
    private lateinit var dbHelper: TaskDatabaseHelper
    private lateinit var listView: ListView
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var taskNameEditText: EditText
    private lateinit var taskDescriptionEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = TaskDatabaseHelper(this)
        copyDatabaseIfNeeded()

        listView = findViewById(R.id.listView)
        taskNameEditText = findViewById(R.id.taskNameEditText)
        taskDescriptionEditText = findViewById(R.id.taskDescriptionEditText)

        loadTasks()

        val btnAddTask: Button = findViewById(R.id.btnAddTask)
        btnAddTask.setOnClickListener {
            addTask()
        }

        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val selectedTask = taskAdapter.getItem(position)
            selectedTask?.let {
                taskNameEditText.setText(it.name)
                taskDescriptionEditText.setText(it.description)
                // Cập nhật Task tại vị trí đã chọn (ví dụ có thể thêm nút cập nhật sau)
                // addTask(it.id) // Gọi hàm thêm với id của task hiện tại
            }
        }
    }

    private fun loadTasks() {
        val tasks = dbHelper.getAllTasks()
        taskAdapter = TaskAdapter(this, tasks)
        listView.adapter = taskAdapter
    }

    private fun addTask() {
        val taskName = taskNameEditText.text.toString()
        val taskDescription = taskDescriptionEditText.text.toString()

        if (taskName.isNotEmpty() && taskDescription.isNotEmpty()) {
            val newTask = Task(0, taskName, taskDescription)
            dbHelper.addTask(newTask)
            loadTasks()
            taskNameEditText.text.clear()
            taskDescriptionEditText.text.clear()
        } else {
            Log.e("MainActivity", "Task name or description cannot be empty")
        }
    }

    private fun copyDatabaseIfNeeded() {
        val dbFile = getDatabasePath("tasks.db")
        if (!dbFile.exists()) {
            dbFile.parentFile?.mkdirs()
            assets.open("tasks.db").use { input ->
                FileOutputStream(dbFile).use { output ->
                    input.copyTo(output)
                }
            }
        }
    }
}