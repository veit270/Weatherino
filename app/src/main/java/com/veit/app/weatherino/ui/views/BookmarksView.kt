package com.veit.app.weatherino.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.veit.app.weatherino.adapter.BookmarksAdapter
import com.veit.app.weatherino.adapter.DeleteOnSwipeHelper
import com.veit.app.weatherino.adapter.SwipeAction
import com.veit.app.weatherino.data.BookmarkedWeatherInfo
import com.veit.app.weatherino.databinding.BookmarksViewBinding
import com.veit.app.weatherino.utils.Resource
import com.veit.app.weatherino.utils.Status

class BookmarksView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private val adapter = BookmarksAdapter()

    private val binding = BookmarksViewBinding.inflate(LayoutInflater.from(context), this, true).apply {
        with(recyclerView) {
            layoutManager = LinearLayoutManager(context)
            DeleteOnSwipeHelper().attachToRecyclerView(this)
            adapter = this@BookmarksView.adapter
        }
    }

    fun setBookmarksResource(bookmarksResource: Resource<List<BookmarkedWeatherInfo>>) {
        when(bookmarksResource.status) {
            Status.SUCCESS -> {
                bookmarksResource.data!!.let { data ->
                    adapter.submitList(data)
                    binding.empty = data.isEmpty()
                    binding.loading = false
                }
            }
            Status.ERROR -> {
                adapter.submitList(emptyList())
                binding.empty = false
                binding.loading = false
            }
            Status.LOADING -> {
                adapter.submitList(emptyList())
                binding.empty = false
                binding.loading = true
            }
        }
    }

    fun setSwipeAction(swipeAction: SwipeAction<BookmarkedWeatherInfo>) {
        adapter.swipeAction = swipeAction
    }
}

@BindingAdapter("bookmarksResource")
fun BookmarksView.bindBookmarksResource(bookmarksResource: Resource<List<BookmarkedWeatherInfo>>) {
    setBookmarksResource(bookmarksResource)
}

@BindingAdapter("swipeAction")
fun BookmarksView.bindSwipeAction(swipeAction: SwipeAction<BookmarkedWeatherInfo>) {
    setSwipeAction(swipeAction)
}