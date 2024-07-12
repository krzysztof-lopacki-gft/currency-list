package com.crypto.recruitmenttest.common.domain.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * [INFO FOR REVIEWER]
 * The following class/methods are copied from libraries created by GFT and used in our projects.
 * GFT plans to release these libraries under an Open Source license soon.
 */

/**
 * Creates a `StateFlow` backed by the current `Flow`.
 * Although this extension seems similar to [stateIn], there are noticeable differences in the behavior of the returned [StateFlow].
 * The [StateFlow] returned from this method:
 * - Does not keep a subscription to the source [Flow] when there are no observers subscribed
 *   (like [stateIn] with `started = SharingStarted.WhileSubscribed()`).
 * - If [updateValueOnGet] is set to `true` it tries to provide the latest value from the source [Flow] whenever the `value` getter is called,
 *   even when there are no observers subscribed (unlike [stateIn]).
 * - Remembers the last dispatched or prompted value (unlike [stateIn], which always starts with `initialValue` on subsequent subscriptions).
 */
fun <T> Flow<T>.toStateFlow(
    initialValue: T,
    scope: CoroutineScope,
    updateValueOnGet: Boolean = false,
): StateFlow<T> = MutableStateFlowWithSource(
    initialValue = initialValue,
    source = this,
    scope = scope,
    updateValueOnGet = updateValueOnGet
)

private class MutableStateFlowWithSource<T>(
    initialValue: T,
    private val source: Flow<T>,
    private val scope: CoroutineScope,
    private val dispatcher: MutableStateFlow<T> = MutableStateFlow(initialValue),
    private val updateValueOnGet: Boolean,
) : MutableStateFlow<T> by dispatcher {
    private var subscription: Job? = null

    private val sharedSource by lazy {
        dispatcher
            .onSubscription {
                subscription = scope.launch(start = CoroutineStart.UNDISPATCHED) {
                    source.collect { item -> value = item }
                }
            }
            .onCompletion {
                val subscriptionToCancel = subscription
                subscription = null
                subscriptionToCancel?.cancel()

            }
            .shareIn(scope, SharingStarted.WhileSubscribed())
    }

    override var value: T
        get() {
            if (updateValueOnGet && subscription == null) {
                // We try to get the latest item from the source - some flows (e.g. StateFlows, buffered flows) are able to provide last item immediately.
                // This is very important during State creation (that is, when `collectAsStateWithLifecycle` is called).
                val latestValue = dispatcher.value
                var newValue = latestValue
                scope
                    .launch(start = CoroutineStart.UNDISPATCHED, context = Dispatchers.Unconfined) {
                        runCatching {
                            newValue = source.first()
                        }
                    }
                    .cancel()
                dispatcher.compareAndSet(latestValue, newValue)
            }

            return dispatcher.value
        }
        set(value) {
            dispatcher.value = value
        }

    override suspend fun collect(collector: FlowCollector<T>): Nothing = sharedSource.collect(collector)

    override val subscriptionCount: StateFlow<Int>
        get() = throw NotImplementedError("subscriptionCount is not supported!")
}
