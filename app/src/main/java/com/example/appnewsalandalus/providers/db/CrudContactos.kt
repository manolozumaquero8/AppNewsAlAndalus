package com.example.appnewsalandalus.providers.db

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.example.appnewsalandalus.Aplicacion
import com.example.appnewsalandalus.models.ContactoModel


class CrudContactos {
    fun create(c: ContactoModel, userId: String): Long{
        val con= Aplicacion.llave.writableDatabase
        return try{
            con.insertWithOnConflict(
                Aplicacion.TABLA_CONTACTOS,
                null,
                c.toContentValues(userId),
                SQLiteDatabase.CONFLICT_IGNORE
            )
        }catch(ex: Exception){
            ex.printStackTrace()
            -1L
        }finally {
            con.close()
        }
    }

    fun read(userId: String): MutableList<ContactoModel> {
        val lista = mutableListOf<ContactoModel>()
        val con = Aplicacion.llave.readableDatabase
        try {
            val cursor = con.query(
                Aplicacion.TABLA_CONTACTOS,
                arrayOf("id", "nombre", "apellidos", "telefono", "userid"),
                "userid=?",
                arrayOf(userId),
                null,
                null,
                null
            )
            while (cursor.moveToNext()) {
                val contacto = ContactoModel(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4)
                )
                lista.add(contacto)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        } finally {
            con.close()
        }
        return lista
    }


    fun borrar(id: Int, userId: String): Boolean {
        val con = Aplicacion.llave.writableDatabase
        val contactoBorrado = con.delete(
            Aplicacion.TABLA_CONTACTOS,
            "id=? AND userid=?",
            arrayOf(id.toString(), userId)
        )
        con.close()
        return contactoBorrado > 0
    }


    fun update(c: ContactoModel): Boolean {
        val con = Aplicacion.llave.writableDatabase
        val values = c.toContentValues(c.userid)
        var filasAfectadas = 0
        val q = "SELECT id FROM ${Aplicacion.TABLA_CONTACTOS} WHERE telefono=? AND id<>? AND userid=?"
        val cursor = con.rawQuery(q, arrayOf(c.telefono, c.id.toString(), c.userid))
        val existeTelefono = cursor.moveToFirst()
        cursor.close()
        if (!existeTelefono) {
            filasAfectadas = con.update(
                Aplicacion.TABLA_CONTACTOS,
                values,
                "id=? AND userid=?",
                arrayOf(c.id.toString(), c.userid)
            )
        }
        con.close()
        return filasAfectadas > 0
    }



    private fun ContactoModel.toContentValues(userId: String): ContentValues {
        return ContentValues().apply {
            put("nombre", nombre)
            put("apellidos", apellidos)
            put("telefono", telefono)
            put("userid", userId)
        }
    }

}