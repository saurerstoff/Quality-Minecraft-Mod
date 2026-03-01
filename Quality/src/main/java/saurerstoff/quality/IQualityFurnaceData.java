package saurerstoff.quality;

public interface IQualityFurnaceData {
    default int getQualityValue() {return 0;}
    default int getSyncedQualityValue() {return 0;}
}
