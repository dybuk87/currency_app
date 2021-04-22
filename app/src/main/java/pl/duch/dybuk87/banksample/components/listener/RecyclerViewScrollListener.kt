package pl.duch.dybuk87.banksample.components.listener

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

open class RecyclerViewScrollListener(val layoutManager: LinearLayoutManager) :  RecyclerView.OnScrollListener() {
    var loading = false

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        if (loading) {
            return
        }
        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val pastVisiblesItems = layoutManager.findFirstVisibleItemPosition()
        if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
            nextPage()
        } else {
            onScrolling(totalItemCount - (visibleItemCount + pastVisiblesItems))
        }


        recyclerView?.layoutManager?.let {
            if (it is LinearLayoutManager) {
                var first = it.findFirstVisibleItemPosition()
                var last = it.findLastVisibleItemPosition()

                safeNotifyVisible(first, last)
            }
        }
    }

    open fun safeNotifyVisible(first: Int, last: Int) {

    }

    open fun onScrolling(scroll: Int) {

    }

    open fun nextPage(){}
}