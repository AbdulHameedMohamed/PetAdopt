package com.example.common.di

import com.example.common.utils.DispatchersProvider
import com.example.common.data.PetFinderAnimalRepository
import com.example.common.domain.repository.AnimalRepository
import com.example.common.utils.CoroutineDispatchersProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import io.reactivex.disposables.CompositeDisposable

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class ActivityRetainedModule {

    @Binds
    @ActivityRetainedScoped
    abstract fun bindAnimalRepository(repository: PetFinderAnimalRepository): AnimalRepository

    @Binds
    abstract fun bindDispatchersProvider(dispatchersProvider: CoroutineDispatchersProvider): DispatchersProvider

    companion object {
        @Provides
        fun provideCompositeDisposable() = CompositeDisposable()
    }
}