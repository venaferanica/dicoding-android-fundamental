package com.dicoding.githubuser.adapter

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bangkit.githubuser.ui.FollowersFragment
import com.dicoding.githubuser.ui.FollowingFragment
import com.dicoding.githubuser.ui.UserActivity
import com.dicoding.githubuser.ui.UserActivity.Companion.username

class SectionsPagerAdapter(activity: AppCompatActivity, data: Bundle) : FragmentStateAdapter(activity){
    var fragmentBundle : Bundle

    init {
        fragmentBundle = data
    }

    override fun getItemCount(): Int = 2
    override fun createFragment(position: Int): Fragment {
        var fragment : Fragment?= null
        when (position) {
            0 -> {
                fragment = FollowersFragment()
            }
            1 -> {
                fragment = FollowingFragment()
            }
        }
        fragment?.arguments = this.fragmentBundle
        return fragment as Fragment
    }
}