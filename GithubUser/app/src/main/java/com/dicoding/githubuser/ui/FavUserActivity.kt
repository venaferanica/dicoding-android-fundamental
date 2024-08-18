package com.dicoding.githubuser.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.adapter.UserAdapter
import com.dicoding.githubuser.data.response.ItemsItem
import com.dicoding.githubuser.databinding.ActivityFavUserBinding
import com.dicoding.githubuser.ui.viewmodel.FavUserViewModel
import com.dicoding.githubuser.ui.viewmodel.FavUserViewModelFactory

class FavUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavUserBinding
    private lateinit var favUserViewModel: FavUserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvFavUser.layoutManager = layoutManager

        favUserViewModel = showViewModel(this)

        favUserViewModel.getFavUser().observe(this) {
            val items = arrayListOf<ItemsItem>()
            it.map {
                val item = ItemsItem(login = it.username, avatarUrl = it.avatarUrl.toString(), htmlUrl = it.htmlUrl.toString())
                items.add(item)
            }
            val adapter = UserAdapter(items)
            binding.rvFavUser.adapter = adapter
            adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
                override fun onItemClicked(data: ItemsItem) {
                    selectedUser(data)
                }
            })
        }

        favUserViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun showViewModel(activity: AppCompatActivity): FavUserViewModel {
        val factory = FavUserViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavUserViewModel::class.java]
    }

    private fun selectedUser(user: ItemsItem) {
        val i = Intent(this, UserActivity::class.java)
        i.putExtra(UserActivity.EXTRA_USER, user.login)
        startActivity(i)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}