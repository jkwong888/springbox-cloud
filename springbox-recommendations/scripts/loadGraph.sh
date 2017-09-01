#!/bin/bash

auth_server_url=http://localhost:9999/uaa/oauth/token

export token=`curl -X POST -u acme:acmesecret ${auth_server_url}\?scope=openid\&username=mstine\&password=secret\&grant_type=password | jq -r '.access_token'`

./loadPeople.sh
./loadMovies.sh
./loadLikes.sh
