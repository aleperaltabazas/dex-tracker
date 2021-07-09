package com.github.aleperaltabazas.dex.service

import com.github.aleperaltabazas.dex.model.Subscription
import com.github.aleperaltabazas.dex.storage.Collection
import com.github.aleperaltabazas.dex.storage.Storage
import com.github.aleperaltabazas.dex.utils.IdGenerator

class SubscriptionService(
    private val storage: Storage,
    private val idGenerator: IdGenerator,
) {
    fun subscribe(
        dexId: String,
        userId: String,
        subscriberUserId: String,
    ): Subscription {
        val id = idGenerator.subscriptionId()
        val subscription = Subscription(
            subscriptionId = id,
            userId = userId,
            dexId = dexId,
            subscriberUserId = subscriberUserId,
        )

        storage.insert(Collection.SUBSCRIPTIONS, subscription)

        return subscription
    }
}