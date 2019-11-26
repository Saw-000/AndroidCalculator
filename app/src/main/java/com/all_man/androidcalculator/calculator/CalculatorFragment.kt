package com.all_man.androidcalculator.calculator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.all_man.androidcalculator.R
import com.all_man.androidcalculator.databinding.FragmentCalculatorBinding

class CalculatorFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding : FragmentCalculatorBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_calculator, container, false)

        binding.viewModel = ViewModelProviders.of(this).get(CalculatorViewModel::class.java)

        // This is used so that the binding can observe LiveData updates.
        // => automatically update the views in the layout
        binding.lifecycleOwner = this
        return binding.root
    }

}