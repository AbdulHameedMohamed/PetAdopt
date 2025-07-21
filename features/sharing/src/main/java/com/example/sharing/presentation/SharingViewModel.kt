package com.example.sharing.presentation

import androidx.lifecycle.ViewModel
import com.example.common.domain.usecases.GetAnimalDetailsUseCase
import com.example.sharing.presentation.model.mapper.UiAnimalToShareMapper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


class SharingViewModel @Inject constructor(
    private val getAnimalDetailsUseCase: GetAnimalDetailsUseCase,
    private val uiAnimalToShareMapper: UiAnimalToShareMapper,
    private val compositeDisposable: CompositeDisposable
) : ViewModel() {

    val viewState: StateFlow<SharingViewState> get() = _viewState

    private val _viewState = MutableStateFlow(SharingViewState())

    fun onEvent(event: SharingEvent) {
        when (event) {
            is SharingEvent.GetAnimalToShare -> getAnimalToShare(event.animalId)
        }
    }

    private fun getAnimalToShare(animalId: Long) {
        getAnimalDetailsUseCase(animalId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { _viewState.value = SharingViewState(uiAnimalToShareMapper.mapToView(it)) },
                { onFailure(it) }
            ).addTo(compositeDisposable)
    }

    private fun onFailure(failure: Throwable) {

    }
}