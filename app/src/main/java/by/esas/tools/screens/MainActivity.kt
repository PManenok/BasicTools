package by.esas.tools.screens

import android.os.Bundle
import android.view.MotionEvent
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import by.esas.tools.R
import by.esas.tools.base.AppActivity
import by.esas.tools.databinding.ActivityMainBinding

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
class MainActivity : AppActivity<MainVM, ActivityMainBinding>() {

    lateinit var navController: NavController
    private var topDestination = R.id.menuFragment

    override fun provideViewModel(): MainVM {
        return ViewModelProvider(this, viewModelFactory.provideFactory()).get(MainVM::class.java)
    }

    override fun provideLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupNavigation()
    }

    override fun handleTouchOutOfInputField(event: MotionEvent) {
        if (event.action == MotionEvent.ACTION_DOWN && navController.currentDestination?.id != R.id.utilKeyboardFragment) {
            super.handleTouchOutOfInputField(event)
        }
    }

    private fun setupNavigation() {
        navController = Navigation.findNavController(this, R.id.a_main_nav_host_fragment)
        navController.addOnDestinationChangedListener { _, destination, arguments ->
            //Set home icon
            viewModel.hasBackBtn.set(destination.id != topDestination)
        }
    }
}
