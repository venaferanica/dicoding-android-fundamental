package com.dicoding.githubuser.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface FavUserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favoriteUser: FavUser)

    @Delete
    fun delete(favoriteUser: FavUser)

    @Query("SELECT * FROM FavUser ORDER BY username ASC")
    fun getFavUser(): LiveData<List<FavUser>>

    @Query("SELECT * FROM FavUser WHERE username = :username")
    fun getByUsername(username: String): LiveData<FavUser>
}