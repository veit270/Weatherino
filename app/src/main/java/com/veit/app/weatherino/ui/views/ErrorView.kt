package com.veit.app.weatherino.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.databinding.BindingAdapter
import com.veit.app.weatherino.databinding.ErrorViewBinding

class ErrorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private val binding = ErrorViewBinding.inflate(LayoutInflater.from(context), this, true)

    fun setupErrorView(text: String, tryAgainListener: OnClickListener) {
        binding.text = text
        binding.tryAgainListener = tryAgainListener
    }
}

@BindingAdapter(value = ["text", "android:onClick"], requireAll = true)
fun ErrorView.bindErrorView(text: String, onClick: View.OnClickListener) {
    setupErrorView(text, onClick)
}