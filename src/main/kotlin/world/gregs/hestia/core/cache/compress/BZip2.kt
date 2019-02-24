package world.gregs.hestia.core.cache.compress

object BZip2 {

    class BlockEntry {
        internal var aBooleanArray2205 = BooleanArray(16)
        internal var aBooleanArray2213 = BooleanArray(256)
        internal var aByte2201: Byte = 0
        internal var aByteArray2204 = ByteArray(4096)
        internal var aByteArray2211 = ByteArray(256)
        internal var aByteArray2212: ByteArray? = null
        internal var aByteArray2214 = ByteArray(18002)
        internal var aByteArray2219 = ByteArray(18002)
        internal var aByteArray2224: ByteArray? = null
        internal var aByteArrayArray2229 = Array(6) { ByteArray(258) }
        internal var anInt2202: Int = 0
        internal var anInt2203 = 0
        internal var anInt2206: Int = 0
        internal var anInt2207: Int = 0
        internal var anInt2208: Int = 0
        internal var anInt2209 = 0
        internal var anInt2215: Int = 0
        internal var anInt2216: Int = 0
        internal var anInt2217: Int = 0
        internal var anInt2221: Int = 0
        internal var anInt2222: Int = 0
        internal var anInt2223: Int = 0
        internal var anInt2225: Int = 0
        internal var anInt2227: Int = 0
        internal var anInt2232: Int = 0
        internal var anIntArray2200 = IntArray(6)
        internal var anIntArray2220 = IntArray(257)
        internal var anIntArray2226 = IntArray(16)
        internal var anIntArray2228 = IntArray(256)
        internal var anIntArrayArray2210 = Array(6) { IntArray(258) }
        internal var anIntArrayArray2218 = Array(6) { IntArray(258) }
        internal var anIntArrayArray2230 = Array(6) { IntArray(258) }
    }

    private var anIntArray257: IntArray? = null
    private var entryInstance = BlockEntry()

    fun decompress(decompressedData: ByteArray, packedData: ByteArray, blockSize: Int) {
        synchronized(entryInstance) {
            entryInstance.aByteArray2224 = packedData
            entryInstance.anInt2209 = blockSize
            entryInstance.aByteArray2212 = decompressedData
            entryInstance.anInt2203 = 0
            entryInstance.anInt2206 = decompressedData.size
            entryInstance.anInt2232 = 0
            entryInstance.anInt2207 = 0
            entryInstance.anInt2217 = 0
            entryInstance.anInt2216 = 0
            method1793(entryInstance)
            entryInstance.aByteArray2224 = null
            entryInstance.aByteArray2212 = null
        }
    }

    private fun method1785(entry: BlockEntry) {
        entry.anInt2215 = 0
        for (i in 0..255) {
            if (entry.aBooleanArray2213[i]) {
                entry.aByteArray2211[entry.anInt2215] = i.toByte()
                ++entry.anInt2215
            }
        }
    }

    private fun method1786(ai: IntArray, ai1: IntArray, ai2: IntArray, abyte0: ByteArray, i: Int, j: Int, k: Int) {
        var l = 0

        var i3: Int
        var k2: Int
        i3 = i
        while (i3 <= j) {
            k2 = 0
            while (k2 < k) {
                if (abyte0[k2].toInt() == i3) {
                    ai2[l] = k2
                    ++l
                }
                ++k2
            }
            ++i3
        }

        i3 = 0
        while (i3 < 23) {
            ai1[i3] = 0
            ++i3
        }

        i3 = 0
        while (i3 < k) {
            ++ai1[abyte0[i3] + 1]
            ++i3
        }

        i3 = 1
        while (i3 < 23) {
            ai1[i3] += ai1[i3 - 1]
            ++i3
        }

        i3 = 0
        while (i3 < 23) {
            ai[i3] = 0
            ++i3
        }

        i3 = 0

        k2 = i
        while (k2 <= j) {
            i3 += ai1[k2 + 1] - ai1[k2]
            ai[k2] = i3 - 1
            i3 = i3 shl 1
            ++k2
        }

        k2 = i + 1
        while (k2 <= j) {
            ai1[k2] = (ai[k2 - 1] + 1 shl 1) - ai1[k2]
            ++k2
        }

    }

    private fun method1787(entry: BlockEntry) {
        var byte4 = entry.aByte2201
        var i = entry.anInt2222
        var j = entry.anInt2227
        var k = entry.anInt2221
        val ai = anIntArray257!!
        var l = entry.anInt2208
        val abyte0 = entry.aByteArray2212!!
        var i1 = entry.anInt2203
        var j1 = entry.anInt2206
        val l1 = entry.anInt2225 + 1

        label65@ while (true) {
            if (i > 0) {
                while (true) {
                    if (j1 == 0) {
                        break@label65
                    }

                    if (i == 1) {
                        if (j1 == 0) {
                            i = 1
                            break@label65
                        }

                        abyte0[i1] = byte4
                        ++i1
                        --j1
                        break
                    }

                    abyte0[i1] = byte4
                    --i
                    ++i1
                    --j1
                }
            }

            var flag = true

            var byte1: Byte
            while (flag) {
                flag = false
                if (j == l1) {
                    i = 0
                    break@label65
                }

                byte4 = k.toByte()
                l = ai[l]
                byte1 = (l and 255).toByte()
                l = l shr 8
                ++j
                if (byte1.toInt() != k) {
                    k = byte1.toInt()
                    if (j1 == 0) {
                        i = 1
                        break@label65
                    }

                    abyte0[i1] = byte4
                    ++i1
                    --j1
                    flag = true
                } else if (j == l1) {
                    if (j1 == 0) {
                        i = 1
                        break@label65
                    }

                    abyte0[i1] = byte4
                    ++i1
                    --j1
                    flag = true
                }
            }

            i = 2
            l = ai[l]
            byte1 = (l and 255).toByte()
            l = l shr 8
            ++j
            if (j != l1) {
                if (byte1.toInt() != k) {
                    k = byte1.toInt()
                } else {
                    i = 3
                    l = ai[l]
                    val byte2 = (l and 255).toByte()
                    l = l shr 8
                    ++j
                    if (j != l1) {
                        if (byte2.toInt() != k) {
                            k = byte2.toInt()
                        } else {
                            l = ai[l]
                            val byte3 = (l and 255).toByte()
                            l = l shr 8
                            ++j
                            i = (byte3.toInt() and 255) + 4
                            l = ai[l]
                            k = (l and 255).toByte().toInt()
                            l = l shr 8
                            ++j
                        }
                    }
                }
            }
        }

        entry.anInt2216 += j1 - j1
        entry.aByte2201 = byte4
        entry.anInt2222 = i
        entry.anInt2227 = j
        entry.anInt2221 = k
        anIntArray257 = ai
        entry.anInt2208 = l
        entry.aByteArray2212 = abyte0
        entry.anInt2203 = i1
        entry.anInt2206 = j1
    }

    private fun method1788(entry: BlockEntry): Int {
        return method1790(1, entry)
    }

    private fun method1789(entry: BlockEntry): Int {
        return method1790(8, entry)
    }

    private fun method1790(i: Int, entry: BlockEntry): Int {
        while (entry.anInt2232 < i) {
            entry.anInt2207 = entry.anInt2207 shl 8 or (entry.aByteArray2224!![entry.anInt2209].toInt() and 255)
            entry.anInt2232 += 8
            ++entry.anInt2209
            ++entry.anInt2217
        }

        val k = entry.anInt2207 shr entry.anInt2232 - i and (1 shl i) - 1
        entry.anInt2232 -= i
        return k
    }

    private fun method1793(entry: BlockEntry) {
        var j8 = 0
        var ai: IntArray? = null
        var ai1: IntArray? = null
        var ai2: IntArray? = null
        entry.anInt2202 = 1
        if (anIntArray257 == null) {
            anIntArray257 = IntArray(entry.anInt2202 * 100000)
        }

        var flag18 = true

        while (true) {
            while (flag18) {
                var byte0 = method1789(entry)
                if (byte0 == 23) {
                    return
                }

                method1789(entry)
                method1789(entry)
                method1789(entry)
                method1789(entry)
                method1789(entry)
                method1789(entry)
                method1789(entry)
                method1789(entry)
                method1789(entry)
                method1788(entry)
                entry.anInt2223 = 0
                byte0 = method1789(entry)
                entry.anInt2223 = entry.anInt2223 shl 8 or (byte0 and 255)
                byte0 = method1789(entry)
                entry.anInt2223 = entry.anInt2223 shl 8 or (byte0 and 255)
                byte0 = method1789(entry)
                entry.anInt2223 = entry.anInt2223 shl 8 or (byte0 and 255)

                var i4 = 0
                while (i4 < 16) {
                    val byte1 = method1788(entry)
                    entry.aBooleanArray2205[i4] = byte1 == 1
                    ++i4
                }

                i4 = 0
                while (i4 < 256) {
                    entry.aBooleanArray2213[i4] = false
                    ++i4
                }

                var j4: Int
                i4 = 0
                while (i4 < 16) {
                    if (entry.aBooleanArray2205[i4]) {
                        j4 = 0
                        while (j4 < 16) {
                            val byte2 = method1788(entry)
                            if (byte2 == 1) {
                                entry.aBooleanArray2213[i4 * 16 + j4] = true
                            }
                            ++j4
                        }
                    }
                    ++i4
                }

                method1785(entry)
                i4 = entry.anInt2215 + 2
                j4 = method1790(3, entry)
                val k4 = method1790(15, entry)

                var l4: Int
                var byte8: Int
                for (i1 in 0 until k4) {
                    l4 = 0

                    while (true) {
                        byte8 = method1788(entry)
                        if (byte8 == 0) {
                            entry.aByteArray2214[i1] = l4.toByte()
                            break
                        }

                        ++l4
                    }
                }

                val abyte0 = ByteArray(6)

                var byte16 = 0
                while (byte16 < j4) {
                    abyte0[byte16] = (byte16++).toByte()
                }

                var i: Byte
                l4 = 0
                var value: Int
                while (l4 < k4) {
                    value = entry.aByteArray2214[l4].toInt()

                    i = abyte0[value]
                    while (value > 0) {
                        abyte0[value] = abyte0[value - 1]
                        --value
                    }

                    abyte0[0] = i
                    entry.aByteArray2219[l4] = i
                    ++l4
                }

                var i5: Int
                var j5: Int
                l4 = 0
                while (l4 < j4) {
                    i5 = method1790(5, entry)

                    j5 = 0
                    while (j5 < i4) {
                        while (true) {
                            var byte4 = method1788(entry)
                            if (byte4 == 0) {
                                entry.aByteArrayArray2229[l4][j5] = i5.toByte()
                                break
                            }

                            byte4 = method1788(entry)
                            if (byte4 == 0) {
                                ++i5
                            } else {
                                --i5
                            }
                        }
                        ++j5
                    }
                    ++l4
                }

                var i9: Int
                l4 = 0
                while (l4 < j4) {
                    byte8 = 32
                    i = 0

                    i9 = 0
                    while (i9 < i4) {
                        if (entry.aByteArrayArray2229[l4][i9] > i) {
                            i = entry.aByteArrayArray2229[l4][i9]
                        }

                        if (entry.aByteArrayArray2229[l4][i9] < byte8) {
                            byte8 = entry.aByteArrayArray2229[l4][i9].toInt()
                        }
                        ++i9
                    }

                    method1786(entry.anIntArrayArray2230[l4], entry.anIntArrayArray2218[l4], entry.anIntArrayArray2210[l4], entry.aByteArrayArray2229[l4], byte8, i.toInt(), i4)
                    entry.anIntArray2200[l4] = byte8
                    ++l4
                }

                l4 = entry.anInt2215 + 1
                i5 = -1

                i9 = 0
                while (i9 <= 255) {
                    entry.anIntArray2228[i9] = 0
                    ++i9
                }

                i9 = 4095

                var l5 = 15
                var l6: Int
                while (l5 >= 0) {
                    l6 = 15
                    while (l6 >= 0) {
                        entry.aByteArray2204[i9] = (l5 * 16 + l6).toByte()
                        --i9
                        --l6
                    }

                    entry.anIntArray2226[l5] = i9 + 1
                    --l5
                }

                l5 = 0
                var i_46_ = 0
                if (i_46_ == 0) {
                    ++i5
                    i_46_ = 50
                    val byte12 = entry.aByteArray2219[i5].toInt()
                    j8 = entry.anIntArray2200[byte12]
                    ai = entry.anIntArrayArray2230[byte12]
                    ai2 = entry.anIntArrayArray2210[byte12]
                    ai1 = entry.anIntArrayArray2218[byte12]
                }

                i_46_ -= 1
                l6 = j8

                var k7: Int
                var byte9: Int
                k7 = method1790(j8, entry)
                while (k7 > ai!![l6]) {
                    ++l6
                    byte9 = method1788(entry)
                    k7 = k7 shl 1 or byte9
                }

                var k5 = ai2!![k7 - ai1!![l6]]

                while (true) {
                    var var10000: IntArray
                    while (k5 != l4) {
                        var i6: Int
                        var byte5: Int
                        var i8: Int
                        var byte11: Int
                        var j7: Int
                        if (k5 != 0 && k5 != 1) {
                            i6 = k5 - 1
                            val byte6: Byte
                            if (i6 < 16) {
                                j7 = entry.anIntArray2226[0]

                                byte6 = entry.aByteArray2204[j7 + i6]
                                while (i6 > 3) {
                                    i8 = j7 + i6
                                    entry.aByteArray2204[i8] = entry.aByteArray2204[i8 - 1]
                                    entry.aByteArray2204[i8 - 1] = entry.aByteArray2204[i8 - 2]
                                    entry.aByteArray2204[i8 - 2] = entry.aByteArray2204[i8 - 3]
                                    entry.aByteArray2204[i8 - 3] = entry.aByteArray2204[i8 - 4]
                                    i6 -= 4
                                }

                                while (i6 > 0) {
                                    entry.aByteArray2204[j7 + i6] = entry.aByteArray2204[j7 + i6 - 1]
                                    --i6
                                }

                                entry.aByteArray2204[j7] = byte6
                            } else {
                                j7 = i6 / 16
                                i8 = i6 % 16
                                var j10 = entry.anIntArray2226[j7] + i8

                                byte6 = entry.aByteArray2204[j10]
                                while (j10 > entry.anIntArray2226[j7]) {
                                    entry.aByteArray2204[j10] = entry.aByteArray2204[j10 - 1]
                                    --j10
                                }

                                entry.anIntArray2226[j7]++
                                while (j7 > 0) {
                                    entry.anIntArray2226[j7]--
                                    entry.aByteArray2204[entry.anIntArray2226[j7]] = entry.aByteArray2204[entry.anIntArray2226[j7 - 1] + 16 - 1]
                                    --j7
                                }

                                entry.anIntArray2226[0]--
                                entry.aByteArray2204[entry.anIntArray2226[0]] = byte6
                                if (entry.anIntArray2226[0] == 0) {
                                    var l9 = 4095

                                    for (j9 in 15 downTo 0) {
                                        for (k9 in 15 downTo 0) {
                                            entry.aByteArray2204[l9] = entry.aByteArray2204[entry.anIntArray2226[j9] + k9]
                                            --l9
                                        }

                                        entry.anIntArray2226[j9] = l9 + 1
                                    }
                                }
                            }

                            entry.anIntArray2228[entry.aByteArray2211[byte6.toInt() and 255].toInt() and 255]++
                            anIntArray257!![l5] = entry.aByteArray2211[byte6.toInt() and 255].toInt() and 255
                            ++l5
                            if (i_46_ == 0) {
                                ++i5
                                i_46_ = 50
                                byte5 = entry.aByteArray2219[i5].toInt()
                                j8 = entry.anIntArray2200[byte5]
                                ai = entry.anIntArrayArray2230[byte5]
                                ai2 = entry.anIntArrayArray2210[byte5]
                                ai1 = entry.anIntArrayArray2218[byte5]
                            }

                            --i_46_
                            j7 = j8

                            i8 = method1790(j8, entry)
                            while (i8 > ai!![j7]) {
                                ++j7
                                byte11 = method1788(entry)
                                i8 = i8 shl 1 or byte11
                            }

                            k5 = ai2!![i8 - ai1!![j7]]
                        } else {
                            i6 = -1
                            var j6 = 1

                            do {
                                if (k5 == 0) {
                                    i6 += j6
                                } else if (k5 == 1) {
                                    i6 += 2 * j6
                                }

                                j6 *= 2
                                if (i_46_ == 0) {
                                    ++i5
                                    i_46_ = 50
                                    byte5 = entry.aByteArray2219[i5].toInt()
                                    j8 = entry.anIntArray2200[byte5]
                                    ai = entry.anIntArrayArray2230[byte5]
                                    ai2 = entry.anIntArrayArray2210[byte5]
                                    ai1 = entry.anIntArrayArray2218[byte5]
                                }

                                --i_46_
                                j7 = j8

                                i8 = method1790(j8, entry)
                                while (i8 > ai!![j7]) {
                                    ++j7
                                    byte11 = method1788(entry)
                                    i8 = i8 shl 1 or byte11
                                }

                                k5 = ai2!![i8 - ai1!![j7]]
                            } while (k5 == 0 || k5 == 1)

                            ++i6
                            byte5 = entry.aByteArray2211[entry.aByteArray2204[entry.anIntArray2226[0]].toInt() and 255].toInt()
                            var10000 = entry.anIntArray2228

                            var10000[byte5 and 255] += i6
                            while (i6 > 0) {
                                anIntArray257!![l5] = byte5 and 255
                                ++l5
                                --i6
                            }
                        }
                    }

                    entry.anInt2222 = 0
                    entry.aByte2201 = 0
                    entry.anIntArray2220[0] = 0

                    k5 = 1
                    while (k5 <= 256) {
                        entry.anIntArray2220[k5] = entry.anIntArray2228[k5 - 1]
                        ++k5
                    }

                    k5 = 1
                    while (k5 <= 256) {
                        var10000 = entry.anIntArray2220
                        var10000[k5] += entry.anIntArray2220[k5 - 1]
                        ++k5
                    }

                    k5 = 0
                    while (k5 < l5) {
                        val byte7 = anIntArray257!![k5] and 255
                        var10000 = anIntArray257!!
                        val var10001 = entry.anIntArray2220[byte7 and 255]
                        var10000[var10001] = var10000[var10001] or (k5 shl 8)
                        entry.anIntArray2220[byte7 and 255]++
                        ++k5
                    }

                    entry.anInt2208 = anIntArray257!![entry.anInt2223] shr 8
                    entry.anInt2227 = 0
                    entry.anInt2208 = anIntArray257!![entry.anInt2208]
                    entry.anInt2221 = (entry.anInt2208 and 255).toByte().toInt()
                    entry.anInt2208 = entry.anInt2208 shr 8
                    ++entry.anInt2227
                    entry.anInt2225 = l5
                    method1787(entry)
                    if (entry.anInt2227 == entry.anInt2225 + 1 && entry.anInt2222 == 0) {
                        flag18 = true
                        break
                    }

                    flag18 = false
                    break
                }
            }

            return
        }
    }
}

