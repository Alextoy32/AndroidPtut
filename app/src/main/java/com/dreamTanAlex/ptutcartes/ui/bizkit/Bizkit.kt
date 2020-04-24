package com.dreamTanAlex.ptutcartes.ui.bizkit

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil.inflate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.dreamTanAlex.ptutcartes.R
import com.dreamTanAlex.ptutcartes.databinding.BizkitFragmentBinding
import androidx.databinding.BindingAdapter
import android.widget.ImageView


class Bizkit : Fragment() {

    companion object {
        fun newInstance() = Bizkit()
    }

    private lateinit var binding: BizkitFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val model: BizkitViewModel by viewModels()
        binding = inflate(inflater, R.layout.bizkit_fragment, container, false)

        binding.root.setOnClickListener {
            model.rollDice()
        }

        model.displayed.observe(viewLifecycleOwner, Observer {
            run {
                val players = model.party.getCurrentPlayers()
                binding.rule = getString(model.displayed.value!!.rule, players[1], 0, players[2])
                binding.invalidateAll()
            }
        })

        binding.rule = getString(model.displayed.value!!.rule)

        binding.viewModel = model
        binding.lifecycleOwner = this

        return binding.root
    }

}

@BindingAdapter("android:src")
fun setImageViewResource(imageView: ImageView, resource: Int) {
    imageView.setImageResource(resource)
}