package pl.duch.dybuk87.banksample.components.account.history

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.widget_account_history_record.view.*
import pl.duch.dybuk87.banksample.R
import pl.duch.dybuk87.banksample.components.Widget

class AccountHistoryRecordWidget @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr), Widget<AccountHistoryRecordWidgetVM> {

    init {
        LayoutInflater.from(context).inflate(R.layout.widget_account_history_record, this, true)
        setOnClickListener { vm?.clicked() }
    }

    override var vm: AccountHistoryRecordWidgetVM? = null
    private val observerData = Observer<AccountHistoryRecordVD> { setHistoryRecord(it) }


    private fun setHistoryRecord(data: AccountHistoryRecordVD) {
        account_history_record_image.setImageResource(data.image)
        if (data.image == R.drawable.blik) {
            account_history_record_image.colorFilter = null
        } else {
            account_history_record_image.setColorFilter(
                ContextCompat.getColor(context, R.color.content_text), android.graphics.PorterDuff.Mode.SRC_IN)
        }
        account_history_record_desc.text = data.description
        account_history_record_desc_details.text = data.descriptionDetails
        account_history_record_amount.text = data.amount
    }

    override fun subscribe(owner: LifecycleOwner, vm: AccountHistoryRecordWidgetVM) {
        this.vm = vm
        vm.data.observe(owner, observerData)
    }

    override fun unsubscribe() {
        vm?.data?.removeObserver(observerData)
        vm = null
    }

}