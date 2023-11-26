package com.cencen.luminaluxe.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cencen.luminaluxe.data.SkincareRepository
import com.cencen.luminaluxe.ui.screen.cart.CartViewModel
import com.cencen.luminaluxe.ui.screen.details.DetailSkincareViewModel
import com.cencen.luminaluxe.ui.screen.main.MainViewModel

class ViewModelFactory(private val rep: SkincareRepository): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(rep) as T
        } else if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
            return CartViewModel(rep) as T
        } else if (modelClass.isAssignableFrom(DetailSkincareViewModel::class.java)) {
            return DetailSkincareViewModel(rep) as T
        }
        throw IllegalArgumentException("Unknow VM Class : " + modelClass.name)
    }
}