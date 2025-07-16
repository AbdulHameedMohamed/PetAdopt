package com.example.sharing.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.common.data.PetFinderAnimalRepository
import com.example.common.domain.repository.AnimalRepository
import com.example.common.utils.CoroutineDispatchersProvider
import com.example.common.utils.DispatchersProvider
import com.example.sharing.presentation.SharingViewModel
import dagger.Binds
import dagger.Module
import dagger.Reusable
import dagger.hilt.migration.DisableInstallInCheck
import dagger.multibindings.IntoMap

@Module
@DisableInstallInCheck
abstract class SharingModule {

    @Binds
    @Reusable
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SharingViewModel::class)
    abstract fun bindSharingViewModel(
        sharingViewModel: SharingViewModel
    ): ViewModel

    @Binds
    abstract fun bindDispatchersProvider(
        dispatchersProvider: CoroutineDispatchersProvider
    ): DispatchersProvider

    @Binds
    abstract fun bindRepository(
        repository: PetFinderAnimalRepository
    ): AnimalRepository
}