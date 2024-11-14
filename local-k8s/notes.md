## Cluster Setup

Use k3d to create a local k8s cluster. This will create a k3s cluster in docker.

```bash
k3d cluster create disecomm-cluster --servers 1 --agents 3 -p "30000:30000@loadbalancer"
``` 

- `--servers 1` specifies the number of control plane nodes
- `--agents 3` specifies the number of worker nodes
- `-p "30000:30000@loadbalancer"` specifies the port mapping for the load balancer

Delete the cluster with:

```bash
k3d cluster delete disecomm-cluster
```

## Debugging

- naked pod using curl
    - `kubectl run -i --tty --rm debug --image=radial/busyboxplus --restart=Never -- sh`
    - `curl <service-name>.<namespace>.svc.cluster.local:<port>`

## Database deployments

- namespace
    - will contain all database resources
    - `kubectl create namespace disecomm-databases`
- statefulset
    - will contain the database pod
    - will have 1 write pod and 2 read replicas
- service
    - will expose the database pod in a headless service
      - why headless? because we don't need a load balancer
    - will be of type `ClusterIP`
    - to access the primary
        - `<primary-service-name>.<namespace>.svc.cluster.local:<port>`
    - to access the replicas
      - `<replica-service-name>.<namespace>.svc.cluster.local:<port>`
      - to access a specific replica, inform the pod name in addition to the service name
          - `<pod-name>.<replica-service-name>.<namespace>.svc.cluster.local:<port>`
- persistent volume
    - will contain the database data
    - `k get storageclass`
- configmap
    - will contain the database configuration
- secret
    - will contain the database url, username, and password
- testing with port-forward
    - `kubectl port-forward svc/<service-name> <local-port>:<service-port>`
