package pl.duch.dybuk87.banksample.components.account.widget

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.duch.dybuk87.banksample.components.account.action.AccountActionButtonWidgetVM
import pl.duch.dybuk87.banksample.components.account.balance.AccountBalanceWidgetVM
import pl.duch.dybuk87.core.initMutableLiveData
import pl.duch.dybuk87.core.kernel.CurrencyType
import pl.duch.dybuk87.core.kernel.Money
import pl.duch.dybuk87.core.repository.account.Account
import pl.duch.dybuk87.core.repository.account.AccountRepository

data class HomeAccountWidgetData(
    val tmp : String
)

class HomeAccountWidgetVM(
    private val context: Context,
    private val currency: CurrencyType,
    private val accountRepository: AccountRepository
) : ViewModel() {
    private val _data = MutableLiveData<HomeAccountWidgetData>()
    val data: LiveData<HomeAccountWidgetData> = _data

    val newHistoryCount = initMutableLiveData(0)

    val accountBalanceWidget = AccountBalanceWidgetVM(context)

    val actions =
        listOf(AccountActionButtonWidgetVM(), AccountActionButtonWidgetVM(), AccountActionButtonWidgetVM())

    val scrollToTop = BroadcastChannel<Boolean>(1)

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                accountRepository.account(currency) !!. let {
                    updateAccount(it)
                    it.loadNextHistoryPage()
                }
            }
        }
    }

    private suspend fun updateAccount(account: Account) {
        withContext(Dispatchers.Main) {
            accountBalanceWidget.pushModel(account.balance.value)
        }
        setActionButtons(emptyList())

        viewModelScope.launch {
            account.balance.collect {
                withContext(Dispatchers.Main) {
                    accountBalanceWidget.pushModel(it)
                }
            }
        }

        viewModelScope.launch {
            account.newEventCount.collect {
                newHistoryCount.postValue(it)
            }
        }
    }

    private fun setActionButtons(actions: List<Pair<Int, String>>) {
        val size = actions.size.coerceAtMost(this.actions.size)
        for(i in 0 until size) {
            this.actions[i].pushModel(actions[i].first, actions[i].second)
        }
    }

    fun nextHistoryPage() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                accountRepository.account(currency)?.loadNextHistoryPage()
            }
        }
    }

    fun onNewEventClicked() {
        viewModelScope.launch {
            scrollToTop.send(true)
            accountRepository.account(currency)?._newEventCount?.emit(0)
        }
    }

}