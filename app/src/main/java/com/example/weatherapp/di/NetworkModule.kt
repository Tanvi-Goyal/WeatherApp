package com.example.weatherapp.di

import com.example.weatherapp.utils.ApiConstants
import com.example.weatherapp.service.WeatherAPI
import com.example.weatherapp.repositories.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {
    @Provides
    fun provideBaseUrl(): String {
        return ApiConstants.BASE_URL
    }

    @Provides
    fun providesOkHttpClient(): OkHttpClient {
        val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val okHttpClient = OkHttpClient.Builder()

        // Add API KEY and Constant Query Params to all requests
        okHttpClient.addInterceptor(logger).addInterceptor { chain ->
            val original = chain.request()
            val originalHttpUrl = original.url
            val url = originalHttpUrl.newBuilder()
                .addQueryParameter("appid", ApiConstants.API_KEY)
                .build()
            val requestBuilder = original.newBuilder().url(url).build()
            chain.proceed(requestBuilder)
        }

        return okHttpClient.build()
    }

    @Provides
    fun providesConverterFactory(): Converter.Factory = MoshiConverterFactory.create()

    @Provides
    fun providesRetrofit(
        baseUrl: String,
        converterFactory: Converter.Factory,
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(converterFactory)
            .client(okHttpClient).build()
    }

    @Provides
    fun providesNewsApi(retrofit: Retrofit): WeatherAPI = retrofit.create(
        WeatherAPI::class.java)

    @Singleton
    @Provides
    fun providesNewsRepo(remote: WeatherAPI): WeatherRepository = WeatherRepository(remote)
}