package com.dreamTanAlex.ptutcartes.ui.bizkit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BizkitViewModel : ViewModel() {

    data class gameString(var rule: MutableLiveData<String>, var dice1: String, var dice2: String)

    var displayed = gameString(MutableLiveData("startregle"), "ic_dice_one", "ic_dice_one")

    fun rollDice() {
        displayed.rule.postValue("Ceci est la regle")
    }
}
