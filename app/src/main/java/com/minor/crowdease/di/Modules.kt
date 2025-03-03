package com.minor.crowdease.di
import android.content.Context
import android.content.SharedPreferences
import com.minor.crowdease.data.dto.chat.FCMApi
import com.minor.crowdease.data.remote.FoodCourtService
import com.minor.crowdease.data.remote.LoginService
import com.minor.crowdease.data.remote.OrderService
import com.minor.crowdease.data.remote.ShopService
import com.minor.crowdease.utlis.Constants
import com.minor.crowdease.utlis.Constants.Companion.TOKENPREF
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Modules {

    @Provides
    @Singleton
    fun providesFoodCourtService():FoodCourtService{

        val client = OkHttpClient
            .Builder()
            .connectTimeout(2, TimeUnit.MINUTES)
            .callTimeout(2, TimeUnit.MINUTES).build()


        return Retrofit
            .Builder()
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.BASE_URL)
            .build()
            .create(FoodCourtService::class.java)

    }


    @Provides
    @Singleton
    fun providesShopService():ShopService{

        val client = OkHttpClient
            .Builder()
            .connectTimeout(2, TimeUnit.MINUTES)
            .callTimeout(2, TimeUnit.MINUTES).build()


        return Retrofit
            .Builder()
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.BASE_URL)
            .build()
            .create(ShopService::class.java)

    }

    @Provides
    @Singleton
    fun providesLoginService():LoginService{

        val client = OkHttpClient
            .Builder()
            .connectTimeout(2, TimeUnit.MINUTES)
            .callTimeout(2, TimeUnit.MINUTES).build()


        return Retrofit
            .Builder()
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.BASE_URL)
            .build()
            .create(LoginService::class.java)

    }



    @Provides
    @Singleton
    fun providesOrderService():OrderService{

        val client = OkHttpClient
            .Builder()
            .connectTimeout(2, TimeUnit.MINUTES)
            .callTimeout(2, TimeUnit.MINUTES).build()


        return Retrofit
            .Builder()
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.BASE_URL)
            .build()
            .create(OrderService::class.java)

    }




    @Provides
    @Singleton
    fun providesSharedPreferences(@ApplicationContext context: Context): SharedPreferences {

        return context.getSharedPreferences(TOKENPREF, Context.MODE_PRIVATE)

    }



    @Provides
    @Singleton
    fun providesChatService(): FCMApi {
        val client = OkHttpClient
            .Builder()
            .connectTimeout(2, TimeUnit.MINUTES)
            .callTimeout(2, TimeUnit.MINUTES).build()

        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(FCMApi::class.java)

    }





}