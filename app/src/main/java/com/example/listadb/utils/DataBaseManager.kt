package com.example.listadb.utils

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.listadb.data.Recordatorio

class DataBaseManager(context: Context) : SQLiteOpenHelper( context, DATABASE_NAME, null, DATABASE_VERSION
) {
    companion object {
        const val DATABASE_NAME = "mi_base_datos.db"
        const val DATABASE_VERSION = 1



        private const val CREATE_TABLE = """
            CREATE TABLE ${Recordatorio.TABLE_NAME} (
                ${Recordatorio.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${Recordatorio.COLUMN_NAME} TEXT,
                ${Recordatorio.COLUMN_VISTA} INTEGER DEFAULT 0
            )
        """
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS ${Recordatorio.TABLE_NAME}")
        onCreate(db)
    }




}