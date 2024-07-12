package com.crypto.recruitmenttest.common.ui.fragment

import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment

fun Fragment.handleBackButton(callback: () -> Unit) {
    requireActivity().onBackPressedDispatcher
        .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() = callback()
        })
}
