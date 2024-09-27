package by.esas.tools.screens.timeparser

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import by.esas.tools.R
import by.esas.tools.app_domain.error_mapper.AppErrorStatusEnum
import by.esas.tools.base.AppFragment
import by.esas.tools.checker.Checker
import by.esas.tools.databinding.FMainTimeparserBinding
import by.esas.tools.logger.handler.ErrorMessageHelper
import by.esas.tools.util.TAGk
import by.esas.tools.utils.checking.AppChecker
import by.esas.tools.utils.checking.FieldChecking
import by.esas.tools.utils.logger.ErrorModel
import java.util.*

class TimeParserFragment : AppFragment<TimeParserVM, FMainTimeparserBinding>() {

    override val fragmentDestinationId = R.id.timeparserFragment
    override fun provideLayoutId() = R.layout.f_main_timeparser

    override fun provideViewModel(): TimeParserVM {
        return ViewModelProvider(
            this,
            viewModelFactory.provideFactory()
        ).get(TimeParserVM::class.java)
    }

    override fun provideErrorStringHelper(): ErrorMessageHelper<ErrorModel> {
        return object : ErrorMessageHelper<ErrorModel> {

            override fun getErrorMessage(error: ErrorModel): String {
                return when (error.getStatusAsEnum()) {
                    AppErrorStatusEnum.APP_DATE_IN_UNEXPECTED_FORMAT -> resources.getString(R.string.timeparser_error_date_in_unexpected_format)
                    AppErrorStatusEnum.APP_ILLEGAL_PATTERN_CHARACTER -> resources.getString(R.string.timeparser_error_illegal_pattern_character)
                    else -> resources.getString(R.string.timeparser_error_unexpected)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setDateFromCalendar(
            binding.fTimeparserCalendar.year,
            binding.fTimeparserCalendar.month,
            binding.fTimeparserCalendar.dayOfMonth
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.fTimeparserCalendar.setOnDateChangedListener { _, year, monthOfYear, dayOfMonth ->
                setDateFromCalendar(year, monthOfYear, dayOfMonth)
            }
        }

        binding.fTimeparserPattern2Btn.setOnClickListener {
            if (binding.fTimeparserPattern2.getText().isNotEmpty())
                viewModel.onGetUTCPatternClick()
            else
                binding.fTimeparserPattern2.setError(R.string.timeparser_empty_field)
        }
        binding.fTimeparserPattern3Btn.setOnClickListener {
            if (binding.fTimeparserPattern3.getText().isNotEmpty())
                viewModel.onGetLocalePatternClick()
            else
                binding.fTimeparserPattern3.setError(R.string.timeparser_empty_field)
        }
        binding.fTimeparserPattern4Btn.setOnClickListener {
            val parserChecker = AppChecker().setListener(object : Checker.CheckListener {
                override fun onSuccess() {
                    viewModel.onGetDateFromLocalPatternClick()
                }
            })
            parserChecker.validate(
                listOf(
                    FieldChecking(binding.fTimeparserPattern4),
                    FieldChecking(binding.fTimeparserDate4)
                )
            )
        }
        binding.fTimeparserPattern5Btn.setOnClickListener {
            val parserChecker = AppChecker().setListener(object : Checker.CheckListener {
                override fun onSuccess() {
                    viewModel.onGetDateFromUTCPatternClick()
                }
            })
            parserChecker.validate(
                listOf(
                    FieldChecking(binding.fTimeparserPattern5),
                    FieldChecking(binding.fTimeparserDate5)
                )
            )
        }
    }

    private fun setDateFromCalendar(year: Int, month: Int, day: Int) {
        viewModel.selectedDate = Date(year - 1900, month, day)
    }
}
