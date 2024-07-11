package com.crypto.recruitmenttest.common.ui.mvi

interface ViewState {
    companion object {
        val Empty = object : ViewState {}
    }
}

interface ViewEffect
interface NavigationEffect
interface ViewEvent
