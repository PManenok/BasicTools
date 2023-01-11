package by.esas.tools.screens.baseui.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import by.esas.tools.App
import by.esas.tools.R
import by.esas.tools.app_data.AppSharedPrefs
import by.esas.tools.base.AppFragment
import by.esas.tools.databinding.FMainBaseuiThemeBinding
import by.esas.tools.logger.Action
import by.esas.tools.screens.MainActivity
import by.esas.tools.util.configs.UiModeType

class BaseUIThemeFragment: AppFragment<BaseUIThemeVM, FMainBaseuiThemeBinding>() {
    override val fragmentDestinationId = R.id.baseuiThemeFragment

    override fun provideLayoutId() = R.layout.f_main_baseui_theme

    override fun provideViewModel(): BaseUIThemeVM {
        return ViewModelProvider(this, viewModelFactory.provideFactory()).get(BaseUIThemeVM::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setCheckedLang()
        setCheckedTheme()

        binding.fBaseuiThemeSetLangBtn.setOnClickListener {
            if (activity is MainActivity)
                (activity as MainActivity).handleAction(Action(MainActivity.NEED_TO_UPDATE_MENU))
            viewModel.requestLanguageChange(getLanguage())
        }
        binding.fBaseuiThemeSetModeBtn.setOnClickListener {
            viewModel.requestThemeChange(getThemeMode())
        }
    }

    private fun setCheckedLang() {
        val checkedLang = AppSharedPrefs(App.instance).getLanguage()
        when(checkedLang) {
            "ru" -> binding.fBaseuiThemeLangRadioRussian.isChecked = true
            "en" -> binding.fBaseuiThemeLangRadioEnglish.isChecked = true
        }
    }

    private fun setCheckedTheme() {
        val checkedTheme = AppSharedPrefs(App.instance).getTheme()
        when(checkedTheme) {
            UiModeType.SYSTEM -> binding.fBaseuiThemeModeRadioSystem.isChecked = true
            UiModeType.DAY -> binding.fBaseuiThemeModeRadioLight.isChecked = true
            UiModeType.NIGHT -> binding.fBaseuiThemeModeRadioNight.isChecked = true
        }
    }

    private fun getLanguage(): String {
        val selectedItem = binding.fBaseuiThemeLangRadioGroup.checkedRadioButtonId
        return when(selectedItem) {
            R.id.f_baseui_theme_lang_radio_russian -> "ru"
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