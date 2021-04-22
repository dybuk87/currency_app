package pl.duch.dybuk87.core.repository.account

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.consume
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import org.joda.time.DateTime
import pl.duch.dybuk87.core.datasource.AccountDataSource
import pl.duch.dybuk87.core.datasource.AccountDto
import pl.duch.dybuk87.core.datasource.AccountHistoryDataSource
import pl.duch.dybuk87.core.event.AppEvents
import pl.duch.dybuk87.core.kernel.*
import pl.duch.dybuk87.core.repository.account.history.AccountHistoryRecord
import java.lang.Thread.sleep

@ExperimentalCoroutinesApi
class AccountRepositoryImpl(
    private val dataSource: AccountDataSource,
    private val historyDataSource: AccountHistoryDataSource,
    private val appEvents: AppEvents
) : AccountRepository {

    private val counterContext = newSingleThreadContext("CounterContext")

    private val _ownedAccounts =
        MutableStateFlow(RemoteData(RemoteDataState.NotReady, emptyList<Account>()))

    override val ownedAccounts: StateFlow<RemoteData<List<Account>>> = _ownedAccounts

    init {
        GlobalScope.launch {
            appEvents.accountHistory.openSubscription().consumeEach {event ->
                ownedAccounts.value.data
                    .firstOrNull { it.accountNumber == event.accountNumber }
                    ?.run {
                        this._balance.emit(event.balance)
                        val oldHistory = this._history.value
                        val newHistory = RemoteData(
                            oldHistory.state, listOf(event.accountHistoryRecord) + oldHistory.data
                        )
                        this._history.emit(newHistory)
                        this._newEventCount.emit(this.newEventCount.value + 1)
                    }
            }
        }
    }

    override suspend fun loadAccountList() {
        withContext(Dispatchers.IO) {
            withContext(counterContext) {
                val currentState = _ownedAccounts.value
                if (currentState.state == RemoteDataState.Loading) {
                    cancel()
                }
                _ownedAccounts.emit(RemoteData(RemoteDataState.Loading, currentState.data))
            }

            val callData = dataSource.accounts()

            val result = callData.fold({
                RemoteData(RemoteDataState.Error, emptyList<Account>())
            }, {
                RemoteData(RemoteDataState.Success, it.map(::convertAccountDto))
            })

            withContext(counterContext) {
                _ownedAccounts.emit(result)
            }
        }
    }

    override suspend fun account(currencyType: CurrencyType): Account? =
        ownedAccounts.value.data.firstOrNull { it.balance.value.currencyType == currencyType }

    private fun convertAccountDto(accountDto: AccountDto): Account =
        Account(
            historyDataSource,
            accountDto.accountNumber,
            Money(accountDto.balance, CurrencyType.valueOf(accountDto.currency))
        )


}