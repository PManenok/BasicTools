package by.esas.tools.screens.access_container.utility

import android.content.Context
import by.esas.tools.App
import by.esas.tools.R
import by.esas.tools.accesscontainer.support.supporter.ResourceStrProvider

class ResProviderImpl : ResourceStrProvider {

    override fun requireContext(): Context {
        return App.appContext
    }

    override fun getString(resId: Int): String {
        return App.appContext.getString(resId)
    }

    override fun provideAlterCancelStr(): Int = R.string.dialog_change_btn
    override fun provideCancelStr(): Int {
        return R.string.common_cancel
    }

    override fun provideAccessStr(): Int = R.string.dialog_auth_title
    override fun provideAccessConfirmStr(): Int = R.string.dialog_auth_title_confirm
    override fun provideEncryptTitle(): Int = R.string.dialog_auth_encrypt_title
}