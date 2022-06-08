package nl.joozd.serializing

/**
 * Takes a list of ByteArrays and returns it as a single ByteArray
 * wrapped with an Int.toBytearray() that describes its length
 */
fun packSerialized(series: List<ByteArray>): ByteArray = series.map{it.size.toByteArray().toList() + it.toList()}.flatten().toByteArray()

fun packSerializable(series: List<JoozdSerializable>): ByteArray = packSerialized(series.map{it.serialize()})

fun unpackSerialized(packed: ByteArray): List<ByteArray>{
    val list = mutableListOf<ByteArray>()
    var index = 0
    while (packed.size > index+3){
        val size =
            intFromBytes(packed.slice(index until index + 4))
        if (size == 0) break // catch padding zeroes
        list.add(packed.slice(index+4 until (index+size+4)).toByteArray())
        index += size+4
    }
    return list
}

fun <T: JoozdSerializable> unpackSerialized(packed: ByteArray, deserializer: (ByteArray) -> T): List<T> =
    unpackSerialized(packed).map { deserializer(it) }
