package com.manogarrafa.organizze.utils

import com.heinrichreimersoftware.materialintro.slide.FragmentSlide

class CustomSlider {
    fun create(fragment: Int, first: Boolean = false, last: Boolean = false): FragmentSlide =
        FragmentSlide.Builder().background(android.R.color.white).canGoBackward(!first)
            .canGoForward(!last)
            .fragment(fragment).build()
}