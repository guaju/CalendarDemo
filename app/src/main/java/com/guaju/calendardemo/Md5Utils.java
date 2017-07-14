package com.guaju.calendardemo;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by root on 17-7-14.
 */

public class Md5Utils {


    public static String getLocalMd5(Activity activity) {
        String signValidString="";
        try {
            PackageInfo packageInfo = activity.getPackageManager().getPackageInfo(BuildConfig.APPLICATION_ID, PackageManager.GET_SIGNATURES);
            signValidString = getSignValidString(packageInfo.signatures[0].toByteArray());
            Log.e("获取应用签名", BuildConfig.APPLICATION_ID + "__" + signValidString);
        } catch (Exception e) {
            Log.e("获取应用签名", "异常__" + e);
        }
        return signValidString;
    }
        private static String getSignValidString(byte[] paramArrayOfByte) throws NoSuchAlgorithmException
        {
            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
            localMessageDigest.update(paramArrayOfByte);
            return toHexString(localMessageDigest.digest());
        }

        public static String toHexString(byte[] paramArrayOfByte) {
            if (paramArrayOfByte == null) {
                return null;
            }
            StringBuilder localStringBuilder = new StringBuilder(2 * paramArrayOfByte.length);
            for (int i = 0; ; i++) {
                if (i >= paramArrayOfByte.length) {
                    return localStringBuilder.toString();
                }
                String str = Integer.toString(0xFF & paramArrayOfByte[i], 16);
                if (str.length() == 1) {
                    str = "0" + str;
                }
                localStringBuilder.append(str);
            }
        }


        public static String  getFileMd5(File file) throws FileNotFoundException {
            String value = null;
            FileInputStream in = null;
            try {
                in = new FileInputStream(file);
                MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                md5.update(byteBuffer);
                BigInteger bi = new BigInteger(1, md5.digest());
                value = bi.toString(16);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (null != in) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return value;
        }


    public static String  getFileMd5(InputStream in) throws FileNotFoundException {
        String value = null;
        try {
            File dir = new File(Environment.getExternalStorageDirectory() + "/test");
            if (!dir.exists()){
                dir.mkdirs();
            }
            File file = new File(dir, "app.apk");
            if (!file.exists()){
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            int temp=0;
            byte[] buf=new byte[1024];
            while((temp=in.read(buf))!=-1){
                fos.write(buf,0,temp);
            }
            fos.close();
            in.close();
            buf=null;
            FileInputStream fileIn = new FileInputStream(file);


            MappedByteBuffer byteBuffer = fileIn.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(byteBuffer);
            BigInteger bi = new BigInteger(1, md5.digest());
            value = bi.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return value;
    }
    public static String Upper2(String str){
        String str1 = str.replace(":", "");
        String s = str1.toLowerCase();

        return s;
    }

    public static String showUninstallAPKSignatures(String apkPath) {


        String PATH_PackageParser = "android.content.pm.PackageParser";
        try {
            // apk包的文件路径
            // 这是一个Package 解释器, 是隐藏的
            // 构造函数的参数只有一个, apk文件的路径
            // PackageParser packageParser = new PackageParser(apkPath);
            Class pkgParserCls = Class.forName(PATH_PackageParser);
            Class[] typeArgs = new Class[1];
            typeArgs[0] = String.class;
            Constructor pkgParserCt =pkgParserCls.getConstructor(String.class);
            Object[] valueArgs = new Object[1];
            valueArgs[0] = apkPath;
            Object pkgParser = pkgParserCt.newInstance(valueArgs[0]);
            Log.e("hehe","pkgParser:" + pkgParser.toString());
            // 这个是与显示有关的, 里面涉及到一些像素显示等等, 我们使用默认的情况
            DisplayMetrics metrics = new DisplayMetrics();
            metrics.setToDefaults();
            // PackageParser.Package mPkgInfo = packageParser.parsePackage(new
            // File(apkPath), apkPath,
            // metrics, 0);
            typeArgs = new Class[4];
            typeArgs[0] = File.class;
            typeArgs[1] = String.class;
            typeArgs[2] = DisplayMetrics.class;
            typeArgs[3] = Integer.TYPE;
            Method pkgParser_parsePackageMtd = pkgParserCls.getDeclaredMethod("parsePackage",
                    typeArgs);
            valueArgs = new Object[4];
            valueArgs[0] = new File(apkPath);
            valueArgs[1] = apkPath;
            valueArgs[2] = metrics;
            valueArgs[3] = PackageManager.GET_SIGNATURES;
            Object pkgParserPkg = pkgParser_parsePackageMtd.invoke(pkgParser, valueArgs);

            typeArgs = new Class[2];
            typeArgs[0] = pkgParserPkg.getClass();
            typeArgs[1] = Integer.TYPE;
            Method pkgParser_collectCertificatesMtd = pkgParserCls.getDeclaredMethod("collectCertificates",
                    typeArgs);
            valueArgs = new Object[2];
            valueArgs[0] = pkgParserPkg;
            valueArgs[1] = PackageManager.GET_SIGNATURES;
            pkgParser_collectCertificatesMtd.invoke(pkgParser, valueArgs);
            // 应用程序信息包, 这个公开的, 不过有些函数, 变量没公开
            Field packageInfoFld = pkgParserPkg.getClass().getDeclaredField("mSignatures");
            Signature[] info = (Signature[]) packageInfoFld.get(pkgParserPkg);
            Log.e("hehe", "size:"+info.length);
            Log.e("hehe", info[0].toCharsString());
            return info[0].toCharsString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 得到任意apk公钥信息的md5字符串
    public static String getApkSignatureMD5(Activity activity,String apkPath) throws Exception {
        Class clazz = Class.forName("android.content.pm.PackageParser");
        Method parsePackageMethod = clazz.getMethod("parsePackage", File.class, String.class, DisplayMetrics.class, int.class);

        Object packageParser = clazz.getConstructor(String.class).newInstance("");
        Object packag = parsePackageMethod.invoke(packageParser, new File(apkPath), null, activity.getResources().getDisplayMetrics(), 0x0004);

        Method collectCertificatesMethod = clazz.getMethod("collectCertificates", Class.forName("android.content.pm.PackageParser$Package"), int.class);
        collectCertificatesMethod.invoke(packageParser, packag, PackageManager.GET_SIGNATURES);
        Signature mSignatures[] = (Signature[]) packag.getClass().getField("mSignatures").get(packag);

        Signature apkSignature = mSignatures.length > 0 ? mSignatures[0] : null;

        if(apkSignature != null) {
            // 说明：没有提供md5的具体实现
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            String signMd5 = md5.digest(apkSignature.toByteArray()).toString();
            return signMd5;
//            return StringUtils.md5(apkSignature.toCharsString());
        }

        return null;
    }
}

