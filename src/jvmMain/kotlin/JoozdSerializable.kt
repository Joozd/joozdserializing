package nl.joozd.serializing

/**
 * a JoozdSerializable can pack itself into a ByteArray, or be created from such a ByteArray
 */
interface JoozdSerializable{
    /**
     * Serialize a data class by wrapping all its components and stitching the wraps in a ByteArray
     */
    fun serialize(): ByteArray
    // Suggest to use like this:
    /*
    override fun serialize(): ByteArray {
        var serialized = ByteArray(0)

        serialized += wrap(component1())
        serialized += wrap(component2())
        serialized += wrap(component3())

        return serialized
    }
     */

    interface Deserializer<T: JoozdSerializable>{
        /**
         * Create a new instance of the JoozdSerializable class, instantiating it
         * from the given Parcel whose data had previously been written by
         * [serialize]
         *
         * NOTE: Unwrapping lists needs to be done with unwrapList()
         *
         * @param source The Parcel to read the object's data from.
         * @return Returns a new instance of the JoozdSerializable class.
         */
        fun deserialize(source: ByteArray): T

        //Suggested implementation:
        /*
        override fun deserialize(source: ByteArray): T {
            val wraps = serializedToWraps(source)
            return T(
                unwrap(wraps[0]),
                unwrap(wraps[1]),
                unwrapList(wraps[2])
            )
        }
        */


        fun serializedToWraps(bytes: ByteArray): List<ByteArray>{
            var index = 0
            val wraps = mutableListOf<ByteArray>()
            while (index < bytes.size){
                wraps.add(nextWrap(bytes, index).also{
                    index += it.size
                })
            }
            return wraps
        }


    }
}