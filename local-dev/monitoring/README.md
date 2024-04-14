## Monitoring Notes

### Grafana

- Docs
    - https://grafana.com/docs/grafana/latest/
- Provisioning Data Sources
    - https://grafana.com/docs/grafana/latest/administration/provisioning/
- Extract datasources
    - `curl -s "http://localhost:3000/api/datasources"  -u admin:admin|jq -c -M '.[]' | jq .`
- Datasources yml ref:
    - https://github.com/grafana/grafana/blob/main/devenv/datasources.yaml

### Otel

- Exporters
    - [Loki Exporter](https://github.com/open-telemetry/opentelemetry-collector-contrib/blob/main/exporter/lokiexporter/README.md)
- Processors
    - [Attributes Processors](https://github.com/open-telemetry/opentelemetry-collector-contrib/blob/main/processor/attributesprocessor/README.md)
    - [Resource Processor](https://github.com/open-telemetry/opentelemetry-collector-contrib/tree/main/processor/resourceprocessor)
    - [Resource Detector Processor](https://github.com/open-telemetry/opentelemetry-collector-contrib/tree/main/processor/resourcedetectionprocessor)
