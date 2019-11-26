package com.all_man.androidcalculator.bananagame2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.all_man.androidcalculator.R
import com.all_man.androidcalculator.databinding.FragmentBananaGameBinding

class BananaGame2Fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding : FragmentBananaGameBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_banana_game_2, container, false)

        return binding.root
    }

}