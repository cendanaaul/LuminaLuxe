package com.cencen.luminaluxe.ui.screen.cart

import com.cencen.luminaluxe.model.OrderSkincare

data class CartState(
    val orderSkincare: List<OrderSkincare>,
    val totalPrice: Int
)