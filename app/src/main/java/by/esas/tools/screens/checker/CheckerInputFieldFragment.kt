package by.esas.tools.screens.checker

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import by.esas.tools.R
import by.esas.tools.base.AppFragment
import by.esas.tools.checker.Checker
import by.esas.tools.checker.Checking
import by.esas.tools.checker.checks.BiggerCheck
import by.esas.tools.checker.checks.CustomCheck
import by.esas.tools.checker.checks.DateRangeCheck
import by.esas.tools.checker.checks.LengthCheck
import by.esas.tools.checker.checks.MinLengthCheck
import by.esas.tools.checker.checks.RangeCheck
import by.esas.tools.checker.checks.RegexCheck
import by.esas.tools.databinding.FMainCheckerBinding
import by.esas.tools.utils.checking.AppChecker
import by.esas.tools.utils.checking.FieldChecking
import java.text.SimpleDateFormat
import java.util.*

class CheckerInputFieldFragment : AppFragment<CheckerInputFieldVM, FMainCheckerBinding>() {

    override val fragmentDestinationId = R.id.checkerFragment

    override fun provideLayoutId() = R.layout.f_main_checker

    override fun provideViewModel(): CheckerInputFieldVM {
        return ViewModelProvider(
            this,
            viewModelFactory.provideFactory()
        ).get(CheckerInputFieldVM::class.java)
    }

    override fun provideChecks(): List<Checking> {
        return listOf(
            FieldChecking(binding.fCheckerInputNotEmpty, true),
            FieldChecking(binding.fCheckerInputRange)
                .addCheck(RangeCheck(1.0, 999.0, resources.getString(R.string.checker_error_range))),
            FieldChecking(binding.fCheckerInputRegex)
                .addCheck(
                    RegexCheck(
                        "^[a-zA-Zа-яА-ЯёЁ,._\\-+=?!]*\$",
                        resources.getString(R.string.checker_error_regex)
                    )
                ),
            FieldChecking(binding.fCheckerInputLength)
                .addCheck(LengthCheck(1, 20, resources.getString(R.string.checker_error_length))),
            FieldChecking(binding.fCheckerInputBigger)
                .addCheck(BiggerCheck(10, resources.getString(R.string.checker_error_bigger))),
            FieldChecking(binding.fCheckerInputMinLength)
                .addCheck(MinLengthCheck(5, resources.getString(R.string.checker_error_min_length))),
            FieldChecking(binding.fCheckerInputCustom)
                .addCheck(
                    CustomCheck(
                        { binding.fCheckerInputCustom.getText() == binding.fCheckerInputMinLength.getText() },
                        resources.getString(R.string.checker_error_custom)
                    )
                ),
            FieldChecking(binding.fCheckerInputDate).addCheck(
                DateRangeCheck(
                    getCheckerDateRange().first,
                    getCheckerDateRange().second,
                    errorMessage = resources.getString(R.string.checker_error_date),
                    pattern = binding.fCheckerPatternSpinner.selectedItem.toString()
                )
            )
        )
    }

    override fun provideChecker(): Checker {
        return AppChecker().setMode(false).setListener(object : Checker.CheckListener {
            override fun onFailed() {
                viewModel.enableControls()
            }

            override fun onSuccess() {
                viewModel.enableControls()
                Toast.makeText(context, resources.getString(R.string.checker_everything_correct), Toast.LENGTH_LONG)
                    .show()
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fCheckerInputDate.setText(
            getFormattedDate(
                getCalendarDate(
                    binding.fCheckerCalendar.year,
                    binding.fCheckerCalendar.month,
                    binding.fCheckerCalendar.dayOfMonth
                )
            )
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.fCheckerCalendar.setOnDateChangedListener { _, year, monthOfYear, dayOfMonth ->
                val date = getCalendarDate(year, monthOfYear, dayOfMonth)
                binding.fCheckerInputDate.setText(getFormattedDate(date))
            }
        }

        binding.fCheckerPatternSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val date = getCalendarDate(
                        binding.fCheckerCalendar.year,
                        binding.fCheckerCalendar.month,
                        binding.fCheckerCalendar.dayOfMonth
                    )
                    binding.fCheckerInputDate.setText(getFormattedDate(date))
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        binding.fCheckerCheckButton.setOnClickListener {
            provideChecker().setMode(!binding.fCheckerSwitcher.isChecked).validate(provideChecks())
        }
    }

    private fun getCalendarDate(year: Int, month: Int, day: Int): Date {
        return Date(year - 1900, month, day)
    }

    private fun getFormattedDate(date: Date): String {
        val formatter = SimpleDateFormat(
            binding.fCheckerPatternSpinner.selectedItem.toString(),
            Locale.getDefault()
        )
        formatter.timeZone = TimeZone.getDefault()

        return formatter.format(date)
    }

    private fun getCheckerDateRange(): Pair<Long, Long> {
        val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        formatter.timeZone = TimeZone.getDefault()

        val year = Calendar.getInstance().get(Calendar.YEAR)
        val currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1
        val lastDayOfMonth = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH)

        val dateStart = formatter.parse("1-$currentMonth-$year")
        val dateEnd = formatter.parse("$lastDayOfMonth-$currentMonth-$year")

        return Pair(dateStart.time, dateEnd.time)
    }
}
