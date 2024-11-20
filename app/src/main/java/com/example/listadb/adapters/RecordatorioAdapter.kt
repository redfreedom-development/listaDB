import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.listadb.R
import com.example.listadb.activities.MainActivity
import com.example.listadb.data.DAO.RecordatorioDAO
import com.example.listadb.data.Recordatorio

class RecordatorioAdapter(
    private var recordatorios: List<Recordatorio>,
    private val onItemClick: (Recordatorio) -> Unit,
    private val onDeleteClick: (Recordatorio) -> Unit
) : RecyclerView.Adapter<RecordatorioAdapter.RecordatorioViewHolder>() {

    // Clase interna para definir el ViewHolder
    class RecordatorioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pelicula: TextView = itemView.findViewById(R.id.nombrePelicula)
        val visto: CheckBox = itemView.findViewById(R.id.chkVisto)
        val botonBorrar: ImageButton = itemView.findViewById(R.id.botonBorrar)

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


        // Manejar clics en el ítem
        holder.itemView.setOnClickListener { onItemClick(recordatorio) }

        // Establecer el clic para el botón de eliminar
        holder.botonBorrar.setOnClickListener {
            onDeleteClick(recordatorio)

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