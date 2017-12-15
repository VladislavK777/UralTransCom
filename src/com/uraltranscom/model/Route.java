package com.uraltranscom.model;

/*
*
* Класс Маршрута
*
* @author Vladislav Klochkov
* @version 2.0
* @create 17.11.2017
*
*/

public class Route implements Comparable<Object> {

    // Код станции отправления
    private String keyOfStationDeparture;

    // Станция отправления
    private String nameOfStationDeparture;

    // Код станции назначения
    private String keyOfStationDestination;

    // Станция назначения
    private String nameOfStationDestination;

    // Расстояние маршрута
    private String distanceOfWay;

    // Флаг приоритера 1 - Приоритетный, 0 - Неприоритетный
    private String VIP;

    public Route(String keyOfStationDeparture, String nameOfStationDeparture, String keyOfStationDestination, String nameOfStationDestination, String distanceOfWay, String VIP) {
        this.keyOfStationDeparture = keyOfStationDeparture;
        this.nameOfStationDeparture = nameOfStationDeparture;
        this.keyOfStationDestination = keyOfStationDestination;
        this.nameOfStationDestination = nameOfStationDestination;
        this.distanceOfWay = distanceOfWay;
        this.VIP = VIP;
    }

    public String getKeyOfStationDeparture() {
        return keyOfStationDeparture;
    }

    public void setKeyOfStationDeparture(String keyOfStationDeparture) {
        this.keyOfStationDeparture = keyOfStationDeparture;
    }

    public String getNameOfStationDeparture() {
        return nameOfStationDeparture;
    }

    public void setNameOfStationDeparture(String nameOfStationDeparture) {
        this.nameOfStationDeparture = nameOfStationDeparture;
    }

    public String getKeyOfStationDestination() {
        return keyOfStationDestination;
    }

    public void setKeyOfStationDestination(String keyOfStationDestination) {
        this.keyOfStationDestination = keyOfStationDestination;
    }

    public String getNameOfStationDestination() {
        return nameOfStationDestination;
    }

    public void setNameOfStationDestination(String nameOfStationDestination) {
        this.nameOfStationDestination = nameOfStationDestination;
    }

    public String getDistanceOfWay() {
        return distanceOfWay;
    }

    public void setDistanceOfWay(String distanceOfWay) {
        this.distanceOfWay = distanceOfWay;
    }

    public String getVIP() {
        return VIP;
    }

    public void setVIP(String VIP) {
        this.VIP = VIP;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Route route = (Route) o;

        if (keyOfStationDeparture != null ? !keyOfStationDeparture.equals(route.keyOfStationDeparture) : route.keyOfStationDeparture != null)
            return false;
        if (nameOfStationDeparture != null ? !nameOfStationDeparture.equals(route.nameOfStationDeparture) : route.nameOfStationDeparture != null)
            return false;
        if (keyOfStationDestination != null ? !keyOfStationDestination.equals(route.keyOfStationDestination) : route.keyOfStationDestination != null)
            return false;
        if (nameOfStationDestination != null ? !nameOfStationDestination.equals(route.nameOfStationDestination) : route.nameOfStationDestination != null)
            return false;
        if (distanceOfWay != null ? !distanceOfWay.equals(route.distanceOfWay) : route.distanceOfWay != null)
            return false;
        return VIP != null ? VIP.equals(route.VIP) : route.VIP == null;
    }

    @Override
    public int hashCode() {
        int result = keyOfStationDeparture != null ? keyOfStationDeparture.hashCode() : 0;
        result = 31 * result + (nameOfStationDeparture != null ? nameOfStationDeparture.hashCode() : 0);
        result = 31 * result + (keyOfStationDestination != null ? keyOfStationDestination.hashCode() : 0);
        result = 31 * result + (nameOfStationDestination != null ? nameOfStationDestination.hashCode() : 0);
        result = 31 * result + (distanceOfWay != null ? distanceOfWay.hashCode() : 0);
        result = 31 * result + (VIP != null ? VIP.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return  keyOfStationDeparture +
                ", " + nameOfStationDeparture +
                ", " + keyOfStationDestination +
                ", " + nameOfStationDestination +
                ", " + distanceOfWay +
                ", " + VIP;
    }

    @Override
    public int compareTo(Object o) {
        try {
            Route p = (Route) o;
            if (p.getVIP() == this.VIP) {
                return p.hashCode() - this.hashCode();
            }
            if (Integer.parseInt(p.getVIP()) < Integer.parseInt(this.VIP)) {
                return -1;
            } else {
                return 1;
            }
        } catch (NullPointerException | ClassCastException e) {
            return 1;
        }
    }
}
