package net.wanji.business.pdf.enums;

public enum TestPaperTypeEnum {
    A_PAPER(1, "A卷"),
    B_PAPER(2, "B卷"),
    C_PAPER(3, "C卷");

    TestPaperTypeEnum(Integer type, String name){
        this.type = type;
        this.name = name;
    }

    private final Integer type;

    private final String name;

    public Integer getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public static String getNameByType(Integer type){
        for (TestPaperTypeEnum value : values()) {
            if(value.getType().compareTo(type) == 0){
                return value.getName();
            }
        }
        return null;
    }
}
