package com.crypto.recruitmenttest.common.domain.model

class ConsumableEvent<T> (private val payload: T) {
    /**
     * States whether the event has been consumed already.
     */
    var isConsumed = false
        private set

    /**
     * Consumes the event if it has not been consumed yet.
     * @param consumer  Event consumer that will be invoked if the event has not been consumed yet.
     * @return          Boolean value of 'true' if the event has been provided to the consumer.
     *                  Boolean value of 'false' if the event has been consumed already and it was not provided to the consumer.
     */
    @Synchronized
    fun consume(consumer: (T) -> Unit): Boolean {
        if (isConsumed) return false
        isConsumed = true
        consumer(payload)
        return true
    }
}
