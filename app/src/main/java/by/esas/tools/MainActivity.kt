package by.esas.tools

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import by.esas.tools.basedaggerui.simple.SimpleActivity
import by.esas.tools.checker.Checker
import by.esas.tools.checker.checks.LengthCheck
import by.esas.tools.checking.AppChecker
import by.esas.tools.checking.FieldChecking
import by.esas.tools.databinding.ActivityMainBinding
import by.esas.tools.error_mapper.AppErrorMapper
import by.esas.tools.error_mapper.AppErrorStatusEnum
import by.esas.tools.inputfieldview.InputFieldView
import by.esas.tools.logger.ErrorModel
import by.esas.tools.logger.ILogger
import by.esas.tools.logger.LoggerImpl
import by.esas.tools.usecase.GetDefaultCardUseCase
import by.esas.tools.util.LanguageSetter
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers

open class MainActivity : SimpleActivity<MainVM, ActivityMainBinding, AppErrorStatusEnum>() {
    override val TAG: String = "MainActivity"
    override var logger: ILogger<AppErrorStatusEnum> = LoggerImpl()
    override fun provideViewModel(): MainVM {
        return ViewModelProvider(this, viewModelFactory).get(MainVM::class.java)
    }

    override fun provideLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun provideVariableInd(): Int {
        return BR.viewModel
    }

    override fun provideLifecycleOwner(): LifecycleOwner {
        return this
    }

    override fun provideSetter(): LanguageSetter {
        return object : LanguageSetter {
            override fun getDefaultLanguage(): String {
                return "en"
            }

            override fun getLanguage(): String {
                return "en"
            }

            override fun setLanguage(lang: String) {

            }
        }
    }

    override fun getAppContext(): Context {
        return App.appContext
    }

    override fun setAppContext(context: Context) {
        App.appContext = context
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val field = findViewById<InputFieldView>(R.id.a_main_text2)
        field.setInputPrefix("1")
        /*val spinner = findViewById<SpinnerFieldView>(R.id.a_main_service_spinner)
        spinner.adapter.clear()
        (spinner.adapter as ArrayAdapter<String>).addAll(listOf("1", "2", "3"))
        spinner.adapter.notifyDataSetChanged()
        val currentText = "1"
        spinner.setText(currentText)*/
        viewModel.addToObservable(viewModel.mainText, Observer<String> {
            AppChecker()
                .setShowError(true)
                .setListener(object : Checker.CheckListener {})
                .validate(listOf(FieldChecking(field, true).addCheck(LengthCheck(0, 4))))
        })
    }


    fun testError() {
        val uc = GetDefaultCardUseCase(
            AppErrorMapper(Moshi.Builder().build(), LoggerImpl()),
            Dispatchers.Main
        )
        uc.execute {
            onComplete {

            }
            onError {
                it.statusEnum
            }
            onCancel {

            }
        }
        val model = ErrorModel(0, AppErrorStatusEnum.APP_UNPREDICTED_ERROR)
        model.statusEnum
    }
}
