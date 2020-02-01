package com.topyunp.notepad.atys

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.topyunp.notepad.NotePadApplication
import com.topyunp.notepad.R
import com.topyunp.notepad.adapters.NoteListAdapter
import com.topyunp.notepad.codecs.NoteCodec
import com.topyunp.notepad.dal.Note
import com.topyunp.notepad.helpers.DateHelper
import com.topyunp.notepad.helpers.FileHelper
import kotlinx.android.synthetic.main.activity_note_pad.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.File
import java.util.*

class NotePadActivity : AppCompatActivity() {

    private var _deleteMode = false
    lateinit var floatBtnAddNote: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_pad)
        setSupportActionBar(toolbar)

        if (intent.data != null) {
            readFromData()
        }

        notesRV.adapter = NoteListAdapter(application as NotePadApplication, this)
        addListeners()
    }

    private fun readFromData() {
        if (intent.data != null) {
            val filePath = intent.data!!.path
            if (filePath != null) {
                val fileName = filePath.substring(filePath.lastIndexOf('/') + 1)
                if (fileName.isNotEmpty()) {
                    val stream = contentResolver.openInputStream(intent.data!!)
                    val content = stream?.bufferedReader(Charsets.UTF_8)?.readText()
                    if (content != null) {
                        val jsonObj = NoteCodec.decode(content)
                        if (jsonObj != null) {
                            if (jsonObj.has("notes")) {
                                val notes = jsonObj.optJSONArray("notes")
                                if (notes != null) {
                                    if (notes.length() > 0) {
                                        AlertDialog.Builder(this).setTitle("提醒")
                                            .setMessage("是否导入日志 $fileName")
                                            .setPositiveButton("导入") { _, _ -> importNotes(jsonObj) }
                                            .setNegativeButton("取消", null).show()
                                    }
                                }
                            }
                        }
                    }
                    stream?.close()
                }
            }
        }
    }

    private fun importNotes(jsonObj: JSONObject) {
        GlobalScope.launch {
            val notesJsonArray = jsonObj.optJSONArray("notes")
            if (notesJsonArray != null) {
                (0 until notesJsonArray.length()).forEach {
                    val noteJsonObj = notesJsonArray.optJSONObject(it)
                    val title = noteJsonObj.optString("title")
                    val modifiedDateRead = noteJsonObj.opt("modified_date")
                    var modifiedDate = Date().time
                    val content = noteJsonObj.optString("content")

                    if (modifiedDateRead is String) {
                        val r = Regex("(\\d{4})-(\\d{2})-(\\d{2}) (\\d{2}):(\\d{2}):(\\d{2})")
                        val m = r.find(modifiedDateRead)
                        if (m != null) {
                            val d = Calendar.getInstance()

                            d.set(
                                Integer.parseInt(m.groupValues[1]),
                                Integer.parseInt(m.groupValues[2]) - 1,
                                Integer.parseInt(m.groupValues[3]),
                                Integer.parseInt(m.groupValues[4]),
                                Integer.parseInt(m.groupValues[5]),
                                Integer.parseInt(m.groupValues[6])
                            )
                            modifiedDate = d.timeInMillis
                        }
                    } else if (modifiedDateRead is Long) {
                        modifiedDate = modifiedDateRead
                    } else if (modifiedDateRead is Int) {
                        modifiedDate = modifiedDateRead.toLong()
                    }

                    (application as NotePadApplication).db.noteDao()
                        .insertNotes(
                            Note(
                                title = title,
                                content = content,
                                modifiedDate = modifiedDate
                            )
                        )

                    runOnUiThread {
                        (notesRV.adapter as? NoteListAdapter)?.refresh()
                    }
                }
            }
        }
    }

    private fun addListeners() {
        floatBtnAddNote = fab
        fab.setOnClickListener {
            startAddNewNoteActivity()
        }
    }

    private fun startAddNewNoteActivity() {
        val intent = Intent(this, EditNoteActivity::class.java)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()

        (notesRV.adapter as? NoteListAdapter)?.refresh()
    }

    override fun onPause() {
        super.onPause()

        hideDeleteButtons()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_about -> {
                startActivity(Intent(this, AboutActivity::class.java))
                true
            }
            R.id.delete_note -> {
                toggleDeleteMode()
                true
            }
            R.id.add_new_note -> {
                startAddNewNoteActivity()
                true
            }
            R.id.export_note -> {
                exportToExternalStorage()
                true
            }
            R.id.manage_exported_notes -> {
                startActivityForResult(
                    Intent(this, ManageExportedNotesActivity::class.java),
                    ManageExportedNotesActivity.REQUEST_CODE_SELECT_NOTES_TO_IMPORT
                )
                true
            }
            R.id.export_as_text -> {
                exportAsText()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun exportAsText() {
        val adapter = notesRV.adapter as? NoteListAdapter

        if (adapter != null) {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/*"
            intent.putExtra(Intent.EXTRA_TEXT, adapter.getNotesText())
            startActivity(intent)
        }
    }

    private fun exportToExternalStorage() {
        if (notesRV.adapter != null && notesRV.adapter!!.itemCount > 0) {
            val json = (notesRV.adapter as NoteListAdapter).getNotesJson()
            val file = File(
                getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),
                DateHelper.getCurrentTimeString("", "", "") + ".notes"
            )
            file.writeText(json.toString(), Charsets.UTF_8)
            AlertDialog.Builder(this).setTitle("恭喜").setMessage("已将日志成功导出到文件 ${file.absolutePath}")
                .setPositiveButton("发送") { _, _ -> FileHelper.shareFile(this, file) }
                .setNeutralButton("关闭", null)
                .setNegativeButton(
                    "查看"
                ) { _, _ ->
                    startActivity(
                        Intent(
                            this,
                            ManageExportedNotesActivity::class.java
                        )
                    )
                }.show()
        } else {
            AlertDialog.Builder(this).setTitle("提示").setMessage("请写日志后再执行导出任务")
                .setPositiveButton("好的", null).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == ManageExportedNotesActivity.REQUEST_CODE_SELECT_NOTES_TO_IMPORT && resultCode == ManageExportedNotesActivity.RESULT_CODE_OK) {
            if (data != null && data.hasExtra(ManageExportedNotesActivity.RESULT_EXTRA_KEY_FILE_PATH)) {
                val filePath =
                    data.getStringExtra(ManageExportedNotesActivity.RESULT_EXTRA_KEY_FILE_PATH)
                if (filePath != null) {
                    val file = File(filePath)
                    if (file.exists()) {
                        val jsonString = file.readText(Charsets.UTF_8)
                        if (jsonString.isNotEmpty()) {
                            val jsonObj = NoteCodec.decode(jsonString)
                            if (jsonObj != null) {
                                if (jsonObj.has("notes")) {
                                    importNotes(jsonObj)
                                } else {
                                    AlertDialog.Builder(this).setTitle("提示")
                                        .setMessage("主人您好，文件数据错误，无法导入，该文件可能已经损坏。")
                                        .setPositiveButton("关闭", null).show()
                                }
                            } else {
                                AlertDialog.Builder(this).setTitle("提示")
                                    .setMessage("主人您好，不能解析文件内容，无法导入。")
                                    .setPositiveButton("关闭", null).show()
                            }
                        } else {
                            AlertDialog.Builder(this).setTitle("提示").setMessage("主人您好，文件内容为空，无法导入。")
                                .setPositiveButton("关闭", null).show()
                        }
                    } else {
                        AlertDialog.Builder(this).setTitle("提示").setMessage("主人您好，文件不存在，无法导入。")
                            .setPositiveButton("关闭", null).show()
                    }
                } else {
                    AlertDialog.Builder(this).setTitle("提示").setMessage("主人您好，不能获取文件路径。")
                        .setPositiveButton("关闭", null).show()
                }
            } else {
                AlertDialog.Builder(this).setTitle("提示").setMessage("主人您好，数据为空，无法导入。")
                    .setPositiveButton("关闭", null).show()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun toggleDeleteMode() {
        _deleteMode = !_deleteMode
        notesRV.adapter?.notifyDataSetChanged()
    }

    fun hideDeleteButtons() {
        _deleteMode = false
        notesRV.adapter?.notifyDataSetChanged()
    }

    val deleteMode: Boolean
        get() = _deleteMode
}
