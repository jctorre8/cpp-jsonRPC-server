package ser321.assign5;

import java.util.Vector;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;
import java.util.Enumeration;
import java.io.FileInputStream;
import java.io.Serializable;
import java.io.PrintWriter;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import org.json.JSONString;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.JSONArray;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Copyright 201 Jean Torres,
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
 * Purpose: Class tp act as a library of waypoints compatible with JSON
 *
 * Ser321 Foundations of Distributed Applications
 * see http://pooh.poly.asu.edu/Ser321
 * @author Jean Torres jctorre8@asu.edu
 * @version January 2018
 */

public class WaypointLibraryHttpProxy  extends Object implements JSONString, Serializable {
    private Vector<Waypoint> library;
    static final boolean debugOn = false;
    private final Map<String, String> headers;
    private URL url;
    private String requestData;
    private static int callid =0;

    /**
    * No parameter constructor. Just creates an empty Vector.
    */
    public WaypointLibraryHttpProxy(URL url) {
      this.url = url;
      this.headers = new HashMap<String, String>();
    }

    private JSONObject buildCall(String method){
      JSONObject jobj = new JSONObject("{\"jsonrpc\":\"2.0\",\"method\":\""+method+"\",\"id\":"+callid+"}");
      return jobj;
    }


    /**
    * Outputs the content of the library into a string representation of json.
    * 
    * @return The string json content of the library.
    */
    public String toJSONString(){
        String ret;
        JSONObject obj = new JSONObject();
        for (Enumeration<Waypoint> e = this.library.elements(); e.hasMoreElements();){
            Waypoint waypoint = (Waypoint)e.nextElement();
            obj.put(waypoint.getName(),waypoint.toJSONObject());
        }
        ret = obj.toString();
        return ret;
    }

    /**
    * Adds a waypoint to the library.
    * 
    * @param A Waypoint object to be added to the library
    * @return True if successful and false if don't.
    */
    public boolean add(Waypoint aWaypoint){
        boolean ret = false;
        try{
            JSONObject jobj = this.buildCall("add");
            JSONArray params = new JSONArray();
            params.put(aWaypoint.toJSONObject());
            jobj.put("params",params);
            String request = jobj.toString();
            String response = this.call(request);
            debug("add returned: "+response);
        }catch(Exception ex){
            System.out.println("exception in add "+aWaypoint.toJsonString()+" error: "+ex.getMessage());
        }
        return ret;
    }

    /**
    * Adds a new waypoint to the library.
    * 
    * @param A Waypoint object to be added to the library
    * @return True if successful and false if don't.
    */
    public boolean addNew(String lat, String lon, String ele, String name, String address){
        double latitude = Double.parseDouble(lat);
        double longitude = Double.parseDouble(lon);
        double elevation = Double.parseDouble(ele);
        Waypoint temp = new Waypoint(latitude, longitude, elevation, name, address);
        
        return this.add(temp);
    }

    /**
    * Removes the waypoint with the matching name.
    * 
    * @param  The name of the waypoint that needs to be removed.
    * @return True if the waypoint was removed successfully, false if don't.
    */
    public boolean remove(String name){
        boolean ret = false;
        try{
            JSONObject jobj = this.buildCall("remove");
            JSONArray params = new JSONArray();
            params.put(name);
            jobj.put("params",params);
            String request = jobj.toString();
            String response = this.call(request);
            debug("remove returned: "+response);
            JSONObject respObj = new JSONObject(response);
            if(!respObj.has("error")){
                ret = respObj.optBoolean("result");
            }
        }catch(Exception ex){
            System.out.println("exception in remove "+name+" error: "+ex.getMessage());
        }
        return ret;
   }

    /**
    * Get the waypoint that matches the given name.
    * 
    * @param  The the name of the waypoint that needs to be returned.
    * @return The waypoint that has the name.
    */
    public Waypoint get(String name){
        Waypoint ret =  new Waypoint(0,0,0,"unknown","unknown");
        try{
            JSONObject jobj = this.buildCall("get");
            JSONArray params = new JSONArray();
            params.put(name);
            jobj.put("params",params);
            String request = jobj.toString();
            String response = this.call(request);
            debug("remove returned: "+response);
            JSONObject respObj = new JSONObject(response);
            if(!respObj.has("error")){
                JSONObject obj = respObj.optJSONObject("result");
                ret = new Waypoint(obj);
            }
      }catch(Exception ex){
            System.out.println("exception in get "+name+" error: "+ex.getMessage());
      }
      return ret;
    }

    /**
    * Imports the waypoints from JSON file.
    * 
    * @return True if the waypoints were successfully imported to a JSON file, 
    *          False if not.
    */
    public boolean restoreFromFile(){
        boolean ret = false;
        try{
            JSONObject jobj = this.buildCall("resetFromJsonFile");
            JSONArray params = new JSONArray();
            jobj.put("params",params);
            String request = jobj.toString();
            String response = this.call(request);
            debug("restoreFromFile returned: "+response);
            JSONObject respObj = new JSONObject(response);
            if(!respObj.has("error")){
                ret = respObj.optBoolean("result");
            }
      }catch(Exception ex){
            System.out.println("exception in resetFromJsonFile error: "+ex.getMessage());
      }
      return ret;
   }

    /**
    * Export the current waypoints to a JSON file.
    * 
    * @return True if the waypoints were successfully exported to a JSON file, 
    *          False if not.
    */
    public boolean saveToFile(){
      boolean ret = false;
      try{
            JSONObject jobj = this.buildCall("saveToJsonFile");
            JSONArray params = new JSONArray();
            jobj.put("params",params);
            String request = jobj.toString();
            String response = this.call(request);
            debug("getNames returned: "+response);
            JSONObject respObj = new JSONObject(response);
            if(!respObj.has("error")){
                ret = respObj.optBoolean("result");
            }
      }catch(Exception ex){
            System.out.println("exception in resetFromJsonFile error: "+ex.getMessage());
      }
      return ret;
    }

    /**
    * This method collects all the the waypoint names in the library and returns them.
    * 
    * @return An array of strings with all the waypoint names in the library.
    */
    public String[] getNames(){
        String[] ret = new String[]{};
        try{
            JSONObject jobj = this.buildCall("getNames");
            JSONArray params = new JSONArray();
            jobj.put("params",params);
            String request = jobj.toString();
            String response = this.call(request);
            debug("getNames returned: "+response);
            JSONObject respObj = new JSONObject(response);
            if(!respObj.has("error")){
                ArrayList<String> al = new ArrayList<String>();
                JSONArray ja = respObj.optJSONArray("result");
                for(int i=0; i<ja.length(); i++){
                    String aName = ja.optString(i);
                    if(aName!=""){
                        al.add(aName);
                    }
                }
                ret = al.toArray(new String[]{});
                Arrays.sort(ret);
            }
        }catch(Exception ex){
            System.out.println("exception in getNames error: "+ex.getMessage());
        }
        return ret;
    }

    /*
    *getById(int id)-->String
    */
    public String getById(int id){
        String ret = "unknown";
        try{
            JSONObject jobj = this.buildCall("getById");
            JSONArray params = new JSONArray();
            params.put(id);
            jobj.put("params",params);
            String request = jobj.toString();
            String response = this.call(request);
            debug("remove returned: "+response);
            JSONObject respObj = new JSONObject(response);
            if(!respObj.has("error")){
                ret = respObj.optString("result");
            }
        }catch(Exception ex){
            System.out.println("exception in getById "+id+", error: "+ex.getMessage());
        }
        return ret;
    }

    public void setHeader(String key, String value) {
        this.headers.put(key, value);
    }

    public String call(String requestData) throws Exception {
        debug("in call, url: "+url.toString()+" requestData: "+requestData);
        String respData = post(url, headers, requestData);
        return respData;
    }

    private String post(URL url, Map<String, String> headers, String data) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        this.requestData = data;
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                connection.addRequestProperty(entry.getKey(), entry.getValue());
            }
        }
        connection.addRequestProperty("Accept-Encoding", "gzip");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.connect();
        OutputStream out = null;
        try {
            out = connection.getOutputStream();
            out.write(data.getBytes());
            out.flush();
            out.close();
            int statusCode = connection.getResponseCode();
            if (statusCode != HttpURLConnection.HTTP_OK) {
                throw new Exception(
                "Unexpected status from post: " + statusCode);
            }
        } finally {
            if (out != null) {
                out.close();
            }
        }
        String responseEncoding = connection.getHeaderField("Content-Encoding");
        responseEncoding = (responseEncoding == null ? "" : responseEncoding.trim());
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        InputStream in = connection.getInputStream();
        try {
            in = connection.getInputStream();
            if ("gzip".equalsIgnoreCase(responseEncoding)) {
                in = new GZIPInputStream(in);
            }
            in = new BufferedInputStream(in);
            byte[] buff = new byte[1024];
            int n;
            while ((n = in.read(buff)) > 0) {
                bos.write(buff, 0, n);
            }
            bos.flush();
            bos.close();
        } finally {
            if (in != null) {
                in.close();
            }
        }
        debug("post: json rpc request via http returned string "+bos.toString());
        return bos.toString();
    }

    private static void debug(String message) {
      if (debugOn)
         System.out.println("debug: "+message);
   }

}
