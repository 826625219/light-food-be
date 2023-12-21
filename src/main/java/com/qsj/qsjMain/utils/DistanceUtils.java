package com.qsj.qsjMain.utils;

import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GeodeticCurve;
import org.gavaghan.geodesy.GlobalCoordinates;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DistanceUtils {
    /**
     * 根据经纬度计算距离
     *
     * @param lon1 经度1
     * @param lat1 纬度1
     * @param lon2 经度2
     * @param lat2 纬度2
     * @return 距离 （km）
     */
    public static Double getDistance(String lon1, String lat1, String lon2, String lat2) {
        GlobalCoordinates source = new GlobalCoordinates(Double.parseDouble(lat1), Double.parseDouble(lon1));
        GlobalCoordinates target = new GlobalCoordinates(Double.parseDouble(lat2), Double.parseDouble(lon2));
        GeodeticCurve geoCurve = new GeodeticCalculator().calculateGeodeticCurve(Ellipsoid.Sphere, source, target);
        double distance = geoCurve.getEllipsoidalDistance();
        BigDecimal distanceBig = new BigDecimal(distance).setScale(2, RoundingMode.UP);
        distanceBig = distanceBig.multiply(new BigDecimal("0.001")).setScale(2, RoundingMode.UP);
        return distanceBig.doubleValue();
    }

    public static void main(String[] args) {
        String lon1 = "119.6438888888889";
        String lat1 = "37.966944444444444";
        String lon2 = "119.63916666666667";
        String lat2 = "37.9675";
        System.out.println(getDistance(lon1, lat1, lon2, lat2));

    }

}