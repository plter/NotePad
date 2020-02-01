package com.topyunp.notepad.helpers

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import java.io.File

object FileHelper {

    fun shareFile(context: Context, file: File) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/json"
        val fileUri = FileProvider.getUriForFile(
            context,
            "${context.applicationContext.packageName}.provider",
            file
        )
        intent.putExtra(Intent.EXTRA_STREAM, fileUri)
        intent.putExtra(Intent.EXTRA_TITLE, file.name)
        intent.putExtra(Intent.EXTRA_TEXT, file.name)
        context.startActivity(intent)
    }
}