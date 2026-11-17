package edu.pablogj0397.add_1evaluacion.features.data

import android.content.Context
import edu.pablogj0397.add_1evaluacion.R
import edu.pablogj0397.add_1evaluacion.core.cache.AppCache
import edu.pablogj0397.add_1evaluacion.core.serialization.JsonSerializer
import edu.pablogj0397.add_1evaluacion.features.domain.Car

class CarXmlLocalDataSource (
    private val context: Context,
    private val serializer: JsonSerializer<List<Car>>,
    private val appCache: AppCache) {

    private val carKey = "carKey"
    private val ttl = 2 * 1000L
    private val sharedPref = context.getSharedPreferences(context.getString(R.string.xml_car_datasource),
        Context.MODE_PRIVATE)



    fun save(cars: List<Car>){
        sharedPref.edit().apply {
            putString(carKey,serializer.toJson(cars))
            apply()
        }
        appCache.updateTime(carKey)

    }

    fun saveOne(car: Car){
        val carList = listOf<Car>(car)
        save(carList)

    }
    fun findAll(): List<Car>{
        if (!appCache.isExpired(carKey,ttl)){
            sharedPref.getString(carKey,null)?.let{
                return serializer.fromJson(it)
            }
        }
        return emptyList()
    }

    fun findByPlate(plateNumber: String): Car?{
        return findAll().find { plateNumber == it.carPlate}
    }

    fun deleteByPlate(plateNumber: String){
       save(findAll().filterNot { plateNumber == it.carPlate })
    }

    fun update(car:Car){
        val cars = findAll().filterNot { it.carPlate == car.carPlate }.toMutableList()
        cars.add(car)
        save(cars)

    }
}