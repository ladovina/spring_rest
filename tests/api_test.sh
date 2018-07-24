#!/usr/bin/env bash

function print_api_call_info(){
    URL=$1
    HTTP_REQ_TYPE=$2
    echo -e "\n\nCalled URL: ${URL}, request type: ${HTTP_REQ_TYPE}\n"
}
URL="localhost:8080/users"
REQUEST_TYPE="POST"
print_api_call_info ${URL} ${REQUEST_TYPE}
curl -si ${URL} -X ${REQUEST_TYPE}  --data '{"googleId": "googleIdValue", "accessToken": "accessTokenValue"}' --header 'Content-Type: application/json' --header 'Accept: application/json'

URL="localhost:8080/users/googleIdValue"
REQUEST_TYPE="DELETE"
print_api_call_info ${URL} ${REQUEST_TYPE}
curl -si ${URL} -X ${REQUEST_TYPE} --header 'Content-Type: application/json' --header 'Accept: application/json'

URL="localhost:8080/users/googleIdValue"
REQUEST_TYPE="GET"
print_api_call_info ${URL} ${REQUEST_TYPE}
curl -si ${URL} -X ${REQUEST_TYPE} --header 'Content-Type: application/json' --header 'Accept: application/json'
echo -e "\n"

URL="localhost:8080/users/googleIdValue/activities"
REQUEST_TYPE="GET"
print_api_call_info ${URL} ${REQUEST_TYPE}
curl -si ${URL} -X ${REQUEST_TYPE} --header 'Content-Type: application/json' --header 'Accept: application/json'
