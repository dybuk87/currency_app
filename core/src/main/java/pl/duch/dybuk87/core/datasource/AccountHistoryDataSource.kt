package pl.duch.dybuk87.core.datasource

import arrow.core.Either
import org.joda.time.DateTime
import pl.duch.dybuk87.core.kernel.AppError
import pl.duch.dybuk87.core.kernel.Money
import java.math.BigDecimal

enum class AccountHistoryRecordTypeDto {
    TRANSFER,
    CARD_PAYMENT,
    EXCHANGE,
    PAYU,
    WITHDRAW
}

class AccountHistoryRecordDto(
    val type: AccountHistoryRecordTypeDto,

    val id: String,

    val title: String,
    val sourceAccount: String,
    val sender : String,
    val recipientAccount: String,
    val recipientName: String,
    val balance: Money,
    val amount: Money,
    val operationDate: DateTime,

    val cardNumber: String?,

    val exchangeRate : BigDecimal?,
    val exchangeCost : Money?
)

class AccountHistoryRecordPageDto(
    val list : List<AccountHistoryRecordDto>,
    val nextPage : String? = null
)

interface AccountHistoryDataSource {
    suspend fun history(accountNumber: String, nextPage: String?) : Either<AppError, AccountHistoryRecordPageDto>
}

