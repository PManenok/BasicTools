package by.esas.tools

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ObservableField
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    val serviceName = ObservableField<String>("")

    fun testError() {
        val uc = GetDefaultCardUseCase(AppErrorMapper(Moshi.Builder().build(), LoggerImpl()), Dispatchers.Main)
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
