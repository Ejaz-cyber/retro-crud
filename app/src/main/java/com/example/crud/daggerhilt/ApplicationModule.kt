package com.example.crud.daggerhilt

import com.example.crud.utils.BASEURL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

//@Module
//@InstallIn(ActivityComponent::class)
class ApplicationModule {

//    @Provides
    fun provideBaseURL() : String = BASEURL
}