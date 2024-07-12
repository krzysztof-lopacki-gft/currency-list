package com.crypto.recruitmenttest.common.ui.mvi

/**
 * [INFO FOR REVIEWER]
 * The following class/methods are copied from libraries created by GFT and used in our projects.
 * GFT plans to release these libraries under an Open Source license soon.
 */

interface ViewState {
    companion object {
        val Empty = object : ViewState {}
    }
}

interface ViewEffect
interface NavigationEffect
interface ViewEvent
