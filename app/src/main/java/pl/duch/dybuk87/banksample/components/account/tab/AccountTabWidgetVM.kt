package pl.duch.dybuk87.banksample.components.account.tab

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import pl.duch.dybuk87.banksample.currency.CurrencyMapping
import pl.duch.dybuk87.core.initMutableLiveData
import pl.duch.dybuk87.core.kernel.CurrencyType
import pl.duch.dybuk87.core.kernel.Money

data class AccountTabData(
    @DrawableRes val currencyIcon: Int,
    val description: String,
)

class AccountTabWidgetVM(val context: Context) {
    private val _data = MutableLiveData<AccountTabData>()
    val data : LiveData<AccountTabData> = _data
    var currency : CurrencyType = CurrencyType.PLN

    private val _selected = initMutableLiveData(false)
    val selected: LiveData<Boolean> = _selected

    fun tabClicked() {

    }

    fun pushModel(money: Money) {
        currency = money.currencyType
        CurrencyMapping.mapping[money.currencyType] ?. let { currency ->
            _data.value =
                AccountTabData(
                    currency.image,
                    "${money.amount} ${money.currencyType.name}"
                )
        }
    }
}