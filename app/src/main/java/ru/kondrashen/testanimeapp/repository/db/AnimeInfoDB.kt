package ru.kondrashen.testanimeapp.repository.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.kondrashen.testanimeapp.presentation.base.DateConverter
import ru.kondrashen.testanimeapp.repository.dao.AnimeDAO
import ru.kondrashen.testanimeapp.repository.dao.AnimeToPageDAO
import ru.kondrashen.testanimeapp.repository.dao.AnimeVirtualTableDAO
import ru.kondrashen.testanimeapp.repository.dao.FavoriteDAO
import ru.kondrashen.testanimeapp.repository.dao.GenresDAO
import ru.kondrashen.testanimeapp.repository.dao.OperationTypeDAO
import ru.kondrashen.testanimeapp.repository.dao.StudiosDAO
import ru.kondrashen.testanimeapp.domain.data_class.room_table.Anime
import ru.kondrashen.testanimeapp.domain.data_class.room_table.Favorite
import ru.kondrashen.testanimeapp.domain.data_class.relation.GenreToAnim
import ru.kondrashen.testanimeapp.domain.data_class.relation.StudioToAnim
import ru.kondrashen.testanimeapp.domain.data_class.room_table.AnimeFTS
import ru.kondrashen.testanimeapp.domain.data_class.room_table.AnimeToPage
import ru.kondrashen.testanimeapp.domain.data_class.room_table.Genres
import ru.kondrashen.testanimeapp.domain.data_class.room_table.Studios
import ru.kondrashen.testanimeapp.domain.data_class.room_table.ReleaseInfo
import ru.kondrashen.testanimeapp.domain.data_class.room_table.UserOperationType

@Database(entities = [Anime::class, Favorite::class, AnimeToPage::class,
    ReleaseInfo::class, Genres::class, GenreToAnim::class, Studios::class,
    StudioToAnim::class, AnimeFTS::class, UserOperationType::class], version = 2)

@TypeConverters(DateConverter::class)

abstract class AnimeInfoDB: RoomDatabase() {
    abstract fun animeDao(): AnimeDAO
    abstract fun animeToPageDao(): AnimeToPageDAO
    abstract fun genreDao(): GenresDAO
    abstract fun studiosDao(): StudiosDAO
    abstract fun favoriteDao(): FavoriteDAO
    abstract fun animeVirtualDao(): AnimeVirtualTableDAO
    abstract fun operationTypeDao(): OperationTypeDAO


    companion object{
        @Volatile
        private var INSTANCE:  AnimeInfoDB? = null
        fun getDatabase(context: Context): AnimeInfoDB{
            val tempInstance = INSTANCE
            if (tempInstance != null)
                return tempInstance
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AnimeInfoDB::class.java,
                    "anime_info_database.db")
//                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                return instance
            }
        }

        // Виртуальная таблица для продвинутого поиска
//        private val MIGRATION_1_2 = object : Migration(1, 2) {
//            override fun migrate(db: SupportSQLiteDatabase) {
//                db.execSQL(
//                    """
//                    CREATE VIRTUAL TABLE anime_fts
//                    USING fts4(title, description)
//                    """.trimIndent()
//                )
//            }
//        }
    }
}