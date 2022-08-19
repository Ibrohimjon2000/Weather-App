package uz.mobiler.lesson71

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uz.mobiler.lesson71.models.LocationModel
import uz.mobiler.lesson71.models.WeatherModel
import uz.mobiler.lesson71.networking.ApiClient
import uz.mobiler.lesson71.networking.ApiService
import uz.mobiler.lesson71.utils.UserResource

class MyViewModel(private var locationModel: LocationModel) : ViewModel() {

    private var liveData = MutableLiveData<UserResource>(UserResource.Loading)

    init {
        fetchWeather()
    }

    private fun fetchWeather() {
        ApiClient.getRetrofit().create(ApiService::class.java)
            .getWeather(
                locationModel.latitude,
                locationModel.longitude,
                "b7317871690035a07d1a67ad8eca5000"
            ).enqueue(object : Callback<WeatherModel> {
                override fun onResponse(
                    call: Call<WeatherModel>,
                    response: Response<WeatherModel>
                ) {
                    if (response.isSuccessful) {
                        liveData.postValue(UserResource.Success(response.body()))
                    } else {
                        when (response.code()) {
                            in 400..499 -> liveData.postValue(UserResource.Error("Client error"))
                            in 500..599 -> liveData.postValue(UserResource.Error("Server error"))
                        }
                    }
                }

                override fun onFailure(call: Call<WeatherModel>, t: Throwable) {
                    liveData.postValue(UserResource.Error(t.message ?: ""))
                }
            })
    }

    fun getWeather(): LiveData<UserResource> {
        return liveData
    }
}