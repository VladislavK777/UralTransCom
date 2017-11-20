package com.uraltranscom.model;

/*
*
* Класс Вагон
*
* @author Vladislav Klochkov
* @version 1.0
* @create 17.11.2017
*
*/

public class Wagon {

    // Номер вагона
    private String numberOfWagon;

    // Дорога станции назначения
    private String roadNameDestination;

    // Станция назначения
    private String stationNameDestination;

    public Wagon(String numberOfWagon, String roadNameDestination, String stationNameDestination) {
        this.numberOfWagon = numberOfWagon;
        this.roadNameDestination = roadNameDestination;
        this.stationNameDestination = stationNameDestination;
    }

    public String getNumberOfWagon() {
        return numberOfWagon;
    }

    public void setNumberOfWagon(String numberOfWagon) {
        this.numberOfWagon = numberOfWagon;
    }

    public String getRoadNameDestination() {
        return roadNameDestination;
    }

    public void setRoadNameDestination(String roadNameDestination) {
        this.roadNameDestination = roadNameDestination;
    }

    public String getStationNameDestination() {
        return stationNameDestination;
    }

    public void setStationNameDestination(String stationNameDestination) {
        this.stationNameDestination = stationNameDestination;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Wagon wagon = (Wagon) o;

        if (numberOfWagon != null ? !numberOfWagon.equals(wagon.numberOfWagon) : wagon.numberOfWagon != null)
            return false;
        if (roadNameDestination != null ? !roadNameDestination.equals(wagon.roadNameDestination) : wagon.roadNameDestination != null)
            return false;
        return stationNameDestination != null ? stationNameDestination.equals(wagon.stationNameDestination) : wagon.stationNameDestination == null;
    }

    @Override
    public int hashCode() {
        int result = numberOfWagon != null ? numberOfWagon.hashCode() : 0;
        result = 31 * result + (roadNameDestination != null ? roadNameDestination.hashCode() : 0);
        result = 31 * result + (stationNameDestination != null ? stationNameDestination.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return numberOfWagon +
                ", " + roadNameDestination +
                ", " + stationNameDestination;
    }
}
