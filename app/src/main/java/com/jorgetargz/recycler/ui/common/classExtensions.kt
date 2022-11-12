package com.jorgetargz.recycler.ui.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import coil.load
import coil.size.Scale
import com.jorgetargz.recycler.R

fun ViewGroup.inflate(layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

fun ImageView.loadUrl(url: String) {
    this.load(url) {
        scale(Scale.FILL)
        crossfade(true)
        placeholder(R.drawable.arrows_rotate_solid)
        error(R.drawable.ic_launcher_foreground)
    }
}