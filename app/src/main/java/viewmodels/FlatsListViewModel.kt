package viewmodels

import android.app.Application
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.lifecycle.*
import com.example.kvartirkaclient.R
import com.google.android.gms.location.LocationServices
import models.City
import models.Flat
import models.Flats
import models.LocationData
import retrofit2.Retrofit
import services.*

class FlatsListViewModel(private val application: Application): ViewModel() {

    private var _cityName = MutableLiveData<String>()
    val cityName: LiveData<String>
        get() = _cityName

    private var _flats = MutableLiveData<Flats>()
    val flats: LiveData<Flats>
        get() = _flats

    private var _cityLoading = MutableLiveData<Boolean>()
    val cityLoading: LiveData<Boolean>
        get() = _cityLoading

    private var _flatsLoading = MutableLiveData<Boolean>()
    val flatsLoading: LiveData<Boolean>
        get() = _flatsLoading

    private var _noFlatsFounded = MutableLiveData<Boolean>()
    val noFlatsFounded: LiveData<Boolean>
        get() = _noFlatsFounded

    private var _connectionIsUnavailable = MutableLiveData<Boolean>()
    val connectionIsUnavailable: LiveData<Boolean>
        get() = _connectionIsUnavailable


    private var cityList: List<City> = mutableListOf()

    private val MoscowCity = City(coordinates = LocationData(ltd=55.75, lng=37.61), name = "Москва", name_genitive = "", name_prep = "", id=18)

    private lateinit var apiClient: IKvartirkaAPI

    init {

         val isAvailable = CheckConnectionService.checkConnection(application)
        _connectionIsUnavailable.value = !isAvailable
        if (isAvailable) {
            apiClient = ApiClientInit()
            getFlatsByLocation()
        }
    }

    fun findCityFlats(cityName: String) {
        val kvartirkaDetectCityService = KvartirkaDetectCityService(apiClient)

        if (!cityList.isNullOrEmpty())
            getFoundedCityFlats(cityName)
        else {
            _flatsLoading.value = true
            kvartirkaDetectCityService.startGetAllCities {
                cityList = it.cities
                getFoundedCityFlats(cityName)
            }
        }
    }

    fun getFoundedCityFlats(cityName: String) {
        val filteredCityList = cityList.filter {
            it.name.toLowerCase()==cityName.toLowerCase()
                    || it.name_genitive.toLowerCase()==cityName.toLowerCase()
                    || it.name_prep.toLowerCase() ==cityName.toLowerCase()
        }
        if (!filteredCityList.isNullOrEmpty()) {
            _cityName.value = filteredCityList[0].name
            _flatsLoading.value = true
            KvartirkaFindFlatsService(apiClient, filteredCityList[0].id).startGetFlats { flats ->
                _flats.value = flats
                _flatsLoading.value = false
            }
        }
        else {
            _flatsLoading.value = false
            _noFlatsFounded.value = true
        }
    }

    private fun loadDefaultCity(service: KvartirkaDetectCityService) {
        service.startGetCity(MoscowCity.coordinates.ltd, MoscowCity.coordinates.lng) { city ->
            _cityLoading.value = false
            _cityName.value = application.applicationContext.getString(R.string.Moscow)
            _flatsLoading.value = true
            city?.let {
                KvartirkaFindFlatsService(apiClient, city.id).startGetFlats {
                        flats ->
                    _flatsLoading.value = false
                    _flats.value = flats
                }
            }

        }
    }

    private fun getFlatsByLocation() {
        val kvartirkaDetectCityService = KvartirkaDetectCityService(apiClient)

        LocationService(application.applicationContext, {
            //onSuccess
            _cityLoading.value = true
            kvartirkaDetectCityService .startGetCity(it.ltd, it.lng) { city ->
                if (city!=null) {
                    _cityLoading.value = false
                    _cityName.value = city.name
                    _flatsLoading.value = true
                    KvartirkaFindFlatsService(apiClient, city.id).startGetFlats {
                            flats ->
                        _flatsLoading.value = false
                        _flats.value = flats
                    }
                }
                else {
                    loadDefaultCity(kvartirkaDetectCityService)
                }
            }
        }, {
            //onFailure
            loadDefaultCity(kvartirkaDetectCityService)
        })
    }


    private fun ApiClientInit(): IKvartirkaAPI {
        val rClient = RetrofitService()
        val retrofit: Retrofit = rClient.GetRetrofitEntity()
        val client = retrofit.create<IKvartirkaAPI>(IKvartirkaAPI::class.java)

        return client
    }

}