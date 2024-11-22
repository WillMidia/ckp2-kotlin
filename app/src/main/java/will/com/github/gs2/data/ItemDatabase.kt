package will.com.github.gs2.data

import androidx.room.Database
import androidx.room.RoomDatabase
import will.com.github.gs2.data.ItemDao
import will.com.github.gs2.model.ItemModel

@Database(entities = [ItemModel::class], version = 1)
abstract class ItemDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao
}