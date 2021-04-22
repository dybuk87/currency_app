package pl.duch.dybuk87.banksample.components.account.balance

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import pl.duch.dybuk87.banksample.currency.CurrencyMapping
import pl.duch.dybuk87.core.kernel.Money

data class AccountBalanceData(
    @DrawableRes val currencyIcon: Int,
    val description: String
)


class AccountBalanceWidgetVM(val context: Context) {
    private val _data = MutableLiveData<AccountBalanceData>()
    val data: LiveData<AccountBalanceData> = _data

    fun pushModel(money: Money) {
        CurrencyMapping.mapping[money.currencyType]?.let { currency ->
            _data.value =
                AccountBalanceData(
                    currency.image,
                    "${money.amount} ${money.currencyType.name}"
                )
        }
    }
}