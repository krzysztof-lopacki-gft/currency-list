package com.crypto.recruitmenttest.common.ui.mvi

import com.crypto.recruitmenttest.common.domain.coroutines.toStateFlow
import com.crypto.recruitmenttest.common.domain.model.ConsumableEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map

fun <T : ViewState> Flow<T>.toViewStates(initialState: T, scope: CoroutineScope): StateFlow<T> = this
    .toStateFlow(
        initialValue = initialState,
        scope = scope
    )

fun <T : ViewEffect> Flow<T?>.toViewEffects(scope: CoroutineScope): StateFlow<ConsumableEvent<T>?> = this
    .map { item ->
        item?.let { ConsumableEvent(item) }
    }
    .toStateFlow(
        initialValue = null,
        scope = scope
    )

fun <T : NavigationEffect> Flow<T?>.toNavigationEffects(scope: CoroutineScope): StateFlow<ConsumableEvent<T>?> = this
    .map { item ->
        item?.let { ConsumableEvent(item) }
    }
    .toStateFlow(
        initialValue = null,
        scope = scope
    )
