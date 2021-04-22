package pl.duch.dybuk87.banksample.components.account.action
import android.content.Context
import android.view.ViewGroup
import androidx.core.content.ContextCompat
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

@RunWith(AndroidJUnit4::class)
class AccountActionButtonWidgetTest {

    @Rule
    @JvmField
    var mockkitoRule = InstMockkitoRule()

    @Rule
    @JvmField
    var rule : ViewTestRule<AccountActionButtonWidget> =
        ViewTestRule<AccountActionButtonWidget> { context, _ ->
            AccountActionButtonWidget (
                context
            ).also {
                it.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)
            }
        }


    lateinit var context : Context

    @Before
    fun init() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun viewAccountActionButton() {
        val vm = AccountActionButtonWidgetVM()

        rule.runOnMainSynchronously {
            rule.view.subscribe(LCOwner(), vm)
            vm.pushModel(R.drawable.ic_money_request)
        }

        rule.runOnMainSynchronously {
            val testOwner = rule.view.unsubscribe()
        }


    }

}