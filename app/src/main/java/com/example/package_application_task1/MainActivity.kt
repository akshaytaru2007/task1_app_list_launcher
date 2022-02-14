package com.example.package_application_task1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.package_application_task1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)


        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val adapter = AppListAdapter()
        binding.listView.adapter = adapter
        binding.listView.layoutManager = LinearLayoutManager(this)

        viewModel.appList.observe(this, {
            adapter.submitList(it)
        })
    }
}