package pl.duch.dybuk87.banksample.components.account.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.widget_home_account.view.*
import pl.duch.dybuk87.banksample.R
import pl.duch.dybuk87.banksample.components.Widget

class HomeAccountWidget @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr), Widget<HomeAccountWidgetVM> {
    override var vm: HomeAccountWidgetVM? = null
    private val observer = Observer<HomeAccountWidgetData> { setHomeAccountData(it) }

    init {
        LayoutInflater.from(context).inflate(R.layout.widget_home_account, this, true)
    }


    private fun setHomeAccountData(data: HomeAccountWidgetData) {

    }

    override fun subscribe(owner: LifecycleOwner, vm: HomeAccountWidgetVM) {
        this.vm = vm
        vm.data.observe(owner, observer)

        account_balance_widget.subscribe(owner, vm.accountBalanceWidget)
        account_balance_action_1.subscribe(owner, vm.actions[0])
        account_balance_action_2.subscribe(owner, vm.actions[1])
        account_balance_action_3.subscribe(owner, vm.actions[2])
    }

    override fun unsubscribe() {
        account_balance_widget.unsubscribe()
        account_balance_action_1.unsubscribe()
        account_balance_action_2.unsubscribe()
        account_balance_action_3.unsubscribe()

        vm?.data?.removeObserver(observer)
        vm = null
    }
}