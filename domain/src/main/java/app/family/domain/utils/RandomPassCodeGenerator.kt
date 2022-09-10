package app.family.domain.utils

import java.util.*

class RandomPassCodeGenerator {

    fun generate(length: Int = 6): String {
        val r = Random()
        val sb = StringBuffer()
        while (sb.length < length) {
            sb.append(Integer.toHexString(r.nextInt()))
        }
        return sb.toString().substring(0, length)
    }
}