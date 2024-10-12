package com.example.todoapp_sqlitestudio

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class TaskDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "tasks.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_TASKS = "tasks"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableSQL = "CREATE TABLE tasks (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "description TEXT)"
        db.execSQL(createTableSQL)
    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TASKS")
        onCreate(db)
    }

    fun addTask(task: Task) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("name", task.name)
        values.put("description", task.description)

        val result = db.insert(TABLE_TASKS, null, values)
        if (result == -1L) {
            Log.e("DatabaseError", "Failed to insert task: $task")
        } else {
            Log.d("DatabaseSuccess", "Task added with ID: $result")
        }
        db.close()
    }

    fun getAllTasks(): List<Task> {
        val tasks = mutableListOf<Task>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_TASKS", null)

        if (cursor.moveToFirst()) {
            do {
                val task = Task(cursor.getInt(0), cursor.getString(1), cursor.getString(2))
                tasks.add(task)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return tasks
    }

    fun updateTask(task: Task) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("name", task.name)
        values.put("description", task.description)
        db.update(TABLE_TASKS, values, "id = ?", arrayOf(task.id.toString()))
        db.close()
    }

    fun deleteTask(id: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_TASKS, "id = ?", arrayOf(id.toString()))
        db.close()
    }
}