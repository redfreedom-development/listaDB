package com.example.listadb.data.DAO

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.listadb.data.Recordatorio
import com.example.listadb.utils.DataBaseManager


class RecordatorioDAO(val context: Context) {

    private lateinit var db: SQLiteDatabase

    private fun open() {
        db = DataBaseManager(context).writableDatabase
    }

    private fun close() {
        db.close()
    }

    fun insert(recordatorio: Recordatorio) {
        open()

        // Create a new map of values, where column names are the keys
        val values = ContentValues().apply {
            put(Recordatorio.COLUMN_NAME, recordatorio.nombre)
            put(Recordatorio.COLUMN_VISTA, recordatorio.vista)
        }

        try {
            // Insert the new row, returning the primary key value of the new row
            val id = db.insert(Recordatorio.TABLE_NAME, null, values)
        } catch (e: Exception) {
            Log.e("DB", e.stackTraceToString())
        } finally {
            close()
        }
    }

    fun update(recordatorio: Recordatorio) {
        open()

        // Create a new map of values, where column names are the keys
        val values = ContentValues().apply {
            //put(Recordatorio.COLUMN_NAME, recordatorio.nombre)
            put(Recordatorio.COLUMN_VISTA, recordatorio.vista)
        }
            println("el check es: ${recordatorio.vista}")
        try {
            // Update the existing rows, returning the number of affected rows
            val updatedRows = db.update(Recordatorio.TABLE_NAME, values, "${Recordatorio.COLUMN_ID} = ${recordatorio.id}", null)
        } catch (e: Exception) {
            Log.e("DB", e.stackTraceToString())
        } finally {
            close()
        }
    }

    fun deleteByName(recordatorio: Recordatorio): Int {

        var deletedRows=0
        open()


        try {
            // Delete the existing row, returning the number of affected rows


            deletedRows = db.delete(Recordatorio.TABLE_NAME, "${Recordatorio.COLUMN_NAME} = ?", arrayOf(recordatorio.nombre) )// Aquí pasas el valor del nombre como argumento)


        } catch (e: Exception) {
            Log.e("DB", e.stackTraceToString())
        } finally {
            close()


        }
        return deletedRows

    }
    fun deleteById(recordatorio: Recordatorio): Int {

        var deletedRows=0
        open()


        try {
            // Delete the existing row, returning the number of affected rows
            deletedRows = db.delete(
                Recordatorio.TABLE_NAME,
                "${Recordatorio.COLUMN_ID} = ?",
                arrayOf(recordatorio.id.toString()) // Asegúrate de convertir los valores a String
            )



        } catch (e: Exception) {
            Log.e("DB", e.stackTraceToString())
        } finally {
            close()


        }
        return deletedRows

    }


    fun findAll() : MutableList<Recordatorio> {
        open()

        val list: MutableList<Recordatorio> = mutableListOf()

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        val projection = arrayOf(Recordatorio.COLUMN_ID, Recordatorio.COLUMN_NAME, Recordatorio.COLUMN_VISTA)

        try {
            val cursor = db.query(
                Recordatorio.TABLE_NAME,                    // The table to query
                projection,                         // The array of columns to return (pass null to get all)
                null,                       // The columns for the WHERE clause
                null,                   // The values for the WHERE clause
                null,                       // don't group the rows
                null,                         // don't filter by row groups
                null                         // The sort order
            )

            while (cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(Recordatorio.COLUMN_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(Recordatorio.COLUMN_NAME))
                val visto = cursor.getInt(cursor.getColumnIndexOrThrow(Recordatorio.COLUMN_VISTA)) != 0

                val recordatorio = Recordatorio(id, name, visto)
                list.add(recordatorio)
            }
        } catch (e: Exception) {
            Log.e("DB", e.stackTraceToString())
        } finally {
            close()
        }
        return list
    }

}