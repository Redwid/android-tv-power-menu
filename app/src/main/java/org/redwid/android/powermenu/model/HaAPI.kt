package org.redwid.android.powermenu.model

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface HaAPI {

    @POST("/api/events/{eventId}")
    fun fireEvent(@Path("eventId") eventId: String, @Body event: Event): Call<ResponseBody>

}