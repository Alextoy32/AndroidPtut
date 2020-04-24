package com.dreamTanAlex.ptutcartes.ui.partyGames

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.dreamTanAlex.ptutcartes.R


class PartyGames : Fragment() {

    companion object {
        fun newInstance() = PartyGames()
    }

    private lateinit var viewModel: PartyGamesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.party_games_fragment, container, false)
        val bizkitImage = view.findViewById<ImageView>(R.id.bizkitImage)
        bizkitImage.setOnClickListener {
            findNavController().navigate(R.id.action_partyGames_to_bizkit)
        }
        val autoroutes = view.findViewById<ImageView>(R.id.autorouteImage)
        autoroutes.setOnClickListener {
            findNavController().navigate(R.id.action_partyGames_to_connectionPage)
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(PartyGamesViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
