package com.dicoding.githubuser.ui

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.githubuser.R
import com.dicoding.githubuser.adapter.SectionsPagerAdapter
import com.dicoding.githubuser.database.FavUser
import com.dicoding.githubuser.databinding.ActivityUserBinding
import com.dicoding.githubuser.ui.viewmodel.FavUserViewModel
import com.dicoding.githubuser.ui.viewmodel.FavUserViewModelFactory
import com.dicoding.githubuser.ui.viewmodel.UserViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class UserActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val EXTRA_USER = "extra_user"
        val username = String()

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.ferslabel,
            R.string.finglabel
        )
    }

    private val userViewModel: UserViewModel by viewModels()
    private lateinit var favUserViewModel: FavUserViewModel
    private var favoriteUser: FavUser? = null
    private lateinit var binding: ActivityUserBinding
    var username = String()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_user)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = resources.getString(R.string.user_detail)

        username = intent.getStringExtra(EXTRA_USER).toString()
        val bundle = Bundle()
        bundle.putString("username", username)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, bundle)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()


        username?.let { userViewModel.userDetail(it) }
        userViewModel.userDetail.observe(this) { user ->
            user?.let { safeUser ->
                Glide.with(this)
                    .load(safeUser.avatarUrl)
                    .skipMemoryCache(true)
                    .into(binding.imgAvatar)

                binding.tvName.text = safeUser.name ?: ""
                binding.tvUsername.text = safeUser.login ?: ""
                if (safeUser.company != null){
                    binding.tvCompany.text = safeUser.company.toString()
                }
                if (safeUser.company != null){
                    binding.tvLocation.text = safeUser.location.toString()
                }
                binding.tvRepositoryValue.text = safeUser.publicRepos.toString()
                binding.tvFollowers.text = safeUser.followers.toString()
                binding.tvFollowing.text = safeUser.following.toString()
            }
        }

        favUserViewModel = showViewModel(this@UserActivity)

        favUserViewModel.getByUsername(username).observe(this) { favoriteUser ->
            if(favoriteUser != null){
                binding.buttonFavorite.visibility = View.INVISIBLE
                binding.buttonUnfavorite.visibility = View.VISIBLE
            }
        }

        binding.buttonFavorite.setOnClickListener(this)
        binding.buttonUnfavorite.setOnClickListener(this)
    }

    private fun showViewModel(activity: AppCompatActivity): FavUserViewModel {
        val factory = FavUserViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(FavUserViewModel::class.java)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when(v.id){
                R.id.buttonFavorite -> {
                    var username : String? = null
                    var avatarUrl :String? = null
                    var htmlUrl: String? = null

                    userViewModel.userDetail.observe(this) {
                        username = it.login.trim()
                        avatarUrl = it.avatarUrl.trim()
                        htmlUrl = it.htmlUrl.trim()
                    }

                    favoriteUser = FavUser()

                    favoriteUser.let {
                        it?.username = username.toString()
                        it?.avatarUrl = avatarUrl
                        it?.htmlUrl = htmlUrl.toString()
                    }

                    favUserViewModel.insert(favoriteUser as FavUser)

                    binding.buttonFavorite.visibility = View.INVISIBLE
                    binding.buttonUnfavorite.visibility = View.VISIBLE

                }
                R.id.buttonUnfavorite -> {
                    var username : String? = null
                    var avatarUrl :String? = null

                    userViewModel.userDetail.observe(this) {
                        username = it.login.trim()
                        avatarUrl = it.avatarUrl.trim()
                    }

                    favoriteUser = FavUser()

                    favoriteUser.let {
                        it?.username = username.toString()
                        it?.avatarUrl = avatarUrl
                    }

                    favUserViewModel.delete(favoriteUser as FavUser)

                    binding.buttonFavorite.visibility = View.VISIBLE
                    binding.buttonUnfavorite.visibility = View.INVISIBLE
                }
            }
        }
    }
}
