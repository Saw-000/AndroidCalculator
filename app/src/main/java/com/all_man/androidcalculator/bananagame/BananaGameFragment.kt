package com.all_man.androidcalculator.bananagame

import android.os.Bundle
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
import com.all_man.androidcalculator.database.AppleImageDatabase
import com.all_man.androidcalculator.databinding.FragmentBananaGameBinding

class BananaGameFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Get a reference of the binding object of the layout.
        val binding : FragmentBananaGameBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_banana_game, container, false)

        // ViewModel作成
        // ViewModelに電卓計算式の数字の数を渡す。
        val application = requireNotNull(this.activity).application
        val dataSource = AppleImageDatabase.getInstance(application).appleImageDatabaseDao

        val viewModelFactory = BananaGameViewModelFactory(
            dataSource,
            application,
            BananaGameFragmentArgs.fromBundle(arguments!!).howManyTapped
        )
        val viewModel = ViewModelProviders.of(
            this, viewModelFactory).get(BananaGameViewModel::class.java)
        binding.viewModel = viewModel

        binding.setLifecycleOwner(this)

        // adapter作成。viewModelから、recyclerViewに使うListを渡す。
        val adapter = BananaGameAdapter(RecyclerViewItemListener {
            when (it.imageNumber) {
                1 -> viewModel.onSetAppleInfo(it.dataId, -1, false)
                2 -> viewModel.onSetAppleInfo(it.dataId, -2, true)
                0, -1, -2 -> {}
                else -> viewModel.onSetAppleInfo(it.dataId, 0, false)
            }
        })
        binding.appleRecyclerView.adapter = adapter

        // recyclerViewにallImageInfoを渡す
        viewModel.allImageInfo.observe(viewLifecycleOwner, Observer {
            it?.let { adapter.submitList(it) }
        })


        binding.backButton.setOnClickListener {
            it.findNavController().navigate(BananaGameFragmentDirections.actionBananaGameFragmentToCalculatorFragment())
        }

        binding.appleRecyclerView.layoutManager = GridLayoutManager(activity, 3)


        return binding.root
    }
}
