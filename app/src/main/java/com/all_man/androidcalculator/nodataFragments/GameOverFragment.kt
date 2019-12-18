package com.all_man.androidcalculator.nodataFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.all_man.androidcalculator.R
import com.all_man.androidcalculator.databinding.FragmentGameOverBinding

class GameOverFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding : FragmentGameOverBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_game_over, container, false)

        binding.tryAgainButton.setOnClickListener {
            when (GameOverFragmentArgs.fromBundle(arguments!!).imgNum) {
                Int.MIN_VALUE -> this.findNavController().navigate(GameOverFragmentDirections.actionGameOverFragmentToBananaGame2Fragment())
                else -> this.findNavController().navigate(GameOverFragmentDirections.actionGameOverFragmentToBananaGameFragment(GameOverFragmentArgs.fromBundle(arguments!!).imgNum))
            }
        }

        binding.backToCalculator2Button.setOnClickListener {
            this.findNavController().navigate(
                GameOverFragmentDirections.actionGameOverFragmentToCalculatorFragment()
            )
        }

        binding.setLifecycleOwner(this)
        return binding.root
    }

}