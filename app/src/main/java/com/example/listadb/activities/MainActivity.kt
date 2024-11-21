package com.example.listadb.activities

import RecordatorioAdapter
import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.listadb.R
import com.example.listadb.data.DAO.RecordatorioDAO
import com.example.listadb.data.Recordatorio
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity() {

    val dao= RecordatorioDAO(this)
    private var recordatorios: MutableList<Recordatorio> = mutableListOf()
    private lateinit var navigatonBar: NavigationBarView
    private lateinit var recyclerView: RecyclerView
    private lateinit var checkBox: CheckBox



    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        navigatonBar= findViewById (R.id.navigationBar)





        //nada mas empezar la app buscamos todos los registros de la BBDD
       recordatorios = dao.findAll()



        // Configura el RecyclerView
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = RecordatorioAdapter(recordatorios,
            onItemClick = { recordatorio ->
                // Manejo de clics en el ítem
                println("Recordatorio seleccionado: ${recordatorio.nombre}")
            },
            onDeleteClick = { recordatorio ->
                // Manejo del clic en el botón de eliminar
                println("Eliminar recordatorio: ${recordatorio.id}")
                dao.deleteById(recordatorio)
                actualizar_datos_recyclerView()


                // Lógica para eliminar el recordatorio de la lista o base de datos
            },
            onUpdateCheckBox = {recordatorio->

                checkBox = findViewById(R.id.chkVisto)
                println("has pulsado el check: ${recordatorio.vista}")
                var mycheck = false
                mycheck= checkBox.isChecked

                if (mycheck)
                {
                    recordatorio.vista=true
                }else{
                    recordatorio.vista=false
                }
                dao.update(recordatorio)

            }
        )

        navigatonBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_add -> {
                   cuadro_dialogo_add("add")


                }
                R.id.menu_delete -> {
                    cuadro_dialogo_add("delete")
                }

            }

            return@setOnItemSelectedListener true
        }


        //######  FUNCIONALIDAD PARA ARRASTRAR Y BORRAR UN ELEMENTO########

        // Configura el ItemTouchHelper
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false // No necesitamos mover los elementos, solo deslizar
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // Obtén la posición del elemento deslizado
                val position = viewHolder.adapterPosition

                // Elimina el elemento de la lista y actualiza el RecyclerView
             //   recordatorios.removeAt(position) // Elimina el item de tu lista de datos
               // (recyclerView.adapter as Any).notifyItemRemoved(position) // Notifica al adaptador para eliminar el item

                // Si necesitas eliminar el item de la base de datos:
                val recordatorio = recordatorios[position]
                dao.deleteById(recordatorio) // Elimina de la base de datos
                actualizar_datos_recyclerView()
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                // Dibuja el fondo rojo cuando se desliza hacia la izquierda
                val background = ColorDrawable(Color.RED)
                val itemView = viewHolder.itemView

                if (dX < 0) { // Si se desliza hacia la izquierda
                    background.setBounds(
                        itemView.right + dX.toInt(),
                        itemView.top,
                        itemView.right,
                        itemView.bottom
                    )
                }

                background.draw(c) // Dibuja el fondo rojo

                // Agregar el ícono de eliminación
                if (dX < 0) { // Solo dibujamos el ícono cuando el usuario desliza hacia la izquierda
                    val icon = ContextCompat.getDrawable(viewHolder.itemView.context, R.drawable.ic_delete)!!

                    // Calculamos las posiciones para dibujar el ícono
                    icon.setBounds(
                        itemView.right - icon.intrinsicWidth - 16, // Ubicación del ícono a la izquierda del item
                        itemView.top + (itemView.height - icon.intrinsicHeight) / 2, // Centrado verticalmente
                        itemView.right - 16, // Espacio entre el ícono y el borde derecho
                        itemView.bottom - (itemView.height - icon.intrinsicHeight) / 2
                    )

                    icon.draw(c) // Dibuja el ícono de eliminación
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }

        // Asocia el ItemTouchHelper al RecyclerView
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }




        //##################### FIN DE LA FUNCIONALIDAD ##################





    private fun cuadro_dialogo_add(accion: String) {
        val editText = EditText(this)

        // Configurar el cuadro de diálogo
        val dialog = AlertDialog.Builder(this)

        if(accion=="add") {
            dialog.setTitle("¿Qué pelicula quieres recordar para ver?")

        }
        else{
            dialog.setTitle("¿Qué pelicula quieres borrar")

        }



            dialog.setView(editText) // Establecer el EditText como el contenido del diálogo
            dialog.setPositiveButton("Aceptar") { dialog, which ->
                // Obtener el texto ingresado
                val inputText = editText.text.toString()
                if (inputText.isNotBlank()) { // Verifica que no esté vacío
                    val peticionUsuario = Recordatorio(inputText, false)

                    if(accion=="add") {

                        dao.insert(peticionUsuario)
                    }
                    else{

                       var control= dao.deleteByName(peticionUsuario)

                        if(control==0){
                            val toast = Toast.makeText(this, "NO EXISTE LA PELICULA", Toast.LENGTH_SHORT)
                            toast.setGravity(Gravity.CENTER, 0, 1000) // Centrar el Toast en la pantalla
                            toast.show()


                        }
                    }
                    actualizar_datos_recyclerView()
                }



            }
            .setNegativeButton("Cancelar") { dialog, which ->
                dialog.dismiss() // Cerrar el cuadro de diálogo si se cancela
            }
            .create()

        // Mostrar el cuadro de diálogo
        dialog.show()
    }

    fun actualizar_datos_recyclerView(){

        // Actualizar la lista desde el DAO
        recordatorios.clear() // Limpia la lista actual
        recordatorios.addAll(dao.findAll()) // Supongamos que `getAll` devuelve la lista actualizada

        // Notificar al adaptador del cambio
        recyclerView.adapter?.notifyDataSetChanged()
    }


    }
