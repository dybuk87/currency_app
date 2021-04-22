package pl.duch.dybuk87.banksample.components.account.history

import android.content.Context
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.novoda.espresso.ViewTestRule
import kotlinx.android.synthetic.main.widget_account_history_record.view.*
import org.joda.time.DateTime
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import pl.duch.dybuk87.banksample.InstMockkitoRule
import pl.duch.dybuk87.banksample.LCOwner
import pl.duch.dybuk87.banksample.R
import pl.duch.dybuk87.banksample.withDrawable
import pl.duch.dybuk87.core.kernel.CurrencyType
import pl.duch.dybuk87.core.kernel.Money
import pl.duch.dybuk87.core.repository.account.history.AccountHistoryRecord

@RunWith(AndroidJUnit4::class)
class AccountHistoryRecordWidgetTest {

    @Rule
    @JvmField
    var mockkitoRule = InstMockkitoRule()

    @Rule
    @JvmField
    var rule : ViewTestRule<AccountHistoryRecordWidget> =
        ViewTestRule<AccountHistoryRecordWidget> { context, _ ->
            AccountHistoryRecordWidget (
                context
            ).also {
                it.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)
                it.setBackgroundColor(ContextCompat.getColor(context, R.color.content_detail))
            }
        }


    lateinit var context : Context

    @Before
    fun init() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun viewAccountHistoryRecordWidget_Card() {
        val vm = AccountHistoryRecordWidgetVM(context)

        rule.runOnMainSynchronously {
            rule.view.subscribe(LCOwner(), vm)
            vm.pushModel(
                AccountHistoryRecord.CardPayment("233", "Lewiatan",
                    "PL234", "PL555",
                    Money("5343.54".toBigDecimal(), CurrencyType.USD),
                    Money("81.20".toBigDecimal(), CurrencyType.USD),
                    DateTime.now()
                )
            )
        }

        onView(withId(R.id.account_history_record_image)).check(matches(withDrawable(R.drawable.ic_credit_card_black_36dp)))
        onView(withId(R.id.account_history_record_desc)).check(matches(withText("Lewiatan")))
        onView(withId(R.id.account_history_record_desc_details)).check(matches(withText("Card payment")))
        onView(withId(R.id.account_history_record_amount)).check(matches(withText("81.20 USD")))

        rule.runOnMainSynchronously {
            val testOwner = rule.view.unsubscribe()
        }
    }

    @Test
    fun viewAccountHistoryRecordWidget_Exchange() {
        val vm = AccountHistoryRecordWidgetVM(context)

        rule.runOnMainSynchronously {
            rule.view.subscribe(LCOwner(), vm)
            vm.pushModel(
                AccountHistoryRecord.Exchange("233",
                    "PL234", "PL555",
                    Money("5343.54".toBigDecimal(), CurrencyType.PLN),
                    Money("441.22".toBigDecimal(), CurrencyType.PLN),
                    DateTime.now(),
                    "3.42".toBigDecimal(),
                    Money((441.22 * 3.42).toBigDecimal(), CurrencyType.PLN)
                )
            )
        }

        onView(withId(R.id.account_history_record_image)).check(matches(withDrawable(R.drawable.ic_trending_up_black_24dp)))
        onView(withId(R.id.account_history_record_desc)).check(matches(withText("Money exchange")))
        onView(withId(R.id.account_history_record_desc_details)).check(matches(withText("")))
        onView(withId(R.id.account_history_record_amount)).check(matches(withText("441.22 PLN")))

        rule.runOnMainSynchronously {
            val testOwner = rule.view.unsubscribe()
        }
    }

    @Test
    fun viewAccountHistoryRecordWidget_Blik() {
        val vm = AccountHistoryRecordWidgetVM(context)

        rule.runOnMainSynchronously {
            rule.view.subscribe(LCOwner(), vm)
            vm.pushModel(
                AccountHistoryRecord.PAYU("233", "Frankie & Bennys 1949",
                    "PL234",
                    Money("5343.54".toBigDecimal(), CurrencyType.USD),
                    Money("11.29".toBigDecimal(), CurrencyType.USD),
                    DateTime.now()
                )
            )
        }

        onView(withId(R.id.account_history_record_image)).check(matches(withDrawable(R.drawable.blik)))
        onView(withId(R.id.account_history_record_desc)).check(matches(withText("Frankie & Bennys 1949")))
        onView(withId(R.id.account_history_record_desc_details)).check(matches(withText("Blik payment")))
        onView(withId(R.id.account_history_record_amount)).check(matches(withText("11.29 USD")))

        rule.runOnMainSynchronously {
            val testOwner = rule.view.unsubscribe()
        }
    }


    @Test
    fun viewAccountHistoryRecordWidget_Transfer() {
        val vm = AccountHistoryRecordWidgetVM(context)

        rule.runOnMainSynchronously {
            rule.view.subscribe(LCOwner(), vm)
            vm.pushModel(
                AccountHistoryRecord.Transfer("233",
                    "PL234",
                    "PL234234", "Artur Duch",
                    "PL5555", "Monika M",
                    Money("5343.54".toBigDecimal(), CurrencyType.CHF),
                    Money("131.59".toBigDecimal(), CurrencyType.CHF),
                    DateTime.now()
                )
            )
        }

        onView(withId(R.id.account_history_record_image)).check(matches(withDrawable(R.drawable.ic_swap_horiz_black_24dp)))
        onView(withId(R.id.account_history_record_desc)).check(matches(withText("Artur Duch")))
        onView(withId(R.id.account_history_record_desc_details)).check(matches(withText("Money transfer")))
        onView(withId(R.id.account_history_record_amount)).check(matches(withText("131.59 CHF")))

        rule.runOnMainSynchronously {
            val testOwner = rule.view.unsubscribe()
        }
    }


    @Test
    fun viewAccountHistoryRecordWidget_Withdraw() {
        val vm = AccountHistoryRecordWidgetVM(context)

        rule.runOnMainSynchronously {
            rule.view.subscribe(LCOwner(), vm)
            vm.pushModel(
                AccountHistoryRecord.Withdraw("233", "CARD1",
                    "PL234",
                    "PL234234",
                    Money("5343.54".toBigDecimal(), CurrencyType.THB),
                    Money("21.29".toBigDecimal(), CurrencyType.THB),
                    DateTime.now()
                )
            )
        }

        onView(withId(R.id.account_history_record_image)).check(matches(withDrawable(R.drawable.ic_account_balance_wallet_black_24dp)))
        onView(withId(R.id.account_history_record_desc)).check(matches(withText("ING BANK SLASKI")))
        onView(withId(R.id.account_history_record_desc_details)).check(matches(withText("Money withdraw")))
        onView(withId(R.id.account_history_record_amount)).check(matches(withText("21.29 THB")))

        rule.runOnMainSynchronously {
            val testOwner = rule.view.unsubscribe()
        }
    }
}