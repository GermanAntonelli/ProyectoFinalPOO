package com.example.myapplicationcustomfab

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

   private var listGernotes = ArrayList<GernoteActivity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fabButton_Opciones.setClosedOnTouchOutside(true)//Cierra el menu si se preciona fuera de el


        fabButton_Add.setOnClickListener {
            val intento1 = Intent(this, Add_gernote::class.java)
            startActivity(intento1)
            Toast.makeText(this, "AGREGAR NOTA", Toast.LENGTH_SHORT).show()
        }

        fabButton_Add_Photo.setOnClickListener {
            val intento2 = Intent(this, Photo_Activity::class.java)
            startActivity(intento2)
            Toast.makeText(this, "PHOTO", Toast.LENGTH_SHORT).show()
        }


        loadQueryAll()

    }

    override fun onResume() {
        super.onResume()
        loadQueryAll()
    }

    fun loadQueryAll() {

        var dbManager = GernoteDbManager(this)
        val cursor = dbManager.queryAll()

        listGernotes.clear()
        if (cursor.moveToFirst()) {

            do {
                val id = cursor.getInt(cursor.getColumnIndex("Id"))
                val title = cursor.getString(cursor.getColumnIndex("Title"))
                val content = cursor.getString(cursor.getColumnIndex("Content"))
                //val image = cursor.getString(cursor.getColumnIndex("Image"))

                listGernotes.add(GernoteActivity(id, title,content))

            } while (cursor.moveToNext())
        }

        var notesAdapter = NotesAdapter(this, listGernotes)
        lvgernote.adapter = notesAdapter
    }


    inner class NotesAdapter : BaseAdapter {

        private var notesList = ArrayList<GernoteActivity>()
        private var context: Context? = null

        constructor(context: Context, notesList: ArrayList<GernoteActivity>) : super() {
            this.notesList = notesList
            this.context = context
        }

        //sobreescribimos la funcion para tomar la vista devolver la nposicion, hacemos una convercion de una vista y tomamos como padre el grupo
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {

            val view: View?
            val vh: ViewHolder

            if (convertView == null) {
                view = layoutInflater.inflate(R.layout.activity_gernote_activity, parent, false)
                vh = ViewHolder(view)
                view.tag = vh
                Log.i("JSA", "set Tag for ViewHolder, position: " + position)
            } else {
                view = convertView
                vh = view.tag as ViewHolder
            }

            var mNote = notesList[position]

            vh.tvTitle.text = mNote.title

            vh.tvContent.text = mNote.content

            vh.ivEdit.setOnClickListener {
                updateNote(mNote)
            }

            vh.ivDelete.setOnClickListener {
                var dbManager = GernoteDbManager(this.context!!)
                val selectionArgs = arrayOf(mNote.id.toString())
                dbManager.delete("Id=?", selectionArgs)

                loadQueryAll()
            }

            vh.ivCamera.setOnClickListener {

            }

            return view
        }


        override fun getItem(position: Int): Any {
            return notesList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return notesList.size
        }
    }


    private fun updateNote(note: GernoteActivity) {
        var intent = Intent(this, Add_gernote::class.java)
        intent.putExtra("MainActId", note.id)
        intent.putExtra("MainActTitle", note.title)
        intent.putExtra("MainActContent", note.content)
        startActivity(intent)
    }

    private class ViewHolder(view: View?) {
        val tvTitle: TextView
        val tvContent: TextView
        val ivEdit: ImageView
        val ivDelete: ImageView
        val ivCamera: ImageView

        init {
            this.tvTitle = view?.findViewById(R.id.tvTitle) as TextView
            this.tvContent = view?.findViewById(R.id.tvContent) as TextView
            this.ivEdit = view?.findViewById(R.id.ivEdit) as ImageView
            this.ivDelete = view?.findViewById(R.id.ivDelete) as ImageView
            this.ivCamera = view?.findViewById(R.id.ivCamera) as ImageView
        }
    }

}
