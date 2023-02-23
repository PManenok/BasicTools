package by.esas.tools.screens.biometric

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import by.esas.tools.R
import by.esas.tools.base.AppFragment
import by.esas.tools.biometric_decryption.checkBiometricSupport
import by.esas.tools.databinding.FMainBiometricDecryptionBinding

class BiometricDecryptionFragment :
    AppFragment<BiometricDecryptionVM, FMainBiometricDecryptionBinding>() {

    override val fragmentDestinationId = R.id.biometricDecryptionFragment

    override fun provideLayoutId() = R.layout.f_main_biometric_decryption

    override fun provideViewModel(): BiometricDecryptionVM {
        return ViewModelProvider(
            this,
            viewModelFactory.provideFactory()
        ).get(BiometricDecryptionVM::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.showBiometricDialog = { block: (Fragment) -> Unit -> block(this) }
        viewModel.isBiometricAvailable = checkBiometricSupport(requireContext()) { tag, msg ->
            logger.log(tag, msg)
        }

        binding.fBiometricDecryptionDialogBtn.setOnClickListener {
            if (viewModel.isBiometricAvailable)
                viewModel.showBiometricDialog()
            else
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.biometric_decryption_not_available),
                    Toast.LENGTH_SHORT
                ).show()
        }

        binding.fBiometricDecryptionCheckBtn.setOnClickListener {
            val message =
                if (viewModel.isBiometricAvailable) resources.getString(R.string.biometric_decryption_available)
                else resources.getString(R.string.biometric_decryption_not_available)
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun setupObservers() {
        super.setupObservers()
        viewModel.messageLive.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.showBiometricDialog = {}
    }
}