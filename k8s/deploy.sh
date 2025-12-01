#!/bin/bash

set -e

echo "Deploying Books Management System..."

CLUSTER_NAME="books-system"
CONTEXT="kind-${CLUSTER_NAME}"

wait_for_pods() {
    local label=$1
    local timeout=${2:-300}
    local extra_wait=60
    
    echo "Waiting for $label..."
    sleep 5
    
    # Primeira tentativa com o timeout original
    if kubectl wait --for=condition=ready pod -l app=$label -n books-system --context=$CONTEXT --timeout=${timeout}s 2>/dev/null; then
        echo "$label is ready!"
        return 0
    fi
    
    # Se falhou, tenta mais 2x com 60s cada
    for attempt in 1 2; do
        echo "Attempt $attempt failed for $label. Waiting extra ${extra_wait}s..."
        if kubectl wait --for=condition=ready pod -l app=$label -n books-system --context=$CONTEXT --timeout=${extra_wait}s 2>/dev/null; then
            echo "$label is ready!"
            return 0
        fi
    done
    
    echo "ERROR: $label failed to become ready"
    kubectl get pods -l app=$label -n books-system --context=$CONTEXT
    kubectl describe pod -l app=$label -n books-system --context=$CONTEXT | tail -20
    exit 1
}

wait_for_daemonset() {
    local name=$1
    local timeout=${2:-120}
    local extra_wait=60
    
    echo "Waiting for $name..."
    sleep 3
    
    # Primeira tentativa com o timeout original
    if kubectl rollout status daemonset/$name -n books-system --context=$CONTEXT --timeout=${timeout}s 2>/dev/null; then
        echo "$name is ready!"
        return 0
    fi
    
    # Se falhou, tenta mais 2x com 60s cada
    for attempt in 1 2; do
        echo "Attempt $attempt failed for $name. Waiting extra ${extra_wait}s..."
        if kubectl rollout status daemonset/$name -n books-system --context=$CONTEXT --timeout=${extra_wait}s 2>/dev/null; then
            echo "$name is ready!"
            return 0
        fi
    done
    
    echo "ERROR: $name failed to become ready"
    kubectl get daemonset $name -n books-system --context=$CONTEXT
    exit 1
}

# Cluster
if kind get clusters 2>/dev/null | grep -q "^books-system$"; then
    echo "Cluster 'books-system' already exists"
    # Garantir que o contexto está configurado
    kubectl config use-context $CONTEXT
else
    echo "Creating Kind cluster..."
    kind create cluster --config kind-config.yaml --name books-system
fi

# Namespace and configs
kubectl apply -f namespace.yaml --context=$CONTEXT
kubectl apply -f configmaps.yaml --context=$CONTEXT
kubectl apply -f persistent-volumes.yaml --context=$CONTEXT

# PostgreSQL
kubectl apply -f postgres.yaml --context=$CONTEXT
wait_for_pods "postgres" 180

# MongoDB
kubectl apply -f mongodb.yaml --context=$CONTEXT
wait_for_pods "mongodb" 180

# RabbitMQ
kubectl apply -f rabbitmq.yaml --context=$CONTEXT
wait_for_pods "rabbitmq" 180

# Consul
kubectl apply -f consul.yaml --context=$CONTEXT
wait_for_pods "consul" 120

# Zipkin
kubectl apply -f zipkin.yaml --context=$CONTEXT
wait_for_pods "zipkin" 120

# Loki
kubectl apply -f loki.yaml --context=$CONTEXT
wait_for_pods "loki" 120

# Promtail
kubectl apply -f promtail.yaml --context=$CONTEXT
sleep 5
wait_for_daemonset "promtail" 120

# Grafana
kubectl apply -f grafana-dashboards-configmap.yaml --context=$CONTEXT
kubectl apply -f grafana.yaml --context=$CONTEXT
wait_for_pods "grafana" 120

echo "Infrastructure ready. Building images..."

# Build images
cd ..
docker build -t api-gateway:latest ./api-gateway
docker build -t books-service:latest ./books-management-api
docker build -t reviews-service:latest ./reviews-service
docker build -t frontend:latest ./books-management-frontend

# Load images into Kind
kind load docker-image api-gateway:latest --name books-system
kind load docker-image books-service:latest --name books-system
kind load docker-image reviews-service:latest --name books-system
kind load docker-image frontend:latest --name books-system

cd k8s

# Books Service
kubectl apply -f books-service.yaml --context=$CONTEXT
wait_for_pods "books-service" 180
sleep 10

# Reviews Service
kubectl apply -f reviews-service.yaml --context=$CONTEXT
wait_for_pods "reviews-service" 180
sleep 10

# API Gateway
kubectl apply -f api-gateway.yaml --context=$CONTEXT
wait_for_pods "api-gateway" 180

# Frontend
kubectl apply -f frontend.yaml --context=$CONTEXT
wait_for_pods "frontend" 120

echo ""
echo "Deploy complete!"
echo ""
kubectl get pods -n books-system --context=$CONTEXT
echo ""
echo "URLs:"
echo "  Frontend:     http://localhost:3000"
echo "  API Gateway:  http://localhost:8080"
echo "  Grafana:      http://localhost:3001 (admin/admin)"
echo "  Zipkin:       http://localhost:9411"
echo "  Consul:       http://localhost:8500"
echo "  RabbitMQ:     http://localhost:15672 (admin/admin)"