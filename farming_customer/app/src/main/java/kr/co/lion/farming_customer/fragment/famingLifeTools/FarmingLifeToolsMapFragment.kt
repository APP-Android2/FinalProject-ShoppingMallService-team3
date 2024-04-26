package kr.co.lion.farming_customer.fragment.famingLifeTools

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.dao.farmingLifeTools.RentalDao
import kr.co.lion.farming_customer.databinding.FragmentFarmingLifeToolsMapBinding
import kr.co.lion.farming_customer.fragment.famingLifeTools.adapter.FarmingLifeMapVPAdapter
import kr.co.lion.farming_customer.model.farminLifeTools.RentalModel

@Suppress("DEPRECATION")
class FarmingLifeToolsMapFragment() : Fragment() {
    lateinit var binding: FragmentFarmingLifeToolsMapBinding
    lateinit var mapVPAdapter: FarmingLifeMapVPAdapter

    // 위치 정보를 관리하는 객체
    lateinit var locationManager: LocationManager

    // 위치 측정이 성공하면 동작할 리스너
    var gpsLocationListener: FarmingLifeToolsMapFragment.MyLocationListener? = null
    var networkLocationListener: FarmingLifeToolsMapFragment.MyLocationListener? = null

    // 구글 지도 객체를 담을 프로퍼티
    lateinit var mainGoogleMap: GoogleMap

    // 현재 사용자 위치를 표시하기 위한 마커
    var myMarker: Marker? = null

    // 확인할 권한 목록
    val permissionList = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    var rentalList = mutableListOf<RentalModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFarmingLifeToolsMapBinding.inflate(layoutInflater)

        MapsInitializer.initialize(requireContext(), MapsInitializer.Renderer.LATEST, null)
        // 권한을 확인 받는다.
        requestPermissions(permissionList, 0)

        settingGoogleMap()


        return binding.root
    }

    private fun settingRentalData(location1: Location) {
        CoroutineScope(Dispatchers.Main).launch {
            rentalList = RentalDao.gettingRentalListByLocation(location1)
            settingMapAdapter(rentalList)
            settingViewPagerChange()
        }
    }

    private fun settingViewPagerChange() {
        binding.farmingLifeToolsMapVp.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val location = Location("rental")
                location.latitude = rentalList[position].rental_longitude!!
                location.longitude = rentalList[position].rental_latitude!!
                setMyLocation(location)
            }
        })
    }

    fun settingMapAdapter(rentalList: MutableList<RentalModel>) {
        mapVPAdapter = FarmingLifeMapVPAdapter(requireContext(), rentalList)
        binding.farmingLifeToolsMapVp.adapter = mapVPAdapter
        binding.farmingLifeToolsMapVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL
    }

    // 구글 지도 셋팅
    fun settingGoogleMap(){
        // MapFragment 객체를 가져온다.
        val supportMapFragment = childFragmentManager.findFragmentById(R.id.farming_life_tools_map_fragment) as SupportMapFragment

        supportMapFragment.getMapAsync {

            mainGoogleMap = it

            // 확대 축소 버튼
            mainGoogleMap.uiSettings.isZoomControlsEnabled = false

            mainGoogleMap.mapType = GoogleMap.MAP_TYPE_NORMAL

            // 위치 정보를 관리하는 객체를 가지고 온다.
            locationManager = requireActivity().getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager

            // 위치정보 사용 권한 허용 여부 확인
            val a1 = ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            val a2 = ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

            // 모든 권한이 허용되어 있다면
            if(a1 && a2){
                // 저장되어 있는 위치값을 가져온다.(없으면 null이 반환된다)
                val location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                val location2 = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

                // 현재 위치를 지도에 표시한다.
                if(location1 != null){
                    setMyLocation(location1)
                    settingRentalData(location1)
                } else if(location2 != null){
                    setMyLocation(location2)
                    settingRentalData(location2)
                }

                // 현재 위치를 측정한다.
                getMyLocation()
            }
        }
    }

    // 현재 위치를 가져오는 메서드
    fun getMyLocation(){
        // 위치정보 사용 권한 허용 여부 확인
        val a1 = ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED
        val a2 = ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED

        if(a1 || a2){
            return
        }

        // GPS 프로바이더 사용이 가능하다면
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) == true){
            if(gpsLocationListener == null) {
                gpsLocationListener = MyLocationListener()
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0.0f, gpsLocationListener!!)
            }
        }
        // Network 프로바이더 사용이 가능하다면
        if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) == true){
            if(networkLocationListener == null) {
                networkLocationListener = MyLocationListener()
            }
        }
    }

    // 지도의 위치를 설정하는 메서드
    fun setMyLocation(location: Location){
        // 위도와 경도를 관리하는 객체를 생성한다.
        val userLocation = LatLng(location.latitude, location.longitude)

        // 지도를 이동시키기 위한 객체를 생성한다.
        // 첫 번째 : 표시할 지도의 위치(위도 경고)
        // 두 번째 : 줌 배율
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(userLocation, 15.0f)

        // 카메라를 이동
        mainGoogleMap.animateCamera(cameraUpdate)

        // 현재위치를 담은 마커를 생성한다.
        val markerOptions = MarkerOptions()
        markerOptions.position(userLocation)

        // 이미 마커가 있다면 제거한다.
        if(myMarker != null){
            myMarker?.remove()
            myMarker = null
        }

        // 마커의 이미지를 변경한다.
        val markerBitmap = BitmapDescriptorFactory.fromResource(R.drawable.ic_marker)
        markerOptions.icon(markerBitmap)

        myMarker = mainGoogleMap.addMarker(markerOptions)
    }

    // 위치 측정이 성공하면 동작하는 리스너
    inner class MyLocationListener() : LocationListener {
        // 위치가 측정되면 호출되는 메서드
        // location : 측정된 위치 정보가 담긴 객체
        override fun onLocationChanged(location: Location) {
            // 사용한 위치 정보 프로바이더로 분기한다.
            when(location.provider){
                // GPS 프로바이더 라면
                LocationManager.GPS_PROVIDER -> {
                    // GPS 리스너 연결을 해제해 준다.
                    locationManager.removeUpdates(gpsLocationListener!!)
                    gpsLocationListener = null
                }
                // NetworkProvider 라면
                LocationManager.NETWORK_PROVIDER -> {
                    // 네트워크 리스너 연결을 해제해 준다.
                    locationManager.removeUpdates(networkLocationListener!!)
                    networkLocationListener = null
                }
            }
            // 측정된 위치로 지도를 움직인다.
            setMyLocation(location)
        }
    }
}