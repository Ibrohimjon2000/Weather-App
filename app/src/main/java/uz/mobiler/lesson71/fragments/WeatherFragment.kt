package uz.mobiler.lesson71.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import uz.mobiler.lesson71.MyViewModel
import uz.mobiler.lesson71.R
import uz.mobiler.lesson71.databinding.FragmentWeatherBinding
import uz.mobiler.lesson71.models.LocationModel
import uz.mobiler.lesson71.utils.UserResource
import java.text.SimpleDateFormat
import java.util.*


private const val ARG_PARAM1 = "location"
private const val ARG_PARAM2 = "param2"

class WeatherFragment : Fragment() {
    private var param1: LocationModel? = null
    private var param2: String? = null
    private lateinit var binding: FragmentWeatherBinding
    private var latitude = 0.0
    private var longitude = 0.0
    private val TAG = "WeatherFragment"
    private lateinit var myViewModel: MyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getSerializable(ARG_PARAM1) as LocationModel?
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWeatherBinding.inflate(inflater, container, false)
        binding.apply {
            myViewModel = MyViewModel(param1!!)
            if (param1 != null) {
                latitude = param1!!.latitude
                longitude = param1!!.longitude
            }

            back.setOnClickListener {
                Navigation.findNavController(root).popBackStack()
            }

            myViewModel.getWeather().observe(requireActivity()) {

                when (it) {
                    is UserResource.Error -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    is UserResource.Loading -> {
                        progress.visibility = View.VISIBLE
                        locationName.visibility = View.INVISIBLE
                        sun.visibility = View.INVISIBLE
                        temperature.visibility = View.INVISIBLE
                        tempbelgi.visibility = View.INVISIBLE
                        main.visibility = View.INVISIBLE
                        humidity.visibility = View.INVISIBLE
                        pressure.visibility = View.INVISIBLE
                        maxTemperature.visibility = View.INVISIBLE
                        maxbelgi.visibility = View.INVISIBLE
                        minTemperature.visibility = View.INVISIBLE
                        minbelgi.visibility = View.INVISIBLE
                        sunrise.visibility = View.INVISIBLE
                        sunset.visibility = View.INVISIBLE
                        wind.visibility = View.INVISIBLE
                    }
                    is UserResource.Success -> {
                        progress.visibility = View.GONE
                        locationName.visibility = View.VISIBLE
                        sun.visibility = View.VISIBLE
                        temperature.visibility = View.VISIBLE
                        tempbelgi.visibility = View.VISIBLE
                        main.visibility = View.VISIBLE
                        humidity.visibility = View.VISIBLE
                        pressure.visibility = View.VISIBLE
                        maxTemperature.visibility = View.VISIBLE
                        maxbelgi.visibility = View.VISIBLE
                        minTemperature.visibility = View.VISIBLE
                        minbelgi.visibility = View.VISIBLE
                        sunrise.visibility = View.VISIBLE
                        sunset.visibility = View.VISIBLE
                        wind.visibility = View.VISIBLE
                        val main1 = it.weatherModel?.weather?.get(0)?.main
                        val name = it.weatherModel?.name
                        val windSpeed = it.weatherModel?.wind?.speed
                        val sunrise1 = it.weatherModel?.sys?.sunrise
                        val sunset1 = it.weatherModel?.sys?.sunset
                        val temp = it.weatherModel?.main?.temp
                        val tempMax = it.weatherModel?.main?.temp_max
                        val tempMin = it.weatherModel?.main?.temp_min
                        val pressure1 = it.weatherModel?.main?.pressure
                        val humidity1 = it.weatherModel?.main?.humidity

                        if (name!!.isNotEmpty()) {
                            locationName.text = name.toString()
                        } else {
                            locationName.text = "Name unknown"
                        }
                        temperature.text = (temp?.minus(273.15))?.toInt().toString()
                        main.text = main1.toString()
                        when (main1.toString()) {
                            "Clouds" -> {
                                sun.setImageResource(R.drawable.ic_cloud)
                            }
                            "Clear" -> {
                                sun.setImageResource(R.drawable.ic_sun)
                            }
                            "Rain" -> {
                                sun.setImageResource(R.drawable.ic_rain)
                            }
                            "Snow" -> {
                                sun.setImageResource(R.drawable.ic_snow)
                            }
                        }
                        wind.text = windSpeed.toString() + " m/s"
                        humidity.text = humidity1.toString() + " %"
                        pressure.text = pressure1.toString()
                        maxTemperature.text = (tempMax?.minus(273.15))?.toInt().toString()
                        minTemperature.text = (tempMin?.minus(273.15))?.toInt().toString()
                        sunrise.text = sunrise1?.let { it1 -> time(it1) }
                        sunset.text = sunset1?.let { it1 -> time(it1) }
                    }
                }
            }
        }
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: LocationModel, param2: String) =
            WeatherFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun time(mills: Int): String {
        val formatter1 = SimpleDateFormat("HH:mm")
        formatter1.timeZone = TimeZone.getTimeZone("UTC")
        return formatter1.format(mills)
    }
}