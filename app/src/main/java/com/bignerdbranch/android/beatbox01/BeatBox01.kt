package com.bignerdbranch.android.beatbox01

import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.media.SoundPool
import android.util.Log
import java.io.IOException

/**
 * Constant for logging: Log.d()
 */
private const val TAG = "BeatBox"

/** Constants to set the maximum number of sounds the SoundPool can play spontaneously*/
private const val MAX_SOUNDS = 1//5

/**
 * Constant for remembering the folder for sample sounds in assets folder
 */
private const val SOUNDS_FOLDER = "sample_sounds"

/**
 * BeatBox will end up doing a lot of work related to asset management: finding assets, keeping track of
 *them, and eventually playing them as sounds.
 *@param assets: AssetsManager for the calling Activity invoking an instance of BeatBox
 */
class BeatBox(private val assets: AssetManager) {
    /**A property that contains a List of Sound.kt object*/
    val sounds: List<Sound>

    /**
     * SoundPool can load a large set of sounds into memory and control the maximum number of sounds that are playing back at any one time.
     * SoundPool plays sound with no lag.The trade-off for that is that you must load sounds into your SoundPool before you play them. Each
     *sound you load will get its own integer ID.
     */
    private val soundPool = SoundPool.Builder().setMaxStreams(MAX_SOUNDS).build()

    lateinit var currentSound: Sound //get the sound currently playing
    var mRate = 1.0F//the rate for the soundpool
    var streamID: Int = 0 //streamID for the song playing

    init {
        sounds = loadSounds()
    }

    /** @return a list of Sound.kt objects each containing a file in the .../assets/sample_sounds folder
     *          else returns an empty readOnly list in the Kotlin.collections class
     */
    private fun loadSounds(): List<Sound> {
        val soundNames: Array<String>

        try {
            soundNames = assets.list(SOUNDS_FOLDER)!!
        } catch (e: Exception) {
            Log.e(TAG, "Could not list assets", e)
            return emptyList()
        }

        val sounds = mutableListOf<Sound>()
        soundNames.forEach { filename ->
            val assetPath = "$SOUNDS_FOLDER/$filename"
            val sound = Sound(assetPath)
            try {//load the sound to the soundPool immediately it is created
                load(sound)
                sounds.add(sound)
                currentSound = sound
            } catch (ioe: IOException) {
                Log.e(TAG, "Cound not load sound $filename", ioe)
            }
        }
        return sounds
    }

    /**
     * Now to load your sounds. Add a load(Sound) function to BeatBox to load a Sound into your SoundPool.
     *
     * Note: soundPool.load(afd,1) returns the id the SoundPool class in android gave to the sound.
     */
    private fun load(sound: Sound) {
        val afd: AssetFileDescriptor = assets.openFd(sound.assetPath)
        val soundId = soundPool.load(afd, 1)
        sound.soundId = soundId
    }

    /**Plays a Sound
     *
     * Before playing your soundId, you check to make sure it is not null. This might happen if the Sound failed to load.
     * SoundPool plays a sound using the soundId
     *
     * @param sound: The Sound to play
     * @see com.bignerdbranch.android.beatbox01.BeatBox.load
     */
    fun play(sound: Sound) {
        sound.soundId?.let {
            currentSound = sound
            streamID = soundPool.play(it, 1.0f, 1.0f, 1, 0, mRate)
        }
    }

    /**
     * Used to set the playback rate of a sound using it's it
     * @param rate: The rate at which the soundPool is set to for the sound
     */
    fun setMRate(rate: Double){
        mRate = rate.toFloat()
        //do this so that the rate can reflect
        if(streamID != 0){
           // soundPool.pause(streamID)
            soundPool.autoPause()
            soundPool.setRate(currentSound.soundId!!, mRate)
            soundPool.autoResume()
           // soundPool.resume(streamID)
        }
        else{
            soundPool.setRate(currentSound.soundId!!, rate.toFloat())
        }
    }


    /**
     *This function clean up the SoundPool by calling SoundPool.release() when you are done with it.
     * Call this if you want SoundPool to stop when the app is onDestroy().
     */
    fun release() {
        soundPool.release()
    }
}
/*
//region This gives you a standard InputStream for the data, which you can use like any other InputStream in Kotlin.
/**Your Sound object has an asset file path defined on it. Asset file paths will not work if you try to open
them with a File; you must use them with an AssetManager:*//*

val assetPath = sound.assetPath
val assetManager = context.assets
val soundData = assetManager.open(assetPath)
//endregion*/
 */
