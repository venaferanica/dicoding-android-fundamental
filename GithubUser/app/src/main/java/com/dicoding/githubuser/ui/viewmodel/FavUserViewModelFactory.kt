package com.dicoding.githubuser.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNREACHABLE_CODE")
class FavUserViewModelFactory private constructor(private val mApplication: Application) : ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var INSTANCE: FavUserViewModelFactory? = null

        @JvmStatic
        fun getInstance(application: Application): FavUserViewModelFactory {
            if (INSTANCE == null) {
                synchronized(FavUserViewModelFactory::class.java) {
                    INSTANCE = FavUserViewModelFactory(application)
                }
            }
            return INSTANCE as FavUserViewModelFactory
        }
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FavUserViewModel(mApplication) as T
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}

