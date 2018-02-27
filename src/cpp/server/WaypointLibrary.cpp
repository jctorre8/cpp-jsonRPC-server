#include "WaypointLibrary.hpp"
#include <fstream>
#include <iostream>
#include <vector>
#include <algorithm>
#include <sstream> 
#include <iomanip> // setprecision


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

/**
* No parameter constructor. Just creates an empty Vector.
*/
WaypointLibrary::WaypointLibrary(){}

/**
* Waypoint Library constructor that takes a list as an argument.
*
* @param An old list of waypoints to initialize the library.
*/
WaypointLibrary::WaypointLibrary(vector<Waypoint> oldLibrary){
    for (int i=0; i<oldLibrary.size(); i++)
        this->library.push_back(oldLibrary[i]);
}

/**
* Waypoint Library constructor that gets populated by a json file.
* 
* @param The name of the json file.
*/
WaypointLibrary::WaypointLibrary(string jsonFileName){
    
    std::ifstream infile;
    Json::Value root;
    Json::Reader reader;
    infile.open("waypoints.json");      
    string data((std::istreambuf_iterator<char>(infile)),
                 std::istreambuf_iterator<char>());  
    // close the opened file.
    infile.close();
    bool parsingSuccessful = reader.parse( data, root );
    if (!parsingSuccessful){
        // report to the user the failure and their locations in the document.
        std::cout  << "Failed to parse configuration\n"
                   << reader.getFormattedErrorMessages();
        return;
    }
    
    for(Json::Value::iterator i= root.begin(); i != root.end(); i++){
        std::cout << (*i)["name"].asString() << endl;
    }

    for (Json::Value::iterator i= root.begin(); i != root.end(); i++){
        Waypoint aWaypoint((*i));
        this->library.push_back(aWaypoint);
    }


}

/**
* Outputs the content of the library into a string representation of json.
* 
* @return The string json content of the library.
*/
string WaypointLibrary::toJSONstring(){
    string ret = "";
    Json::Value obj;
    for (int i = 0; i < this->library.size(); i++){
        cout << "Converting to JSON STR:" << this->library[i].name << endl;
        obj[this->library[i].name] = this->library[i].toJSONObject();
    }
    ret = obj.toStyledString();
    return ret;
}

/**
* Adds a waypoint to the library.
* 
* @param A Waypoint object to be added to the library
* @return True if successful and false if don't.
*/
bool WaypointLibrary::add(const Json::Value& aWaypointJson){
    Waypoint aWaypoint(aWaypointJson);
    this->library.push_back(aWaypoint);
    return true;
}

/**
* Adds a new waypoint to the library.
* 
* @param A Waypoint object to be added to the library
* @return True if successful and false if don't.
*/
bool WaypointLibrary::addNew(string lat, string lon, string ele, string name, string address){
    bool ret = false;
    std::string::size_type sz;
    double latitude = std::stod (lat,&sz);
    double longitude = std::stod (lon,&sz);
    double elevation = std::stod (ele,&sz);
    Waypoint temp(latitude, longitude, elevation, name, address);
    
    this->library.push_back(temp);
    ret = true;
    return true;
}

bool WaypointLibrary::updateWaypoint(string lat, string lon, string ele, string name, string address){
    bool ret = false;
    std::string::size_type sz;
    double latitude = std::stod (lat,&sz);
    double longitude = std::stod (lon,&sz);
    double elevation = std::stod (ele,&sz);
    this->remove(name);
    Waypoint temp(latitude,longitude,elevation,name,address);
    this->library.push_back(temp); 
    ret = true;

    return ret;
    
}

/**
* Removes the waypoint with the matching name.
* 
* @param  The name of the waypoint that needs to be removed.
* @return True if the waypoint was removed successfully, false if don't.
*/
bool WaypointLibrary::remove(string name){
    bool ret = false;
    for(int i = 0; i < library.size(); i++){
        Waypoint temp = library[i];
        if(name.compare(temp.name) == 0){
            library.erase(library.begin()+i);
            bool ret = true;
        }
    }
    return ret;
}

/**
* Get the waypoint that matches the given name.
* 
* @param  The the name of the waypoint that needs to be returned.
* @return The waypoint that has the name.
*/
Json::Value  WaypointLibrary::get(string name){
    Waypoint * toReturn = NULL;
    for(int i = 0; i < library.size(); i++){
        if(name.compare(library[i].name) == 0){
            toReturn = &library[i];
            return toReturn;
        }
    }
    return toReturn->toJSONObject();
}

/**
* Imports the waypoints from JSON file.
* 
* @return True if the waypoints were successfully imported to a JSON file, 
*          False if not.
*/
bool WaypointLibrary::restoreFromFile(){
    bool ret = false;
    this->library.clear();
    std::ifstream infile;
    Json::Value root;
    Json::Reader reader;
    infile.open("waypoints.json");      
    string data((std::istreambuf_iterator<char>(infile)),
                 std::istreambuf_iterator<char>());  
    // close the opened file.
    infile.close();
    bool parsingSuccessful = reader.parse( data, root );
    if (parsingSuccessful){
        // report to the user the failure and their locations in the document.
        std::cout  << "Failed to parse configuration\n"
                   << reader.getFormattedErrorMessages();
        for(Json::Value::iterator i= root.begin(); i != root.end(); i++){
        std::cout << (*i)["name"].asString() << endl;
        }

        for (Json::Value::iterator i= root.begin(); i != root.end(); i++){
            Waypoint aWaypoint((*i));
            this->library.push_back(aWaypoint);
        }
        ret = true;
        std::cout << "Done importing waypoints in from waypoints.json" << endl;
    }
    
    return ret;    
}

/**
* Export the current waypoints to a JSON file.
* 
* @return True if the waypoints were successfully exported to a JSON file, 
*          False if not.
*/
bool WaypointLibrary::saveToFile(){
        
        ofstream outfile;
        outfile.open("waypoints.json");
        string data = this->toJSONstring();
        // write inputted data into the file.
        outfile << data << endl;
        outfile.close();
        cout << "Done exporting library to waypoints.json" << endl;
        return true;
}

/**
* This method collects all the the waypoint names in the library and returns them.
* 
* @return An array of strings with all the waypoint names in the library.
*/
Json::Value WaypointLibrary::getNames(){
    Json::Value ret(Json::arrayValue);
    vector<string> myVec;

    for(int i = 0; i < library.size(); i++){
        myVec.push_back(this->library[i].name);
    }
    for(std::vector<string>::iterator it = myVec.begin(); it!=myVec.end();++it) {
        ret.append(Json::Value(*it));
    }
    return ret;
}

string WaypointLibrary::distanceAndBearing(string waypoint1, string waypoint2){
    Waypoint * wpnt1 = new Waypoint(this->get(waypoint1));
    Waypoint * wpnt2 = new Waypoint(this->get(waypoint2));

    double distance = wpnt1->distanceGCTo(*wpnt2,0);
    double bearing = wpnt1->bearingGCInitTo(*wpnt2,0);

    stringstream stream1, stream2;
    string toReturn = "";

    stream1 << fixed << std::setprecision(2) << distance;

    toReturn += stream1.str() + " miles at ";

    stream2 << fixed << std::setprecision(2) << bearing;

    toReturn += stream2.str() + " degrees ";

    delete wpnt2;
    delete wpnt1;

    return toReturn;
}


