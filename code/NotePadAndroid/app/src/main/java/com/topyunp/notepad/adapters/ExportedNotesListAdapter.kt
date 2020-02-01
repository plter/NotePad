package com.topyunp.notepad.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.topyunp.notepad.atys.ManageExportedNotesActivity
import com.topyunp.notepad.databinding.NotesFilesListCellBinding
import java.io.File

class ExportedNotesListAdapter(private val manageExportedNotesActivity: ManageExportedNotesActivity) :
    RecyclerView.Adapter<ExportedNotesListItemVH>() {

    private val dataSource = ArrayList<File>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExportedNotesListItemVH {
        val binding = NotesFilesListCellBinding.inflate(LayoutInflater.from(parent.context))
        binding.root.layoutParams = RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.MATCH_PARENT,
            RecyclerView.LayoutParams.WRAP_CONTENT
        )
        return ExportedNotesListItemVH(
            binding.root,
            binding,
            manageExportedNotesActivity,
            this
        )
    }

    override fun getItemCount(): Int = dataSource.size

    override fun onBindViewHolder(holder: ExportedNotesListItemVH, position: Int) {
        val data = dataSource[position]
        holder.currentFile = data
        holder.binding.fileNameTv.text = data.name
        if (manageExportedNotesActivity.deleteMode) {
            holder.binding.btnDelete.visibility = View.VISIBLE
        } else {
            holder.binding.btnDelete.visibility = View.GONE
        }
    }

    fun setAll(files: List<File>) {
        dataSource.clear()
        dataSource.addAll(files)
        notifyDataSetChanged()
    }

    fun performItemClick(file: File) {
        onItemClickedListener(file)
    }

    private var _onItemClickedListener = { _: File -> }
    var onItemClickedListener: (File) -> Unit
        get() = _onItemClickedListener
        set(value) {
            _onItemClickedListener = value
        }
}


