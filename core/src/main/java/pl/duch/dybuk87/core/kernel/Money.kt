package pl.duch.dybuk87.core.kernel

import java.lang.RuntimeException
import java.math.BigDecimal

data class Money(
    val amount: BigDecimal,
    val currencyType: CurrencyType
) {
    fun format(): String = "$amount $currencyType"

    operator fun unaryMinus(): Money {
        return Money(amount.negate(), currencyType)
    }

    operator fun plus(money: Money): Money {
        assertCurrency(money)
        return Money(amount.add(money.amount), currencyType)
    }

    operator fun plus(money: BigDecimal): Money {
        return Money(amount.add(money), currencyType)
    }

    operator fun plus(money: String): Money {
        return Money(amount.add(money.toBigDecimal()), currencyType)
    }

    operator fun minus(money: Money): Money {
        assertCurrency(money)
        return Money(amount.subtract(money.amount), currencyType)
    }

    operator fun minus(money: BigDecimal): Money {
        return Money(amount.subtract(money), currencyType)
    }

    operator fun minus(money: String): Money {
        return Money(amount.subtract(money.toBigDecimal()), currencyType)
    }


    operator fun times(value: BigDecimal): Money {
        return Money(amount.multiply(value), currencyType)
    }

    operator fun div(value: BigDecimal): Money {
        return Money(amount.divide(value), currencyType)
    }

    private fun assertCurrency(money: Money) {
        if (currencyType != money.currencyType) {
            throw RuntimeException("Unable to perform operation money: ${this.format()} ; ${money.format()}")
        }
    }
}