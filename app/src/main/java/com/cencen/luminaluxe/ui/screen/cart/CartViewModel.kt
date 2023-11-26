package com.cencen.luminaluxe.ui.screen.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cencen.luminaluxe.data.SkincareRepository
import com.cencen.luminaluxe.ui.commonstate.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CartViewModel(private  val rep: SkincareRepository): ViewModel() {
    private val _uiState: MutableStateFlow<UiState<CartState>> = MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<CartState>>
        get() = _uiState

    fun getAddedOrderSkincare() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            rep.getAddedOrderSkincare()
                .collect { orderSkincare ->
                    val totalPrice = orderSkincare.sumOf { it.skincare.price * it.count }
                    _uiState.value = UiState.Success(CartState(orderSkincare, totalPrice))
                }
        }
    }

    fun updateOrderSkincare(skincareId: Long, count: Int) {
        viewModelScope.launch {
            rep.updateOrderSkincare(skincareId, count)
                .collect { isUpdate ->
                    if (isUpdate) getAddedOrderSkincare()
                }
        }
    }
}