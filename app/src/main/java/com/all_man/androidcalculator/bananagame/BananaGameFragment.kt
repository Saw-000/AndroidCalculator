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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.all_man.androidcalculator.R
import com.all_man.androidcalculator.database.AppleImageDatabase
import com.all_man.androidcalculator.databinding.FragmentBananaGameBinding
import kotlinx.coroutines.delay

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
                1 -> {
                    if (viewModel.flag.value!!) {
                        viewModel.flagFalse()
                        viewModel.onSetAppleInfo(it.dataId, -1, false)
                        viewModel.onNavigateToClearFragment()
                    }
                }
                2 -> {
                    if (viewModel.flag.value!!) {
                        viewModel.onSetAppleInfo(it.dataId, -2, true)
                    }
                }
                0, -1, -2 -> {}
                else -> {
                    if (viewModel.flag.value!!) {
                        viewModel.onSetAppleInfo(it.dataId, 0, false)
                    }
                }
            }
        })
        binding.appleRecyclerView.adapter = adapter

        //gameの結果によってnavigation
        // clear
        viewModel.navigateToClearFragment.observe(this, Observer {
            it?.let {
                this.findNavController().navigate(
                BananaGameFragmentDirections.actionBananaGameFragmentToClearFragment()
                )
                viewModel.onFinishNavigateToClearFragment()
            }
        })
        // false
        viewModel.navigateToGameOverFragment.observe(this, Observer {
            it?.let {
                this.findNavController().navigate(
                    BananaGameFragmentDirections.actionBananaGameFragmentToGameOverFragment(viewModel.navigateToGameOverFragment.value!!)
                )
                viewModel.onFinishNavigateToGameOverFragment()
            }
        })

        // recyclerViewにallImageInfoを渡す
        viewModel.allImageInfo.observe(viewLifecycleOwner, Observer {
            it?.let { adapter.addHeaderAndSubmitList(it) }
        })

        // "back"ボタンの処理
        binding.backButton.setOnClickListener {
            it.findNavController().navigate(BananaGameFragmentDirections.actionBananaGameFragmentToCalculatorFragment())
        }

        // recyclerViewにlayoutManagerを渡す。
        val manager = GridLayoutManager(activity, 3)
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int = when (position){
                0 -> 3
                else -> 1
            }
        }
        binding.appleRecyclerView.layoutManager = manager


        return binding.root
    }
}
