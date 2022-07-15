package by.esas.tools

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.core.view.ViewCompat
import androidx.core.view.updatePadding
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
        handleStatusBar()
        setupNavigation()
    }

    override fun onStart() {
        super.onStart()
        viewModel.testError()
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
