package net.media.spamserver.util;

import net.media.spamserver.config.RedisConfig;
import sun.misc.BASE64Encoder;

import java.security.MessageDigest;
import java.util.Date;

import static java.lang.Math.floor;

public class BasicUtil {

    private static Long getTimeStamp(){
        Date time = new Date();
        return time.getTime();
    }

    private static boolean isValidIp(String ip){
        return ip.length() > 1;
    }

    private static long getLongValueOfByte(String addressByte, int power) {
        return (long) (Integer.parseInt(addressByte) % 256 * Math.pow(256, power) );
    }

    public static long[] getRedisSuffixes(int expireTime) {
        long currentTime = getTimeStamp();
        long[] ret = new long[2];
        ret[0] = currentTime / expireTime;
        ret[1] = (long)floor((currentTime % expireTime) / floor(expireTime / RedisConfig.TIME_SLOTS_IN_VALUE));
        return ret;
    }

    public static Long ipToLong(String ipStr) {
        if(! isValidIp(ipStr)) {
            return (long) 0;
        }
        String[] addressByteArray = ipStr.split("\\.");
        long num = 0;
        for (int i = 0; i < addressByteArray.length; i++) {
            int power = 3 - i;
            num += getLongValueOfByte(addressByteArray[i], power);
        }
        return num;
    }

    public static String hash(String str){
        try{
            byte[] bytesOfMessage = str.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] theDigest = md.digest(bytesOfMessage);
            BASE64Encoder encoder = new BASE64Encoder();
            return encoder.encode(theDigest);
        }
        catch (Exception e){
            return "#UAHASHFAIL#";
        }
    }

    public static boolean isSystemGeneratedVisitorId(String visiterId){
        return visiterId.contains("vr");
    }

    public static boolean isCorrectionAdIP(String ipStr){
      return  ipStr.indexOf("222.222.222.") == 0;
    }
}
