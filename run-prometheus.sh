#!/bin/bash

source ./.env

$PROMETHEUS_LOCATION --config.file=./prometheus.yml