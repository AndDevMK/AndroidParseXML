package com.pengmj.androidparsexml

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.pengmj.androidparsexml.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
                DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        binding.presenter = Presenter()
    }
}