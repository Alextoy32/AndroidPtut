package com.dreamTanAlex.ptutcartes.model

import android.util.Log

/*
Created by Alexis Boutan on 22/04/2020 from the project : PtutCartes
*/

class BizkitParty {

    var players = mutableListOf<Player>()
    var pigeon = -1

    var lastPlay = 0
    var currentPlay = 0
    var nextPlay = 1

    fun startParty() {
        lastPlay = players.count() - 1
        currentPlay = 0
        nextPlay = 1
    }

    fun getCurrentPlayers(): Array<String> {
        return arrayOf(
            players[lastPlay].pseudo,
            players[currentPlay].pseudo,
            players[nextPlay].pseudo
        )
    }

    fun nextTurn() {
        when {
            players.count() - 1 == currentPlay -> {
                currentPlay = 0
                nextPlay = currentPlay + 1
                lastPlay += 1
            }
            players.count() - 1 == nextPlay -> {
                currentPlay += 1
                nextPlay = 0
                lastPlay += 1
            }
            players.count() - 1 == lastPlay -> {
                currentPlay += 1
                nextPlay += 1
                lastPlay = 0
            }
            else -> {
                currentPlay += 1
                nextPlay += 1
                lastPlay += 1
            }
        }
        Log.d("last", lastPlay.toString())
        Log.d("Current", currentPlay.toString())
        Log.d("Next", nextPlay.toString())
    }
}