#!/bin/bash

# network shared-disecomm-network

# running local ref
# k6 run --out experimental-prometheus-rw main.js

# set container name to k6-load-test
docker run -i \
  --rm \
  -e DOCKER=true \
  -e K6_PROMETHEUS_RW_SERVER_URL="http://prometheus:9090/api/v1/write" \
  -e K6_PROMETHEUS_RW_TREND_STATS="p(95),p(99),min,max,sum,avg,med" \
  -e K6_PROMETHEUS_RW_TREND_AS_NATIVE_HISTOGRAM=true \
  --network shared-disecomm-network \
  grafana/k6 run - < main.js
