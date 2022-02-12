package com.example.androidflows

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.androidflows.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel>()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUIListeners()
        subscribeToObservables()
    }

    private fun setupUIListeners() {
        binding.run {
            btnLivedata.setOnClickListener {
                viewModel.triggerLiveData()
            }

            btnStateFlow.setOnClickListener {
                // Calling it again won't trigger snack bar multiple times
                // as state flow keeps the value and doesn't emit the same value again.
                viewModel.triggerStateFlow()
            }

            btnFlow.setOnClickListener {
                lifecycleScope.launch {
                    viewModel.triggerFlow().collectLatest {
                        binding.tvFlow.text = it
                    }
                }
            }

            btnSharedFlow.setOnClickListener {
                viewModel.triggerSharedFlow()
            }
        }
    }

    private fun subscribeToObservables() {
        viewModel.liveData.observe(this) {
            binding.tvLiveData.text = it
        }

        lifecycleScope.launchWhenStarted {
            viewModel.stateFlow.collectLatest {
                binding.tvStateFlow.text = it
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.stateFlow.collectLatest {
                // Snackbar appears again on screen rotation which is not expected
                Snackbar.make(
                    binding.root,
                    it,
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.sharedFlow.collectLatest {
                binding.tvSharedFlow.text = it
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.sharedFlow.collectLatest {
                // Snackbar doesn't appears again on screen rotation
                Snackbar.make(
                    binding.root,
                    it,
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }
}