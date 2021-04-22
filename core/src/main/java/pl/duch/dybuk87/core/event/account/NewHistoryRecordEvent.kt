package pl.duch.dybuk87.core.event.account

import pl.duch.dybuk87.core.kernel.Money
import pl.duch.dybuk87.core.repository.account.history.AccountHistoryRecord

data class NewHistoryRecordEvent(
    val accountNumber: String,
    val balance : Money,
    val accountHistoryRecord: AccountHistoryRecord
)