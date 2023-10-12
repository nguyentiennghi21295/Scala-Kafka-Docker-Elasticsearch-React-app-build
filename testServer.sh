#!/bin/bash
echo "Rule No1 - Ensure that the server is running "
echo "Sending request to localhost:8080/api/v1/event"

for i in {1..1}
do
    curl --request POST -sL -i -v \
         --header "Content-Type: application/json" \
         --url "http://localhost:8080/api/v1/event" \
         --data '{
            "userName": "BlueBerry12321321",
            "message": "I have completed the Elasticsearch part of this app",
            "mood": "I feel great today :)",
            "whatIPlanToAchieve": "Tomorrow is a good day to get on new gooals"
         }'
    echo "Request number $i"
done
