package kr.co.lion.farming_customer.fragment.famingLifeTools

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.MapsInitializer
import kr.co.lion.farming_customer.databinding.FragmentFarmingLifeToolsBinding

class FarmingLifeToolsFragment : Fragment() {
    lateinit var binding: FragmentFarmingLifeToolsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFarmingLifeToolsBinding.inflate(layoutInflater)

        MapsInitializer.initialize(requireContext(), MapsInitializer.Renderer.LATEST, null)


        return binding.root
    }
}