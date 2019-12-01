package com.all_man.androidcalculator.calculator

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.all_man.androidcalculator.R
import com.all_man.androidcalculator.databinding.FragmentCalculatorBinding

class CalculatorFragment: Fragment() {

    lateinit var binding: FragmentCalculatorBinding
    lateinit var viewModel: CalculatorViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_calculator, container, false)
        viewModel = ViewModelProviders.of(this).get(CalculatorViewModel::class.java)
        binding.viewModel = viewModel

        viewModel.showErrorMessage.observe(this, Observer {
            if (it==true) {
                Toast.makeText(context, "Your formula hasn't completed.", Toast.LENGTH_SHORT).show()
                viewModel.showErrorMessageFinished()
            }
        })
        // This is used so that the binding can observe LiveData updates.
        // => automatically update the views in the layout
        binding.lifecycleOwner = this
        setHasOptionsMenu(true)
        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.option_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item!!, view!!.findNavController())
                || super.onOptionsItemSelected(item)
    }

}