package dev.moru3.compsql

interface ObjectSerializer {
    fun serializeObject(obj: Any): String {
        return when(obj) {
            is CharSequence -> "`${obj.toString().replace("`", "\\`")}`"
            else -> obj.toString().replace("`", "\\`")
        }
    }
}