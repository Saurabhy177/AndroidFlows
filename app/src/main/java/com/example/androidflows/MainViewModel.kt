package com.example.androidflows

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private val _liveData = MutableLiveData("Hello World")
    val liveData: LiveData<String> = _liveData

    /**
     * Hot flow:
     * A hot stream starts producing values immediately.
     * StateFlow and SharedFlow are hot flows.
     * */

    // StateFlow sends out events and it is re-emitted on screen rotation.
    private val _stateFlow = MutableStateFlow("Hello World")
    val stateFlow= _stateFlow.asStateFlow()

    // SharedFlow sends out a one time event and it is not re-emitted on screen rotation.
    private val _sharedFlow = MutableSharedFlow<String>()
    val sharedFlow = _sharedFlow.asSharedFlow()


    fun triggerLiveData() {
        _liveData.value = "LiveData"
    }

    fun triggerStateFlow() {
        _stateFlow.value = "StateFlow"
    }

    fun triggerSharedFlow() {
        viewModelScope.launch {
            _sharedFlow.emit("SharedFlow")
        }
    }

    /**
     * Cold flow:
     * A cold stream does not start producing values until one starts to collect them.
     * flow { } is a cold flow.
     * */
    fun triggerFlow(): Flow<String> {
        return flow {
            repeat(5) {
                emit("Item $it")
                delay(1000)
            }
        }
    }
}