package dev.moru3.compsql

interface Select {
    fun where(where: Where): Select
}

interface Where {

}

interface WhereKey {
    fun equals(any: Any): Select


}

class TestYo() {
    fun main() {

    }
}