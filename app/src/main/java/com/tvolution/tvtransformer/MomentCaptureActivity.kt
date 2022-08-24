package com.tvolution.tvtransformer

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.tvolution.tvtransformer.databinding.ActivityEndStateBinding
import com.tvolution.tvtransformer.databinding.ActivityMomentCaptureBinding

class MomentCaptureActivity : FragmentActivity() {

    private lateinit var binding: ActivityMomentCaptureBinding
    private var capturedMoment : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMomentCaptureBinding.inflate(layoutInflater)
        capturedMoment = intent.getStringExtra("CAPTURED_MOMENT") as String
        setContentView(binding.root)
        initView()
    }


    private fun initView() {
        binding.apply {
            Glide.with(ivBackground.context).load(capturedMoment)
                .into(ivBackground)
        }
    }


}