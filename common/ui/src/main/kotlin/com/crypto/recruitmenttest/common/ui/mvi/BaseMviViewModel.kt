package com.crypto.recruitmenttest.common.ui.mvi

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crypto.recruitmenttest.common.domain.model.ConsumableEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Optional

private const val VIEW_STATE_KEY = "MviViewMode.viewState"

/**
 * [INFO FOR REVIEWER]
 * The following class/methods are copied from libraries created by GFT and used in our projects.
 * GFT plans to release these libraries under an Open Source license soon.
 */
abstract class BaseMviViewModel<VS : ViewState, EV : ViewEvent, NE : NavigationEffect, VE : ViewEffect> private constructor(
    private val initialState: Optional<VS>,
    private val savedStateHandle: SavedStateHandle? = null,
) : ViewModel(), MviViewModel<VS, EV, NE, VE> {

    constructor(
        initialState: VS,
        savedStateHandle: SavedStateHandle? = null,
    ) : this(Optional.of(initialState), savedStateHandle)

    constructor() : this(Optional.empty(), null)

    override val viewStates: StateFlow<VS> by lazy {
        when {
            savedStateHandle != null -> savedStateHandle.getLiveData<VS>(VIEW_STATE_KEY).let { liveData ->
                MutableStateFlow(liveData.value ?: initialState.get()).apply {
                    viewModelScope.launch {
                        collectLatest { newValue -> liveData.value = newValue }
                    }
                }
            }

            initialState.isPresent -> MutableStateFlow(initialState.get())
            else -> throw IllegalArgumentException("You must either override 'val viewStates: StateFlow<VS>' or provide `initialState: VS` through constructor.")
        }
    }
    override val viewEffects: StateFlow<ConsumableEvent<VE>?> = MutableStateFlow<ConsumableEvent<VE>?>(null)
    override val navigationEffects: StateFlow<ConsumableEvent<NE>?> = MutableStateFlow<ConsumableEvent<NE>?>(null)
}
