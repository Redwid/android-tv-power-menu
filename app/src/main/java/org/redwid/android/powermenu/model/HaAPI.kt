package org.redwid.android.powermenu.model

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface HaAPI {

    @Headers("Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiI4YmIzMmZhNGI1NjA0MWFmYjc4OTZhYmUzMzkzZmM4OCIsImlhdCI6MTU5ODM4ODU1NSwiZXhwIjoxOTEzNzQ4NTU1fQ.NEZOCu3_wkGqiAfAU8hUBpB_d3ZtJBJKFtMj2aFM3LM")
    @POST("/api/events/{event}")
    fun fireEvent(@Path("event") event: String, @Body data: Event): Call<ResponseBody>

}