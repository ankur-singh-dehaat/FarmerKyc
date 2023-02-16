package com.dehaat.kyc.di

import com.dehaat.kyc.KycRepository
import com.dehaat.kyc.KycRepositoryImpl
import com.dehaat.kyc.OcrRepository
import com.dehaat.kyc.OcrRepositoryImpl
import com.dehaat.kyc.framework.network.KycAPIService
import com.dehaat.kyc.framework.repo.FarmerKycRepository
import com.dehaat.kyc.framework.repo.FarmerKycRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
abstract class KycModule {

	@Binds
	abstract fun bindOcrRepository(
		ocrRepositoryImpl: OcrRepositoryImpl
	): OcrRepository

	@Binds
	abstract fun bindKycRepository(
		kycRepository: KycRepositoryImpl
	): KycRepository

	@Binds
	abstract fun bindFarmerKycRepository(
		farmerKycRepositoryImpl: FarmerKycRepositoryImpl
	): FarmerKycRepository

	companion object {

		@Provides
		fun provideCreditProgramAPIService(
			@Named("retrofit-kheti-moshi") retrofit: Retrofit
		): KycAPIService = retrofit.create(KycAPIService::class.java)
	}
}
