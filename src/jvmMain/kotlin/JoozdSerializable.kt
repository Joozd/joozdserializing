package nl.joozd.joozdlogcommon.serializing

/**
 * a JoozdSerializable can pack itself into a ByteArray, or be created from such a ByteArray
 */
interface JoozdSerializable{
    fun serialize(): ByteArray

    interface Deserializer<T: JoozdSerializable>{
        /**
         * Create a new instance of the JoozdSerializable class, instantiating it
         * from the given Parcel whose data had previously been written by
         * [serialize]
         *
         * @param source The Parcel to read the object's data from.
         * @return Returns a new instance of the JoozdSerializable class.
         */
        fun deserialize(source: ByteArray): T

        fun serializedToWraps(bytes: ByteArray): List<ByteArray>{
            val bb = bytes.toList().toMutableList()
            val wraps = mutableListOf<ByteArray>()
            while (bb.isNotEmpty()){
                wraps.add(nextWrap(bb.toByteArray()))
                repeat(wraps.last().size){ bb.removeAt(0)}
            }
            return wraps
        }
    }
}