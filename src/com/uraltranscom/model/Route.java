package com.uraltranscom.model;

/*
*
* Класс Маршрута
*
* @author Vladislav Klochkov
* @version 1.0
* @create 17.11.2017
*
*/

public class Route implements Comparable<Object> {

    // Дорога станции отправления
    private String roadOfStationDeparture;

    // Станция отправления
    private String nameOfStationDeparture;

    // Дорога станции назначения
    private String roadOfStationDestination;

    // Станция назначения
    private String nameOfStationDestination;

    // Расстояние маршрута
    private String distanceOfWay;

    // Флаг приоритера 1 - Приоритетный, 0 - Неприоритетный
    private String VIP;

    public Route(String roadOfStationDeparture, String nameOfStationDeparture, String roadOfStationDestination, String nameOfStationDestination, String distanceOfWay, String VIP) {
        this.roadOfStationDeparture = roadOfStationDeparture;
        this.nameOfStationDeparture = nameOfStationDeparture;
        this.roadOfStationDestination = roadOfStationDestination;
        this.nameOfStationDestination = nameOfStationDestination;
        this.distanceOfWay = distanceOfWay;
        this.VIP = VIP;
    }

    public String getRoadOfStationDeparture() {
        return roadOfStationDeparture;
    }

    public void setRoadOfStationDeparture(String roadOfStationDeparture) {
        this.roadOfStationDeparture = roadOfStationDeparture;
    }

    public String getNameOfStationDeparture() {
        return nameOfStationDeparture;
    }

    public void setNameOfStationDeparture(String nameOfStationDeparture) {
        this.nameOfStationDeparture = nameOfStationDeparture;
    }

    public String getRoadOfStationDestination() {
        return roadOfStationDestination;
    }

    public void setRoadOfStationDestination(String roadOfStationDestination) {
        this.roadOfStationDestination = roadOfStationDestination;
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

        if (roadOfStationDeparture != null ? !roadOfStationDeparture.equals(route.roadOfStationDeparture) : route.roadOfStationDeparture != null)
            return false;
        if (nameOfStationDeparture != null ? !nameOfStationDeparture.equals(route.nameOfStationDeparture) : route.nameOfStationDeparture != null)
            return false;
        if (roadOfStationDestination != null ? !roadOfStationDestination.equals(route.roadOfStationDestination) : route.roadOfStationDestination != null)
            return false;
        if (nameOfStationDestination != null ? !nameOfStationDestination.equals(route.nameOfStationDestination) : route.nameOfStationDestination != null)
            return false;
        if (distanceOfWay != null ? !distanceOfWay.equals(route.distanceOfWay) : route.distanceOfWay != null)
            return false;
        return VIP != null ? VIP.equals(route.VIP) : route.VIP == null;
    }

    @Override
    public int hashCode() {
        int result = roadOfStationDeparture != null ? roadOfStationDeparture.hashCode() : 0;
        result = 31 * result + (nameOfStationDeparture != null ? nameOfStationDeparture.hashCode() : 0);
        result = 31 * result + (roadOfStationDestination != null ? roadOfStationDestination.hashCode() : 0);
        result = 31 * result + (nameOfStationDestination != null ? nameOfStationDestination.hashCode() : 0);
        result = 31 * result + (distanceOfWay != null ? distanceOfWay.hashCode() : 0);
        result = 31 * result + (VIP != null ? VIP.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return  roadOfStationDeparture +
                ", " + nameOfStationDeparture +
                ", " + roadOfStationDestination +
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
