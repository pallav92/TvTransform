package com.tvolution.tvtransformer

import android.app.Activity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.tvolution.tvtransformer.databinding.LayoutActivityPlayerBinding

class PlayerActivity : FragmentActivity(),  Player.Listener {

    private val playWhenReady: Boolean = false
    private val DEFAULT_SEEK_VALUE: Int = 10
    private var playbackPosition = 0L
    private var player: SimpleExoPlayer? = null
    private lateinit var binding :LayoutActivityPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LayoutActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //initializePlayer()
    }

    fun setTopPanelState(panelState: TopPanelState){
        when(panelState){
            TopPanelState.Shown -> {
                val panelFragment = PanelFragment.getInstance()
                supportFragmentManager.beginTransaction().replace(R.id.panel_container,panelFragment).commit()
                binding.panelContainer.visibility = View.VISIBLE
                binding.panelContent.visibility = View.GONE
            }
            TopPanelState.Hidden -> {
                binding.panelContainer.visibility = View.GONE
                binding.panelContent.visibility = View.VISIBLE
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN && binding.panelContent.visibility == View.GONE){
            setTopPanelState(TopPanelState.Hidden)
            return true
        }
        if(keyCode == KeyEvent.KEYCODE_DPAD_UP && binding.panelContent.visibility == View.VISIBLE){
            setTopPanelState(TopPanelState.Shown)
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    enum class TopPanelState{
        Shown, Hidden
    }

    private fun initializePlayer() {
        player = SimpleExoPlayer.Builder(this)
            .build().also { exoPlayer ->
                binding.videoView.player = exoPlayer
                //TODO : Add URL
                exoPlayer.setMediaItem(MediaItem.fromUri("sample url"))
                exoPlayer.playWhenReady = playWhenReady
                exoPlayer.seekTo(playbackPosition)
                exoPlayer.prepare()
                exoPlayer.addListener(this)
            }
    }


    var sidePanelStateValue = SidePanelState.Hide

    lateinit var sidePanelFragment: Fragment
    fun setSidePanelState(sidePanelState: SidePanelState){
        when(sidePanelState){
            SidePanelState.Show -> {
                sidePanelFragment = SidePanelFragment.getInstance("")
                supportFragmentManager.beginTransaction().replace(binding.sidePanelContainer.id, sidePanelFragment).commit()
                binding.rightGuideline.setGuidelinePercent(0.7f)
                sidePanelStateValue = SidePanelState.Show
            }
            SidePanelState.Hide -> {
                binding.rightGuideline.setGuidelinePercent(1f)
                if (::sidePanelFragment.isInitialized)
                    supportFragmentManager.beginTransaction().remove(sidePanelFragment).commit()
                sidePanelStateValue = SidePanelState.Hide
            }
        }
    }

    enum class SidePanelState{
        Show, Hide}

    fun showNudge(icon: Int, title:String, okbutton:Boolean){
        val nudgeFragment = NudgeFragment.getInstance(title, icon, 10)
        supportFragmentManager.beginTransaction().replace(binding.nudgeContainer.id, nudgeFragment).commit()
        binding.nudgeContainer.visibility = View.VISIBLE
        if(okbutton)
            binding.nudgeBackContainer.visibility = View.VISIBLE
    }

    fun hideNudge(){
        binding.nudgeContainer.visibility = View.GONE
        binding.nudgeBackContainer.visibility = View.GONE
    }

    override fun onPlaybackStateChanged(state: Int) {
        super.onPlaybackStateChanged(state)
        val playState: PlayState = when (state) {
            Player.STATE_ENDED -> PlayState.ENDED
            Player.STATE_READY -> {

//                if (isTotalDurationSet.not()) {
//                    isTotalDurationSet = true
//                    player?.let {
//                        currentSeekBar.max = (it.duration / 1000).toInt()
//                    }
//                    handler.post(updateProgressAction)
//                    PlayState.INIT
//                } else {
                    PlayState.UNKNOWN
//                }
            }
            Player.STATE_BUFFERING -> PlayState.BUFFERING
            else -> PlayState.UNKNOWN
        }

//        viewModel.handlePlaybackStateChange(playState)
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        super.onIsPlayingChanged(isPlaying)
        if (isPlaying) {
//            viewModel.handlePlaybackStateChange(PlayState.PLAYING)
        } else {
            player?.playbackState?.let { state ->
                if (state != Player.STATE_BUFFERING) {
//                    viewModel.handlePlaybackStateChange(PlayState.PAUSED)
                }
            }
        }
    }

    fun seekForward() {
//        showSeekForwardArrow()
        player?.let {
            it.seekTo(it.currentPosition + DEFAULT_SEEK_VALUE)
        }
    }

    fun seekBackward() {
//        showSeekBackwardArrow()
        player?.run {
            if (currentPosition - DEFAULT_SEEK_VALUE >= 0) {
                seekTo(currentPosition - DEFAULT_SEEK_VALUE)
            }
        }
    }
}


enum class PlayState {
    INIT, JUST_STARTED, PLAYING, PAUSED, ENDED, BUFFERING, UNKNOWN, ERROR,
    STREAM_PAUSED, STREAM_ENDED
}