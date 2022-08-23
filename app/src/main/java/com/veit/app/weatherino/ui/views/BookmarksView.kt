package com.veit.app.weatherino.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.veit.app.weatherino.databinding.BookmarksViewBinding

class BookmarksView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    val binding = BookmarksViewBinding.inflate(LayoutInflater.from(context), this, true).apply {
        with(recyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter
        }
    }
}