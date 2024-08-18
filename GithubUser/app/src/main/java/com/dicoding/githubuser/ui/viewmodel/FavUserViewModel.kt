package com.dicoding.githubuser.ui.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubuser.database.FavUser
import com.dicoding.githubuser.repository.FavUserRepository

class FavUserViewModel(application: Application) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val mFavouriteUserRepository: FavUserRepository = FavUserRepository(application)
    fun getFavUser(): LiveData<List<FavUser>> = mFavouriteUserRepository.getFavUser()

    fun insert(favouriteUser: FavUser) {
        mFavouriteUserRepository.insert(favouriteUser)
    }

    fun delete(favouriteUser: FavUser) {
        mFavouriteUserRepository.delete(favouriteUser)
    }

    fun getByUsername(username : String) : LiveData<FavUser> = mFavouriteUserRepository.getByUsername(username)
}