package com.bignerdbranch.android.beatbox01

import android.content.res.AssetManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * ViewModelProvider should be used if there are parameters to be passed into a ViewModel
 * @param assets: collects [assets] from an activity,creates a newInstance of the [assets] and stores it
 */
class BeatBoxObjectFactoryModel(val assets: AssetManager): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(AssetManager::class.java).newInstance(assets)
    }
}