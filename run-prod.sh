#!/bin/bash

docker-compose down

docker-compose -f compose.yml -f compose.prod.yml up --build -d