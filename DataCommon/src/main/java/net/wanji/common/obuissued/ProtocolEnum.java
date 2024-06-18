package net.wanji.common.obuissued;

/**
 * ClassName: ProtocolEnum
 * Description: 协议头&尾 枚举
 * date: 2021/09/13 13:42
 *
 * @author gdj
 */
public enum ProtocolEnum {

    /*
    帧开始
    */
    FRAME_START(new byte[]{(byte) 0xFF, (byte) 0xFF}),
    /*
    帧结束
     */
    FRAME_END(new byte[]{(byte) 0xFF});

    private final byte[] frameValue;

    ProtocolEnum(byte[] frameValue) {
        this.frameValue = frameValue;
    }

    public byte[] getFrameValue() {
        return frameValue;
    }

}
