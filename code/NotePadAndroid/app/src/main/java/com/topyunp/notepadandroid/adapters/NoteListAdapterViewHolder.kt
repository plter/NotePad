package com.topyunp.notepadandroid.adapters

import android.content.Intent
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.topyunp.notepadandroid.atys.EditNoteActivity
import com.topyunp.notepadandroid.atys.NotePadActivity
import com.topyunp.notepadandroid.dal.Note
import com.topyunp.notepadandroid.databinding.NoteListCellLayoutBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class NoteListAdapterViewHolder(
    private val mainActivity: NotePadActivity,
    itemView: View,
    val binding: NoteListCellLayoutBinding,
    private val contextAdapter: NoteListAdapter
) :
    RecyclerView.ViewHolder(itemView) {

    private var _currentNote: Note? = null

    var currentNote: Note?
        get() = _currentNote
        set(value) {
            _currentNote = value
        }

    init {
        addListeners()
    }

    private fun addListeners() {
        addContentClickListeners()

        addContentLongClickListener()

        binding.btnDelete.setOnClickListener {
            deleteNote()
        }
    }

    private fun addContentClickListeners() {
        binding.contentContainer.setOnClickListener {
            if (mainActivity.deleteMode) {
                mainActivity.hideDeleteButtons()
            } else {
                viewNode()
            }
        }
    }

    private fun addContentLongClickListener() {
        binding.contentContainer.setOnLongClickListener {
            AlertDialog.Builder(mainActivity)
                .setTitle("操作选项")
                .setItems(
                    arrayOf<CharSequence>("预览", "编辑", "删除")
                ) { _, which ->
                    when (which) {
                        0 -> {
                            previewContent()
                        }
                        1 -> viewNode()
                        2 -> deleteNote()
                        else -> {
                        }
                    }
                }
                .show()
            return@setOnLongClickListener true
        }
    }

    private fun previewContent() {
        if (currentNote != null) {
            Snackbar.make(
                itemView,
                currentNote?.content.toString(),
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    private fun viewNode() {
        if (currentNote != null) {
            val intent = Intent(itemView.context, EditNoteActivity::class.java)
            intent.putExtra(EditNoteActivity.EXTRA_KEY_NID, currentNote!!.nid!!)
            intent.putExtra(EditNoteActivity.EXTRA_KEY_TITLE, currentNote!!.title!!)
            intent.putExtra(EditNoteActivity.EXTRA_KEY_CONTENT, currentNote!!.content!!)
            itemView.context.startActivity(intent)
        }
    }

    private fun deleteNote() {
        AlertDialog.Builder(itemView.context).setTitle("提示").setMessage("删除后无法恢复，请问您真的要删除吗？")
            .setPositiveButton("确定删除") { _, _ ->
                if (currentNote != null) {
                    GlobalScope.launch {
                        contextAdapter.notePadApplication.db.noteDao()
                            .deleteNotes(currentNote!!)
                        mainActivity.runOnUiThread {
                            contextAdapter.refresh()
                        }
                    }
                }
            }.setNegativeButton("取消", null).show()
    }
}