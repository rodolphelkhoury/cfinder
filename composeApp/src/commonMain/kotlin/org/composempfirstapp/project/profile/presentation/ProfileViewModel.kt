package org.composempfirstapp.project.profile.presentation


import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.composempfirstapp.project.court.domain.Court
import org.composempfirstapp.project.core.Resource

class ProfileViewModel : ViewModel() {

    //    Idle means we do not need to do anything at first
    private val _courtStateFlow = MutableStateFlow<Resource<List<Court>>>(Resource.Idle)

    val courtStateFlow : StateFlow<Resource<List<Court>>>
        get() = _courtStateFlow

    init {
    }
}