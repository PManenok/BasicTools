package by.esas.tools

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import com.squareup.moshi.Moshi
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        serviceName.addOnPropertyChangedCallback(
            object : Observable.OnPropertyChangedCallback() {
                override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                    a_main_text.setInputLabel(serviceName.get() ?: "")
                }
            }
        )
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
