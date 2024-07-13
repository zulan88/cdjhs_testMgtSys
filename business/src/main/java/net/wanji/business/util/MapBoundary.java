package net.wanji.business.util;

public enum MapBoundary {

    CHANGAN(new Double[][]{{108.89028310775757, 34.37376204215178},
            {108.9028787612915, 34.37772911466851},
            {108.90408039093018, 34.37415167366642},
            {108.8908839225769, 34.37053865737726}});

    private final Double[][] values;
    MapBoundary(Double[][] doubles) {
        this.values = doubles;
    }

    public Double[][] getValues() {
        return values;
    }
}
