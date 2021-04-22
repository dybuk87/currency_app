package pl.duch.dybuk87.core.event

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import pl.duch.dybuk87.core.datasource.AccountHistoryRecordDto
import pl.duch.dybuk87.core.event.account.NewHistoryRecordEvent
import pl.duch.dybuk87.core.kernel.Money
import pl.duch.dybuk87.core.repository.account.history.AccountHistoryRecord

abstract class AppEvents {
    @ExperimentalCoroutinesApi
    abstract val accountHistory : BroadcastChannel<NewHistoryRecordEvent>

    @ExperimentalCoroutinesApi
    suspend fun send(accountNumber: String, newBalance: Money, history: AccountHistoryRecordDto) {
        val historyEvent = NewHistoryRecordEvent(accountNumber, newBalance, AccountHistoryRecord.create(history))
        accountHistory.send(historyEvent)
    }
}