package uk.ac.shef.oak.com4510.data.access.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import uk.ac.shef.oak.com4510.data.access.daos.LocationDao
import uk.ac.shef.oak.com4510.data.access.daos.PhotoDao
import uk.ac.shef.oak.com4510.data.access.daos.TagDao
import uk.ac.shef.oak.com4510.data.access.daos.TripDao
import uk.ac.shef.oak.com4510.data.access.entities.LocationEntity
import uk.ac.shef.oak.com4510.data.access.entities.PhotoEntity
import uk.ac.shef.oak.com4510.data.access.entities.TagEntity
import uk.ac.shef.oak.com4510.data.access.entities.TripEntity

/**
 * Class AppDatabase.
 *
 * Defines Room database implementation for the application.
 */
@Database(
    entities = [LocationEntity::class, PhotoEntity::class, TagEntity::class, TripEntity::class],
    version = 1,
    exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun photoDao(): PhotoDao
    abstract fun tripDao(): TripDao
    abstract fun locationDao(): LocationDao
    abstract fun tagDao(): TagDao

    companion object{
        private val DATABASE_NAME = "TripPlannerDatabase"

        @Volatile
        private var database_instance: AppDatabase? = null

        /**
         * Gets instance of a database. If not initialised yet, initialises.
         */
        fun getInstance(context: Context): AppDatabase {
            return database_instance ?: synchronized(this){
                database_instance ?: buildDatabase(context).also{
                    database_instance = it
                }
            }
        }

        /**
         * Builds the database.
         */
        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}