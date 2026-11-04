package za.co.unimarket.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import za.co.unimarket.data.MockData

@Database(
    entities = [
        UserEntity::class,
        CategoryEntity::class,
        ProductEntity::class,
        WishlistEntity::class,
        MessageEntity::class,
        NotificationEntity::class,
        SavedSearchEntity::class,
        SyncActionEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun categoryDao(): CategoryDao
    abstract fun productDao(): ProductDao
    abstract fun messageDao(): MessageDao
    abstract fun notificationDao(): NotificationDao
    abstract fun savedSearchDao(): SavedSearchDao
    abstract fun syncActionDao(): SyncActionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "unimarket_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateInitialData(database)
                    }
                }
            }
        }

        private suspend fun populateInitialData(db: AppDatabase) {
            db.categoryDao().insertCategories(
                MockData.categories.map { CategoryEntity.fromDomain(it) }
            )
            db.productDao().insertProducts(
                MockData.products.map { ProductEntity.fromDomain(it) }
            )
            db.messageDao().insertMessages(
                MockData.messages.map { MessageEntity.fromDomain(it) }
            )
            db.notificationDao().insertNotifications(
                MockData.notifications.map { NotificationEntity.fromDomain(it) }
            )
        }
    }
}
