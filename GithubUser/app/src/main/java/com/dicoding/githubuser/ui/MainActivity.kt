package com.dicoding.githubuser.ui

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.databinding.ActivityMainBinding
import com.dicoding.githubuser.adapter.UserAdapter
import com.dicoding.githubuser.data.response.ItemsItem
import com.dicoding.githubuser.ui.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UserAdapter
    private val viewModel by viewModels<MainViewModel> {
        MainViewModel.MainViewModelFactory(SettingPreferences.getInstance(dataStore))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        with(binding){
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, actionID, event ->
                    searchBar.setText(searchView.text)
                    searchView.hide()
                    viewModel.searchUser(searchBar.text.toString())
                    false
                }

            favoriteButton.setOnClickListener {
                val intent = Intent(this@MainActivity, FavUserActivity::class.java)
                startActivity(intent)
            }

            settingButton.setOnClickListener {
                val themeIntent =  Intent(this@MainActivity, SetThemeActivity::class.java)
                startActivity(themeIntent)
            }

        }
        showViewModel()
        showRecyclerView()
        viewModel.isLoading.observe(this, this::showLoading)

        binding.tvNotFound.visibility = View.GONE
    }

    private fun showViewModel() {
        viewModel.userList.observe(this) { userList ->
            if (userList.isNotEmpty()) {
                binding.tvNotFound.visibility = View.GONE
                binding.rvUser.visibility = View.VISIBLE
                adapter.updateData(userList)
            } else {
                binding.tvNotFound.visibility = View.VISIBLE
                binding.rvUser.visibility = View.GONE
                Toast.makeText(this, "User Not Found!", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.isLoading.observe(this, this::showLoading)
    }

    private fun showRecyclerView() {
        adapter = UserAdapter(ArrayList())
        binding.rvUser.layoutManager =
            if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                GridLayoutManager(this, 2)
            } else {
                LinearLayoutManager(this)
            }

        binding.rvUser.setHasFixedSize(true)
        binding.rvUser.adapter = adapter

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ItemsItem) {
                selectedUser(data)
            }
        })

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