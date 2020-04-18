package com.dreamTanAlex.ptutcartes.ui.partyGames

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        return inflater.inflate(R.layout.party_games_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(PartyGamesViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
