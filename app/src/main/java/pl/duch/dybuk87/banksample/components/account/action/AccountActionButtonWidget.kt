package pl.duch.dybuk87.banksample.components.account.action

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.widget_account_action_button.view.*
import pl.duch.dybuk87.banksample.R
import pl.duch.dybuk87.banksample.components.Widget

class AccountActionButtonWidget @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr), Widget<AccountActionButtonWidgetVM> {
    override var vm: AccountActionButtonWidgetVM? = null

    private val observer = Observer<AccountActionButtonData> { setAccountButtonData(it) }

    init {
        LayoutInflater.from(context).inflate(R.layout.widget_account_action_button, this, true)
    }

    override fun subscribe(owner: LifecycleOwner, vm: AccountActionButtonWidgetVM) {
        this.vm = vm
        vm.data.observe(owner, observer)
    }

    private fun setAccountButtonData(data: AccountActionButtonData) {
        if (data.currencyIcon == R.drawable.blik) {
            widget_account_action_button_blik.visibility = View.VISIBLE
            widget_account_action_button_image.visibility = View.GONE
        } else {
            widget_account_action_button_blik.visibility = View.GONE
            widget_account_action_button_image.visibility = View.VISIBLE
            widget_account_action_button_image.setImageResource(data.currencyIcon)
        }
        widget_account_action_button_desc.visibility = if (data.description.isNullOrEmpty()) View.GONE else View.VISIBLE
        widget_account_action_button_desc.text = data.description?:""
    }

    override fun unsubscribe() {
        vm?.data?.removeObserver(observer)
        this.vm = null
    }

}