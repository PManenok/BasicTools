package by.esas.tools.app_data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import by.esas.tools.app_data.db.converter.CaseStatusConverter
import by.esas.tools.app_data.db.dao.CaseStatusDao
import by.esas.tools.entity.CaseStatus

@Database(entities = [CaseStatus::class], version = 1)

@TypeConverters(CaseStatusConverter::class)
abstract class AppDataBase : RoomDatabase() {

    companion object {

        private const val DATABASE_NAME = "ABL_DATABASE"

        fun getInstance(context: Context): AppDataBase {
            return Room.databaseBuilder(
                context,
                AppDataBase::class.java,
                DATABASE_NAME
            ).build()
        }
    }

    abstract fun getCaseStatusDao(): CaseStatusDao
}