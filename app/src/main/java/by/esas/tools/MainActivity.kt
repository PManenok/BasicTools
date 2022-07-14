package by.esas.tools

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.ViewModelProvider
import by.esas.tools.databinding.ActivityMainBinding
import by.esas.tools.dialog.GetPasswordDialog
import by.esas.tools.dialog.MessageDialog
import by.esas.tools.util.TAGk
import by.esas.tools.util.hideSystemUIR
import dagger.android.AndroidInjection
import androidx.navigation.NavController
import androidx.navigation.Navigation
import by.esas.tools.simple.AppActivity

/**
 * To use HasAndroidInjector with Activity do not forget to add
 *     AndroidInjection.inject(this)
 * in the fun onCreate(savedInstanceState: Bundle?)
 *
 * To use HasAndroidInjector with Fragment do not forget to add
 *     override fun onAttach(context: Context) {
 *         AndroidSupportInjection.inject(this)
 *         super.onAttach(context)
 *     }
 */
class MainActivity : AppActivity<MainVM, ActivityMainBinding>(), FragmentResultListener {

    lateinit var navController: NavController
    private var topDestination = R.id.menuFragment

    override fun provideViewModel(): MainVM {
        return ViewModelProvider(this, viewModelFactory.provideFactory()).get(MainVM::class.java)
    }

    override fun provideLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
//        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
//            val statusBar = insets.getSystemWindowInsetTop()
//            val navigationBar = insets.getSystemWindowInsetBottom()
//            binding.root.updatePadding(top = statusBar)
//            return@setOnApplyWindowInsetsListener insets
//        }
        /*viewModel.update = {
            binding.fMainAddInvoiceAdditionalContainer.invalidate()
        }*/

//        binding.fMainSetErrorPin.setOnClickListener {
//            doCheck()
//            if (!binding.aMainText3.getText().equals("12345678")) {
//                binding.aMainText3.setError("Some error")
//            } else {
//                binding.aMainText3.setError("")
//            }
//        }

        setupNavigation()
    }

//    fun doCheck() {
//        AppChecker()
//            .setShowError(true)
//            .setListener(object : Checker.CheckListener {})
//            .validate(
//                listOf(
//                    FieldChecking(binding.aMainText4, true)
//                        .setRequestFocusHandler(object : IRequestFocusHandler {
//                            override fun handleRequestFocus() {
//                                binding.aMainText4.visibility = View.VISIBLE
//                                binding.aMainText4.requestFocus()
//                                //scroll to position
//                            }
//                        })
//                        .addCheck(LengthCheck(0, 4))
//                )
//            )
//    }
    /*val field = findViewById<InputFieldView>(R.id.a_main_text2)
    field.setInputPrefix("1")*/
    /*val spinner = findViewById<SpinnerFieldView>(R.id.a_main_service_spinner)
    spinner.adapter.clear()
    (spinner.adapter as ArrayAdapter<String>).addAll(listOf("1", "2", "3"))
    spinner.adapter.notifyDataSetChanged()
    val currentText = "1"
    spinner.setText(currentText)*/
    /*viewModel.addToObservable(viewModel.mainText, Observer<String> {
        AppChecker()
            .setShowError(true)
            .setListener(object : Checker.CheckListener {})
            .validate(listOf(FieldChecking(field, true).addCheck(LengthCheck(0, 4))))
    })*/


    //CHECK if should be in base class
    fun setFullScreen() {
        //WindowCompat.setDecorFitsSystemWindows(window, false)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
            window.insetsController?.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.apply {
                //clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                //addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    decorView.systemUiVisibility =
                        (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
                } else {
                    decorView.systemUiVisibility =
                        (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
                }
                statusBarColor = Color.TRANSPARENT
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.testError()
    }

    override fun hideSystemUI() {
        logger.logInfo("hideSystemUI")
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                hideSystemUIR(this)
            } else {
                hideSystemUIApp(this)
            }
        } catch (e: NullPointerException) {
            logger.logError(e)
        }
    }

    override fun provideSwitchableViews(): List<View?> {
        return emptyList()
    }

    override fun onFragmentResult(requestKey: String, result: Bundle) {
        logger.logInfo("onFragmentResult $requestKey, $result")
        viewModel.enableControls()
    }

    override fun provideRequestKeys(): List<String> {
        val set = mutableSetOf(MessageDialog::class.TAGk, GetPasswordDialog::class.TAGk)
        set.addAll(super.provideRequestKeys())
        return set.toList()
    }

    override fun provideFragmentResultListener(requestKey: String): FragmentResultListener? {
        //here we want to use super.provideFragmentResultListener for request keys from parent this behavior is optional
        return if (requestKey in super.provideRequestKeys()) {
            super.provideFragmentResultListener(requestKey)
        } else {
            this
        }
    }

    private fun setupNavigation(){
        navController = Navigation.findNavController(this, R.id.a_main_nav_host_fragment)
        navController.addOnDestinationChangedListener { _, destination, arguments ->
            //Set home icon
            viewModel.hasBackBtn.set(destination.id != topDestination)
        }
    }
}
