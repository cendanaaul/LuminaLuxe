package com.cencen.luminaluxe.ui.screen.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cencen.luminaluxe.data.SkincareRepository
import com.cencen.luminaluxe.model.OrderSkincare
import com.cencen.luminaluxe.model.Skincare
import com.cencen.luminaluxe.ui.commonstate.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailSkincareViewModel(private val rep: SkincareRepository): ViewModel() {
    private val _uistate: MutableStateFlow<UiState<OrderSkincare>> = MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<OrderSkincare>>
        get() = _uistate

    fun getSkincareById(skincareId: Long) {
        viewModelScope.launch {
            _uistate.value = UiState.Loading
            _uistate.value = UiState.Success(rep.getOrderSkincareById(skincareId))
        }
    }

    fun addSkincareToCart(skincare: Skincare, count: Int) {
        viewModelScope.launch {
            rep.updateOrderSkincare(skincare.id, count)
        }
    }
}