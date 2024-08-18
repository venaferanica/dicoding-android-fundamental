package com.dicoding.githubuser.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.dicoding.githubuser.database.FavUser
import com.dicoding.githubuser.database.FavUserDao
import com.dicoding.githubuser.database.FavUserRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavUserRepository(application: Application) {

    private val mFavouriteUserDao: FavUserDao

    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavUserRoomDatabase.getDatabase(application)
        mFavouriteUserDao = db.favouriteUserDao()
    }

    fun getFavUser(): LiveData<List<FavUser>> = mFavouriteUserDao.getFavUser()

    fun insert(favouriteUser: FavUser) {
        executorService.execute { mFavouriteUserDao.insert(favouriteUser) }
    }

    fun delete(favouriteUser: FavUser) {
        executorService.execute { mFavouriteUserDao.delete(favouriteUser) }
    }

    fun getByUsername(username : String): LiveData<FavUser> = mFavouriteUserDao.getByUsername(username)
}