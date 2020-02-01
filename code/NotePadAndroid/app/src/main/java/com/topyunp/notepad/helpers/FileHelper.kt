package com.topyunp.notepad.helpers

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import java.io.File

object FileHelper {

    fun shareFile(context: Context, file: File) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/*"
        val fileUri = FileProvider.getUriForFile(
            context,
            "${context.applicationContext.packageName}.provider",
            file
        )
        intent.putExtra(Intent.EXTRA_STREAM, fileUri)
        intent.putExtra(Intent.EXTRA_TEXT, file.name)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        context.startActivity(Intent.createChooser(intent, "日志分享"))
    }
}