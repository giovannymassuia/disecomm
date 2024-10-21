#!/bin/bash

export K6_PROMETHEUS_RW_SERVER_URL="http://localhost:9090/api/v1/write"
export K6_PROMETHEUS_RW_TREND_STATS="p(95),p(99),min,max,sum,avg,med"
export K6_PROMETHEUS_RW_TREND_AS_NATIVE_HISTOGRAM=true

k6 run --out experimental-prometheus-rw main.js
