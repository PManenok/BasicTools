package by.esas.tools.screens

import android.os.Bundle
import android.view.MotionEvent
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import by.esas.tools.R
import by.esas.tools.base.AppActivity
import by.esas.tools.baseui.Config.ERROR_MESSAGE_DIALOG
import by.esas.tools.databinding.ActivityMainBinding
import by.esas.tools.logger.Action
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

    companion object {

        const val NEED_TO_UPDATE_MENU: String = "NEED_TO_UPDATE_MENU"
        const val NEED_TO_UPDATE_CURRENT_CASE: String = "NEED_TO_UPDATE_CURRENT_CASE"
        const val CURRENT_CASE_ID: String = "CURRENT_CASE_ID"
    }

    lateinit var navController: NavController
    private var topDestination = R.id.menuFragment

    override fun provideViewModel(): MainVM {
        return ViewModelProvider(this, viewModelFactory.provideFactory()).get(MainVM::class.java)
    }

    override fun provideLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun provideRequestKeys(): List<String> {
        return listOf(ERROR_MESSAGE_DIALOG, MainVM.CASE_STATUS_DIALOG, MainVM.CLEAR_CASES_TEST_DATA_DIALOG)
    }

    override fun provideFragmentResultListener(requestKey: String): FragmentResultListener? {
        return when (requestKey) {
            //REMEMBER we can register result listener for CLEAR_CASES_TEST_DATA_DIALOG
            // in MenuFragment so it will receive the result right away
            MainVM.CASE_STATUS_DIALOG, MainVM.CLEAR_CASES_TEST_DATA_DIALOG -> {
                FragmentResultListener { key, result ->
                    val actionName = result.getString(by.esas.tools.dialog.Config.DIALOG_USER_ACTION)
                    result.putString(MainVM.DIALOG_KEY, key)
                    if (!actionName.isNullOrBlank()) {
                        viewModel.handleAction(Action(actionName, result))
                    } else {
                        viewModel.enableControls()
                    }
                }
            }
            else -> {
                super.provideFragmentResultListener(requestKey)
            }
        }
    }

    override fun handleAction(action: Action): Boolean {
        when (action.name) {
            NEED_TO_UPDATE_MENU -> {
                val bundle = Bundle()
                bundle.putBoolean(NEED_TO_UPDATE_MENU, true)
                intent.putExtras(bundle)
            }
            NEED_TO_UPDATE_CURRENT_CASE -> {
                val newId = action.parameters?.getInt(CURRENT_CASE_ID) ?: -1
                viewModel.currentCaseId = newId
            }
            else -> return super.handleAction(action)
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupNavigation()
        setupSettingsMenu()

        binding.aMainTopBar.setupHandler(object : ITopbarHandler {
            override fun onNavigationClick() {
                onBackPressed()
            }

            override fun onActionClick() {
                navController.currentDestination?.let {
                    if (it.id == topDestination)
                        binding.aMainDrawerLay.openDrawer(GravityCompat.END)
                    else
                        viewModel.openCaseStatusDialog(it.label.toString())
                }
            }
        })
    }

    override fun setupObservers() {
        super.setupObservers()

        viewModel.updateMenuLive.observe(this) {
            handleAction(Action(NEED_TO_UPDATE_MENU))
            if (viewModel.needToRecreate) {
                viewModel.needToRecreate = false
                //REMEMBER to fix
                recreateActivity()
            }
        }
    }

    override fun handleTouchOutOfInputField(event: MotionEvent) {
        if (event.action == MotionEvent.ACTION_DOWN && navController.currentDestination?.id != R.id.utilKeyboardFragment) {
            super.handleTouchOutOfInputField(event)
        }
    }

    private fun setupNavigation() {
        navController = Navigation.findNavController(this, R.id.a_main_nav_host_fragment)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == topDestination) viewModel.currentCaseId = -1
            viewModel.hasBackBtn.set(destination.id != topDestination)
            viewModel.hasSettingsBtn.set(destination.id == topDestination)
            viewModel.title.set(destination.label.toString())
            binding.aMainTopBar.setEndActionViewVisibility(viewModel.currentCaseId != -1)
        }
    }

    private fun setupSettingsMenu() {
        binding.aMainSettingsMenu.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menuUiSettings -> navController.navigate(R.id.baseuiThemeFragment)
                R.id.menuUpdateTest -> viewModel.openClearCasesTestDataDialog()
            }
            binding.aMainDrawerLay.closeDrawer(GravityCompat.END)
            true
        }
    }
}
