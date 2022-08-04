package com.tvolution.tvtransformer

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.tvolution.tvtransformer.databinding.ActivityEndStateBinding

class EndStateActivity : FragmentActivity() {

    private lateinit var binding: ActivityEndStateBinding
    private lateinit var endStateRecommendAdapter: EndStateRecommendAdapter
    private lateinit var endStateMomentAdapter: EndStateMomentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEndStateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        initData()
    }

    private fun initData() {
        endStateRecommendAdapter.list = arrayListOf(R.drawable.cardone, R.drawable.cardtwo, R.drawable.cardthree, R.drawable.cardfour, R.drawable.cardfive)
        endStateRecommendAdapter.notifyDataSetChanged()
        endStateMomentAdapter.list = arrayListOf(R.drawable.moment_one)
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
            }
        }
    }


}