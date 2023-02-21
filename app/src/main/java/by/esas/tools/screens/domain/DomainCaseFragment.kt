package by.esas.tools.screens.domain

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import by.esas.tools.R
import by.esas.tools.base.AppFragment
import by.esas.tools.checker.Checker
import by.esas.tools.checker.Checking
import by.esas.tools.databinding.FMainDomainCaseBinding
import by.esas.tools.logger.handler.ErrorHandler
import by.esas.tools.util.TAGk
import by.esas.tools.utils.checking.AppChecker
import by.esas.tools.utils.checking.FieldChecking
import by.esas.tools.utils.logger.ErrorModel

class DomainCaseFragment : AppFragment<DomainCaseVM, FMainDomainCaseBinding>() {
    override val fragmentDestinationId = R.id.domainCaseFragment
    override fun provideLayoutId() = R.layout.f_main_domain_case

    override fun provideViewModel(): DomainCaseVM {
        return ViewModelProvider(
            this,
            viewModelFactory.provideFactory()
        ).get(DomainCaseVM::class.java)
    }

    override fun provideChecks(): List<Checking> {
        return listOf(
            FieldChecking(binding.fDomainCaseNumber, true),
            FieldChecking(binding.fDomainCaseRange, true)
        )
    }

    override fun provideErrorHandler(): ErrorHandler<ErrorModel> {
        return object : ErrorHandler<ErrorModel>() {

            override fun getErrorMessage(error: ErrorModel): String {
                return resources.getString(R.string.domain_case_fail)
            }

            override fun getErrorMessage(e: Throwable): String {
                return resources.getString(R.string.domain_case_fail)
            }

            override fun mapError(e: Throwable): ErrorModel {
                return viewModel.provideMapper().mapErrorException(this.TAGk, e)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fDomainCasePlayBtn.setOnClickListener {
            AppChecker().setListener(object : Checker.CheckListener {
                override fun onFailed() {
                    viewModel.enableControls()
                }

                override fun onSuccess() {
                    viewModel.enableControls()
                    viewModel.playRandomNumber(
                        binding.fDomainCaseRange.getText().toInt(),
                        binding.fDomainCaseNumber.getText().toInt()
                    )
                }
            }).validate(provideChecks())
        }
        binding.fDomainCaseEncryptAesBtn.setOnClickListener {
            val checks = listOf(FieldChecking(binding.fDomainCaseInputEncryptAes, true))

            AppChecker().setListener(object : Checker.CheckListener {
                override fun onFailed() {
                    viewModel.enableControls()
                }

                override fun onSuccess() {
                    viewModel.enableControls()
                    val aesResult = viewModel.encryptAES(binding.fDomainCaseInputEncryptAes.getText())
                    binding.fDomainCaseEncryptAesData.text = resources.getString(R.string.domain_case_encrypt) + aesResult.first
                    binding.fDomainCaseDecryptAesData.text = resources.getString(R.string.domain_case_decrypt) + aesResult.second
                }
            }).validate(checks)
        }

        binding.fDomainCaseEncryptRsaBtn.setOnClickListener {
            val checks = listOf(FieldChecking(binding.fDomainCaseInputEncryptRsa, true))
            AppChecker().setListener(object : Checker.CheckListener {
                override fun onFailed() {
                    viewModel.enableControls()
                }

                override fun onSuccess() {
                    viewModel.enableControls()
                    val rsaResult = viewModel.encryptRSA(binding.fDomainCaseInputEncryptRsa.getText())
                    binding.fDomainCaseEncryptRsaData.text = resources.getString(R.string.domain_case_encrypt) + rsaResult.first
                    binding.fDomainCaseDecryptRsaData.text = resources.getString(R.string.domain_case_decrypt) + rsaResult.second
                }
            }).validate(checks)
        }
    }
}
