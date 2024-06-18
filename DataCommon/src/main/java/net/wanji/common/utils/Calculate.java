package net.wanji.common.utils;

import net.dreamlu.iot.mqtt.core.client.MqttClient;
import org.apache.commons.codec.binary.Hex;

import java.text.DecimalFormat;
import java.util.concurrent.ConcurrentHashMap;

public class Calculate {
    public static ConcurrentHashMap<String, MqttClient> clientMap=new ConcurrentHashMap();

    public static byte[] hexItr2Arr(String hexItr) throws org.apache.commons.codec.DecoderException {
        return Hex.decodeHex(hexItr);
    }

    public static String arr2HexStr(byte[] arr,boolean lowerCase){
        return Hex.encodeHexString(arr, lowerCase);
    }

    public static byte[] checksum(byte cs, byte id, byte[]len, byte[]payload){
        byte cka= (byte) (cs+id);
        byte ckb= (byte) (cs+cs+id);
        for(byte nowbyte:len){
            cka+=nowbyte;
            ckb+=cka;
        }
        for(byte nowbyte:payload){
            cka+=nowbyte;
            ckb+=cka;
        }
        return new byte[]{cka,ckb};
    }

    public static byte[] numberToLe(byte[] target, Number number, int offset, int len) {
        long src = number.longValue();

        for(int i = 0; i < len; ++i) {
            target[offset + i] = (byte)((int)(src >> i * 8 & 255L));
        }

        return target;
    }

    public static String getPercent(int a, int b) {

        // 计算百分比
        double percentage = (double) a / b * 100;

        // 四舍五入
        long roundedPercentage = Math.round(percentage);

        // 创建DecimalFormat对象，指定输出格式为n%
        DecimalFormat decimalFormat = new DecimalFormat("0'%'");

        // 使用DecimalFormat格式化百分比
        return decimalFormat.format(roundedPercentage);

    }

}
