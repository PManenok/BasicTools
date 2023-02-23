package by.esas.tools.app_data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import by.esas.tools.entity.CaseStatus
import by.esas.tools.entity.TestStatusEnum

@Dao
interface CaseStatusDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(caseStatus: CaseStatus)

    @Query("UPDATE caseStatusTable SET case_status=:status WHERE case_id = :id")
    fun update(id: Int, status: TestStatusEnum)

    @Query("SELECT * FROM caseStatusTable")
    fun getAll(): List<CaseStatus>

    @Query("SELECT * FROM caseStatusTable WHERE case_id = :id")
    fun getById(id: Int): CaseStatus

    @Query("DELETE FROM caseStatusTable")
    fun deleteAll()
}
