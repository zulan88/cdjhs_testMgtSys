package net.wanji.common.obuissued;


/**
 * ClassName: ObuDecode
 * Description: OBU 数据加密
 * date: 2021/09/13 13:44
 *
 * @author gdj
 */
public class ObuDecode {

    private static final char[] DIGITS_HEX = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static String decode(String json) {
        StringBuilder sb = new StringBuilder();

        String start = getStart();
        String data = toHex(json);
        String len = getLen(data);
        String end = getEnd();

        sb.append(start).append(len).append(data).append(end);
        return sb.toString();
    }

    private static String getStart() {
        byte[] frameValue = ProtocolEnum.FRAME_START.getFrameValue();
        return byteToHexString(frameValue);
    }

    private static String getEnd() {
        byte[] frameValue = ProtocolEnum.FRAME_END.getFrameValue();
        return byteToHexString(frameValue);
    }

    private static String getLen(String data) {
        int len = data.length() / 2;
        byte[] bytes = intToByte2(len);
        String lenStr = byteToHexString(bytes);
        return lenStr;
    }

    /**
     * byte转16进制
     *
     * @param bytes
     * @return 16进制
     */
    private static String byteToHexString(byte[] bytes) {
        StringBuilder re = new StringBuilder();
        for (byte b : bytes) {
            re.append(DIGITS_HEX[(b & 0xF0) >> 4])
                    .append(DIGITS_HEX[b & 0x0F]);
        }
        return re.toString();
    }

    private static String toHex(String str) {
        return new String(encodeHex(str.getBytes()));
    }

    private static char[] encodeHex(byte[] data) {
        int l = data.length;
        char[] out = new char[l << 1];
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = DIGITS_HEX[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS_HEX[0x0F & data[i]];
        }
        return out;
    }

    /**
     * int整数转换为2字节的byte数组
     *
     * @param i 整数
     * @return byte数组
     */
    private static byte[] intToByte2(int i) {
        byte[] targets = new byte[2];
        targets[1] = (byte) (i & 0xFF);
        targets[0] = (byte) (i >> 8 & 0xFF);
        return targets;
    }

//    /**
//     * 测试解析
//     * 密文：FFFF00907B227265636F72644964223A302C22696E737472756374696F6E4C6576656C223A302C226D7367223A22E8AFB73573E58685E58AA0E9809FE887B33830222C226D736754797065223A2231222C2276616C756531223A2235222C2276616C756532223A2231222C2276616C756533223A223830222C2273656E6454696D65223A2231363330323933313030363933227DFF
//     * 明文：{"recodId":0,"instructionLevel":0,"msg":"请5s内加速至80","msgType":"1","value1":"5","value2":"1","value3":"80","sendTime":"1630293100693"}
//     * TODO 测试代码
//     *
//     * @param args
//     */
//    public static void main(String[] args) {
//
//        String plaintext = "{\"recodId\":0,\"instructionLevel\":0,\"msg\":\"请5s内加速至80\",\"msgType\":\"1\",\"value1\":\"5\",\"value2\":\"1\",\"value3\":\"80\",\"sendTime\":\"1630293100693\"}";
//        String data = decode(plaintext);
//        System.out.println(data);
//
//        byte[] bytes = hexString2Str(data);
//        // 解析长度
//        int o = Integer.parseInt(Objects.requireNonNull(bytes2Int2(bytes, 3, 2, true)).toString());
//
//        // 根据长度解析数据
//        byte[] bytes1 = getBytes(bytes, 5, o);
//        assert bytes1 != null;
//        String res = new String(bytes1);
//        System.out.println(res);
//    }

    private static byte[] hexString2Str(String s) {
        s = s.replace(" ", "");
        s = s.replace("#", "");
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return baKeyword;
    }

    /**
     * 2位byte转int
     *
     * @param bytes 需要解析的数组
     * @param start 开始的位置
     * @return 返回的int数
     */
    private static Object bytes2Int2(byte[] bytes, int start, int length, boolean b) {
        int value = 0;
        byte[] newBytes;
        if (b) {
            newBytes = getBytes(bytes, start, length);
        } else {
            newBytes = getBytesInversion(bytes, start, length);
        }
        if (newBytes == null) {
            return null;
        }

        value = (int) ((newBytes[1] & 0xFF) | ((newBytes[0] & 0xFF) << 8));
        return value;
    }

    /**
     * 正着
     * 获得的需要解析的新数组　　第一位从1开始计算
     *
     * @param bytes  需要解析的数组
     * @param start  开始的位置  第一位就是1 不是0 下面会自动计算
     * @param length 解析的长度
     * @return 返回的新数组
     */
    private static byte[] getBytes(byte[] bytes, int start, int length) {
        if (bytes.length < start + length - 1) {
            return null;
        }
        byte[] newByte = new byte[length];
        for (int i = 0; i < length; i++) {
            newByte[i] = bytes[start + i - 1];
        }
        return newByte;
    }

    /**
     * 反过来解析
     *
     * @param bytes  需要解析的数组
     * @param start  开始的位置  第一位就是1 不是0 下面会自动计算
     * @param length 解析的长度
     * @return 返回的新数组
     */
    private static byte[] getBytesInversion(byte[] bytes, int start, int length) {
        if (bytes.length < start + length - 1) {
            return null;
        }

        byte[] newByte = new byte[length];
        for (int i = 0; i < length; i++) {
            newByte[length - i - 1] = bytes[start + i - 1];
        }
        return newByte;
    }

}
