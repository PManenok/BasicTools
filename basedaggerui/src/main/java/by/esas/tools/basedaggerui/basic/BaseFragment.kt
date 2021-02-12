package by.esas.tools.basedaggerui.basic

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import by.esas.tools.basedaggerui.inject.factory.InjectingViewModelFactory
import by.esas.tools.logger.BaseLogger
import by.esas.tools.logger.ILogger
import by.esas.tools.util.defocusAndHideKeyboard
import dagger.android.support.DaggerFragment
import javax.inject.Inject

abstract class BaseFragment<E : Enum<E>> : DaggerFragment() {
    open val TAG: String = "BaseFragment"

    open var logger: ILogger<E, *> = BaseLogger(TAG, null)

    @Inject
    lateinit var viewModelFactory: InjectingViewModelFactory

    abstract fun provideAppContext(): Context

    override fun onStart() {
        super.onStart()
        logger.logInfo("onStart")
    }

    override fun onStop() {
        super.onStop()
        logger.logInfo("onStop")
    }

    override fun onResume() {
        super.onResume()
        logger.logInfo("onResume")
    }

    override fun onPause() {
        super.onPause()
        logger.logInfo("onPause")
        hideKeyboardAndNavigation(activity)
    }

    private fun hideSystemUi(activity: Activity?) {
        logger.logInfo("hideSystemUi")
        activity?.onWindowFocusChanged(true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        logger.logInfo("onActivityCreated")
        hideSystemUi(activity)
        //navController = findNavController(this)
    }

    protected fun hideKeyboardAndNavigation(activity: Activity?) {
        defocusAndHideKeyboard(activity)
        logger.logInfo("defocusAndHideKeyboard")
        //hideSystemUi(activity)
    }

    fun showMessage(text: String, duration: Int = Toast.LENGTH_SHORT) {
        logger.logInfo(text)
        logger.showMessage(text, duration)
    }

    fun showMessage(textId: Int, duration: Int = Toast.LENGTH_SHORT) {
        logger.logInfo(provideAppContext().resources.getString(textId))
        logger.showMessage(textId, duration)
    }
}