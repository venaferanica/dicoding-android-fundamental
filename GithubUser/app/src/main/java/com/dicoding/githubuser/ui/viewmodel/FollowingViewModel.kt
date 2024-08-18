package com.dicoding.githubuser.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubuser.data.response.FollowingResponseItem
import com.dicoding.githubuser.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingViewModel : ViewModel() {

    private val _following = MutableLiveData<ArrayList<FollowingResponseItem>>()
    val following: LiveData<ArrayList<FollowingResponseItem>> = _following

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getFollowing(username: String) {
        try {
            _isLoading.value = true
            val client = ApiConfig.getApiService().following(username)
            client.enqueue(object : Callback<ArrayList<FollowingResponseItem>> {
                override fun onResponse(
                    call: Call<ArrayList<FollowingResponseItem>>,
                    response: Response<ArrayList<FollowingResponseItem>>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful && response.body() != null) {
                        _following.value = response.body()
                    }
                }

                override fun onFailure(call: Call<ArrayList<FollowingResponseItem>>, t: Throwable) {
                    _isLoading.value = false
                    Log.e(TAG, "onFailure: ${t.message.toString()}")
                }
            })
        } catch (e: Exception) {
            Log.d("Token e", e.toString())
        }
    }

    companion object {
        private const val TAG = "FollowingViewModel"
    }
}