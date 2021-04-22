package pl.duch.dybuk87.backend.datasource

import arrow.core.Either
import org.joda.time.DateTime
import pl.duch.dybuk87.backend.datasource.model.HistoryPage
import pl.duch.dybuk87.backend.random
import pl.duch.dybuk87.core.datasource.AccountHistoryDataSource
import pl.duch.dybuk87.core.datasource.AccountHistoryRecordDto
import pl.duch.dybuk87.core.datasource.AccountHistoryRecordPageDto
import pl.duch.dybuk87.core.datasource.AccountHistoryRecordTypeDto
import pl.duch.dybuk87.core.kernel.AppError
import pl.duch.dybuk87.core.kernel.CurrencyType
import pl.duch.dybuk87.core.kernel.Money
import java.math.BigDecimal
import java.util.*
import kotlin.random.Random


class AccountHistoryDataSourceImpl : AccountHistoryDataSource {

    private val historyCache : MutableMap<String, MutableList<HistoryPage>> = mutableMapOf()

    override suspend fun history(accountNumber: String, nextPage: String?): Either<AppError, AccountHistoryRecordPageDto> {
        if (!historyCache.containsKey(accountNumber)) {
            generateData(accountNumber)
        }

        val pages = historyCache[accountNumber]!!

        val pageIndex = if (nextPage == null) {
            if (pages.size > 0) 0 else null
        } else {
            pages.indexOfFirst { it.pageId == nextPage }
        }

        val page = if (pageIndex != null && pageIndex != -1) pages[pageIndex] else null
        val newNextPage= if (pageIndex != null && pageIndex != -1 && pageIndex + 1 < pages.size) pages[pageIndex + 1].pageId else "NO_MORE_PAGES"

        return Either.Right(AccountHistoryRecordPageDto(page?.list?: emptyList(), newNextPage))
    }

    private fun generateData(accountNumber: String) {
        val account = MockAccountData.mockData.find { it.accountNumber == accountNumber }!!

        val pageCount = Random.nextInt(0, 5)
        val pages = mutableListOf<HistoryPage>()

        for(i in 0 until pageCount) {
            val pageId = UUID.randomUUID().toString()
            pages.add(
                HistoryPage(pageId, randomHistoryRecords(pageId, CurrencyType.valueOf(account.currency), 10))
            )
        }

        historyCache[accountNumber] = pages
    }

    fun randomHistoryRecords(idPrefix: String, currencyType: CurrencyType, count: Int): List<AccountHistoryRecordDto> {
        val list = mutableListOf<AccountHistoryRecordDto>()

        for (i in 0 until count) {
            val type = AccountHistoryRecordTypeDto.values().random()
            val id = idPrefix + "_" + i

            val record = when(type) {
                AccountHistoryRecordTypeDto.TRANSFER ->
                    AccountHistoryRecordDto(
                        type, id,
                        AccountHistoryMockData.transferTitles.random(),
                        "PL453236546", "Sender mock",
                        "PL535363", "Recipient Mock",
                        Money("3424.55".toBigDecimal(), currencyType),
                        Money(random(-100.0, 100.0), currencyType),
                        DateTime.now(),
                        null, null, null,
                    )

                AccountHistoryRecordTypeDto.CARD_PAYMENT ->
                    AccountHistoryRecordDto(
                        type, id, AccountHistoryMockData.cardPayment.random(),
                        "", "", "", "",
                        Money("3424.55".toBigDecimal(), currencyType),
                        Money(random(-100.0, -1.0), currencyType),
                        DateTime.now(),
                        "255356665334",
                        null, null,
                    )

                AccountHistoryRecordTypeDto.EXCHANGE -> {
                    val rate = random(0.5, 3.5)
                    val amount = random(20.0, 100.0)
                    AccountHistoryRecordDto(
                        type, id, "Exchange", "", "", "",
                        "",
                        Money("3424.55".toBigDecimal(), currencyType),
                        Money(amount, currencyType),
                        DateTime.now(),
                        null,
                        rate, Money(rate.multiply(amount), currencyType)
                    )
                }

                AccountHistoryRecordTypeDto.PAYU ->
                    AccountHistoryRecordDto(
                        type, id, AccountHistoryMockData.blik.random(),
                        "PL53545345", "Sender", "", "",
                        Money("3424.55".toBigDecimal(), currencyType),
                        Money(random(-100.0, -1.0), currencyType),
                        DateTime.now(),
                        null, null,
                        null
                    )

                AccountHistoryRecordTypeDto.WITHDRAW ->
                    AccountHistoryRecordDto(
                        type, id, AccountHistoryMockData.withdraw.random(),
                        "PL424545", "", "", "",
                        Money("3424.55".toBigDecimal(), currencyType),
                        Money(random(-100.0, -1.0), currencyType),
                        DateTime.now(),
                        "63564756", null,
                        null,
                    )
            }

            list.add(record)
        }

        return list
    }

}


class AccountHistoryMockData {
    companion object {
        val transferTitles = listOf(
            "Elixir express transfer",
            "Tax office",
            "Transfer: Eric",
            "Allegro PL SP. Z O.O.",
            "Amazon payment",
            "Morele.net",
            "Komputronik.pl",
            "Ebay.de",
            "Medic Center",
            "Benefit System",
            "JetBrains",
            "Nintendo E-Shop",
            "Steam Software",
            "Social Security",
            "Transfer: Monica",
            "Transfer: Dominic",
        )

        val cardPayment = listOf(
            "AQUAEL",
            "AUCHAN SHOP",
            "Pharmacy DR. MAX",
            "Laykonik bakery",
            "Kaufland",
            "Tesco",
            "Multivet",
            "Sphinks Restaurant",
            "Pad Thai Restaurant",
            "Pasta Masta",
            "Pizza Hut",
            "Zahir kebab",
            "Orlen",
            "Shel",
            "Decathlon",
            "Obi shop",
            "Trattoria due tavoli",
            "Lood is good",
            "LIDL",
            "Donizetti",
            "MooMoo",
            "McDonalds",
            "Thai Express",
            "Subway"
        )

        val blik = listOf(
            "PAYU",
            "PAYPRO S.A.",
            "GOGCOMECOM",
        )

        val withdraw = listOf(
            "ING BANK SLASKI",
            "mBANK PL",
            "PLANET CASH",
            "Alior Bank",
            "Pekao S.A.",
            "PKO Bank Polski S.A.",
            "Santander Bank Polska",
            "Getin Noble Bank",
            "Bank Millenium",
            "BNP Paribas Bank Polska",
            "Bank Handlowy w Warszawie"
        )
    }
}