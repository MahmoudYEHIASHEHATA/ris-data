package com.cassbana.risk.workers.di

import android.os.Build
import com.cassbana.risk.BuildConfig
import com.cassbana.risk.data.source.local.RSUserDataSource
import com.cassbana.risk.data.source.prefs.preferencesGateway
import com.cassbana.risk.RSConstants
import com.cassbana.risk.RSDomain
import com.cassbana.risk.utils.RSUtils
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.io.File
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


const val LOGGING_INTERCEPTOR_RS = "logging-interceptor-RS"
const val NETWORK_INTERCEPTOR_RS = "network-interceptor-RS"
const val ONLINE_CACHE_INTERCEPTOR_RS = "online-cache-interceptor-RS"
const val OFFLINE_CACHE_INTERCEPTOR_RS = "offline-cache-interceptor-RS"
const val CACHE_INTERCEPTOR_RS = "cache-interceptor-RS"
const val LANGUAGE_INTERCEPTOR_RS = "language-interceptor-RS"
const val GZIP_INTERCEPTOR_RS = "GZIP_interceptor-RS"
const val DEFAULT_OKHTTP_RS = "DEFAULT-OKHTTP-RS"
const val GZIP_OKHTTP_RS = "GZIP-OKHTTP-RS"
const val CALL_ADAPTER_RS = "call-adapter-RS"
const val DEFAULT_RETROFIT_RS = "DEFAULT_RETROFIT-RS"
const val FRAUD_RETROFIT_RS = "FRAUD_RETROFIT-RS"
const val OCR_RETROFIT = "OCR_RETROFIT-RS"

fun provideHTTPLoggingInterceptor(): HttpLoggingInterceptor {
    val interceptor = HttpLoggingInterceptor { message -> Timber.e(message) };
    interceptor.level = HttpLoggingInterceptor.Level.BODY
    return interceptor
}

fun provideNetworkInterceptor(): Interceptor {

    return Interceptor { chain ->
        val original = chain.request()
        val request = original.newBuilder()
        runBlocking {
            val job = CoroutineScope(Dispatchers.IO).launch {
                request.addHeader("Authorization","Bearer " + preferencesGateway.load(RSConstants.FCM_TOKEN,""))
            }
            job.join()
        }
        chain.proceed(request.build())
    }
}

fun provideLanguageInterceptor(): Interceptor {
    return Interceptor { chain ->
        val original = chain.request()
        val request = original.newBuilder()
        runBlocking {
            val job = CoroutineScope(Dispatchers.IO).launch {
                val cachedLocale = preferencesGateway.load(
                    com.cassbana.risk.RSConstants.APP_LANGUAGE,
                    com.cassbana.risk.RSConstants.DEFAULT_LANGUAGE
                )

                request.addHeader("Accept-Language", cachedLocale)
                request.addHeader("Content-Type", com.cassbana.risk.RSConstants.CONTENT_TYPE)
                request.addHeader("Accept", com.cassbana.risk.RSConstants.CONTENT_TYPE)
            }
            job.join()
        }
        chain.proceed(request.build())
    }
}

fun provideOnlineCacheInterceptor(): Interceptor {
    return Interceptor { chain ->
        val request = chain.request()
        val originalResponse = chain.proceed(request)
        val cacheControl = originalResponse.header("Cache-Control")
        if ((cacheControl == null ||
                    cacheControl.contains("no-store") ||
                    cacheControl.contains("no-cache") ||
                    cacheControl.contains("must-revalidate") ||
                    cacheControl.contains("max-age=0"))
        ) {
            originalResponse
                .newBuilder()
                .removeHeader("Pragma")
                .header("Cache-Control", "public, max-age=" + 0)
                .build()
        } else
            originalResponse
    }
}

fun provideOfflineCacheInterceptor(): Interceptor {
    return Interceptor { chain ->
        var request = chain.request()
        try {
            chain.proceed(chain.request())
        } catch (e: Exception) {
            request = request.newBuilder()
                .removeHeader("Pragma")
                .header(
                    "Cache-Control",
                    "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7
                )
                .build()

            chain.proceed(request)
        }
    }
}

fun provideGZIPInterceptor() : Interceptor = GzipInterceptor()

fun provideCacheInterceptor(): Interceptor {
    return Interceptor { chain ->
        val originalResponse = chain.proceed(chain.request())
        if (RSUtils.isConnectingToInternet(com.cassbana.risk.RSDomain.application)) {
            val maxAge = 60
            originalResponse
                .newBuilder()
                .removeHeader("Pragma")
                .header("Cache-Control", "public, max-age=$maxAge")
                .build()
        } else {
            val maxStale = 60 * 60 * 24 * 7
            originalResponse
                .newBuilder()
                .removeHeader("Pragma")
                .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                .build()
        }
    }
}


// Create the Interceptor


fun provideOkHttpClient(
    cache: Cache?,
    httpLoggingInterceptor: HttpLoggingInterceptor,
    networkInterceptor: Interceptor,
    languageInterceptor: Interceptor,
    onlineCacheInterceptor: Interceptor,
    offlineCacheInterceptor: Interceptor,
    cacheInterceptor: Interceptor,
    gzipInterceptor: Interceptor?
): OkHttpClient {

    val trustAllCerts: Array<TrustManager> = arrayOf(
        object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<X509Certificate?>?, authType: String?) {
            }

            override fun checkServerTrusted(chain: Array<X509Certificate?>?, authType: String?) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }

        }
    )
    val sslContext: SSLContext = SSLContext.getInstance("TLS")
    sslContext.init(null, trustAllCerts, SecureRandom())

    return OkHttpClient().newBuilder().apply {
            connectTimeout(1, TimeUnit.MINUTES)
            readTimeout(1, TimeUnit.MINUTES)
            writeTimeout(1, TimeUnit.MINUTES)
            addInterceptor(languageInterceptor)
            addInterceptor(networkInterceptor)
            gzipInterceptor?.let { addInterceptor(it) }
            addInterceptor(httpLoggingInterceptor)
//        addNetworkInterceptor(cacheInterceptor)
            addNetworkInterceptor(onlineCacheInterceptor)
            addInterceptor(offlineCacheInterceptor)
            cache(cache)
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
                sslSocketFactory(sslContext.socketFactory, trustAllCerts[0] as X509TrustManager)
            hostnameVerifier { _, _ -> true }
    }.build()
}

fun provideRetrofit(
    url: String,
    okHttpClient: OkHttpClient,
    callAdapterFactory: CallAdapter.Factory,
    converterFactory: Converter.Factory
): Retrofit {
    return Retrofit
        .Builder().apply {
            client(okHttpClient)
            addConverterFactory(converterFactory)
            baseUrl(url)
            addCallAdapterFactory(callAdapterFactory)
        }.build()
}


fun provideJsonConverterFactory(gson: Gson): Converter.Factory {
    return GsonConverterFactory.create(gson);
}

fun provideCallAdapter(): CallAdapter.Factory {
    return RxJava2CallAdapterFactory.create();
}

fun provideCache(): Cache {
    return Cache(File(RSDomain.application.cacheDir, "Responses"), (10 * 1000 * 1000).toLong())
}

val networkModuleRS = module {
    single(named(LOGGING_INTERCEPTOR_RS)) { provideHTTPLoggingInterceptor() }
    single(named(NETWORK_INTERCEPTOR_RS)) { provideNetworkInterceptor() }
    single(named(ONLINE_CACHE_INTERCEPTOR_RS)) { provideOnlineCacheInterceptor() }
    single(named(OFFLINE_CACHE_INTERCEPTOR_RS)) { provideOfflineCacheInterceptor() }
    single(named(CACHE_INTERCEPTOR_RS)) { provideCacheInterceptor() }
    single(named(LANGUAGE_INTERCEPTOR_RS)) { provideLanguageInterceptor() }
    single { provideCache() }
    single { provideJsonConverterFactory(get()) }
    single(named(CALL_ADAPTER_RS)) { provideCallAdapter() }
    single (named(GZIP_INTERCEPTOR_RS)) { provideGZIPInterceptor()}
    single {
        GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()
    }
    single (named(DEFAULT_OKHTTP_RS)){
        provideOkHttpClient(
            cache = null,
            httpLoggingInterceptor = get(named(LOGGING_INTERCEPTOR_RS)),
            networkInterceptor = get(named(NETWORK_INTERCEPTOR_RS)),
            languageInterceptor = get(named(LANGUAGE_INTERCEPTOR_RS)),
            onlineCacheInterceptor = get(named(ONLINE_CACHE_INTERCEPTOR_RS)),
            offlineCacheInterceptor = get(named(OFFLINE_CACHE_INTERCEPTOR_RS)),
            cacheInterceptor = get(named(CACHE_INTERCEPTOR_RS)),
            gzipInterceptor = null
        )
    }
    single (named(GZIP_OKHTTP_RS)){
        provideOkHttpClient(
            get(),
            get(named(LOGGING_INTERCEPTOR_RS)),
            get(named(NETWORK_INTERCEPTOR_RS)),
            get(named(LANGUAGE_INTERCEPTOR_RS)),
            get(named(ONLINE_CACHE_INTERCEPTOR_RS)),
            get(named(OFFLINE_CACHE_INTERCEPTOR_RS)),
            get(named(CACHE_INTERCEPTOR_RS)),
            get(named(GZIP_INTERCEPTOR_RS))
        )
    }


    single(named(DEFAULT_RETROFIT_RS)) {
        provideRetrofit("https://cassbana-core-api-prod.azure-api.net/api/", get(named(DEFAULT_OKHTTP_RS)), get(named(CALL_ADAPTER_RS)), get())
    }

    single(named(FRAUD_RETROFIT_RS)) {
        provideRetrofit("https://datacollectorapidev.azurewebsites.net/api/",get(named(GZIP_OKHTTP_RS)), get(named(CALL_ADAPTER_RS)), get())
    }
}