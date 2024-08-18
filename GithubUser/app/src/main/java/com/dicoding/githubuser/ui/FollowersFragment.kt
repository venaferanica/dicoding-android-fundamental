package com.bangkit.githubuser.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.adapter.FollowersAdapter
import com.dicoding.githubuser.data.response.FollowerResponseItem
import com.dicoding.githubuser.databinding.FragmentFollowersBinding
import com.dicoding.githubuser.ui.UserActivity
import com.dicoding.githubuser.ui.viewmodel.FollowersViewModel


class FollowersFragment : Fragment() {

    private var _binding : FragmentFollowersBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FollowersViewModel
    private lateinit var username : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        username = arguments?.getString("username").toString()

        val layoutManager = LinearLayoutManager(activity)
        binding.rvFollowers.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.rvFollowers.addItemDecoration(itemDecoration)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[FollowersViewModel::class.java]

        viewModel.getFollowers(username)

        viewModel.followers.observe(viewLifecycleOwner){
            setFollowersData(it)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

    }

    private fun setFollowersData(items: ArrayList<FollowerResponseItem>) {
        val adapter = FollowersAdapter()
        adapter.setData(items)
        binding.rvFollowers.adapter = adapter
        adapter.setOnItemClickCallback { data -> showDetailUser(data) }
    }

    private fun showDetailUser(data: FollowerResponseItem) {
        val intent = Intent(requireActivity(), UserActivity::class.java)
        intent.putExtra(UserActivity.EXTRA_USER, data.login)
        startActivity(intent)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}