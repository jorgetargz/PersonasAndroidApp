package com.jorgetargz.recycler.util

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object StringProviderModule {

    @Provides
    @Named("string_provider")
    fun getStringProvider(@ApplicationContext context: Context): StringProvider =
        StringProvider(context)
}
