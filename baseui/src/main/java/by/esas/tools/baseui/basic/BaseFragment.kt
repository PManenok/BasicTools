package by.esas.tools.baseui.basic

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import by.esas.tools.baseui.app.BaseApp
import by.esas.tools.logger.ILogger
import by.esas.tools.util.defocusAndHideKeyboard

abstract class BaseFragment<E : Enum<E>> : Fragment() {
    abstract val TAG: String

    abstract var logger: ILogger<E,*>

    abstract fun provideLogger(): ILogger<E,*>

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
        logger.logInfo(BaseApp.appContext.resources.getString(textId))
        logger.showMessage(textId, duration)
    }
}