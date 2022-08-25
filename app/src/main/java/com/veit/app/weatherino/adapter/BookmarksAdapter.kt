package com.veit.app.weatherino.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.veit.app.weatherino.data.BookmarkedWeatherInfo
import com.veit.app.weatherino.databinding.BookmarkViewHolderBinding

class BookmarksAdapter: ListAdapter<BookmarkedWeatherInfo, BookmarksAdapter.ViewHolder>(BookmarksCallback()) {
    lateinit var swipeAction: SwipeAction<BookmarkedWeatherInfo>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            BookmarkViewHolderBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: BookmarkViewHolderBinding
    ) : RecyclerView.ViewHolder(binding.root), Swipeable {
        fun bind(bookmarkedWeatherInfo: BookmarkedWeatherInfo) {
            with(binding) {
                binding.bookmarkedWeatherInfo = bookmarkedWeatherInfo
                executePendingBindings()
            }
        }

        override fun onSwiped() {
            bindingAdapterPosition.let {
                if (it != -1) {
                    swipeAction.onItemSwiped(getItem(it))
                }
            }
        }
    }
}

private class BookmarksCallback : DiffUtil.ItemCallback<BookmarkedWeatherInfo>() {
    override fun areItemsTheSame(oldItem: BookmarkedWeatherInfo, newItem: BookmarkedWeatherInfo): Boolean {
        return oldItem.bookmark.dateTsSeconds == newItem.bookmark.dateTsSeconds
    }

    override fun areContentsTheSame(oldItem: BookmarkedWeatherInfo, newItem: BookmarkedWeatherInfo): Boolean {
        return oldItem == newItem
    }
}