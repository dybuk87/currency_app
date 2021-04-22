package pl.duch.dybuk87.core.repository.account

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import pl.duch.dybuk87.core.datasource.AccountHistoryDataSource
import pl.duch.dybuk87.core.deeplink.DeepLinkType
import pl.duch.dybuk87.core.kernel.Entity
import pl.duch.dybuk87.core.kernel.Money
import pl.duch.dybuk87.core.kernel.RemoteData
import pl.duch.dybuk87.core.kernel.RemoteDataState
import pl.duch.dybuk87.core.repository.account.history.AccountHistoryRecord

class Account(
    private val historyDataSource: AccountHistoryDataSource,
    val accountNumber: String,
    initialBalance: Money
) : Entity {

    override val id = accountNumber

    val _balance = MutableStateFlow(initialBalance)
    val balance: StateFlow<Money> = _balance

    val _history = MutableStateFlow(
        RemoteData(RemoteDataState.NotReady, emptyList<AccountHistoryRecord>()))
    val history: StateFlow<RemoteData<List<AccountHistoryRecord>>> = _history

    val _newEventCount = MutableStateFlow<Int>(0)
    val newEventCount : StateFlow<Int> = _newEventCount

    private val _deepLinks =
        MutableStateFlow(listOf(DeepLinkType.TRANSFER, DeepLinkType.BLIK))
    val deepLinks: StateFlow<List<DeepLinkType>> = _deepLinks

    var nextPage: String? = null

    suspend fun loadNextHistoryPage() {
        if (_history.value.state == RemoteDataState.Loading ||
            (_history.value.state == RemoteDataState.Success && _history.value.data.isEmpty())) {
            return
        }

        if (nextPage == null) {
            _newEventCount.emit(0)
        }

        _history.emit(RemoteData(RemoteDataState.Loading, _history.value.data))
        Thread.sleep(1500)

        val list =
            historyDataSource.history(accountNumber, nextPage).fold({
                mutableListOf<AccountHistoryRecord>()
            }, {
                nextPage = it.nextPage
                it.list.map { AccountHistoryRecord.create(it) }
            })

        _history.emit(RemoteData(RemoteDataState.Success, _history.value.data + list))
    }


}