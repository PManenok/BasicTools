package by.esas.tools.screens.baseui.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import by.esas.tools.R
import by.esas.tools.base.AppFragment
import by.esas.tools.databinding.FMainBaseuiThemeBinding
import by.esas.tools.util.configs.UiModeType

class BaseUIThemeFragment: AppFragment<BaseUIThemeVM, FMainBaseuiThemeBinding>() {
    override val fragmentDestinationId = R.id.baseuiThemeFragment

    override fun provideLayoutId() = R.layout.f_main_baseui_theme

    override fun provideViewModel(): BaseUIThemeVM {
        return ViewModelProvider(this, viewModelFactory.provideFactory()).get(BaseUIThemeVM::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fBaseuiThemeSetLangBtn.setOnClickListener {
            viewModel.requestLanguageChange(getLanguage())
        }

        binding.fBaseuiThemeSetModeBtn.setOnClickListener {
            viewModel.requestThemeChange(getThemeMode())
        }
    }

    private fun getLanguage(): String {
        val selectedItem = binding.fBaseuiThemeLangRadioGroup.checkedRadioButtonId
        return when(selectedItem) {
            R.id.f_baseui_theme_lang_radio_russian -> "ru"
            R.id.f_baseui_theme_lang_radio_belorussian -> "be"
            else -> "en"
        }
    }

    private fun getThemeMode(): UiModeType {
        val selectedItem = binding.fBaseuiThemeModeRadioGroup.checkedRadioButtonId
        return when(selectedItem) {
            R.id.f_baseui_theme_mode_radio_light -> UiModeType.DAY
            R.id.f_baseui_theme_mode_radio_night -> UiModeType.NIGHT
            else -> UiModeType.SYSTEM
        }
    }
}