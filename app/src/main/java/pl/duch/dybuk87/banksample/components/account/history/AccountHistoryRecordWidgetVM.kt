package pl.duch.dybuk87.banksample.components.account.history

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import pl.duch.dybuk87.banksample.R
import pl.duch.dybuk87.core.repository.account.history.AccountHistoryRecord

data class AccountHistoryRecordVD(
    @DrawableRes val image: Int,
    val description: String,
    val descriptionDetails: String,
    val amount: String
)

class AccountHistoryRecordWidgetVM(context: Context) {

    private val cardPaymentDescDetails = context.getString(R.string.card_payment_desc_details)
    private val moneyExchangeDescDetails = context.getString(R.string.money_exchange_desc_details)
    private val blikPaymentDescDetails = context.getString(R.string.blik_payment_desc_details)
    private val moneyTransferDescDetails = context.getString(R.string.money_transfer_desc_details)
    private val moneyWithdrawDescDetails = context.getString(R.string.money_withdraw_desc_details)

    fun clicked() {}

    private var model: AccountHistoryRecord? = null

    private val _data = MutableLiveData<AccountHistoryRecordVD>()
    val data : LiveData<AccountHistoryRecordVD> = _data

    fun pushModel(model : AccountHistoryRecord) {
        this.model = model

        val viewData =
            when(model) {
                is AccountHistoryRecord.CardPayment ->
                    AccountHistoryRecordVD(R.drawable.ic_credit_card_black_36dp, model.title, cardPaymentDescDetails, model.amount.format())
                is AccountHistoryRecord.Exchange ->
                    AccountHistoryRecordVD(R.drawable.ic_trending_up_black_24dp, moneyExchangeDescDetails, "", model.amount.format())
                is AccountHistoryRecord.PAYU ->
                    AccountHistoryRecordVD(R.drawable.blik, model.title,blikPaymentDescDetails, model.amount.format())
                is AccountHistoryRecord.Transfer ->
                    AccountHistoryRecordVD(R.drawable.ic_swap_horiz_black_24dp, model.title,moneyTransferDescDetails, model.amount.format())
                is AccountHistoryRecord.Withdraw ->
                    AccountHistoryRecordVD(R.drawable.ic_account_balance_wallet_black_24dp, model.title,moneyWithdrawDescDetails, model.amount.format())
            }

        _data.value = viewData

    }
}