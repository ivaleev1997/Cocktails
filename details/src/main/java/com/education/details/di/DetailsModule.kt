package com.education.details.di

import androidx.lifecycle.ViewModel
import com.education.details.repository.DetailsRepository
import com.education.details.repository.DetailsRepositoryImpl
import com.education.details.usecase.DetailsUseCase
import com.education.details.usecase.DetailsUseCaseImpl
import com.education.details.viewmodel.DetailsViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class DetailsModule {

    @Binds
    abstract fun bindsDetailsUseCase(detailsUseCaseImpl: DetailsUseCaseImpl): DetailsUseCase

    @Binds
    abstract fun bindsDetailsRepository(repositoryImpl: DetailsRepositoryImpl): DetailsRepository

    @Module
    companion object {
        @Provides
        @JvmStatic
        @Singleton
        fun provideSongsListViewModel(
            map: @JvmSuppressWildcards MutableMap<Class<out ViewModel>, ViewModel>,
            detailsUseCase: DetailsUseCase
        ): ViewModel = DetailsViewModel(detailsUseCase).also {
            map[DetailsViewModel::class.java] = it
        }

        @Provides
        @JvmStatic
        @Singleton
        fun provideTrigger(viewModel: ViewModel) = ViewModelTrigger()
    }
}

class ViewModelTrigger