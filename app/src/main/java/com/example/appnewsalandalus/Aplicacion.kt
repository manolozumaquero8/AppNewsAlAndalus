package com.example.appnewsalandalus

import android.app.Application
import android.content.Context
import com.example.appnewsalandalus.providers.db.MyDatabase

class Aplicacion: Application() {
    companion object{
        const val VERSION = 1
        const val DB = "AppNewsAlAndalues"
        const val TABLA_CONTACTOS = "contactos"
        lateinit var appContext: Context
        lateinit var llave: MyDatabase
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        llave = MyDatabase()
    }
}









