package com.cencen.luminaluxe.datainjection

import com.cencen.luminaluxe.data.SkincareRepository

object Injection {
    fun provideRepositories(): SkincareRepository {
        return SkincareRepository.getInstance()
    }
}