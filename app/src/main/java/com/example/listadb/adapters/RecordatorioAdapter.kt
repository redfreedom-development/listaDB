import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.listadb.R
import com.example.listadb.activities.MainActivity
import com.example.listadb.data.DAO.RecordatorioDAO
import com.example.listadb.data.Recordatorio

class RecordatorioAdapter(
    private var recordatorios: List<Recordatorio>,
    private val onItemClick: (Recordatorio) -> Unit,
    private val onDeleteClick: (Recordatorio) -> Unit,
    private val onUpdateCheckBox: (Recordatorio) -> Unit
) : RecyclerView.Adapter<RecordatorioAdapter.RecordatorioViewHolder>() {

    // Clase interna para definir el ViewHolder
    class RecordatorioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pelicula: TextView = itemView.findViewById(R.id.nombrePelicula)
        val visto: CheckBox = itemView.findViewById(R.id.chkVisto)
        val botonBorrar: ImageButton = itemView.findViewById(R.id.botonBorrar)
        val checkBox : CheckBox = itemView.findViewById(R.id.chkVisto)
        val itemLayout: LinearLayout = itemView.findViewById(R.id.itemLinearLayout)

    }

    // Inflar el diseño del ítem
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordatorioViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pelicula, parent, false)
        return RecordatorioViewHolder(view)
    }

    // Vincular los datos del recordatorio con el ViewHolder
    override fun onBindViewHolder(holder: RecordatorioViewHolder, position: Int) {


        val recordatorio = recordatorios[position]
        holder.pelicula.text = recordatorio.nombre
        holder.checkBox.isChecked=recordatorio.vista


        // Manejar clics en el ítem
        holder.itemView.setOnClickListener { onItemClick(recordatorio) }

        // Establecer el clic para el botón de eliminar
        holder.botonBorrar.setOnClickListener {
            onDeleteClick(recordatorio)

            println(recordatorio.id)

        }
        holder.checkBox.setOnClickListener{
            onUpdateCheckBox(recordatorio)

        }

        // Cambia el fondo según la posición (par o impar)
        if (position % 2 == 0) {
            // Ítem en posición par (0, 2, 4, ...)
            holder.itemLayout.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.white))
        } else {
            // Ítem en posición impar (1, 3, 5, ...)
            holder.itemLayout.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.celda_impar))
        }
    }

    // Devolver el tamaño de la lista
    override fun getItemCount(): Int = recordatorios.size

    // Este método sirve para actualizar los datos
    fun updateData (newDataSet: List<Recordatorio>) {
        recordatorios = newDataSet
        notifyDataSetChanged()
    }


}