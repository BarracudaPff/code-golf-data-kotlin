package src
import api.Fibonacci
import java.awt.Color
class Functional : Fibonacci {
    init {
        println("Initialize Fibonacci Functional")
    }
    override fun method(n : Long) : Long {
        fun _method(index: Long, ant: Long = 0, act: Long = 1): Long =
                when (index) {
                    0L -> ant
                    else -> _method(index - 1, act, ant + act)
                }
        return _method(n, 0, 1)
    }
}