package will.com.github.gs2.viewmodel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import carreiras.com.github.kotlin_android_lista_de_compras.R
import will.com.github.gs2.model.ItemModel

class ItemsAdapter(
    private val onItemClicked: (ItemModel) -> Unit,
    private val onItemRemoved: (ItemModel) -> Unit
) : RecyclerView.Adapter<ItemsAdapter.ItemViewHolder>() {

    private var items = listOf<ItemModel>()
    private var filteredItems = listOf<ItemModel>()

    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleView = view.findViewById<TextView>(R.id.textViewTitle)
        val descriptionView = view.findViewById<TextView>(R.id.textViewDescription)
        val button = view.findViewById<ImageButton>(R.id.imageButton)

        fun bind(item: ItemModel) {
            titleView.text = item.title
            descriptionView.text = item.description

            itemView.setOnClickListener {
                onItemClicked(item)
            }

            button.setOnClickListener {
                onItemRemoved(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.eco_tip_item_layout, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int = filteredItems.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(filteredItems[position])
    }

    fun updateItems(newItems: List<ItemModel>) {
        items = newItems
        filteredItems = newItems
        notifyDataSetChanged()
    }

    fun filter(query: String) {
        filteredItems = if (query.isEmpty()) {
            items
        } else {
            items.filter {
                it.title.contains(query, ignoreCase = true) ||
                        it.description.contains(query, ignoreCase = true)
            }
        }
        notifyDataSetChanged()
    }
}
