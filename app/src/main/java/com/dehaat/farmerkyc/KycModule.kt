package com.dehaat.farmerkyc

import com.dehaat.androidbase.coroutine.Dispatchers
import com.dehaat.androidbase.coroutine.IDispatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class KycModule {

	@Provides
	fun provideDispatchers(): IDispatchers = Dispatchers()

	@Provides
	@Named("retrofit-kheti-moshi")
	fun provideRetrofit() = Retrofit.Builder().baseUrl("https://url").build()
}
