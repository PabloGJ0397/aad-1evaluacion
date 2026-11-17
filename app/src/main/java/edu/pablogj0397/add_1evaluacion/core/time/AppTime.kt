package edu.pablogj0397.add_1evaluacion.core.time

interface AppTime{
    fun currentTime(): Long
}

class LocalAppTime : AppTime{
    override fun currentTime(): Long {
        return System.currentTimeMillis()
    }

}