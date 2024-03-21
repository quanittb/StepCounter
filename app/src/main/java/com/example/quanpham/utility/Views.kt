package com.example.quanpham.utility

import android.view.View

fun View.isVisible(): Boolean {
    return this.visibility == View.VISIBLE
}

fun View.makeVisible() {
    this.visibility = View.VISIBLE
}

fun View.isGone(): Boolean {
    return this.visibility == View.GONE
}

fun View.makeGone() {
    this.visibility = View.GONE
}

fun View.isInvisible(): Boolean {
    return this.visibility == View.INVISIBLE
}

fun View.makeInvisible() {
    this.visibility = View.INVISIBLE
}