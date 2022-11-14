package by.esas.tools.screens

import android.os.Bundle
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
        binding.aMainTopBar.setStartActionListener {
            viewModel.disableControls()
            onBackPressed()
            viewModel.enableControls()
        }
    }

    private fun setupNavigation() {
        navController = Navigation.findNavController(this, R.id.a_main_nav_host_fragment)
        navController.addOnDestinationChangedListener { _, destination, arguments ->
            viewModel.header.set(destination.label.toString())
            //Set home icon
            viewModel.hasBackBtn.set(destination.id != topDestination)
        }
    }
}
