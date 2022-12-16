package by.esas.tools.screens

import android.os.Bundle
import android.view.MotionEvent
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import by.esas.tools.R
import by.esas.tools.base.AppActivity
import by.esas.tools.databinding.ActivityMainBinding
import by.esas.tools.topbarview.ITopbarHandler

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

        binding.aMainTopBar.setupHandler(object : ITopbarHandler {
            override fun onNavigationClick() {
                onBackPressed()
            }

            override fun onActionClick() {
                navController.navigate(R.id.baseuiThemeFragment)
            }
        })
    }

    override fun handleTouchOutOfInputField(event: MotionEvent) {
        if (event.action == MotionEvent.ACTION_DOWN && navController.currentDestination?.id != R.id.utilKeyboardFragment) {
            super.handleTouchOutOfInputField(event)
        }
    }

    private fun setupNavigation() {
        navController = Navigation.findNavController(this, R.id.a_main_nav_host_fragment)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            viewModel.hasBackBtn.set(destination.id != topDestination)
            viewModel.hasSettingsBtn.set(destination.id == topDestination)
            viewModel.title.set(destination.label.toString())
        }
    }
}
