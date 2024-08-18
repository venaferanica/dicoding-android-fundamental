package com.dicoding.githubuser.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubuser.data.response.FollowerResponseItem
import com.dicoding.githubuser.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersViewModel : ViewModel() {

    private val _followers = MutableLiveData<ArrayList<FollowerResponseItem>>()
    val followers: LiveData<ArrayList<FollowerResponseItem>> = _followers

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getFollowers(username: String) {
        try {
            _isLoading.value = true
            val client = ApiConfig.getApiService().followers(username)
            client.enqueue(object : Callback<ArrayList<FollowerResponseItem>> {
                override fun onResponse(
                    call: Call<ArrayList<FollowerResponseItem>>,
                    response: Response<ArrayList<FollowerResponseItem>>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful && response.body() != null) {
                        _followers.value = response.body()
                    }
                }

                override fun onFailure(call: Call<ArrayList<FollowerResponseItem>>, t: Throwable) {
                    _isLoading.value = false
                    Log.e(TAG, "onFailure: ${t.message.toString()}")
                }
            })
        } catch (e: Exception) {
            Log.d("Token e", e.toString())
        }
    }

    companion object {
        private const val TAG = "FollowersViewModel"
    }



}