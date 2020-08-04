package by.esas.tools.baseui.basic

import androidx.fragment.app.Fragment
import by.hgrosh.domain.util.ILogger
import by.hgrosh.notary.utils.logger.LoggerImpl
import dagger.android.support.DaggerFragment
import javax.inject.Inject

abstract class BaseFragment<TViewModel : Any, TBinding : Any> : Fragment() {
    abstract val TAG: String

    protected lateinit var binding: TBinding

    protected lateinit var viewModel: TViewModel

    var logger: ILogger = LoggerImpl()
}