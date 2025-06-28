package com.example.test.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * For Compose views set ViewEffect to Any
 */
open class MviViewModel<ViewState, ViewEffect>(initialState: ViewState) : ViewModel() {
    protected val _state = MutableStateFlow(initialState)
    val state = _state.asStateFlow()
}