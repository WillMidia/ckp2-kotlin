package will.com.github.gs2.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import will.com.github.gs2.data.ItemDao
import will.com.github.gs2.data.ItemDatabase
import will.com.github.gs2.model.ItemModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ItemsViewModel(application: Application) : AndroidViewModel(application) {
    private val itemDao: ItemDao
    val itemsLiveData: LiveData<List<ItemModel>>
    private val _searchQuery = MutableLiveData<String>()

    init {
        val database = Room.databaseBuilder(
            getApplication(),
            ItemDatabase::class.java,
            "eco_tips_database"
        ).build()

        itemDao = database.itemDao()
        itemsLiveData = itemDao.getAll()
    }

    fun addTip(title: String, description: String, url: String? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            val newItem = ItemModel(title = title, description = description, url = url)
            itemDao.insert(newItem)
        }
    }

    fun removeItem(item: ItemModel) {
        viewModelScope.launch(Dispatchers.IO) {
            itemDao.delete(item)
        }
    }

    fun searchItems(query: String) {
        _searchQuery.value = query
    }
}