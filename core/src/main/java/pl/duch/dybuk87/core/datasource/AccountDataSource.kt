package pl.duch.dybuk87.core.datasource

import arrow.core.Either
import pl.duch.dybuk87.core.kernel.AppError
import java.math.BigDecimal

data class AccountDto(
    val accountNumber : String,
    val balance: BigDecimal,
    val currency: String
)

interface AccountDataSource {
    suspend fun accounts() : Either<AppError, List<AccountDto>>
}