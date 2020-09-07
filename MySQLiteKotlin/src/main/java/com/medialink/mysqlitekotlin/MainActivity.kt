package com.medialink.mysqlitekotlin

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.medialink.mysqlitekotlin.adapter.NoteAdapter
import com.medialink.mysqlitekotlin.db.NoteHelper
import com.medialink.mysqlitekotlin.entity.Note
import com.medialink.mysqlitekotlin.helper.MappingHelper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_STATE = "extra_state"
    }

    private lateinit var adapter: NoteAdapter
    private lateinit var noteHelper: NoteHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.title = "Notes"

        adapter = NoteAdapter(this)
        initView()

        fab_add.setOnClickListener {
            val intent = Intent(this@MainActivity, NoteAddUpdateActivity::class.java)
            startActivityForResult(intent, NoteAddUpdateActivity.REQUEST_ADD)
        }

        noteHelper = NoteHelper.getInstance(applicationContext)
        noteHelper.open()

        // proses ambil data
        if (savedInstanceState == null) {
            loadNoteAsyc()
        } else {
            val list = savedInstanceState.getParcelableArrayList<Note>(EXTRA_STATE)
            if (list != null) {
                adapter.listNote = list
            }
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listNote)
    }

    private fun loadNoteAsyc() {
        GlobalScope.launch(Dispatchers.Main) {
            progress_circular.visibility = View.VISIBLE
            val defferedNotes = async(Dispatchers.IO) {
                val cursor = noteHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }

            progress_circular.visibility = View.GONE
            val notes = defferedNotes.await()
            if (notes.size > 0) {
                adapter.listNote = notes
            } else {
                adapter.listNote = ArrayList()
                showSnackbarMessage("Tidak ada data saat ini")
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        noteHelper.close()
    }

    private fun initView() {
        rv_notes.layoutManager = LinearLayoutManager(this)
        rv_notes.setHasFixedSize(true)
        rv_notes.adapter = adapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            when (requestCode) {
                NoteAddUpdateActivity.REQUEST_ADD -> if (resultCode == NoteAddUpdateActivity.RESULT_ADD) {
                    val note = data.getParcelableExtra<Note>(NoteAddUpdateActivity.EXTRA_NOTE)
                    note?.let { adapter.addItem(it) }
                    rv_notes.smoothScrollToPosition(adapter.itemCount - 1)

                    showSnackbarMessage("Satu item berhasil ditambahkan")
                }
                NoteAddUpdateActivity.REQUEST_UPDATE -> when (resultCode) {
                    NoteAddUpdateActivity.RESULT_UPDATE -> {
                        val note = data.getParcelableExtra<Note>(NoteAddUpdateActivity.EXTRA_NOTE)
                        val position = data.getIntExtra(NoteAddUpdateActivity.EXTRA_POSITION, 0)
                        note?.let { adapter.updateItem(position, it) }
                        rv_notes.smoothScrollToPosition(position)

                        showSnackbarMessage("Satu item berhasil diubah")
                    }
                    NoteAddUpdateActivity.RESULT_DELETE -> {
                        val position = data.getIntExtra(NoteAddUpdateActivity.EXTRA_POSITION, 0)
                        adapter.removeItem(position)

                        showSnackbarMessage("Satu item berhasil dihapus")
                    }
                }
            }
        }
    }

    private fun showSnackbarMessage(msg: String) {
        Snackbar.make(rv_notes, msg, Snackbar.LENGTH_SHORT).show()
    }
}
