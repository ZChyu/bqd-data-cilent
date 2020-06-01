package com.bqd.utils.rsa;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

/**
 * @author 顾风桂
 * @date 2020/5/20 20:34
 * @Description:
 */

/**
 * 对称/非对称加密与解密
 */
public class SecretKeyTest {
    public static void main(String[] args) throws Exception {
//       secretEncrypt();
//       secretDecrypt();

//       secretEncryptByPass();
//       secretDecryptByPass();

        publicEncrypt();
        privateDecrypt();
    }

    /**
     * 对称加密，随机产生密钥
     * @throws Exception
     */
    private static void secretEncrypt() throws Exception {
        // Cipher：为加密和解密提供密码功能
        Cipher cipher = Cipher.getInstance("AES");
        // 生成密钥
        SecretKey key = KeyGenerator.getInstance("AES").generateKey();

        /*
         * 小知识： 由于key是一个对象，要把它写到文件中，所在要用ObjectOutputStream.writeObject()
         * 要用这个方法，这个类必须实现Serializable接口进行持久化进行对象保存，Object-->硬盘-->Object
         */

        // 把密钥保存到secret.key文件中
        saveKey(key, "secret.key");
        // 根据密钥把cipher初始化为加密模式
        cipher.init(Cipher.ENCRYPT_MODE, key);
        // cipher.update("aaa".getBytes());
        // cipher.update("aaa".getBytes());
        // byte[] results = cipher.doFinal();

        // 对aaa进行加密操作
        byte[] results = cipher.doFinal("aaa".getBytes());
        System.out.println(new String(results));

        // 把加密后的字符串存放到data.txt中
        saveData(results, "data.txt");
    }

    /**
     * 对称解密，读取存放密钥的文件获取密钥，然后根据密钥来解密
     * @throws Exception
     */
    private static void secretDecrypt() throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        // 读取存放密钥的secret.key文件
        Key key = readKey("secret.key");
        // 根据密钥把cipher初始化为解密模式
        cipher.init(Cipher.DECRYPT_MODE, key);
        // 读取data.txt文件，获取要解密的内容
        byte[] src = readData("data.txt");
        // 解密
        byte[] result = cipher.doFinal(src);
        System.out.println(new String(result));
    }

    /**
     * 根据密码进行对称加密
     * @throws Exception
     */
    private static void secretEncryptByPass() throws Exception {
        Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
        SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES")
                .generateSecret(new PBEKeySpec("12345678".toCharArray()));
        PBEParameterSpec parameterSpec = new PBEParameterSpec(new byte[] { 1,
                2, 3, 4, 5, 6, 7, 8 }, 1000);
        cipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec);

        byte[] results = cipher.doFinal("aaa".getBytes());
        System.out.println(new String(results));

        saveData(results,"data.txt");
    }

    /**
     * 根据密码进行对称解密
     * @throws Exception
     */
    private static void secretDecryptByPass() throws Exception {
        Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
        SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES")
                .generateSecret(new PBEKeySpec("12345678".toCharArray()));
        PBEParameterSpec parameterSpec = new PBEParameterSpec(new byte[] { 1,
                2, 3, 4, 5, 6, 7, 8 }, 1000);
        cipher.init(Cipher.DECRYPT_MODE, key, parameterSpec);

        byte[] src = readData("data.txt");

        // 解密
        byte[] result = cipher.doFinal(src);
        System.out.println(new String(result));
    }

    /**
     * 根据公钥进行非对称加密
     * @throws Exception
     */
    private static void publicEncrypt() throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        KeyPairGenerator kPairGenerator = KeyPairGenerator.getInstance("RSA");
        KeyPair keyPair = kPairGenerator.generateKeyPair();
        Key publicKey = keyPair.getPublic();
        Key privateKey = keyPair.getPrivate();
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] result = cipher.doFinal("离开陪你".getBytes("UTF-8"));

        saveKey(privateKey, "secret2.key");
        saveData(result, "data2.txt");
    }

    /**
     * 根据私钥进行非对称解密
     * @throws Exception
     */
    private static void privateDecrypt() throws Exception{
        Cipher cipher = Cipher.getInstance("RSA");
        Key privateKey = readKey("secret2.key");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        //方法1：
//      byte[] src = readData("data2.txt");
//      byte[] result=cipher.doFinal(src);
//      System.err.println(new String(result,"UTF-8"));

        //方法2：
//      FileInputStream fis = new FileInputStream("data2.txt");
//      CipherInputStream cis = new CipherInputStream(fis, cipher);
//      //简单处理一下
//      byte[] buf = new byte[1024];
//      int len = cis.read(buf);
//      System.out.println(new String(buf,0,len,"UTF-8"));

        //方法3：
        FileInputStream fis = new FileInputStream("data2.txt");
        FileOutputStream fos = new FileOutputStream("result.txt");
        CipherOutputStream cos = new CipherOutputStream(fos, cipher);
        copyStream(fis, cos);
        cos.close();
        fos.close();
        fis.close();
    }

    /**
     * 把输入流拷贝到输出流
     * @param is
     * @param os
     * @throws IOException
     */
    private static void copyStream(InputStream is, OutputStream os)
            throws IOException {
        byte[] buf = new byte[1024];
        int len = is.read(buf);
        while (len != -1) {
            os.write(buf, 0, len);
            len = is.read(buf);
        }
    }

    /**
     * 把key保存到文件中
     * @param key
     * @param fileName
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static void saveKey(Key key, String fileName)
            throws FileNotFoundException, IOException {
        FileOutputStream fosKey = new FileOutputStream(fileName);
        ObjectOutputStream oos = new ObjectOutputStream(fosKey);
        oos.writeObject(key);
        oos.close();
        fosKey.close();
    }

    /**
     * 把二进制数据保存到文件中
     * @param results
     * @param fileName
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static void saveData(byte[] results, String fileName)
            throws FileNotFoundException, IOException {
        FileOutputStream fosData = new FileOutputStream(fileName);
        fosData.write(results);
        fosData.close();
    }

    /**
     * 从文件中获取key
     * @param fileName
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private static Key readKey(String fileName) throws FileNotFoundException, IOException,
            ClassNotFoundException {
        FileInputStream fisKey = new FileInputStream(fileName);
        ObjectInputStream oisKey = new ObjectInputStream(fisKey);
        Key key = (Key) oisKey.readObject();
        oisKey.close();
        fisKey.close();
        return key;
    }

    /**
     * 从文件中读取二进制数据
     * @param fileName
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static byte[] readData(String fileName) throws FileNotFoundException, IOException {
        FileInputStream fisData = new FileInputStream(fileName);
        // 方法1：把输入流拷贝到输出流，再把 输出流转换为byte数组
        // ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // copyStream(fisData, baos);
        // byte[] src = baos.toByteArray();
        // 方法2：用available()方法来读取
        // available()：返回可以不受阻塞地从此输入流中读取（或跳过）的估计剩余字节数。
        byte[] src = new byte[fisData.available()];
        int len = fisData.read(src);
        int total = 0;
        while (total < src.length) {
            total += len;
            len = fisData.read(src, total, src.length - total);
        }

        fisData.close();
        // baos.close();
        return src;
    }
}
