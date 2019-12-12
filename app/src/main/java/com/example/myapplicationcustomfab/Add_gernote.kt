package com.example.myapplicationcustomfab

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_gernote.*

class Add_gernote : AppCompatActivity() {

    var id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_gernote)

        try {
            var bundle: Bundle = intent.extras
            id = bundle.getInt("MainActId", 0)
            if (id != 0) {
                Edit_text_titulo.setText(bundle.getString("Title"))
                edittext_descripcion.setText(bundle.getString("Content"))
            }
        } catch (ex: Exception) {
        }

        button_agregar.setOnClickListener {
            //Creamos una variable en la cual ntendremos que igualar con el gestor GernoteDbManager y le enviamos que tome el contexto de él
            var dbManager = GernoteDbManager(this)

            var values = ContentValues()
            values.put("Title", Edit_text_titulo.text.toString())
            values.put("Content", edittext_descripcion.text.toString())

            //En este caso lo mutilizamos para verificar al momento de ingresarse y si ocurre un error lo desplegara en forma de mensaje con un error
            //e insertara los valores en el caso de que no halla ningun error
            if (id == 0) {
                val mID = dbManager.insert(values)

                if (mID > 0) {
                    Toast.makeText(this, "Nota Agregada Satisfactoriamente", Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    Toast.makeText(this, "Error al agregar la Nota", Toast.LENGTH_LONG).show()
                }
            } else {
                var selectionArs = arrayOf(id.toString())
                val mID = dbManager.update(values, "Id=?", selectionArs)

                if (mID > 0) {
                    Toast.makeText(this, "Nota Agregada Satisfactoriamente", Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    Toast.makeText(this, "Error al agregar la Nota", Toast.LENGTH_LONG).show()
                }
            }
        }

    }

    //funcion para que al momento en que la persona termina de agregar su nota y hace click en agregar entonces lo envie de nuevo a
    //la pantalla principal con la nota recien ingresada
    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
