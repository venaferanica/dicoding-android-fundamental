package com.dicoding.githubuser.data.retrofit

import com.dicoding.githubuser.data.response.DetailResponse
import com.dicoding.githubuser.data.response.FollowerResponseItem
import com.dicoding.githubuser.data.response.FollowingResponseItem
import com.dicoding.githubuser.data.response.SearchResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("search/users")
    fun search(
        @Query("q") username: String
    ): Call<SearchResponse>

    @GET("users/{username}")
    fun detailUser(
        @Path("username") username: String
    ): Call<DetailResponse>

    @GET("users/{username}/followers")
    fun followers(
        @Path("username") username: String
    ): Call<ArrayList<FollowerResponseItem>>

    @GET("users/{username}/following")
    fun following(
        @Path("username") username: String
    ): Call<ArrayList<FollowingResponseItem>>
}