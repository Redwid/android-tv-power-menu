package org.redwid.android.powermenu

import android.content.Context
import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import org.redwid.android.powermenu.model.Event
import org.redwid.android.powermenu.model.HaAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class HaManager {

    fun notifyBootCompleted(context: Context?) {
        Log.d(LOG_TAG, "notifyBootCompleted()")
        fireEvent(context, "rock64_boot_complete")
    }

    fun notifySleep(context: Context?) {
        Log.d(LOG_TAG, "notifySleep()")
        fireEvent(context, "rock64_sleep")
    }

    fun notifyPowerOff(context: Context?) {
        Log.d(LOG_TAG, "notifySleep()")
        fireEvent(context, "rock64_power_off")
    }

    private fun fireEvent(context: Context?, eventId: String) {
        context?.let {
            val data: Call<ResponseBody> = getRetrofit(it).fireEvent(eventId, getEvent())
            data.enqueue(getCallback())
        }
    }

    private fun getEvent(): Event {
        return Event("completed")
    }

    private fun getRetrofit(context: Context): HaAPI {
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .followRedirects(true)
            .followSslRedirects(true)
            .addInterceptor { chain ->
                val newRequest = chain.request().newBuilder()
                    .addHeader("Authorization: Bearer", context.getString(R.string.ha_token))
                .build()
                chain.proceed(newRequest)
            }

        val retrofit = Retrofit.Builder()
            .baseUrl("http://${context.getString(R.string.ha_url)}}:8123/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service: HaAPI = retrofit.create<HaAPI>(HaAPI::class.java)
        return service
    }

    internal fun getCallback(): Callback<ResponseBody> {
        return object : Callback<ResponseBody> {

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Log.d(LOG_TAG, "getCallback().onResponse($call, $response)")
                Log.d(LOG_TAG, "getCallback().onResponse(): ${response.code()}:${response.message()}")
                if (response.isSuccessful) {
                    Log.d(LOG_TAG, "getCallback().onResponse(): ${response.body()?.string()}")
                } else {
                    Log.d(LOG_TAG, "getCallback().onResponse(), error: ${response.message()} - ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e(LOG_TAG, "getCallback().onFailure(), $t")
            }
        }
    }

    companion object {
        val LOG_TAG = HaManager::class.java.simpleName
    }
}