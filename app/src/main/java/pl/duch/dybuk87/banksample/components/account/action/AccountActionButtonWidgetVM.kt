package pl.duch.dybuk87.banksample.components.account.action

import androidx.annotation.DrawableRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

data class AccountActionButtonData(
    @DrawableRes val currencyIcon: Int,
    val description: String?
)

class AccountActionButtonWidgetVM {
    private val _data = MutableLiveData<AccountActionButtonData>()
    val data: LiveData<AccountActionButtonData> = _data

    fun pushModel(@DrawableRes currencyIcon: Int, description: String? = null) {
        _data.value = AccountActionButtonData(currencyIcon, description)
    }
}