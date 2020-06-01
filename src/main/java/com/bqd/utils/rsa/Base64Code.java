package com.bqd.utils.rsa;

import java.nio.charset.Charset;
import org.apache.commons.codec.binary.Base64;
/**
 * @author 顾风桂
 * @date 2020/5/20 21:30
 * @Description:
 */

public abstract class Base64Code {
    private static final int CACHE_SIZE = 1024;
    private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
    private static final Base64Delegate delegate = new CommonsCodecBase64Delegate();

    public Base64Code() {
    }

    private static void assertSupported() {
        Assert.isTrue(delegate != null, "Apache Commons Codec not found - Base64 encoding not supported````````");
    }

    public static byte[] encode(byte[] src) {
        assertSupported();
        return delegate.encode(src);
    }

    public static String encodeToString(byte[] src) {
        assertSupported();
        if (src == null) {
            return null;
        } else {
            return src.length == 0 ? "" : new String(delegate.encode(src), DEFAULT_CHARSET);
        }
    }

    public static byte[] decode(byte[] src) {
        assertSupported();
        return delegate.decode(src);
    }

    public static byte[] decodeFromString(String src) {
        assertSupported();
        if (src == null) {
            return null;
        } else {
            return src.length() == 0 ? new byte[0] : delegate.decode(src.getBytes(DEFAULT_CHARSET));
        }
    }

    private static class CommonsCodecBase64Delegate implements Base64Delegate {
        private final Base64 base64;

        private CommonsCodecBase64Delegate() {
            this.base64 = new Base64();
        }

        public byte[] encode(byte[] src) {
            return this.base64.encode(src);
        }

        public byte[] decode(byte[] src) {
            return this.base64.decode(src);
        }
    }

    private interface Base64Delegate {
        byte[] encode(byte[] var1);

        byte[] decode(byte[] var1);
    }
}
