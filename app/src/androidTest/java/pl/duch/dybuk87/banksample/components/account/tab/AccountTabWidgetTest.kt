package pl.duch.dybuk87.banksample.components.account.tab

import android.content.Context
import android.view.ViewGroup
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.novoda.espresso.ViewTestRule
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

@RunWith(AndroidJUnit4::class)
class AccountTabWidgetTest {

    @Rule
    @JvmField
    var mockkitoRule = InstMockkitoRule()

    @Rule
    @JvmField
    var rule : ViewTestRule<AccountTabWidget> =
        ViewTestRule<AccountTabWidget> { context, _ ->
            AccountTabWidget(
                context
            ).also {
                it.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)
            }
        }


    lateinit var context : Context

    @Before
    fun init() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun viewAccountTab() {
        val vm =
            AccountTabWidgetVM(
                context
            )

        rule.runOnMainSynchronously {
            rule.view.subscribe(LCOwner(), vm)

            vm.pushModel(Money("765.78".toBigDecimal(), CurrencyType.EUR))
        }

        rule.runOnMainSynchronously {
            val testOwner = rule.view.unsubscribe()
        }

        Espresso.onView(ViewMatchers.withId(R.id.widget_account_tab_flag))
            .check(ViewAssertions.matches(withDrawable(R.drawable.ic_flag_eu)))

        Espresso.onView(ViewMatchers.withId(R.id.widget_account_tab_amount))
            .check(ViewAssertions.matches(ViewMatchers.withText("765.78 EUR")))
    }

}