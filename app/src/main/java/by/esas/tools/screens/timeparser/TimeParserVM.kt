package by.esas.tools.screens.timeparser

import androidx.databinding.ObservableField
import by.esas.tools.app_domain.error_mapper.AppErrorStatusEnum
import by.esas.tools.base.AppVM
import by.esas.tools.timeparser.TimeParser
import by.esas.tools.timeparser.TimeParser.getDateFromLocal
import by.esas.tools.timeparser.TimeParser.getDateFromUTC
import by.esas.tools.timeparser.TimeParser.getLocal
import by.esas.tools.timeparser.TimeParser.getUTC
import by.esas.tools.utils.logger.ErrorModel
import java.lang.IllegalArgumentException
import java.util.Date
import javax.inject.Inject

class TimeParserVM @Inject constructor() : AppVM() {
    var selectedDate: Date? = null

    val resultField1 = ObservableField("")
    val resultField2 = ObservableField("")
    val resultField3 = ObservableField("")
    val resultField4 = ObservableField("")
    val resultField5 = ObservableField("")

    val patternField2 = ObservableField("")
    val patternField3 = ObservableField("")
    val patternField4 = ObservableField("")
    val patternField5 = ObservableField("")

    val dateField4 = ObservableField("")
    val dateField5 = ObservableField("")

    fun onGetUTCDateDefaultClick() {
        resultField1.set(TimeParser.getUTCDate(selectedDate).toString())
    }

    fun onGetUTCDefaultClick() {
        resultField2.set(selectedDate?.getUTC())
    }

    fun onGetLocaleDefaultClick() {
        resultField3.set(selectedDate?.getLocal())
    }

    fun onGetUTCPatternClick() {
        if (!patternField2.get().isNullOrEmpty())
            try {
                resultField2.set(selectedDate?.getUTC(outPattern = patternField2.get()!!))
            } catch (e: IllegalArgumentException) {
                handleError(ErrorModel(0, AppErrorStatusEnum.APP_ILLEGAL_PATTERN_CHARACTER))
            } catch (e: Exception) {
                handleError(e)
            }
    }

    fun onGetLocalePatternClick() {
        if (!patternField3.get().isNullOrEmpty())
            try {
                resultField3.set(selectedDate?.getLocal(outPattern = patternField3.get()!!))
            } catch (e: IllegalArgumentException) {
                handleError(ErrorModel(0, AppErrorStatusEnum.APP_ILLEGAL_PATTERN_CHARACTER))
            } catch (e: Exception) {
                handleError(e)
            }
    }

    fun onGetDateFromLocalPatternClick() {
        if (!patternField4.get().isNullOrEmpty() && !dateField4.get().isNullOrEmpty())
            try {
                resultField4.set(
                    getDateFromLocal(
                        dateField4.get(),
                        localPattern = patternField4.get()!!
                    ).toString()
                )
            } catch (e: IllegalArgumentException) {
                handleError(ErrorModel(0, AppErrorStatusEnum.APP_DATE_IN_UNEXPECTED_FORMAT))
            }
    }

    fun onGetDateFromUTCPatternClick() {
        if (!patternField5.get().isNullOrEmpty() && !dateField5.get().isNullOrEmpty())
            try {
                resultField5.set(getDateFromUTC(dateField5.get(), patternField5.get()!!).toString())
            } catch (e: Exception) {
                handleError(ErrorModel(0, AppErrorStatusEnum.APP_DATE_IN_UNEXPECTED_FORMAT))
            }
    }
}