/* History Gluon
   Copyright (C) 2012 MIRACLE LINUX CORPORATION
 
   This program is free software: you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.miraclelinux.historygluon;

import java.nio.ByteBuffer;
import java.io.UnsupportedEncodingException;

public class Utils {
    /* -----------------------------------------------------------------------
     * Private constants
     * -------------------------------------------------------------------- */
    private static final long[] offsetArray64 = {
      -9223372036854775808L, -8070450532247928832L,
      -6917529027641081856L, -5764607523034234880L,
      -4611686018427387904L, -3458764513820540928L,
      -2305843009213693952L, -1152921504606846976L};

    private static final int[] offsetArray32 = {
      -2147483648, -1879048192, -1610612736, -342177280,
      -1073741824, -805306368, -536870912, -268435456};

    /* -----------------------------------------------------------------------
     * Public Methods
     * -------------------------------------------------------------------- */
    public static double toDoubleAsUnsigned(long v) {
        if (v >= 0)
            return (double)v;
        double dbl = v;
        dbl += Math.pow(2, 64);
        return dbl;
    }

    public static int compareAsUnsigned(int v0, int v1) {
        long lv0 = v0;
        long lv1 = v1;
        if (lv0 < 0)
            lv0 += 0x100000000L;
        if (lv1 < 0)
            lv1 += 0x100000000L;

        if (lv0 < lv1)
            return -1;
        else if (lv0 == lv1)
            return 0;
        else
            return 1;
    }

    public static int compareAsUnsigned(long v0, long v1) {
        if (v0 >= 0 && v1 < 0)
            return -1;

        if (v0 < 0 && v1 >= 0)
            return 1;

        // where, lv0 and lv1 must be both positive or both negative.
        if (v0 < v1)
            return -1;
        else if (v0 == v1)
            return 0;
        else
            return 1;
    }

    public static long toUnsignedInt(int v) {
        if (v >= 0)
            return v;
        return v + 0x100000000L;
    }

    public static short byteArrayToShort(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getShort();
    }

    public static int byteArrayToInt(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getInt();
    }

    public static long byteArrayToLong(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getLong();
    }

    public static double byteArrayToDouble(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getDouble();
    }

    public static ByteBuffer longToByteBuffer(long val) {
        return ByteBuffer.allocate(8).putLong(0, val);
    }

    public static ByteBuffer intToByteBuffer(int val) {
        return ByteBuffer.allocate(4).putInt(0, val);
    }

    public static ByteBuffer doubleToByteBuffer(double val) {
        return ByteBuffer.allocate(8).putDouble(0, val);
    }

    public static ByteBuffer stringToByteBuffer(String string) {
        ByteBuffer bb = null;
        try {
            bb = ByteBuffer.wrap(string.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new Error();
        }
        return bb;
    }

    public static String byteArrayToString(byte[] bytes) {
        String string = null;
        try {
            string = new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new Error();
        }
        return string;
    }

    public static int hexCharToInt(char ch) {
        if (ch >= '0' && ch <= '9')
            return ch - '0';
        if (ch >= 'A' && ch <= 'F')
            return ch - 'A' + 10;
        if (ch >= 'a' && ch <= 'f')
            return ch - 'a' + 10;
        throw new IllegalArgumentException("Invalid char: " + ch);
    }

    public static long parseHexLong(String str) {
        int length = str.length();
        if (length > 16)
            throw new InternalCheckException("length > 16: " + length);
        if (length != 16)
            return Long.parseLong(str, 16);
        int top_num = hexCharToInt(str.charAt(0));
        if (top_num < 8)
            return Long.parseLong(str, 16);

        long digit15val = Long.parseLong(str.substring(1), 16);
        return offsetArray64[top_num - 8] + digit15val;
    }

    public static int parseHexInt(String str) {
        int length = str.length();
        if (length > 8)
            throw new InternalCheckException("length > 8: " + length);
        if (length != 8)
            return Integer.parseInt(str, 16);
        int top_num = hexCharToInt(str.charAt(0));
        if (top_num < 8)
            return Integer.parseInt(str, 16);

        int digit7val = Integer.parseInt(str.substring(1), 16);
        return offsetArray32[top_num - 8] + digit7val;
    }

    public static boolean memEquals(byte[] a, byte[] b, int length) {
        for (int i = 0; i < length; i++)
            if (a[i] != b[i])
                return false;
        return true;
    }
}
