package net.wanji.business.exercise.dto.luansheng;

/**
 * @author: jenny
 * @create: 2024-08-28 2:15 下午
 */
public enum StatThresoldEnum {
    ANGULAR_VELOCITY("angularVelocity", new double[]{0, 0.5}),

    LAT_ACC("latAcc", new double[]{-0.5, 0.5}),

    LAT_ACC2("latAcc2", new double[]{-1, 1}),

    LON_ACC("lonAcc", new double[]{-3, 3}),

    LON_ACC2("lonAcc2", new double[]{-6, 6});

    StatThresoldEnum(String name, double[] thresold) {
        this.name = name;
        this.thresold = thresold;
    }

    private String name;

    private double[] thresold;

    public String getName() {
        return name;
    }

    public double[] getThresold() {
        return thresold;
    }

    public static double[] getThresoldByName(String name) {
        StatThresoldEnum[] values = values();
        for(StatThresoldEnum value: values){
            if(value.getName().equals(name)){
                return value.getThresold();
            }
        }

        return null;
    }
}
