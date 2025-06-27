package com.example.petadopt.animals.utils

import android.widget.ImageView
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.petadopt.R
import com.example.petadopt.logging.Logger
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