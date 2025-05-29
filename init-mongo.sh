#!/bin/bash
set -e # Exit immediately if a command exits with a non-zero status.

echo "Importing reviews.json..."

mongoimport --host localhost \
            --port 27017 \
            -u "$MONGO_INITDB_ROOT_USERNAME" \
            -p "$MONGO_INITDB_ROOT_PASSWORD" \
            --authenticationDatabase admin \
            --db "$MONGO_INITDB_DATABASE" \
            --collection reviews \
            --file /init-data/reviews.json \
            --jsonArray # Use this if reviews.json is an array of JSON objects. Remove if it's JSON Lines.
            # Add --drop if you want to clear the collection before importing
            # Add other mongoimport options as needed

echo "Successfully imported reviews.json into $MONGO_INITDB_DATABASE.reviews collection."

mysql  < ../insert_data.sql
