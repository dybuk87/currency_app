package pl.duch.dybuk87.core.repository.account.history

import org.joda.time.DateTime
import pl.duch.dybuk87.core.datasource.AccountHistoryRecordDto
import pl.duch.dybuk87.core.datasource.AccountHistoryRecordTypeDto
import pl.duch.dybuk87.core.kernel.Entity
import pl.duch.dybuk87.core.kernel.Money
import java.math.BigDecimal

sealed class AccountHistoryRecord(val balance: Money, val amount: Money, val title: String, val operationDate: DateTime) : Entity {

    class Transfer(
        override val id: String,
        title: String,
        val sourceAccount: String,
        val sender : String,
        val recipientAccount: String,
        val recipientName: String,
        balance: Money,
        amount: Money,
        operationDate: DateTime,
    ) : AccountHistoryRecord(balance, amount, title, operationDate)

    class CardPayment(
        override val id: String,
        title: String,
        val cardNumber: String,
        val accountNumber: String,
        balance: Money,
        amount: Money,
        operationDate: DateTime
    ) : AccountHistoryRecord(balance, amount, title, operationDate)

    class Exchange(
        override val id: String,
        val destinationAccountNumber: String,
        val sourceAccountNumber: String,
        balance: Money,
        amount: Money,
        operationDate: DateTime,
        val exchangeRate : BigDecimal,
        val exchangeCost : Money
    ) : AccountHistoryRecord(balance, amount, "", operationDate)

    class PAYU(
        override val id: String,
        title: String,
        val accountNumber: String,
        balance: Money,
        amount: Money,
        operationDate: DateTime,
    ) : AccountHistoryRecord(balance, amount, title, operationDate)

    class Withdraw(
        override val id: String,
        title: String,
        val cardNumber: String,
        val accountNumber: String,
        balance: Money,
        amount: Money,
        operationDate: DateTime
    ) : AccountHistoryRecord(balance, amount, title, operationDate)

    companion object {
        fun create(data: AccountHistoryRecordDto) : AccountHistoryRecord =
            when(data.type) {
                AccountHistoryRecordTypeDto.TRANSFER ->
                    Transfer(
                        data.id, data.title, data.sourceAccount, data.sender,
                        data.recipientAccount, data.recipientName,
                        data.balance,
                        data.amount,
                        data.operationDate
                    )

                AccountHistoryRecordTypeDto.CARD_PAYMENT ->
                    CardPayment(
                        data.id, data.title, data.cardNumber?:"",
                        data.sourceAccount?:"",
                        data.balance, data.amount, data.operationDate
                    )

                AccountHistoryRecordTypeDto.EXCHANGE ->
                    Exchange(
                        data.id, data.recipientAccount, data.sourceAccount,
                        data.balance, data.amount, data.operationDate, data.exchangeRate!!,
                        data.exchangeCost!!
                    )

                AccountHistoryRecordTypeDto.PAYU ->
                    PAYU(data.id, data.title, data.sourceAccount, data.balance, data.amount, data.operationDate)

                AccountHistoryRecordTypeDto.WITHDRAW ->
                    Withdraw(data.id, data.title, data.cardNumber!!, data.sourceAccount, data.balance, data.amount, data.operationDate)
            }
    }
}

