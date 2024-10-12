package com.example.todoapp_sqlitestudio

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class TaskAdapter(context: Context, private val tasks: List<Task>) : ArrayAdapter<Task>(context, 0, tasks) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val task = getItem(position)
        val view = convertView ?: LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, parent, false)
        view.findViewById<TextView>(android.R.id.text1).text = task?.name
        view.findViewById<TextView>(android.R.id.text2).text = task?.description
        return view
    }
}