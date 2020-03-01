package com.education.core_api.mediator

interface MediatorProvider {

    fun provideDetailsMediator(): DetailsMediator

    fun provideFavoritesMediator(): FavoritesMediator

    fun provideMainListMediator(): MainListMediator

    fun provideSearchMediator(): SearchMediator
}