package by.esas.tools.screens.checker

import android.os.Build
import android.os.Bundle
import android.view.View
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
import by.esas.tools.checker.checks.NotEmptyCheck
import by.esas.tools.checker.checks.RangeCheck
import by.esas.tools.checker.checks.RegexCheck
import by.esas.tools.databinding.FMainCheckerBinding
import by.esas.tools.utils.checking.AppChecker
import by.esas.tools.utils.checking.FieldChecking

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
                .addCheck(RangeCheck(1.0, 999.0, "Range error")),
            FieldChecking(binding.fCheckerInputRegex)
                .addCheck(RegexCheck("^[a-zA-Zа-яА-ЯёЁ,._\\-+=?!]*\$", "Regex error")),
            FieldChecking(binding.fCheckerInputLength)
                .addCheck(LengthCheck(1, 20, "Length error")),
            FieldChecking(binding.fCheckerInputBigger)
                .addCheck(BiggerCheck(10, "Bigger error")),
            FieldChecking(binding.fCheckerInputMinLength)
                .addCheck(MinLengthCheck(5, "Min length error")),
            FieldChecking(binding.fCheckerInputCustom)
                .addCheck(
                    CustomCheck(
                        { binding.fCheckerInputCustom.getText() == binding.fCheckerInputMinLength.getText() },
                        "Custom error"
                    )
                ),
            FieldChecking(binding.fCheckerInputDate).addCheck(
                DateRangeCheck(1640995200000, 1672520399000, errorMessage = "Date error")
            )
        )
    }

    override fun provideChecker(): Checker {
        return AppChecker().setMode(false).setListener(object : Checker.CheckListener {
            override fun onFailed() {
                enableControls()
            }

            override fun onSuccess() {
                enableControls()
                Toast.makeText(context, "Everything okay", Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.fCheckerInputDate.setText("${binding.fCheckerCalendar.year}-${binding.fCheckerCalendar.month + 1}-${binding.fCheckerCalendar.dayOfMonth}")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.fCheckerCalendar.setOnDateChangedListener { _, year, monthOfYear, dayOfMonth ->
                binding.fCheckerInputDate.setText("$year-${monthOfYear + 1}-$dayOfMonth")
            }
        }

        binding.fCheckerCheckButton.setOnClickListener {
            if (binding.fCheckerSwitcher.isChecked)
                provideChecker().setMode(true).validate(provideChecks())
            else
                provideChecker().setMode(false).validate(provideChecks())
        }
    }
}
