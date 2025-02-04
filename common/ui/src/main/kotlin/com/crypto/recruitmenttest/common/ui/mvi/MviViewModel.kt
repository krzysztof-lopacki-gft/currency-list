package com.crypto.recruitmenttest.common.ui.mvi

import com.crypto.recruitmenttest.common.domain.model.ConsumableEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * [INFO FOR REVIEWER]
 * The following class/methods are copied from libraries created by GFT and used in our projects.
 * GFT plans to release these libraries under an Open Source license soon.
 */

interface MviViewModel<VS : ViewState, EV : ViewEvent, NE : NavigationEffect, VE : ViewEffect> {
    val viewStates: StateFlow<VS>
    val viewEffects: StateFlow<ConsumableEvent<VE>?>
    val navigationEffects: StateFlow<ConsumableEvent<NE>?>
    fun onEvent(event: EV)

    fun MviViewModel<VS, EV, NE, VE>.dispatchNavigationEffect(effect: NE) {
        if (navigationEffects is MutableStateFlow) (navigationEffects as MutableStateFlow).value = ConsumableEvent(effect)
        else throw TypeCastException("MviViewModel.dispatchNavigationEffect extension supports MutableStateFlow only!")
    }

    fun MviViewModel<VS, EV, NE, VE>.clearNavigationEffect() {
        if (navigationEffects is MutableStateFlow) (navigationEffects as MutableStateFlow).value = null
        else throw TypeCastException("MviViewModel.clearNavigationEffect extension supports MutableStateFlow only!")
    }

    fun MviViewModel<VS, EV, NE, VE>.dispatchViewEffect(effect: VE) {
        if (viewEffects is MutableStateFlow) (viewEffects as MutableStateFlow).value = ConsumableEvent(effect)
        else throw TypeCastException("MviViewModel.dispatchViewEffect extension supports MutableStateFlow only!")
    }

    fun MviViewModel<VS, EV, NE, VE>.clearViewEffect(effect: VE) {
        if (viewEffects is MutableStateFlow) (viewEffects as MutableStateFlow).value = null
        else throw TypeCastException("MviViewModel.clearViewEffect extension supports MutableStateFlow only!")
    }

    var MviViewModel<VS, EV, NE, VE>.viewState: VS
        get() {
            return viewStates.value
        }
        set(value) {
            if (viewStates is MutableStateFlow) (viewStates as MutableStateFlow).value = value
            else throw TypeCastException("MviViewModel.viewState extension supports MutableStateFlow only!")
        }
}

inline val <VS : ViewState> MviViewModel<VS, *, *, *>.viewState: VS
    get() = viewStates.value
