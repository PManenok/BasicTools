package by.esas.tools.screens.logger

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import by.esas.tools.R
import by.esas.tools.app_domain.error_mapper.AppErrorStatusEnum
import by.esas.tools.base.AppFragment
import by.esas.tools.checker.Checker
import by.esas.tools.databinding.FMainLoggerBinding
import by.esas.tools.entity.LogItem
import by.esas.tools.logger.ILogger
import by.esas.tools.logger.handler.ErrorAction
import by.esas.tools.logger.handler.ShowErrorType
import by.esas.tools.screens.logger.recycler.LogItemAdapter
import by.esas.tools.utils.checking.AppChecker
import by.esas.tools.utils.checking.FieldChecking
import by.esas.tools.utils.logger.ErrorModel

class LoggerFragment : AppFragment<LoggerVM, FMainLoggerBinding>() {

    override val fragmentDestinationId = R.id.loggerFragment

    override fun provideLayoutId() = R.layout.f_main_logger

    override fun provideViewModel(): LoggerVM {
        return ViewModelProvider(this, viewModelFactory.provideFactory())[LoggerVM::class.java]
    }

    override var logger: ILogger<*> = object : ILogger<ErrorModel> {
        override var currentTag = "CaseLoggerImpl"

        override fun log(tag: String, msg: String, level: Int) {
            setLog(LogItem(tag, "", msg))
        }

        override fun logCategory(category: String, tag: String, msg: String) {
            setLog(LogItem(tag, category, msg))
        }

        override fun logError(throwable: Throwable) {
            setLog(LogItem("", ILogger.CATEGORY_ERROR, throwable.message.toString()))
        }

        override fun logError(error: ErrorModel) {
            setLog(LogItem("", ILogger.CATEGORY_ERROR, error.statusEnum))
        }

        override fun sendLogs(msg: String) {
            setLog(LogItem("", "", msg))
        }

        override fun showMessage(msg: String, length: Int) {
            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
        }

        override fun showMessage(msgRes: Int, length: Int) {
            Toast.makeText(requireContext(), resources.getString(msgRes), Toast.LENGTH_SHORT).show()
        }
    }

    private val logAdapter = LogItemAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()
        setupVMRecycler()

        binding.fLoggerTagBtn.setOnClickListener {
            AppChecker().setListener(object : Checker.CheckListener {
                override fun onSuccess() {
                    logger.log(binding.fLoggerTag.getText(), binding.fLoggerTagMessage.getText())
                }
            }).validate(listOf(FieldChecking(binding.fLoggerTag), FieldChecking(binding.fLoggerTagMessage)))
        }
        binding.fLoggerCategoryBtn.setOnClickListener {
            AppChecker().setListener(object : Checker.CheckListener {
                override fun onSuccess() {
                    logger.logCategory(
                        binding.fLoggerCategory.getText(),
                        binding.fLoggerCategoryTag.getText(),
                        binding.fLoggerCategoryMessage.getText()
                    )
                }
            }).validate(
                listOf(
                    FieldChecking(binding.fLoggerCategory),
                    FieldChecking(binding.fLoggerCategoryTag),
                    FieldChecking(binding.fLoggerCategoryMessage)
                )
            )
        }
        binding.fLoggerErrorBtn.setOnClickListener {
            handleError(
                ErrorAction.Companion.create(
                    ErrorModel(0, AppErrorStatusEnum.UNKNOWN_ERROR),
                    showType = ShowErrorType.SHOW_ERROR_DIALOG.name,
                    ErrorAction.ACTION_ERROR,
                    null
                )
            )
        }
        binding.fLoggerToastBtn.setOnClickListener {
            AppChecker().setListener(object : Checker.CheckListener {
                override fun onSuccess() {
                    logger.showMessage(binding.fLoggerToastMessage.getText())
                }
            }).validate(listOf(FieldChecking(binding.fLoggerToastMessage)))
        }
        binding.fLoggerVmTagBtn.setOnClickListener {
            viewModel.logTag(listOf(FieldChecking(binding.fLoggerVmTag), FieldChecking(binding.fLoggerVmTagMessage)))
        }
        binding.fLoggerVmCategoryBtn.setOnClickListener {
            viewModel.logCategory(
                listOf(
                    FieldChecking(binding.fLoggerVmCategory),
                    FieldChecking(binding.fLoggerVmCategoryTag),
                    FieldChecking(binding.fLoggerVmCategoryMessage)
                )
            )
        }
        binding.fLoggerVmToastBtn.setOnClickListener {
            viewModel.showMessage(listOf(FieldChecking(binding.fLoggerVmToastMessage)))
        }
    }

    override fun setupObservers() {
        super.setupObservers()
        viewModel.needScrolling.observe(viewLifecycleOwner) {
            if (isBindingInitialized()) binding.fLoggerLogRecyclerVm.scrollToPosition(viewModel.logVMAdapter.itemList.size - 1)
        }
    }

    private fun setupRecycler() {
        binding.fLoggerLogRecycler.adapter = logAdapter
        binding.fLoggerLogRecycler.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupVMRecycler() {
        binding.fLoggerLogRecyclerVm.adapter = viewModel.logVMAdapter
        binding.fLoggerLogRecyclerVm.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setLog(log: LogItem) {
        logAdapter.addItem(log)
        if (isBindingInitialized()) binding.fLoggerLogRecycler.scrollToPosition(logAdapter.itemList.size - 1)
    }
}
