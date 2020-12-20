package cn.xinbee.mdrs.data.jpa.domain;

public class Range<T> {
    private T min;
    private T Max;
    private String unit;

    public Range() {
    }

    public Range(T min, T max, String unit) {
        this.min = min;
        Max = max;
        this.unit = unit;
    }

    public T getMin() {
        return min;
    }

    public void setMin(T min) {
        this.min = min;
    }

    public T getMax() {
        return Max;
    }

    public void setMax(T max) {
        Max = max;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
