package com.crypto.recruitmenttest.common.ui.mvi

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch

/**
 * [INFO FOR REVIEWER]
 * The following class/methods are copied from libraries created by GFT and used in our projects.
 * GFT plans to release these libraries under an Open Source license soon.
 */

fun <NE : NavigationEffect> Fragment.handleNavigationEffects(
    viewModel: MviViewModel<*, *, NE, *>,
    consumer: (NE) -> Unit
) = handleNavigationEffects(viewModel, Lifecycle.State.STARTED, consumer)

fun <NE : NavigationEffect> Fragment.handleNavigationEffects(
    viewModel: MviViewModel<*, *, NE, *>,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    consumer: (NE) -> Unit
) {
    viewLifecycleOwner.lifecycleScope.launch {
        repeatOnLifecycle(minActiveState) {
            viewModel.navigationEffects.collect { event ->
                event?.consume { effect ->
                    consumer(effect)
                }
            }
        }
    }
}

fun <VE : ViewEffect> Fragment.handleViewEffects(
    viewModel: MviViewModel<*, *, *, VE>,
    consumer: (VE) -> Unit
) = handleViewEffects(viewModel, Lifecycle.State.STARTED, consumer)

fun <VE : ViewEffect> Fragment.handleViewEffects(
    viewModel: MviViewModel<*, *, *, VE>,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    consumer: (VE) -> Unit
) {
    viewLifecycleOwner.lifecycleScope.launch {
        repeatOnLifecycle(minActiveState) {
            viewModel.viewEffects.collect { event ->
                event?.consume { effect ->
                    consumer(effect)
                }
            }
        }
    }
}

fun <VS : ViewState> Fragment.observeViewState(
    viewModel: MviViewModel<VS, *, *, *>,
    consumer: (VS) -> Unit
) = observeViewState(viewModel, Lifecycle.State.STARTED, consumer)

fun <VS : ViewState> Fragment.observeViewState(
    viewModel: MviViewModel<VS, *, *, *>,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    consumer: (VS) -> Unit
) {
    viewLifecycleOwner.lifecycleScope.launch {
        repeatOnLifecycle(minActiveState) {
            viewModel.viewStates.collect { state ->
                consumer(state)
            }
        }
    }
}
