#!/bin/bash

# set container name to k6-load-test
docker run -i \
  -e DOCKER=true \
  -e K6_PROMETHEUS_RW_SERVER_URL="http://host.docker.internal:9090/api/v1/write" \
  -e K6_PROMETHEUS_RW_TREND_STATS="p(95),p(99),min,max" \
  -e K6_PROMETHEUS_RW_TREND_AS_NATIVE_HISTOGRAM=true \
  grafana/k6 run - < main.js

#export K6_PROMETHEUS_RW_SERVER_URL="http://host.docker.internal:9090/api/v1/write"
#export K6_PROMETHEUS_RW_TREND_STATS="p(95),p(99),min,max"
#export K6_PROMETHEUS_RW_TREND_AS_NATIVE_HISTOGRAM=true
