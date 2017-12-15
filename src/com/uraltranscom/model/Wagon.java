package com.uraltranscom.model;

/*
*
* Класс Вагон
*
* @author Vladislav Klochkov
* @version 2.0
* @create 17.11.2017
*
*/

public class Wagon {

    // Номер вагона
    private String numberOfWagon;

    // Тип вагона
    private String typeOfWagon;

    // Код станции назначения
    private String keyOfStationDestination;

    public Wagon(String numberOfWagon, String typeOfWagon, String keyOfStationDestination) {
        this.numberOfWagon = numberOfWagon;
        this.typeOfWagon = typeOfWagon;
        this.keyOfStationDestination = keyOfStationDestination;
    }

    public String getNumberOfWagon() {
        return numberOfWagon;
    }

    public void setNumberOfWagon(String numberOfWagon) {
        this.numberOfWagon = numberOfWagon;
    }

    public String getTypeOfWagon() {
        return typeOfWagon;
    }

    public void setTypeOfWagon(String typeOfWagon) {
        this.typeOfWagon = typeOfWagon;
    }

    public String getKeyOfStationDestination() {
        return keyOfStationDestination;
    }

    public void setKeyOfStationDestination(String keyOfStationDestination) {
        this.keyOfStationDestination = keyOfStationDestination;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Wagon wagon = (Wagon) o;

        if (numberOfWagon != null ? !numberOfWagon.equals(wagon.numberOfWagon) : wagon.numberOfWagon != null)
            return false;
        if (typeOfWagon != null ? !typeOfWagon.equals(wagon.typeOfWagon) : wagon.typeOfWagon != null) return false;
        return keyOfStationDestination != null ? keyOfStationDestination.equals(wagon.keyOfStationDestination) : wagon.keyOfStationDestination == null;
    }

    @Override
    public int hashCode() {
        int result = numberOfWagon != null ? numberOfWagon.hashCode() : 0;
        result = 31 * result + (typeOfWagon != null ? typeOfWagon.hashCode() : 0);
        result = 31 * result + (keyOfStationDestination != null ? keyOfStationDestination.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return  numberOfWagon +
                ", " + typeOfWagon +
                ", " + keyOfStationDestination;
    }
}
