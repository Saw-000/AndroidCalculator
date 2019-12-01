package com.all_man.androidcalculator.nodataFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.all_man.androidcalculator.R
import com.all_man.androidcalculator.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding : FragmentDetailBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_detail, container, false)

        binding.backToCalculatorButton.setOnClickListener {
            it.findNavController().navigate(DetailFragmentDirections.actionDetailFragmentToCalculatorFragment())
        }

        return binding.root
    }

}