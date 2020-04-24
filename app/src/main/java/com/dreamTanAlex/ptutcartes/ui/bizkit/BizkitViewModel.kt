package com.dreamTanAlex.ptutcartes.ui.bizkit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dreamTanAlex.ptutcartes.R
import com.dreamTanAlex.ptutcartes.model.BizkitParty
import com.dreamTanAlex.ptutcartes.model.BizkitRessources
import com.dreamTanAlex.ptutcartes.model.Player
import kotlin.random.Random

class BizkitViewModel : ViewModel() {

    var party = BizkitParty()

    var dice1 = 1
    var dice2 = 1

    var displayed = MutableLiveData(
        BizkitRessources(
            R.string.startRule,
            R.drawable.ic_dice_one,
            R.drawable.ic_dice_one
        )
    )

    private var drawableDice1 = 0
    private var drawableDice2 = 0
    private var stringRule = 0

    init {
        party.players.add(Player("Tan", false, 0))
        party.players.add(Player("Alexis", false, 0))
        party.players.add(Player("Maxime", false, 0))
        party.startParty()
    }

    fun rollDice() {
        party.nextTurn()
        dice1 = Random.nextInt(1, 7)
        dice2 = Random.nextInt(1, 7)
        drawableDice1 = diceToDrawable(dice1)
        drawableDice2 = diceToDrawable(dice2)

        when {
            dice1 == dice2 && dice1 == 1 -> stringRule = R.string.double1
            dice1 == dice2 && dice1 == 5 -> stringRule = R.string.double5
            dice1 == dice2 -> stringRule = R.string.doubleN
            dice1 + dice2 == 3 -> pigeon()
            dice1 + dice2 == 5 -> stringRule = R.string.som5
            dice1 + dice2 == 7 -> stringRule = R.string.som7
            dice1 + dice2 == 9 -> stringRule = R.string.som9
            dice1 + dice2 == 10 -> stringRule = R.string.som10
            dice1 + dice2 == 11 -> stringRule = R.string.som11
            else -> stringRule = R.string.passe
        }

        displayed.postValue(BizkitRessources(stringRule, drawableDice1, drawableDice2))
    }

    private fun diceToDrawable(dice: Int): Int {
        var d = 0
        when (dice) {
            1 -> d = R.drawable.ic_dice_one
            2 -> d = R.drawable.ic_dice_two
            3 -> d = R.drawable.ic_dice_three
            4 -> d = R.drawable.ic_dice_four
            5 -> d = R.drawable.ic_dice_five
            6 -> d = R.drawable.ic_dice_six
        }
        return d
    }

    private fun pigeon() {
        stringRule = R.string.newPigeon
    }


}
