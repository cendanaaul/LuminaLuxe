package com.cencen.luminaluxe.ui.screen.main

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cencen.luminaluxe.data.SkincareRepository
import com.cencen.luminaluxe.model.OrderSkincare
import com.cencen.luminaluxe.ui.commonstate.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class MainViewModel(private val rep: SkincareRepository): ViewModel() {
    private val _uiState: MutableStateFlow<UiState<List<OrderSkincare>>> = MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<List<OrderSkincare>>>
        get() = _uiState

    fun getAllSkincare() {
        viewModelScope.launch {
            rep.getAllSkincare()
                .catch {
                    _uiState.value = UiState.Error(it.message.toString())
                }
                .collect { orderSkincare ->
                    _uiState.value = UiState.Success(orderSkincare)
                }
        }
    }

    private val _query = mutableStateOf("")
    val query: State<String> get() = _query

    fun searchSkincare(query: String) {
        viewModelScope.launch {
            _query.value = query

            try {
                val searchResult = rep.searchSkincare(_query.value)
                _uiState.value = UiState.Success(searchResult)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message.toString())
            }
        }
    }
}