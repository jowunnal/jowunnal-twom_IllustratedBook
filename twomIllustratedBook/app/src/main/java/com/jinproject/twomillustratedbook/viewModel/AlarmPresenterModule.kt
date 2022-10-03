package com.jinproject.twomillustratedbook.viewModel

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AlarmPresenterModule {

    @Singleton
    @Provides
    fun getAlarmPresenterInstance():AlarmPresenter{
        return AlarmPresenter()
    }
}