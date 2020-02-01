package com.topyunp.notepad.atys

import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.google.android.material.snackbar.Snackbar
import com.topyunp.notepad.NotePadApplication
import com.topyunp.notepad.R
import com.topyunp.notepad.dal.Note
import kotlinx.android.synthetic.main.activity_edit_note.*
import kotlinx.android.synthetic.main.activity_note_pad.toolbar
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class EditNoteActivity : AppCompatActivity() {

    private var nid: Int = -1
    private var title: String? = null
    private var content: String? = null
    private var modified = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_note)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.edit_note)

        readExtras()
        addListeners()
    }

    private fun addListeners() {
        titleTxt.addTextChangedListener { modified = true }
        contentTxt.addTextChangedListener { modified = true }
    }

    private fun readExtras() {
        nid = intent.getIntExtra(EXTRA_KEY_NID, -1)
        title = intent.getStringExtra(EXTRA_KEY_TITLE)
        content = intent.getStringExtra(EXTRA_KEY_CONTENT)

        titleTxt.setText(title)
        contentTxt.setText(content)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_edit_note, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.action_save_note -> {
                saveNote()
                return true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun saveNote() {
        if (!TextUtils.isEmpty(titleTxt.text) || !TextUtils.isEmpty(contentTxt.text)) {
            val app = application as NotePadApplication
            GlobalScope.launch {
                val now = Date().time
                if (nid != -1) {
                    if (
                        app.db.noteDao().updateNotes(
                            Note(
                                nid = nid,
                                title = titleTxt.text.toString(),
                                content = contentTxt.text.toString(),
                                modifiedDate = now
                            )
                        ) <= 0
                    ) {
                        app.db.noteDao().insertNotes(
                            Note(
                                title = titleTxt.text.toString(),
                                content = contentTxt.text.toString(),
                                modifiedDate = now
                            )
                        )
                    }
                } else {
                    app.db.noteDao().insertNotes(
                        Note(
                            title = titleTxt.text.toString(),
                            content = contentTxt.text.toString(),
                            modifiedDate = now
                        )
                    )
                }

                runOnUiThread {
                    finish()
                }
            }
        } else {
            Snackbar.make(contentTxt, "请填写标题或者内容后再保存", Snackbar.LENGTH_LONG).show()
        }
    }

    override fun onBackPressed() {
        if (modified && (!TextUtils.isEmpty(titleTxt.text) || !TextUtils.isEmpty(contentTxt.text))) {
            AlertDialog.Builder(this).setTitle("提醒").setMessage("有内容尚未保存，您确定放弃吗？")
                .setPositiveButton("放弃") { _, _ -> finish() }
                .setNegativeButton("继续编辑", null).show()
        } else {
            super.onBackPressed()
        }
    }

    companion object {
        const val EXTRA_KEY_NID = "nid"
        const val EXTRA_KEY_TITLE = "title"
        const val EXTRA_KEY_CONTENT = "content"
    }
}