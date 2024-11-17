.DEFAULT_GOAL := help

CLUSTER_NAME = disecomm-cluster
CURRENT_DIR = $(shell pwd)

create-cluster:
	@echo "\n🏗️ Choose the cluster to build:"
	@echo "1. K3d"
	@echo "2. Kind"
	@read -p "Enter your choice (1): " choice; \
	choice=$${choice:-1}; \
	if [ "$$choice" = "1" ]; then \
		$(MAKE) create-k3d-cluster; \
	elif [ "$$choice" = "2" ]; then \
		$(MAKE) create-kind-cluster; \
	else \
		echo "❌ Invalid choice"; \
	fi

create-k3d-cluster:
	@echo "\n🏗️ Creating k3d cluster...\n"
	@k3d cluster create $(CLUSTER_NAME) --config k3d-config.yaml
	@$(MAKE) install-calico
	@$(MAKE) install-traefik
	@echo "✅ Cluster created successfully"

# https://docs.tigera.io/calico/latest/getting-started/kubernetes/k3s/quickstart
install-calico:
	@echo "\n🔍 Install Calico network policy engine..."
	@helm repo add projectcalico https://docs.tigera.io/calico/charts
	@helm repo update
	@kubectl create namespace tigera-operator
	@helm install calico projectcalico/tigera-operator --version v3.29.0 --namespace tigera-operator
	@echo "✅ Calico installed successfully"

# https://doc.traefik.io/traefik/getting-started/install-traefik/#use-the-helm-chart
install-traefik:
	@echo "\n🔍 Install Traefik Ingress Controller..."
	@helm repo add traefik https://traefik.github.io/charts
	@helm repo update
	@helm install traefik traefik/traefik --namespace kube-system
	@echo "✅ Traefik installed successfully"

create-kind-cluster:
	@echo "\n😢 Not implemented yet"

delete-cluster:
	@echo "\n🧹 Deleting cluster..."
	@echo "Which cluster do you want to delete?"
	@echo "1. K3d"
	@echo "2. Kind"
	@read -p "Enter your choice (1): " choice; \
	choice=$${choice:-1}; \
	if [ "$$choice" = "1" ]; then \
		k3d cluster delete $(CLUSTER_NAME); \
	elif [ "$$choice" = "2" ]; then \
		echo "😢 Not implemented yet"; \
	else \
		echo "❌ Invalid choice"; \
	fi

help:
	@echo "\n📚 Available commands:"
	@echo "create-cluster: Create a local Kubernetes cluster"
	@echo "delete-cluster: Delete a local Kubernetes cluster"

# Pattern rule to display help for unknown targets
%:
	@echo "Unknown target '$@'."
	@$(MAKE) help
	@exit 1
