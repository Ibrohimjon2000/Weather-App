package uz.mobiler.lesson71.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import uz.mobiler.lesson71.R
import uz.mobiler.lesson71.databinding.FragmentMapsBinding
import uz.mobiler.lesson71.models.LocationModel

class MapsFragment : Fragment() {
    private lateinit var myMarker: Marker
    private lateinit var mMap: GoogleMap
    private lateinit var binding: FragmentMapsBinding

    private val callback = OnMapReadyCallback { googleMap ->
        mMap = googleMap
        binding.btnLocation.visibility = View.GONE
        setUpMap()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapsBinding.inflate(inflater, container, false)
        binding.apply {
            btnLocation.setOnClickListener {
                val bundle = Bundle()
                bundle.putSerializable(
                    "location",
                    LocationModel(myMarker.position.latitude, myMarker.position.longitude)
                )
                Navigation.findNavController(binding.root).navigate(R.id.weatherFragment, bundle)
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun setUpMap() {
        mMap.setOnMapClickListener { p0 ->
            mMap.clear()
            myMarker = mMap.addMarker(
                MarkerOptions()
                    .position(p0)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
            )!!
            binding.btnLocation.visibility = View.VISIBLE
        }
    }

    override fun onStop() {
        super.onStop()
        mMap.clear()
    }
}