package saurerstoff.quality;

public interface IQualityDataHolder {
    default int getQualityValue() {return 0;}
    default int getSyncedQualityValue() {return 0;}
}
