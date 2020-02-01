package com.topyunp.notepad.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.topyunp.notepad.NotePadApplication
import com.topyunp.notepad.atys.NotePadActivity
import com.topyunp.notepad.codecs.NoteCodec
import com.topyunp.notepad.dal.Note
import com.topyunp.notepad.databinding.NoteListCellLayoutBinding
import com.topyunp.notepad.helpers.DateHelper
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.lang.StringBuilder
import java.util.*
import kotlin.collections.ArrayList


class NoteListAdapter(
    private val app: NotePadApplication,
    private val mainActivity: NotePadActivity
) :
    RecyclerView.Adapter<NoteListAdapterViewHolder>() {

    private var dataSource: List<Note> = ArrayList()

    val notePadApplication = app

    init {
        refresh()
    }

    fun refresh() {
        GlobalScope.launch {
            dataSource = app.db.noteDao().getAll()
            mainActivity.runOnUiThread {
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteListAdapterViewHolder {
        val binding = NoteListCellLayoutBinding.inflate(LayoutInflater.from(parent.context))
        binding.root.layoutParams = RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.MATCH_PARENT,
            RecyclerView.LayoutParams.WRAP_CONTENT
        )
        return NoteListAdapterViewHolder(
            mainActivity,
            binding.root,
            binding,
            this
        )
    }

    override fun getItemCount(): Int {
        return dataSource.size
    }

    override fun onBindViewHolder(holder: NoteListAdapterViewHolder, position: Int) {
        val data = dataSource[position]
        holder.binding.titleTv.text = data.title
        holder.currentNote = data
        if (data.modifiedDate != null) {
            val d = Calendar.getInstance()
            d.timeInMillis = data.modifiedDate
            holder.binding.modifiedDateTv.text = DateHelper.convertToReadable(d)
        }

        if (mainActivity.deleteMode) {
            holder.binding.btnDelete.visibility = View.VISIBLE
        } else {
            holder.binding.btnDelete.visibility = View.GONE
        }
    }

    fun getNotesJson(): JSONObject? {
        return NoteCodec.encode(dataSource)
    }

    fun getNotesText(): String {
        val result = StringBuilder()
        dataSource.forEach {
            result.append("**********\n")
            result.append(it.title)
            result.append("\n")
            if (it.modifiedDate != null) {
                val d = Calendar.getInstance()
                d.timeInMillis = it.modifiedDate
                result.append(DateHelper.convertToReadable(d))
                result.append("\n")
            }
            result.append(it.content)
            result.append("\n\n")
        }
        return result.toString()
    }
}