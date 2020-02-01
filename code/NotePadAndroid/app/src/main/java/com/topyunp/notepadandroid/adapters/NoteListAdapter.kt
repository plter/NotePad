package com.topyunp.notepadandroid.adapters

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.topyunp.notepadandroid.NotePadApplication
import com.topyunp.notepadandroid.R
import com.topyunp.notepadandroid.atys.NotePadActivity
import com.topyunp.notepadandroid.codecs.NoteCodec
import com.topyunp.notepadandroid.dal.Note
import com.topyunp.notepadandroid.databinding.NoteListCellLayoutBinding
import com.topyunp.notepadandroid.helpers.DateHelper
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
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
}