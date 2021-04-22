package pl.duch.dybuk87.core.repository.account

import kotlinx.coroutines.flow.StateFlow
import pl.duch.dybuk87.core.kernel.CurrencyType
import pl.duch.dybuk87.core.kernel.RemoteData

interface AccountRepository {
    val ownedAccounts: StateFlow<RemoteData<List<Account>>>

    suspend fun loadAccountList()

    suspend fun account(currencyType: CurrencyType) : Account?
}