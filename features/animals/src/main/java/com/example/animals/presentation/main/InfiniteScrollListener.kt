package com.example.animals.presentation.main

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class InfiniteScrollListener(
    private val layoutManager: GridLayoutManager,
    private val pageSize: Int
) : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

        if (!isLoading() && !isLastPage()) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                && firstVisibleItemPosition >= 0
                && totalItemCount >= pageSize
            ) {
                loadMoreItems()
            }
        }
    }

    abstract fun loadMoreItems()

    abstract fun isLastPage(): Boolean

    abstract fun isLoading(): Boolean
}
