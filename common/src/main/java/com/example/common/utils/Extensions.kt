package com.example.common.utils

import android.content.Context
import android.graphics.Paint
import android.graphics.Rect
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.common.R
import com.example.logging.domain.Logger
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun ImageView.setImage(url: String) {
    Glide.with(this.context)
        .load(url.ifEmpty { null })
        .error(R.drawable.dog_placeholder)
        .centerCrop()
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}

inline fun CoroutineScope.createExceptionHandler(
    message: String,
    crossinline action: (throwable: Throwable) -> Unit
) = CoroutineExceptionHandler { _, throwable ->
    Logger.e(throwable, message)
    throwable.printStackTrace()
    launch {
        action(throwable)
    }
}

fun Context.dpToPx(dp: Float) = this.getResources().getDisplayMetrics().density * dp

fun Paint.getTextWidth(string: String): Float {
    val rect = Rect()
    this.getTextBounds(string, 0, string.length, rect)
    return rect.width().toFloat()
}

const val CHECK_EMOJI = 0x2714
const val CROSS_EMOJI = 0x274C
const val QUESTION_EMOJI = 0x2753

/**
 * Equivalent to toEnglish() but returns emoji unicode instead
 */
fun Boolean?.toEmoji() = if (this != null) {
    String(Character.toChars(if (this) CHECK_EMOJI else CROSS_EMOJI))
} else {
    String(Character.toChars(QUESTION_EMOJI))
}