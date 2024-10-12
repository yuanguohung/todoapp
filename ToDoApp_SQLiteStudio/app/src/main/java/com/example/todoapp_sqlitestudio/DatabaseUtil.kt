package com.example.todoapp_sqlitestudio


import android.content.Context
import java.io.FileOutputStream
import java.io.IOException

object DatabaseUtil {
    fun copyDatabase(context: Context) {
        val dbFile = context.getDatabasePath("tasks.db")
        if (!dbFile.exists()) {
            try {
                val inputStream = context.assets.open("tasks.db")
                val outputStream = FileOutputStream(dbFile)

                val buffer = ByteArray(1024)
                var length: Int
                while (inputStream.read(buffer).also { length = it } > 0) {
                    outputStream.write(buffer, 0, length)
                }

                outputStream.flush()
                outputStream.close()
                inputStream.close()

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}
