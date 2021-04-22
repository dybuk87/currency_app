package pl.duch.dybuk87.banksample.main.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.vh_home_account_history.view.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pl.duch.dybuk87.banksample.R
import pl.duch.dybuk87.banksample.components.DataBoundViewHolder
import pl.duch.dybuk87.banksample.components.account.history.AccountHistoryRecordWidgetVM
import pl.duch.dybuk87.core.kernel.RemoteData
import pl.duch.dybuk87.core.kernel.RemoteDataState
import pl.duch.dybuk87.core.repository.account.history.AccountHistoryRecord
import java.time.LocalDate

abstract class HomeAccountHistoryVH(context: Context, view: View) : DataBoundViewHolder(view) {
    abstract fun pushModel(historyRecord: AccountHistoryRecord)
}

class HomeAccountHistoryVHItem(context: Context, view: View) : HomeAccountHistoryVH(context, view) {
    private val widget = view.account_history_widget
    private val vm = AccountHistoryRecordWidgetVM(context)

    init {
        widget.subscribe(this, vm)
    }

    override fun pushModel(historyRecord: AccountHistoryRecord) {
        vm.pushModel(historyRecord)
    }
}

class HomeAccountHistoryVHLoader(context: Context, view: View) : HomeAccountHistoryVH(context, view) {
    override fun pushModel(historyRecord: AccountHistoryRecord) {

    }
}

class HomeAccountHistoryVHEmpty(context: Context, view: View) : HomeAccountHistoryVH(context, view) {
    override fun pushModel(historyRecord: AccountHistoryRecord) {

    }
}

class AccountHistoryDiffUtil(
    private val oldList: MutableList<AccountHistoryRecord>,
    private val oldListLoading : Boolean,

    private val newList: MutableList<AccountHistoryRecord>,
    private val newListLoading: Boolean
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        if (oldItemPosition < oldList.size && newItemPosition < newList.size) {
            oldList[oldItemPosition].id == newList[newItemPosition].id
        } else oldItemPosition >= oldList.size && newItemPosition >= newList.size && oldListLoading == newListLoading

    override fun getOldListSize(): Int = when(oldListLoading) {
        false -> if (oldList.size > 0) oldList.size else 1
        true -> if (oldList.size == 0) 10 else oldList.size + 3
    }

    override fun getNewListSize(): Int = when(newListLoading) {
        false -> if (newList.size > 0) newList.size else 1
        true -> if (newList.size == 0) 10 else newList.size + 3
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        if (oldItemPosition < oldList.size && newItemPosition < newList.size) {
            oldList[oldItemPosition].id == newList[newItemPosition].id
        } else oldItemPosition >= oldList.size && newItemPosition >= newList.size && oldListLoading == newListLoading

}


class HomeAccountHistoryAdapter(val context: Context) : RecyclerView.Adapter<HomeAccountHistoryVH>() {

    companion object {
        const val ITEM = 1
        const val LOADER = 2
        const val EMPTY = 3
    }

    private var loading : Boolean = false
    val data: MutableList<AccountHistoryRecord> = mutableListOf()
    private var job: Job? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeAccountHistoryVH {
        val vh = when (viewType) {
            LOADER -> HomeAccountHistoryVHLoader(
                context,
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.vh_home_account_history_loader, parent, false)
            )

            EMPTY -> HomeAccountHistoryVHEmpty(
                context,
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.vh_home_account_history_empty, parent, false)
            )

            else -> HomeAccountHistoryVHItem(
                context,
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.vh_home_account_history, parent, false)
            )
        }
        vh.markCreated()
        return vh
    }

    override fun getItemViewType(position: Int): Int =
        when {
            position < data.size -> {
                ITEM
            }
            loading -> {
                LOADER
            }
            else -> {
                EMPTY
            }
        }

    override fun getItemCount(): Int = when(loading) {
        false -> if (data.size > 0) data.size else 1
        true -> if (data.size == 0) 10 else data.size + 3
    }

    override fun onBindViewHolder(holder: HomeAccountHistoryVH, position: Int) {
        if (position < data.size) {
            holder.pushModel(data[position])
        }
    }

    override fun onViewAttachedToWindow(holder: HomeAccountHistoryVH) {
        super.onViewAttachedToWindow(holder)
        holder.markAttach()
    }

    override fun onViewDetachedFromWindow(holder: HomeAccountHistoryVH) {
        super.onViewDetachedFromWindow(holder)
        holder.markDetach()
    }

    fun subscribe(owner: LifecycleOwner, history: StateFlow<RemoteData<List<AccountHistoryRecord>>>) {
        job?.cancel()

        job = owner.lifecycleScope.launch {
            history.collect {
                update(it)
            }
        }
    }

    private fun update(data: RemoteData<List<AccountHistoryRecord>>) {
        val prevLoading = loading
        loading = data.state == RemoteDataState.Loading || data.state == RemoteDataState.NotReady

        val oldList = ArrayList<AccountHistoryRecord>(this.data)

        this.data.clear()
        this.data.addAll(data.data)

        val result= DiffUtil.calculateDiff(
            AccountHistoryDiffUtil(oldList, prevLoading, this.data, loading), true)

        result.dispatchUpdatesTo(this)

    }
}