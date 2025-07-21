package com.example.animals.presentation.animal_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.animals.presentation.animal_details.model.mappers.UiAnimalDetailsMapper
import com.example.common.domain.model.animal.Animal
import com.example.common.domain.usecases.GetAnimalDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class AnimalDetailsViewModel @Inject constructor(
    private val uiAnimalDetailsMapper: UiAnimalDetailsMapper,
    private val getAnimalDetailsUseCase: GetAnimalDetailsUseCase,
    private val compositeDisposable: CompositeDisposable
) : ViewModel() {

    val state: LiveData<AnimalDetailsViewState> get() = _state
    private val _state = MutableLiveData<AnimalDetailsViewState>()

    init {
        _state.value = AnimalDetailsViewState.Loading
    }

    fun handleEvent(event: AnimalDetailsEvent) {
        when (event) {
            is AnimalDetailsEvent.LoadAnimalDetails -> subscribeToAnimalDetails(event.animalId)
            AnimalDetailsEvent.AdoptAnimal -> adoptAnimal()
        }
    }

    private fun subscribeToAnimalDetails(animalId: Long) {
        getAnimalDetailsUseCase(animalId)
            .delay(2L, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { onAnimalsDetails(it) },
                { onFailure(it) }
            )
            .addTo(compositeDisposable)
    }

    private fun onAnimalsDetails(animal: Animal) {
        val animalDetails = uiAnimalDetailsMapper.mapToView(animal)
        _state.value = AnimalDetailsViewState.AnimalDetails(animalDetails)
    }

    private fun adoptAnimal() {
        compositeDisposable.add(
            Observable.timer(2L, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    _state.value = (_state.value as AnimalDetailsViewState.AnimalDetails?)?.copy(adopted = true)
                }
        )
    }

    private fun onFailure(failure: Throwable) {
        _state.value = AnimalDetailsViewState.Failure
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}