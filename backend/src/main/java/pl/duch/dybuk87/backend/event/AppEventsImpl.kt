package pl.duch.dybuk87.backend.event

import android.os.SystemClock.sleep
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.launch
import pl.duch.dybuk87.backend.datasource.AccountHistoryDataSourceImpl
import pl.duch.dybuk87.core.datasource.AccountDataSource
import pl.duch.dybuk87.core.event.AppEvents
import pl.duch.dybuk87.core.event.account.NewHistoryRecordEvent
import pl.duch.dybuk87.core.insertInto
import pl.duch.dybuk87.core.kernel.CurrencyType
import pl.duch.dybuk87.core.kernel.Money
import java.util.*

class AppEventsImpl(
    private val accountDataSource: AccountDataSource,
    private val accountHistoryDataSourceImpl: AccountHistoryDataSourceImpl
) : AppEvents() {

    override val accountHistory = BroadcastChannel<NewHistoryRecordEvent>(1)

    private val currentBalance = mutableMapOf<String, Money>()

    init {
        GlobalScope.launch {
            accountDataSource.accounts().fold({}, { accounts ->

                accounts.map { it.accountNumber to Money(it.balance, CurrencyType.valueOf(it.currency)) } insertInto currentBalance

                for(i in 0 until 500) {
                    sleep(1500)
                    val uid = UUID.randomUUID().toString()
                    val account = accounts.random()
                    val history = accountHistoryDataSourceImpl.randomHistoryRecords(
                        uid, CurrencyType.valueOf(account.currency), 1)[0]

                    val newBalance = currentBalance[account.accountNumber]!! + history.amount
                    currentBalance[account.accountNumber] = newBalance

                    send(
                        account.accountNumber,
                        newBalance,
                        history
                    )
                }
            })
        }

    }

}
