- https://opentelemetry.io/docs/kubernetes/helm/collector/


## Kubernetes
- https://grafana.com/docs/grafana-cloud/monitor-infrastructure/kubernetes-monitoring/configuration/helm-chart-config/otel-collector/


helm repo add open-telemetry https://open-telemetry.github.io/opentelemetry-helm-charts 
helm install otel-collector open-telemetry/opentelemetry-collector




## Using helm

1. Install Grafana
   - https://grafana.com/docs/grafana/latest/setup-grafana/installation/helm/ 
2. Install Prometheus
3. Install Loki
4. Install Tempo
5. Install OpenTelemetry Operator
   - pre-requisite: cert-manager
     - https://cert-manager.io/docs/installation/helm/
   - https://opentelemetry.io/docs/kubernetes/operator/
   - https://github.com/open-telemetry/opentelemetry-helm-charts/tree/main/charts/opentelemetry-operator
6. Install OpenTelemetry Collector

```bash

- operator
  - https://opentelemetry.io/docs/kubernetes/helm/operator/
  - 
