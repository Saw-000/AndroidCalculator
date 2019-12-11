package com.all_man.androidcalculator.nodataFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.all_man.androidcalculator.R
import com.all_man.androidcalculator.databinding.FragmentClearBinding

class ClearFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding : FragmentClearBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_clear, container, false)

        binding.nextLevelButton.setOnClickListener {
            this.findNavController().navigate(
                ClearFragmentDirections.actionClearFragmentToBananaGame2Fragment()
            )
        }
        binding.tryAgainButton.setOnClickListener {
            this.findNavController().navigate(
                ClearFragmentDirections.actionClearFragmentToBananaGameFragment(ClearFragmentArgs.fromBundle(arguments!!).imgNum)
            )
        }

        binding.setLifecycleOwner(this)
        return binding.root
    }

}