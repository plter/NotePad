package com.topyunp.notepadandroid.adapters

import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.topyunp.notepadandroid.atys.ManageExportedNotesActivity
import com.topyunp.notepadandroid.databinding.NotesFilesListCellBinding
import java.io.File

class ExportedNotesListItemVH(
    itemView: View,
    val binding: NotesFilesListCellBinding,
    private val manageExportedNotesActivity: ManageExportedNotesActivity,
    private val exportedNotesListAdapter: ExportedNotesListAdapter
) : RecyclerView.ViewHolder(itemView) {

    private var _currentFile: File? = null

    var currentFile: File?
        get() = _currentFile
        set(value) {
            _currentFile = value
        }

    init {
        binding.contentContainer.setOnClickListener {
            if (manageExportedNotesActivity.deleteMode) {
                manageExportedNotesActivity.hideDeleteButtons()
            } else {
                if (currentFile != null) {
                    exportedNotesListAdapter.performItemClick(currentFile!!)
                }
            }
        }

        binding.btnDelete.setOnClickListener {
            AlertDialog.Builder(manageExportedNotesActivity).setTitle("提醒")
                .setMessage("您真的要删除该文件吗？").setPositiveButton("删除") { _, _ ->
                    _currentFile?.delete()
                    manageExportedNotesActivity.refresh()
                }
                .setNegativeButton("取消", null).show()
        }
    }
}



