package com.fundot.openinfo.utils

import javax.crypto.Cipher
import java.lang.Exception
import javax.crypto.spec.SecretKeySpec
import java.lang.StringBuilder
import java.security.Key

/**
 * DES加密和解密工具,可以对字符串进行加密和解密操作  。
 */
class DesCyUtils(token: String) {
    /**
     * 加密工具
     */
    private var encryptCipher: Cipher? = null

    /**
     * 解密工具
     */
    private var decryptCipher: Cipher? = null

    /**
     * 加密字节数组
     *
     * @param arrB 需加密的字节数组
     * @return 加密后的字节数组
     */
    @Throws(Exception::class)
    private fun encrypt(arrB: ByteArray): ByteArray {
        return encryptCipher!!.doFinal(arrB)
    }

    /**
     * 加密字符串
     *
     * @param strIn 需加密的字符串
     * @return 加密后的字符串
     */
    fun encrypt(strIn: String): String {
        return try {
            byteArr2HexStr(encrypt(strIn.toByteArray()))
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    /**
     * 解密字节数组
     *
     * @param arrB 需解密的字节数组
     * @return 解密后的字节数组
     */
    @Throws(Exception::class)
    private fun decrypt(arrB: ByteArray): ByteArray {
        return decryptCipher!!.doFinal(arrB)
    }

    /**
     * 解密字符串
     *
     * @param strIn 需解密的字符串
     * @return 解密后的字符串
     */
    fun decrypt(strIn: String): String {
        return try {
            String(decrypt(hexStr2ByteArr(strIn)))
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    /**
     * 从指定字符串生成密钥，密钥所需的字节数组长度为8位 不足8位时后面补0，超出8位只取前8位
     *
     * @param arrBTmp 构成该字符串的字节数组
     * @return 生成的密钥
     */
    @Throws(Exception::class)
    private fun getKey(arrBTmp: ByteArray): Key {
        // 创建一个空的8位字节数组（默认值为0）
        val arrB = ByteArray(8)

        // 将原始字节数组转换为8位
        var i = 0
        while (i < arrBTmp.size && i < arrB.size) {
            arrB[i] = arrBTmp[i]
            i++
        }

        // 生成密钥
        return SecretKeySpec(arrB, "DES")
    }

    companion object {
        /**
         * 将byte数组转换为表示16进制值的字符串， 如：byte[]{8,18}转换为：0813， 和public static byte[]
         * hexStr2ByteArr(String strIn) 互为可逆的转换过程
         *
         * @param arrB 需要转换的byte数组
         * @return 转换后的字符串
         * @throws Exception 本方法不处理任何异常，所有异常全部抛出
         */
        @Throws(Exception::class)
        private fun byteArr2HexStr(arrB: ByteArray): String {
            val iLen = arrB.size
            // 每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍
            val sb = StringBuilder(iLen * 2)
            for (anArrB in arrB) {
                var intTmp = anArrB.toInt()
                // 把负数转换为正数
                while (intTmp < 0) {
                    intTmp = intTmp + 256
                }
                // 小于0F的数需要在前面补0
                if (intTmp < 16) {
                    sb.append("0")
                }
                sb.append(Integer.toString(intTmp, 16))
            }
            return sb.toString()
        }

        /**
         * 将表示16进制值的字符串转换为byte数组， 和public static String byteArr2HexStr(byte[] arrB)
         * 互为可逆的转换过程
         *
         * @param strIn 需要转换的字符串
         * @return 转换后的byte数组
         * @throws Exception 本方法不处理任何异常，所有异常全部抛出
         * @author [LiGuoQing](mailto:leo841001@163.com)
         */
        @Throws(Exception::class)
        private fun hexStr2ByteArr(strIn: String): ByteArray {
            val arrB = strIn.toByteArray()
            val iLen = arrB.size

            // 两个字符表示一个字节，所以字节数组长度是字符串长度除以2
            val arrOut = ByteArray(iLen / 2)
            var i = 0
            while (i < iLen) {
                val strTmp = String(arrB, i, 2)
                arrOut[i / 2] = strTmp.toInt(16).toByte()
                i += 2
            }
            return arrOut
        }
    }

    /**
     * 指定密钥构造方法
     */
    init {
        //Security.addProvider(new com.sun.crypto.provider.SunJCE());
        val key = getKey(token.toByteArray())
        encryptCipher = Cipher.getInstance("DES")
        encryptCipher!!.init(Cipher.ENCRYPT_MODE, key)
        decryptCipher = Cipher.getInstance("DES")
        decryptCipher!!.init(Cipher.DECRYPT_MODE, key)
    }
}