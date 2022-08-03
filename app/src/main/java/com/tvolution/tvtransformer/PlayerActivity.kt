package com.tvolution.tvtransformer

import android.app.Activity
import android.os.Bundle
import android.view.View
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
        setContentView(R.layout.layout_activity_player)
        val panelFragment = PanelFragment.getInstance()
        supportFragmentManager.beginTransaction().replace(R.id.panel_container,panelFragment).commit()
        //initializePlayer()
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