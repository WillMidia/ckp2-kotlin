package will.com.github.gs2

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import carreiras.com.github.kotlin_android_lista_de_compras.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import will.com.github.gs2.viewmodel.ItemsAdapter
import will.com.github.gs2.viewmodel.ItemsViewModel
import will.com.github.gs2.viewmodel.ItemsViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: ItemsViewModel
    private lateinit var searchView: androidx.appcompat.widget.SearchView
    private lateinit var adapter: ItemsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "EcoDicas"

        setupRecyclerView()
        setupSearchView()
        setupViewModel()
        addInitialTips()
        setupFab()
    }

    private fun setupFab() {
        val fab: FloatingActionButton = findViewById(R.id.fabAddTip)
        fab.setOnClickListener {
            showAddTipDialog()
        }
    }

    private fun showAddTipDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_tip, null)

        val titleEditText = dialogView.findViewById<TextInputEditText>(R.id.editTextTitle)
        val descriptionEditText = dialogView.findViewById<TextInputEditText>(R.id.editTextDescription)
        val urlEditText = dialogView.findViewById<TextInputEditText>(R.id.editTextUrl)

        MaterialAlertDialogBuilder(this)
            .setTitle("Adicionar Nova Dica")
            .setView(dialogView)
            .setPositiveButton("Adicionar") { _, _ ->
                val title = titleEditText.text.toString()
                val description = descriptionEditText.text.toString()
                val url = urlEditText.text.toString().takeIf { it.isNotBlank() }

                if (title.isBlank() || description.isBlank()) {
                    Toast.makeText(this, "Por favor, preencha título e descrição", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                viewModel.addTip(title, description, url)
                Toast.makeText(this, "Dica adicionada com sucesso!", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }


    private fun setupRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        adapter = ItemsAdapter(
            onItemClicked = { item ->
                item.url?.let { url ->
                    openUrl(url)
                } ?: showToast(item.description)
            },
            onItemRemoved = { item ->
                viewModel.removeItem(item)
            }
        )
        recyclerView.adapter = adapter
    }

    private fun setupSearchView() {
        searchView = findViewById(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter(newText ?: "")
                return true
            }
        })
    }

    private fun setupViewModel() {
        val viewModelFactory = ItemsViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ItemsViewModel::class.java)

        viewModel.itemsLiveData.observe(this) { items ->
            adapter.updateItems(items)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    private fun addInitialTips() {
        viewModel.addTip(
            "Use lâmpadas LED",
            "Substitua suas lâmpadas antigas por LED para economizar até 80% de energia",
        )
        viewModel.addTip(
            "Desligue aparelhos em standby",
            "Aparelhos em modo de espera podem consumir até 15% da sua energia"
        )
    }
}