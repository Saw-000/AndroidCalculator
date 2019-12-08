package com.all_man.androidcalculator.bananagame

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.all_man.androidcalculator.R
import com.all_man.androidcalculator.databinding.FragmentBananaGameBinding

class BananaGameFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding : FragmentBananaGameBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_banana_game, container, false)


        // ViewModelに電卓計算式の数字の数を渡す。
        val viewModelFactory = BananaGameViewModelFactory(BananaGameFragmentArgs.fromBundle(arguments!!).howManyTapped)
        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(BananaGameViewModel::class.java)
        binding.viewModel = viewModel

        // adapter作成。viewModelから、recyclerViewに使うListを渡す。
        val adapter = BananaGameAdapter(RecyclerViewItemListener {
            Log.i("BananaFragment", "item=${it}")
            when (it.second) {
                1 -> viewModel.setImageNum(it.first, -1)
                2 -> viewModel.setImageNum(it.first, -2)
                else -> viewModel.setImageNum(it.first, 0)
            }
        })
        binding.appleRecyclerView.adapter = adapter

        viewModel.imgNumberListLive.observe(this, Observer {
            adapter.data = it
        })

        binding.backButton.setOnClickListener {
            it.findNavController().navigate(BananaGameFragmentDirections.actionBananaGameFragmentToCalculatorFragment())
        }

        binding.appleRecyclerView.layoutManager = GridLayoutManager(activity, 2)

        binding.setLifecycleOwner(this)
        return binding.root
    }
}
