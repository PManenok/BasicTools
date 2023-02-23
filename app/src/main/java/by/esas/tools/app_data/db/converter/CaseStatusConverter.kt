package by.esas.tools.app_data.db.converter

import androidx.room.TypeConverter
import by.esas.tools.entity.TestStatusEnum

class CaseStatusConverter {

    @TypeConverter
    fun toTestStatusEnum(value: String) = enumValueOf<TestStatusEnum>(value)

    @TypeConverter
    fun fromTestStatusEnum(value: TestStatusEnum) = value.name
}