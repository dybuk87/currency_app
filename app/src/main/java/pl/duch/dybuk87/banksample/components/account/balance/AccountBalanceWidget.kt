package pl.duch.dybuk87.banksample.components.account.balance

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.widget_account_balance.view.*
import pl.duch.dybuk87.banksample.R
import pl.duch.dybuk87.banksample.components.Widget

class AccountBalanceWidget  @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr), Widget<AccountBalanceWidgetVM> {
    override var vm: AccountBalanceWidgetVM? = null
    private val observer = Observer<AccountBalanceData> { setAccountBalanceData(it) }

    init {
        LayoutInflater.from(context).inflate(R.layout.widget_account_balance, this, true)
    }


    private fun setAccountBalanceData(data: AccountBalanceData) {
        widget_account_balance_background.setImageResource(data.currencyIcon)
        widget_account_balance_amount.text = data.description
    }

    override fun subscribe(owner: LifecycleOwner, vm: AccountBalanceWidgetVM) {
        this.vm = vm
        vm.data.observe(owner, observer)
    }

    override fun unsubscribe() {
        vm?.data?.removeObserver(observer)
        vm = null
    }
}