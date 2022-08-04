package com.tvolution.tvtransformer

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.firebase.FirebaseApp
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tvolution.tvtransformer.databinding.LayoutActivityPlayerBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class PlayerActivity : FragmentActivity(), Player.Listener {

    private val playWhenReady: Boolean = true
    private val DEFAULT_SEEK_VALUE: Int = 10
    private var playbackPosition = 0L
    private var player: SimpleExoPlayer? = null
    private val TAG = "Hackathon"
    private lateinit var binding: LayoutActivityPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LayoutActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //initializePlayer()
    }

    fun captureMoment(){
        lifecycleScope.launch{
            delay(500)
            showNudge(R.drawable.ic_camera, "Capturing Moment", false, true)
            //
            val second = 9 //Todo: Replace with timestamp second
            gifValue = "https://hackathon2978.s3.ap-south-1.amazonaws.com/transformers/"+second+".gif"
            FirebaseApp.initializeApp(this@PlayerActivity)
            val database = Firebase.database.reference
            val moment = Moment(gifValue, "HBO", System.currentTimeMillis().toString(), "Avengers: Endgame", "SuperHero")
            database.child("moments").child(System.currentTimeMillis().toString()).setValue(moment)
            delay(2000)
            setSidePanelState(SidePanelState.ShowMoment)
        }

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
        if(keyCode == KeyEvent.KEYCODE_BACK && sidePanelStateValue != SidePanelState.Hide){
            setSidePanelState(SidePanelState.Hide)
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
                exoPlayer.setMediaItem(MediaItem.fromUri("https://glance.l.inmobicdn.net/public/glancetv/xiaomi/samsung/samsung_intro_01.mp4"))
                exoPlayer.playWhenReady = playWhenReady
                exoPlayer.seekTo(playbackPosition)
                exoPlayer.prepare()
                exoPlayer.addListener(this)
            }
    }


    var sidePanelStateValue = SidePanelState.Hide
    var gifValue = ""

    lateinit var sidePanelFragment: Fragment
    fun setSidePanelState(sidePanelState: SidePanelState){
        when(sidePanelState){
            SidePanelState.ShowMoment -> {
                sidePanelFragment = SidePanelFragment.getInstance(gifValue, SidePanelFragment.Type.Moment)
                supportFragmentManager.beginTransaction().replace(binding.sidePanelContainer.id, sidePanelFragment).commit()
                binding.rightGuideline.setGuidelinePercent(0.7f)
                sidePanelStateValue = SidePanelState.ShowMoment
            }
            SidePanelState.ShowLink -> {
                sidePanelFragment = SidePanelFragment.getInstance("", SidePanelFragment.Type.Link)
                supportFragmentManager.beginTransaction().replace(binding.sidePanelContainer.id, sidePanelFragment).commit()
                binding.rightGuideline.setGuidelinePercent(0.7f)
                sidePanelStateValue = SidePanelState.ShowLink
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
        ShowMoment, ShowLink, Hide}

    fun showNudge(icon: Int, title:String, okbutton:Boolean, notimer:Boolean){
        val duration = if(notimer) 2 else 10
        val nudgeFragment = NudgeFragment.getInstance(title, icon, duration)
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
                PlayState.UNKNOWN
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

    override fun onBackPressed() {
        val fm = supportFragmentManager
        if (!fm.isDestroyed) {
            val primingDialogFragment: PrimingDialogFragment =
                PrimingDialogFragment.newInstance()
            primingDialogFragment.show(fm, PrimingDialogFragment.TAG)
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


