package pl.duch.dybuk87.backend.datasource

import android.os.SystemClock.sleep
import arrow.core.Either
import pl.duch.dybuk87.core.datasource.AccountDataSource
import pl.duch.dybuk87.core.datasource.AccountDto
import pl.duch.dybuk87.core.kernel.AppError

class MockAccountData {
    companion object {
        val mockData = listOf(
            AccountDto("PL44560907704142829499568389", "765.43".toBigDecimal(), "PLN"),
            AccountDto("PL75676204057912034475130712", "1500.32".toBigDecimal(), "EUR"),
            AccountDto("PL97767137515239100090508625", "-10.33".toBigDecimal(), "USD"),

            AccountDto("PL55476570028429102677929838", "6560.13".toBigDecimal(), "CHF"),
            AccountDto("PL45085844480628684180932564", "-432.87".toBigDecimal(), "THB"),
            AccountDto("PL41333651501829686723084710", "68.69".toBigDecimal(), "AUD"),
            AccountDto("PL10348584733760601145706088", "59.22".toBigDecimal(), "RON"),
        )
    }
}

class AccountDataSourceImpl : AccountDataSource {

    override suspend fun accounts(): Either<AppError, List<AccountDto>> {
        sleep(2000)
        return Either.Right(MockAccountData.mockData)
    }
}