#!/bin/bash

echo "################################"
echo "     Iniciando docker Mongo     "
echo "################################"

docker-compose up -d mongo

echo "#########################################"
echo "     Iniciando docker Mongo Express     "
echo "#########################################"

docker-compose up -d mongo-express

echo "####################################"
echo "     Iniciando docker Aplicação     "
echo "####################################"

docker-compose up java 
