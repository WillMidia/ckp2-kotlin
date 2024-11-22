package will.com.github.gs2.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import will.com.github.gs2.model.ItemModel

@Dao
interface ItemDao {
    @Query("SELECT * FROM ItemModel")
    fun getAll(): LiveData<List<ItemModel>>

    @Query("SELECT * FROM ItemModel WHERE title LIKE :searchQuery OR description LIKE :searchQuery")
    fun searchItems(searchQuery: String): LiveData<List<ItemModel>>

    @Insert
    fun insert(item: ItemModel)

    @Delete
    fun delete(item: ItemModel)
}