- https://opentelemetry.io/docs/kubernetes/helm/collector/


## Kubernetes
- https://grafana.com/docs/grafana-cloud/monitor-infrastructure/kubernetes-monitoring/configuration/helm-chart-config/otel-collector/


helm repo add open-telemetry https://open-telemetry.github.io/opentelemetry-helm-charts 
helm install otel-collector open-telemetry/opentelemetry-collector




## Using helm

1. Install Grafana
   - https://grafana.com/docs/grafana/latest/setup-grafana/installation/helm/ 
     - ```bash
         helm install grafana grafana/grafana \
          --namespace monitoring \
          --values local-k8s/monitoring/grafana/helm-values.yaml
       ```
2. Install Prometheus
    - `helm install ksm prometheus-community/kube-state-metrics --namespace monitoring`
3. Install Loki
4. Install Tempo
5. Install OpenTelemetry Operator
   - pre-requisite: cert-manager
     - https://cert-manager.io/docs/installation/helm/
       - ```bash 
           helm install \
            cert-manager jetstack/cert-manager \
            --namespace cert-manager \
            --create-namespace \
            --version v1.16.2 \
            --set crds.enabled=true
         ```
   - https://opentelemetry.io/docs/kubernetes/operator/
   - https://github.com/open-telemetry/opentelemetry-helm-charts/tree/main/charts/opentelemetry-operator
       - ```bash 
            helm install opentelemetry-operator open-telemetry/opentelemetry-operator \
            --namespace monitoring \
            --create-namespace \
             --set "manager.collectorImage.repository=otel/opentelemetry-collector-k8s" \
             --set admissionWebhooks.certManager.enabled=false \
             --set admissionWebhooks.autoGenerateCert.enabled=true 
         ```
6. Install OpenTelemetry Collector

```bash

- operator
  - https://opentelemetry.io/docs/kubernetes/helm/operator/
  - 
