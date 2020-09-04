package com.medialink.mysqlitekotlin

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.medialink.mysqlitekotlin.db.DatabaseContract
import com.medialink.mysqlitekotlin.db.NoteHelper
import com.medialink.mysqlitekotlin.entity.Note
import kotlinx.android.synthetic.main.activity_note_add_update.*
import java.text.SimpleDateFormat
import java.util.*

class NoteAddUpdateActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val EXTRA_NOTE = "extra_note"
        const val EXTRA_POSITION = "extra_position"
        const val REQUEST_ADD = 100
        const val RESULT_ADD = 101
        const val REQUEST_UPDATE = 200
        const val RESULT_UPDATE = 201
        const val RESULT_DELETE = 301
        const val ALERT_DIALOG_CLOSE = 10
        const val ALERT_DIALOG_DELETE = 20
    }

    private var barTitle: String = ""
    private var btnTitle: String = ""

    private var isEdit = true
        set(value) {
            if (value) {
                barTitle = "Ubah"
                btnTitle = "Update"
            } else {
                barTitle = "Tambah"
                btnTitle = "Simpan"
            }
            field = value
        }
    private var note: Note? = null
    private var position: Int = 0
    private lateinit var noteHelper: NoteHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_add_update)

        initTable()

        note = intent.getParcelableExtra(EXTRA_NOTE)
        if (note != null) {
            position = intent.getIntExtra(EXTRA_POSITION, 0)
            isEdit = true

            note?.let {
                tid_title.setText(it.title)
                tid_description.setText(it.description)
            }
        } else {
            note = Note()
            isEdit = false
        }

        supportActionBar?.title = barTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btn_submit.setText(btnTitle)
        btn_submit.setOnClickListener(this)
    }

    private fun initTable() {
        noteHelper = NoteHelper.getInstance(applicationContext)
        noteHelper.open()
    }

    override fun onClick(view: View?) {
        if (view?.id == R.id.btn_submit) {
            val title = tid_title.text.toString().trim()
            val description = tid_description.text.toString().trim()

            if (title.isEmpty()) {
                tid_title.error = "Field can not be blank"
                return
            }

            note?.title = title
            note?.description = description

            val intent = Intent()
            intent.putExtra(EXTRA_NOTE, note)
            intent.putExtra(EXTRA_POSITION, position)

            val values = ContentValues()
            values.put(DatabaseContract.NoteColumns.TITLE, title)
            values.put(DatabaseContract.NoteColumns.DESCRIPTION, description)

            if (isEdit) {
                val result = noteHelper.update(note?.id.toString(), values).toLong()
                if (result > 0) {
                    setResult(RESULT_UPDATE, intent)
                    finish()
                } else {
                    Toast.makeText(
                            this@NoteAddUpdateActivity,
                            "Gagal mengupdate data", Toast.LENGTH_SHORT)
                            .show()
                }
            } else {
                note?.date = getCurrentDate()
                values.put(DatabaseContract.NoteColumns.DATE, getCurrentDate())
                val result = noteHelper.insert(values)
                if (result > 0) {
                    note?.id = result.toInt()
                    setResult(RESULT_ADD, intent)
                    finish()
                } else {
                    Toast.makeText(this@NoteAddUpdateActivity,
                            "Gagal menambah data", Toast.LENGTH_SHORT)
                            .show()
                }
            }
        }
    }

    private fun getCurrentDate(): String? {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }
}