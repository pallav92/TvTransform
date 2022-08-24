package com.tvolution.tvtransformer

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.KeyEvent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.firebase.FirebaseApp
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tvolution.tvtransformer.databinding.LayoutActivityPlayerBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class PlayerActivity : FragmentActivity(), Player.Listener {

    private val playWhenReady: Boolean = true
    private val DEFAULT_SEEK_VALUE: Int = 10
    private var playbackPosition = 0L
    private var player: SimpleExoPlayer? = null
    private val TAG = "Hackathon"
    private var capturedMoments  = arrayListOf<String>()
    private lateinit var viewModel: PlayerViewModel
    private lateinit var binding: LayoutActivityPlayerBinding
    var boolean = false


    data class StaticNudgeData(val icon: Int, val title: String, val startTimeInMillli:Long, val sutitle:String, var shown:Boolean)
//96000
    val scheduledNudges = listOf(
        StaticNudgeData(R.drawable.ic_camera, "2.5k saved this moment with glance",10000,"", false),
        StaticNudgeData(R.drawable.gauntlet, "Inifinity Gauntlet",135000,"9 pieces left", false),
        StaticNudgeData(R.drawable.stormhammer, "Stormbreaker",150000,"2 pieces left", false),
        StaticNudgeData(R.drawable.gauntlet, "Inifinity Gauntlet",180000,"9 pieces left", false),
        StaticNudgeData(R.drawable.ic_camera, "26k saved this moment with glance",215000,"", false),
        StaticNudgeData(R.drawable.ic_camera, "26k saved this moment with glance",255000,"", false)

    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LayoutActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViewModel()
        initializePlayer()

        startCountDownTimer()
    }

    private fun startCountDownTimer() {
        object : CountDownTimer(127000, 20000) {
            override fun onTick(millisUntilFinished: Long) {
                if(boolean) {
                    var momentToBeShown: StaticNudgeData? = null
                    scheduledNudges.forEach {
                        if (!it.shown) {
                            momentToBeShown = it
                        }
                    }
                    momentToBeShown?.let {
                        showNudge(it.icon, it.title, true, false, R.drawable.ic_camera != it.icon)
                        it.shown = true
                    }
                }
                boolean=true
            }

            override fun onFinish() {

            }
        }.start()
    }

    fun captureMoment() {
        lifecycleScope.launch {
            delay(500)
            showNudge(R.drawable.ic_camera, "Capturing Moment", false, true, false)
            //
            player?.let {
                val second = it.currentPosition/1000 //Todo: Replace with timestamp second
                gifValue = "https://hackathon2978.s3.ap-south-1.amazonaws.com/"+second+".gif"//"https://hackathon2978.s3.ap-south-1.amazonaws.com/testvid/"+second+".gif"//"https://hackathon2978.s3.ap-south-1.amazonaws.com/transformers/"+second+".gif"
                FirebaseApp.initializeApp(this@PlayerActivity)
                val database = Firebase.database.reference
                val moment = Moment(gifValue, "HBO", System.currentTimeMillis().toString(), "Avengers: Endgame", "SuperHero")
                database.child("moments").child(System.currentTimeMillis().toString()).setValue(moment)
                delay(2000)
                setSidePanelState(SidePanelState.ShowMoment)
                capturedMoments.add(gifValue)
            }

        }

    }

    fun setTopPanelState(panelState: TopPanelState){
        when(panelState){
            TopPanelState.Shown -> {
                val panelFragment = PanelFragment.getInstance()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.panel_container, panelFragment).commit()
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
        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN && binding.panelContent.visibility == View.GONE) {
            setTopPanelState(TopPanelState.Hidden)
            return true
        }
        if (keyCode == KeyEvent.KEYCODE_DPAD_UP && binding.panelContent.visibility == View.VISIBLE) {
            setTopPanelState(TopPanelState.Shown)
            return true
        }
        if (keyCode == KeyEvent.KEYCODE_BACK && sidePanelStateValue != SidePanelState.Hide) {
            setSidePanelState(SidePanelState.Hide)
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    enum class TopPanelState {
        Shown, Hidden
    }


    private fun initializePlayer() {
        player = SimpleExoPlayer.Builder(this)
            .build().also { exoPlayer ->
                binding.videoView.player = exoPlayer
                //TODO : Add URL
                exoPlayer.setMediaItem(MediaItem.fromUri("https://hackathon2978.s3.ap-south-1.amazonaws.com/final_vid/video_hackathon.mp4"))
                exoPlayer.playWhenReady = playWhenReady
                exoPlayer.seekTo(playbackPosition)
                exoPlayer.prepare()
                exoPlayer.addListener(this)
            }
    }


    var sidePanelStateValue = SidePanelState.Hide
    var gifValue = ""

    lateinit var sidePanelFragment: Fragment
    fun setSidePanelState(sidePanelState: SidePanelState) {
        when (sidePanelState) {
            SidePanelState.ShowMoment -> {
                sidePanelFragment =
                    SidePanelFragment.getInstance(gifValue, SidePanelFragment.Type.Moment)
                supportFragmentManager.beginTransaction()
                    .replace(binding.sidePanelContainer.id, sidePanelFragment).commit()
                binding.rightGuideline.setGuidelinePercent(0.7f)
                sidePanelStateValue = SidePanelState.ShowMoment
            }
            SidePanelState.ShowLink -> {
                sidePanelFragment = SidePanelFragment.getInstance("", SidePanelFragment.Type.Link)
                supportFragmentManager.beginTransaction()
                    .replace(binding.sidePanelContainer.id, sidePanelFragment).commit()
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

    enum class SidePanelState {
        ShowMoment, ShowLink, Hide
    }

    fun showNudge(icon: Int, title: String, okbutton: Boolean, notimer: Boolean, shopItem: Boolean) {
        val duration = if (notimer) 2 else 10
        val nudgeFragment = NudgeFragment.getInstance(title, icon, duration)
        supportFragmentManager.beginTransaction().replace(binding.nudgeContainer.id, nudgeFragment)
            .commit()
        if(shopItem){
            nudgeFragment.s = true
            nudgeFragment.setShopImage(icon, title)
            binding.nudgeExitText.text = " to explore!"
        }else
            binding.nudgeExitText.text = " to capture this!"
        binding.nudgeContainer.visibility = View.VISIBLE
        if (okbutton)
            binding.nudgeBackContainer.visibility = View.VISIBLE
    }

    fun hideNudge() {
        binding.nudgeContainer.visibility = View.GONE
        binding.nudgeBackContainer.visibility = View.GONE
    }

    override fun onPlaybackStateChanged(state: Int) {
        super.onPlaybackStateChanged(state)
        val playState: PlayState = when (state) {
            Player.STATE_ENDED -> {
                openEndStateActivity()
                PlayState.ENDED
            }
            Player.STATE_READY -> {
                PlayState.UNKNOWN
            }
            Player.STATE_BUFFERING -> PlayState.BUFFERING
            else -> PlayState.UNKNOWN
        }
//        viewModel.handlePlaybackStateChange(playState)
    }

    private fun openEndStateActivity() {
        val intent = Intent(this, EndStateActivity::class.java)
        intent.putStringArrayListExtra("CAPTURED_MOMENTS", capturedMoments)
        startActivity(intent)
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


    override fun onBackPressed() {
        val fm = supportFragmentManager
        if (!fm.isDestroyed) {
            viewModel.playPauseLiveData.value = false
            val primingDialogFragment: PrimingDialogFragment =
                PrimingDialogFragment.newInstance()
            primingDialogFragment.show(fm, PrimingDialogFragment.TAG)
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(PlayerViewModel::class.java)
        viewModel.playPauseLiveData.observe(this) {
            if (it) {
                player?.play()
            } else {
                player?.pause()
            }
        }
    }

    override fun onStop() {
        player?.pause()
        super.onStop()
    }
}


