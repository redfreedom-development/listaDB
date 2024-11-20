package com.example.listadb.data

data class Recordatorio(val id: Int, val nombre: String, val vista: Boolean){


    // Constructor secundario sin el 'id', útil cuando el 'id' es autoincremental
    constructor(nombre: String, vista: Boolean) : this(0, nombre, vista) {
        // El 'id' podría ser 0 por defecto, luego se asignará automáticamente al insertar en la base de datos.
    }
    companion object {
        const val TABLE_NAME = "peliculas"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_VISTA = "done"

    }

}
