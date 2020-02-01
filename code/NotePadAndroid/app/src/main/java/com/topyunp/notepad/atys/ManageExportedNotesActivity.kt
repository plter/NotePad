package com.topyunp.notepad.atys

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.topyunp.notepad.R
import com.topyunp.notepad.adapters.ExportedNotesListAdapter
import com.topyunp.notepad.helpers.FileHelper
import kotlinx.android.synthetic.main.activity_manage_exported_notes.*
import java.io.File

class ManageExportedNotesActivity : AppCompatActivity() {


    private lateinit var adapter: ExportedNotesListAdapter
    private var _deleteMode = false

    val deleteMode: Boolean
        get() = _deleteMode

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_exported_notes)
        supportActionBar?.title = "管理导出的日志"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        adapter = ExportedNotesListAdapter(this)
        notesFilesRV.adapter = adapter

        adapter.onItemClickedListener = { file ->
            AlertDialog.Builder(this).setTitle("选择操作")
                .setMessage("导入 ${file.name}?")
                .setPositiveButton("确定") { _, _ ->
                    val data = Intent()
                    data.putExtra(RESULT_EXTRA_KEY_FILE_PATH, file.absolutePath)
                    setResult(RESULT_CODE_OK, data)
                    finish()
                }.setNegativeButton("取消", null).show()
        }

        refresh()
    }

    fun refresh() {
        val dir = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        val notesFiles = ArrayList<File>()
        dir?.listFiles()?.forEach {
            if (it.name.endsWith(".notes")) {
                notesFiles.add(it)
            }
        }
        notesFiles.sortDescending()
        adapter.setAll(notesFiles)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.delete_exported_notes_file -> {
                switchDeleteMode()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_manage_exported_notes_activity, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun switchDeleteMode() {
        _deleteMode = !_deleteMode
        adapter.notifyDataSetChanged()
    }

    fun hideDeleteButtons() {
        _deleteMode = false
        adapter.notifyDataSetChanged()
    }

    companion object {
        const val REQUEST_CODE_SELECT_NOTES_TO_IMPORT = 10001
        const val RESULT_CODE_OK = 10002
        const val RESULT_EXTRA_KEY_FILE_PATH = "filePath"
    }
}