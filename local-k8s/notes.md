## Cluster Setup

Use k3d to create a local k8s cluster. This will create a k3s cluster in docker.

```bash
k3d cluster create disecomm-cluster --servers 1 --agents 3 -p "30000:30000@loadbalancer"

# with local registry
k3d cluster create disecomm-cluster --servers 1 --agents 3 -p "30000:30000@loadbalancer" --registry-use k3d-myregistry.localhost:5001
k3d registry create myregistry.localhost --port 5001
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


## Local Image Registry

When you create a Docker image locally and want to use it within your k3d cluster, you need to make it accessible to k3d because k3d operates within its own Docker network. There are a few ways to accomplish this:

1. **Import the Local Image Directly into k3d**: You can import your locally built Docker image into the k3d cluster using the following command:

   ```bash
   k3d image import <your-image-name>:<tag> -c <your-cluster-name>
   ```

   This command will push your local image into the k3d cluster, making it accessible to any pod within that cluster.

2. **Tag and Load the Image**: Alternatively, you could tag your image with a repository prefix like `localhost` and then specify it in your Kubernetes manifest:

   ```bash
   docker tag <local-image-name> localhost:5000/<local-image-name>
   ```

   Then, make sure to push it to the local registry and configure your cluster to pull from this registry.

3. **Use `k3d`’s Local Registry**: If you frequently use local images, you might want to set up a local registry in k3d:

   ```bash
   k3d registry create myregistry.localhost --port 5001
   ```

   After that, push your images to `localhost:5001/<image-name>` and configure your k3d cluster to use this registry.

```bash
➜  k3d registry create myregistry.localhost --port 5001
➜  docker tag disecomm-product-catalog:local localhost:5001/disecomm-product-catalog:local
➜  docker push localhost:5001/disecomm-product-catalog:local
```
