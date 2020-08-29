package org.redwid.android.powermenu

import android.content.Context
import android.util.Log
import okhttp3.ResponseBody
import org.redwid.android.powermenu.model.Event
import org.redwid.android.powermenu.model.HaAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HaManager {

    fun notifyBootCompleted(context: Context?) {
        Log.d(LOG_TAG, "notifyBootCompleted()")
        val service: HaAPI = getRetrofit()
        val data: Call<ResponseBody> = service.fireEvent("rock64_boot_complete", Event("completed"))
        data.enqueue(getCallback())
    }

    fun notifySleep(context: Context?) {
        Log.d(LOG_TAG, "notifySleep()")
        val service: HaAPI = getRetrofit()
        val data: Call<ResponseBody> = service.fireEvent("rock64_sleep", Event("completed"))
        data.enqueue(getCallback())
    }

    fun notifyPowerOff(context: Context?) {
        Log.d(LOG_TAG, "notifySleep()")
        val service: HaAPI = getRetrofit()
        val data: Call<ResponseBody> = service.fireEvent("rock64_power_off", Event("completed"))
        data.enqueue(getCallback())
    }

    private fun getRetrofit(): HaAPI {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.29:8123/")
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