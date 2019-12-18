package com.all_man.androidcalculator.bananagame2

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.all_man.androidcalculator.R
import com.all_man.androidcalculator.databinding.FragmentBananaGame2Binding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class BananaGame2Fragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        val binding : FragmentBananaGame2Binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_banana_game2, container, false)

        val viewModel = ViewModelProviders.of(this).get(BananaGame2ViewModel::class.java)
        binding.viewModel = viewModel

        // gameOverへのnavigation
        viewModel.navGameOver.observe(this, Observer {
            if (it) {
                this.findNavController().navigate(BananaGame2FragmentDirections.actionBananaGame2FragmentToGameOverFragment(Int.MIN_VALUE))
                viewModel.finishNavGameOver()
            }
        })

        viewModel.imageUrl.observe(this, Observer {
            if (viewModel.clickableFlag.value!!){
                Glide.with(this)
                    .load(it)
                    .listener(object : RequestListener<Drawable>{
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                            Log.i("GlideListener", "onLoadFailed called")
                            viewModel.clickabilityTofalse()  //TODO: 直でenabledをfalseにできないんかな。
                            viewModel.onNavClear()
                            return true
                        }
                        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            Log.i("GlideListener", "onResourceReady called")
                            return true
                        }

                    })
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.banana_kong)
                    .into(binding.fruitsImage)
            }
        })

        viewModel.navClear.observe(this, Observer {
                if (it) {
                    this.findNavController().navigate(BananaGame2FragmentDirections.actionBananaGame2FragmentToClearFragment())
                    viewModel.finishNavClear()
                }
        })



        binding.lifecycleOwner = this
        return binding.root
    }

}