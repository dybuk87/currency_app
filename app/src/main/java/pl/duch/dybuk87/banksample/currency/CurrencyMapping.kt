package pl.duch.dybuk87.banksample.currency

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import pl.duch.dybuk87.banksample.R
import pl.duch.dybuk87.core.kernel.CurrencyType

class CurrencyStaticData(
    val currencyType: CurrencyType,
    @DrawableRes val image : Int,
    @StringRes val currencyName : Int
)

class CurrencyMapping {
    companion object {
        val list = listOf(
            CurrencyStaticData(CurrencyType.CAD, R.drawable.ic_flag_canada, R.string.currency_cad),
            CurrencyStaticData(CurrencyType.HKD, R.drawable.ic_flag_hong_kong, R.string.currency_hkd),
            CurrencyStaticData(CurrencyType.DKK, R.drawable.ic_flag_denmark, R.string.currency_dkk),
            CurrencyStaticData(CurrencyType.HUF, R.drawable.ic_flag_hungary, R.string.currency_huf),
            CurrencyStaticData(CurrencyType.CZK, R.drawable.ic_flag_czech_republic, R.string.currency_czk),
            CurrencyStaticData(CurrencyType.AUD, R.drawable.ic_flag_australia, R.string.currency_aud),
            CurrencyStaticData(CurrencyType.RON, R.drawable.ic_flag_romania, R.string.currency_ron),
            CurrencyStaticData(CurrencyType.SEK, R.drawable.ic_flag_sweden, R.string.currency_sek),
            CurrencyStaticData(CurrencyType.RUB, R.drawable.ic_flag_russia, R.string.currency_rub),
            CurrencyStaticData(CurrencyType.HRK, R.drawable.ic_flag_croatia, R.string.currency_hrk),
            CurrencyStaticData(CurrencyType.JPY, R.drawable.ic_flag_japan, R.string.currency_jpy),
            CurrencyStaticData(CurrencyType.THB, R.drawable.ic_flag_thailand, R.string.currency_thb),
            CurrencyStaticData(CurrencyType.CHF, R.drawable.ic_flag_switzerland, R.string.currency_chf),
            CurrencyStaticData(CurrencyType.PLN, R.drawable.ic_flag_poland, R.string.currency_pln),
            CurrencyStaticData(CurrencyType.BGN, R.drawable.ic_flag_bulgaria, R.string.currency_bgn),
            CurrencyStaticData(CurrencyType.TRY, R.drawable.ic_flag_turkey, R.string.currency_try),
            CurrencyStaticData(CurrencyType.NOK, R.drawable.ic_flag_norway, R.string.currency_nok),
            CurrencyStaticData(CurrencyType.USD, R.drawable.ic_flag_usa, R.string.currency_usd),
            CurrencyStaticData(CurrencyType.MXN, R.drawable.ic_flag_mexico, R.string.currency_mxn),
            CurrencyStaticData(CurrencyType.ILS, R.drawable.ic_flag_israel, R.string.currency_ils),
            CurrencyStaticData(CurrencyType.GBP, R.drawable.ic_flag_uk, R.string.currency_gbp),
            CurrencyStaticData(CurrencyType.EUR, R.drawable.ic_flag_eu, R.string.currency_eu),
        )

        val mapping = list.map { Pair(it.currencyType, it) }.toMap()
    }
}