package com.example.appnewsalandalus.providers.db

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.appnewsalandalus.Aplicacion

class MyDatabase(): SQLiteOpenHelper(Aplicacion.appContext, Aplicacion.DB, null, Aplicacion.VERSION) {
    private val q="create table ${Aplicacion.TABLA_CONTACTOS}(" +
            "id integer primary key autoincrement," +
            "nombre text not null," +
            "apellidos text not null," +
            "telefono text not null unique," +
            "userid text not null)"

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(q)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if(newVersion>oldVersion){
            val borrarTabla="drop table ${Aplicacion.TABLA_CONTACTOS};"
            db?.execSQL(borrarTabla)
            onCreate(db)
        }
    }
}


