package com.tvolution.tvtransformer

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.tvolution.tvtransformer.databinding.ActivityEndStateBinding

class EndStateActivity : FragmentActivity() {

    private lateinit var binding: ActivityEndStateBinding
    private lateinit var endStateRecommendAdapter: EndStateRecommendAdapter
    private lateinit var endStateMomentAdapter: EndStateMomentAdapter
    private  var capturedMoments = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEndStateBinding.inflate(layoutInflater)
        capturedMoments = intent.getStringArrayListExtra("CAPTURED_MOMENTS") as ArrayList<String>
        setContentView(binding.root)
        initView()
        initData()
    }

    private fun initData() {
        endStateRecommendAdapter.list = arrayListOf(R.drawable.cardone, R.drawable.cardtwo, R.drawable.cardthree, R.drawable.cardfour, R.drawable.cardfive)
        endStateRecommendAdapter.notifyDataSetChanged()
        endStateMomentAdapter.list = capturedMoments
        endStateMomentAdapter.notifyDataSetChanged()
    }

    private fun initView() {
        endStateRecommendAdapter = EndStateRecommendAdapter()
        endStateMomentAdapter = EndStateMomentAdapter()
        binding.apply {
           rvRelated.apply {
                layoutManager =
                    LinearLayoutManager(this@EndStateActivity, LinearLayoutManager.HORIZONTAL, false)
                adapter = endStateRecommendAdapter
            }
            rvCaptured.apply {
                layoutManager =
                    LinearLayoutManager(this@EndStateActivity, LinearLayoutManager.HORIZONTAL, false)
                adapter = endStateMomentAdapter
                endStateMomentAdapter.onItemClick = {url ->
                    openEndStateActivity(url)
                }
            }

        }
    }

    private fun openEndStateActivity(capturedMoment : String) {
        val intent = Intent(this, MomentCaptureActivity::class.java)
        intent.putExtra("CAPTURED_MOMENT", capturedMoment)
        startActivity(intent)
    }


}