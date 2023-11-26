package com.cencen.luminaluxe.data

import com.cencen.luminaluxe.model.FakeSkincareDataResource
import com.cencen.luminaluxe.model.OrderSkincare
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map


class SkincareRepository {
    private val orderSkincares = mutableListOf<OrderSkincare>()

    init {
        if (orderSkincares.isEmpty()) {
            FakeSkincareDataResource.dummySkincare.forEach {
                orderSkincares.add(OrderSkincare(it,0))
            }
        }
    }

    fun getAllSkincare(): Flow<List<OrderSkincare>> {
        return flowOf(orderSkincares)
    }

    fun getOrderSkincareById(skincareId: Long): OrderSkincare {
        return orderSkincares.first {
            it.skincare.id == skincareId
        }
    }

    fun updateOrderSkincare(skincareId: Long, newCount: Int): Flow<Boolean> {
        val index = orderSkincares.indexOfFirst { it.skincare.id == skincareId }
        val result = if (index >= 0) {
            val orderSkincare = orderSkincares[index]
            orderSkincares[index] = orderSkincare.copy(skincare = orderSkincare.skincare, count = newCount)
            true
        } else {
            false
        }
        return flowOf(result)
    }

    fun getAddedOrderSkincare(): Flow<List<OrderSkincare>> {
        return getAllSkincare()
            .map { orderSkincares ->
                orderSkincares.filter { orderSkincare ->
                    orderSkincare.count != 0
                }
            }
    }

    fun searchSkincare(query: String): List<OrderSkincare> {
        return FakeSkincareDataResource.dummySkincare
            .filter { it.title.contains(query, ignoreCase = true) }
            .map { OrderSkincare(it, 0) }
    }

    companion object {
        @Volatile
        private var instance: SkincareRepository? = null

        fun getInstance(): SkincareRepository =
            instance ?: synchronized(this) {
                SkincareRepository().apply { instance = this }
            }
    }
}