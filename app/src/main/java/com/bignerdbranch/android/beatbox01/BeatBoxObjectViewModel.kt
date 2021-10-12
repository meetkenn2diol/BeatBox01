package com.bignerdbranch.android.beatbox01

import android.content.res.AssetManager
import androidx.lifecycle.ViewModel

class BeatBoxObjectViewModel(val assets: AssetManager) : ViewModel() {
    val beatBox: BeatBox = BeatBox(assets)

    override fun onCleared() {
        super.onCleared()
        beatBox.release()
    }

}