/**
 * This file is generated by jsonrpcstub, DO NOT CHANGE IT MANUALLY!
 */

#ifndef JSONRPC_CPP_STUB_WAYPOINTLIBRARYSTUB_H_
#define JSONRPC_CPP_STUB_WAYPOINTLIBRARYSTUB_H_

#include <jsonrpccpp/client.h>

class waypointlibrarystub : public jsonrpc::Client
{
    public:
        waypointlibrarystub(jsonrpc::IClientConnector &conn, jsonrpc::clientVersion_t type = jsonrpc::JSONRPC_CLIENT_V2) : jsonrpc::Client(conn, type) {}

        bool saveToJsonFile() throw (jsonrpc::JsonRpcException)
        {
            Json::Value p;
            p = Json::nullValue;
            Json::Value result = this->CallMethod("saveToJsonFile",p);
            if (result.isBool())
                return result.asBool();
            else
                throw jsonrpc::JsonRpcException(jsonrpc::Errors::ERROR_CLIENT_INVALID_RESPONSE, result.toStyledString());
        }
        bool resetFromJsonFile() throw (jsonrpc::JsonRpcException)
        {
            Json::Value p;
            p = Json::nullValue;
            Json::Value result = this->CallMethod("resetFromJsonFile",p);
            if (result.isBool())
                return result.asBool();
            else
                throw jsonrpc::JsonRpcException(jsonrpc::Errors::ERROR_CLIENT_INVALID_RESPONSE, result.toStyledString());
        }
        bool add(const Json::Value& param1) throw (jsonrpc::JsonRpcException)
        {
            Json::Value p;
            p.append(param1);
            Json::Value result = this->CallMethod("add",p);
            if (result.isBool())
                return result.asBool();
            else
                throw jsonrpc::JsonRpcException(jsonrpc::Errors::ERROR_CLIENT_INVALID_RESPONSE, result.toStyledString());
        }
        bool remove(const std::string& param1) throw (jsonrpc::JsonRpcException)
        {
            Json::Value p;
            p.append(param1);
            Json::Value result = this->CallMethod("remove",p);
            if (result.isBool())
                return result.asBool();
            else
                throw jsonrpc::JsonRpcException(jsonrpc::Errors::ERROR_CLIENT_INVALID_RESPONSE, result.toStyledString());
        }
        Json::Value get(const std::string& param1) throw (jsonrpc::JsonRpcException)
        {
            Json::Value p;
            p.append(param1);
            Json::Value result = this->CallMethod("get",p);
            if (result.isObject())
                return result;
            else
                throw jsonrpc::JsonRpcException(jsonrpc::Errors::ERROR_CLIENT_INVALID_RESPONSE, result.toStyledString());
        }
        Json::Value getNames() throw (jsonrpc::JsonRpcException)
        {
            Json::Value p;
            p = Json::nullValue;
            Json::Value result = this->CallMethod("getNames",p);
            if (result.isArray())
                return result;
            else
                throw jsonrpc::JsonRpcException(jsonrpc::Errors::ERROR_CLIENT_INVALID_RESPONSE, result.toStyledString());
        }
        std::string distanceAndBearing(const std::string& param1, const std::string& param2) throw (jsonrpc::JsonRpcException)
        {
            Json::Value p;
            p.append(param1);
            p.append(param2);
            Json::Value result = this->CallMethod("distanceAndBearing",p);
            if (result.isString())
                return result.asString();
            else
                throw jsonrpc::JsonRpcException(jsonrpc::Errors::ERROR_CLIENT_INVALID_RESPONSE, result.toStyledString());
        }
        bool updateWaypoint(const std::string& param1, const std::string& param2, const std::string& param3, const std::string& param4, const std::string& param5) throw (jsonrpc::JsonRpcException)
        {
            Json::Value p;
            p.append(param1);
            p.append(param2);
            p.append(param3);
            p.append(param4);
            p.append(param5);
            Json::Value result = this->CallMethod("updateWaypoint",p);
            if (result.isBool())
                return result.asBool();
            else
                throw jsonrpc::JsonRpcException(jsonrpc::Errors::ERROR_CLIENT_INVALID_RESPONSE, result.toStyledString());
        }
        bool addNew(const std::string& param1, const std::string& param2, const std::string& param3, const std::string& param4, const std::string& param5) throw (jsonrpc::JsonRpcException)
        {
            Json::Value p;
            p.append(param1);
            p.append(param2);
            p.append(param3);
            p.append(param4);
            p.append(param5);
            Json::Value result = this->CallMethod("addNew",p);
            if (result.isBool())
                return result.asBool();
            else
                throw jsonrpc::JsonRpcException(jsonrpc::Errors::ERROR_CLIENT_INVALID_RESPONSE, result.toStyledString());
        }
};

#endif //JSONRPC_CPP_STUB_WAYPOINTLIBRARYSTUB_H_
