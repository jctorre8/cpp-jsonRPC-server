package ser321.assign5;

import java.lang.Math;
import java.io.*;
import java.util.*;
import java.text.NumberFormat;
import org.json.JSONString;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.JSONArray;

/**
 * Copyright 2018 Tim Lindquist,
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Class to perform various earth surface calculations. Given lat/lon points
 * this class can perform distance and bearing calculations - Great Circle,
 * and Rhumb-line.
 *
 * Ser321 Principles of Distributed Software Systems
 * see http://pooh.poly.asu.edu/Ser321
 * @author Tim Lindquist Tim.Lindquist@asu.edu
 *      Software Engineering, CIDSE, IAFSE, Arizona State University Polytechnic
 * @version January, 2018
 **/

public class Waypoint extends Object {

   public final static int STATUTE = 0;
   public final static int NAUTICAL = 1;
   public final static int KMETER = 2;
   public final static double radiusE = 6371;

   private double lat; // degrees lat in DD.D format (+ north, - south)
   private double lon; // degrees lon in DD.D format (+ east, - west)
   private double ele; // elevation in feet MSL
   private String name; // a name for the waypoint
   private String address;

   public Waypoint(double lat, double lon, double ele, String name, String address){
      this.lat = lat;
      this.lon = lon;
      this.ele = ele;
      this.name = name;
      this.address = address;
   }

   public Waypoint(Waypoint copy){
      this.lat = copy.getLatitude();
      this.lon = copy.getLongitude();
      this.ele = copy.getElevation();
      this.name = copy.getName();
      this.address = copy.getAddress();
   }

   public Waypoint(JSONObject obj){
      this.lat = obj.getDouble("lat");
      this.lon = obj.getDouble("lon");
      this.ele = obj.getDouble("ele");
      this.name = obj.getString("name");
      this.address = obj.getString("address");
   }
   
   /**
    * calculate the distance great circle route between this and the
    * argument waypoints. Return the result in the requested scale
    */
   public double distanceGCTo(Waypoint wp, int scale){
      double ret = 0.0;
      // ret is in kilometers if you use the availble algorithms. S
      double lat1 = Math.toRadians(this.getLatitude());
      double lat2 = Math.toRadians(wp.getLatitude());
      double deltaLat = Math.toRadians(wp.getLatitude()-this.getLatitude());
      double deltaLon = Math.toRadians(wp.getLongitude()-this.getLongitude());
      double a = Math.sin(deltaLat/2)*Math.sin(deltaLat/2) +
                 Math.cos(lat1)*Math.cos(lat2)*
                 Math.sin(deltaLon/2)*Math.sin(deltaLon/2);
      double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
      ret = radiusE * c;
      // Switch to either Statute or Nautical if necessary
      switch(scale) {
      case STATUTE:
         ret = ret * 0.62137119;
         break;
      case NAUTICAL:
         ret = ret * 0.5399568;
         break;
      }
      return ret;
   }

   /**
    * calculate the initial heading on the circle route between this and the
    * argument waypoints. Return the result.
    */
   public double bearingGCInitTo(Waypoint wp, int scale){
      double ret = 0.0;
      double lat1 = Math.toRadians(this.getLatitude());
      double lat2 = Math.toRadians(wp.getLatitude());
      double deltaLat = Math.toRadians(wp.getLatitude()-this.getLatitude());
      double deltaLon = Math.toRadians(wp.getLongitude()-this.getLongitude());
      double y = Math.sin(deltaLon) * Math.cos(lat2);
      double x = Math.cos(lat1) * Math.sin(lat2) - 
                 Math.sin(lat1) * Math.cos(lat2) * Math.cos(deltaLat);
      double brng = Math.atan2(y,x);
      ret = Math.toDegrees(brng);
      return ret;
   }

   /**
    * Latitude getter. 
    * @return The Latitude of the waypoint.
    */
    public double getLatitude(){
        return this.lat;
    }

    /**
    * Latitude setter. 
    * @param The new Latitude of the waypoint.
    */
    public void setLatitude(double lat){
        this.lat = lat;
    }

    /**
    * Longitude getter. 
    * @return The Longitude of the waypoint.
    */
    public double getLongitude(){
        return this.lon;
    }

    /**
    * Longitude setter. 
    * @param The new Longitude of the waypoint.
    */
    public void setLongitude(double lon){
        this.lon = lon;
    }

    /**
    * Elevation getter. 
    * @return The release date of the waypoint.
    */
    public double getElevation(){
        return this.ele;
    }

    /**
    * Elevation setter. 
    * @param The new Elevation of the waypoint.
    */
    public void setElevation(double ele){
        this.ele = ele;
    }

    /**
    * Name getter. 
    * @return The Name of the waypoint.
    */
    public String getName(){
        return this.name;
    }

    /**
    * Name setter. 
    * @param The new name of the waypoint.
    */
    public void setName(String name){
        this.name = name;
    }

    /**
    * Address getter. 
    * @return The Address of the waypoint.
    */
    public String getAddress(){
        return this.address;
    }

    /**
    * Address setter. 
    * @param The new Address of the waypoint.
    */
    public void setAddress(String address){
        this.address = address;
    }

    /**
    * Export the waypoint information into a JSONObject.
    * 
    * @return A JSONObject with all the waypoint information.
    */
    public JSONObject toJSONObject(){
        JSONObject obj = new JSONObject();
        obj.put("lon",this.lon);
        obj.put("address",this.address);
        obj.put("name",this.name);
        obj.put("lat",this.lat);
        obj.put("ele",this.ele);
        return obj;
    }

    public String toJsonString(){
      String ret = "";
      try{
         ret = this.toJSONObject().toString();
      }catch (Exception ex){
         System.out.println(this.getClass().getSimpleName()+
                            ": error converting to json string");
      }
      return ret;
   }

    /**
    * Returns a String representation of the waypoint.
    * 
    * @return A string with all the information.
    */
    public void print () {
      System.out.println("Waypoint "+name+": lat "+lat+" lon "+lon+
                         " elevation "+ele);
   }

   public String toString(){
      StringBuilder sb = new StringBuilder();
      sb.append("Waypoint ").append(name).append(": lat->");
      sb.append(lat).append(" lon->").append(lon);
      sb.append(" elevation->").append(ele);
      return sb.toString();
   }

}
