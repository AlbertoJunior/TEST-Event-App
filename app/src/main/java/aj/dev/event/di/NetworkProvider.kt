package aj.dev.event.di

import aj.dev.event.network.EventsAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkProvider {

    @Provides
    @Singleton
    fun retrofitProvider(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(EventsAPI.BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun retrofitPokemonApiProvider(client: Retrofit): EventsAPI =
        client.create(EventsAPI::class.java)

    @Provides
    @Singleton
    fun okHttpClientProvider() = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .callTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()
}