package by.esas.tools.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "caseStatusTable")
data class CaseStatus(
    @PrimaryKey
    @ColumnInfo(name = "case_id")
    val caseId: Int,

    @ColumnInfo(name = "case_status")
    var status: TestStatusEnum
)