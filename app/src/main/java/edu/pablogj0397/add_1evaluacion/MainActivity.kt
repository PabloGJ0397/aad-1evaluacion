package edu.pablogj0397.add_1evaluacion

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import edu.pablogj0397.add_1evaluacion.core.cache.AppCacheTTL
import edu.pablogj0397.add_1evaluacion.core.serialization.GsonJsonSerializer
import edu.pablogj0397.add_1evaluacion.core.time.LocalAppTime
import edu.pablogj0397.add_1evaluacion.features.data.local.xml.CarXmlLocalDataSource
import edu.pablogj0397.add_1evaluacion.features.domain.Car

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


       val appTime = LocalAppTime()
       val appCache = AppCacheTTL(this,appTime)
       val serializer = GsonJsonSerializer.create<List<Car>>()
       val dataSource = CarXmlLocalDataSource(this,serializer,appCache)

       //Declaramos un mock de datos para probar que funciona
        val mock = listOf(
            Car("1111-AAA","1A","M1"),
            Car("2222-BBB","2B","M2"),
            Car("3333-CCC","3C","M3"),
            Car("4444-DDD","4D","M4")
        )

        //Probamos save() + findAll(), debe devolver el mock de datos
        dataSource.save(mock)
        Log.d("TEST","Prueba save + findAll: ${dataSource.findAll()}")

        //Probamos saveOne(), debe devolver la lista con el nuevo elemento añadido
        dataSource.saveOne(Car("5555-EEE","5E","M5"))
        Log.d("TEST","Prueba saveOne: ${dataSource.findAll()}")

        //Probamos findByPlate
        Log.d("TEST","Prueba findByPlate existente: ${dataSource.findByPlate("1111-AAA")}") //debe devolver el coche buscado
        Log.d("TEST","Prueba findByPlate inexistente: ${dataSource.findByPlate("9999-ZZZ")}") //debe devolver null

        //Probamos deleteByPlate
        dataSource.deleteByPlate("1111-AAA")
        Log.d("TEST","Prueba deleteByPlate existente: ${dataSource.findAll()}") //debe devolver la lista sin el coche 1, eliminado

        dataSource.deleteByPlate("8888-YYY")
        Log.d("TEST","Prueba deleteByPlate inexistente: ${dataSource.findAll()}") //debe devolver la lista intacta

        //Probamos update (sobre el coche 2)
        val cocheUpdated = mock[1].copy(model = "2B-MODIFIED")
        dataSource.update(cocheUpdated)
        Log.d("TEST","Prueba update: ${dataSource.findAll()}") //debe devolver la lista con el modelo del coche 2 modificado

        //Probamos que save no guarda coches con matricula duplicada
        val mock2 = listOf(
            Car("1111-AAA","1A","M1"),
            Car("1111-AAA","1A","M1"),
            Car("4444-DDD","4D","M4")
        )
        dataSource.save(mock2)
        Log.d("TEST","Prueba save con duplicados: ${dataSource.findAll()}") //debe devolver la lista solo con los coches 1 y 4, sin duplicados.



    }
}