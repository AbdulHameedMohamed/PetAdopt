package com.example.animals.domain.usecases

import android.util.Log
import com.example.common.domain.model.animal.Animal
import com.example.common.domain.repository.AnimalRepository
import io.reactivex.Flowable
import javax.inject.Inject
class GetAnimalsUseCase @Inject constructor(
    private val animalRepository: AnimalRepository
) {
    operator fun invoke(): Flowable<List<Animal>> {
        return animalRepository.getAnimals()
            .doOnNext { animals ->
                Log.d("abdulhameed", "Before filter: $animals")
            }
            .filter { it.isNotEmpty() }
            .doOnNext { animals ->
                Log.d("abdulhameed", "After filter: $animals")
            }
    }
}