package uz.mobiler.lesson71.utils

import uz.mobiler.lesson71.models.WeatherModel

sealed class UserResource {

    object Loading : UserResource()

    data class Success(val weatherModel: WeatherModel?) : UserResource()

    data class Error(val message: String) : UserResource()
}
