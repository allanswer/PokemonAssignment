package com.humanforce.features.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.*

/**
 * For Compose views set ViewEffect to Any
 */
open class MviViewModel<ViewState, ViewEffect>(initialState: ViewState) : ViewModel() {
    protected val _state = MutableStateFlow(initialState)
    val state = _state.asStateFlow()
}