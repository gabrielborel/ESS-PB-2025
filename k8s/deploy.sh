#!/bin/bash
set -e

NAMESPACE="books-system"

wait_pod() {
    local app=$1
    local timeout=${2:-180}
    echo "Aguardando $app..."
    kubectl wait --for=condition=ready pod -l app=$app -n $NAMESPACE --timeout=${timeout}s 2>/dev/null || {
        echo "Erro: $app não ficou pronto"
        kubectl get pods -l app=$app -n $NAMESPACE
        exit 1
    }
}

wait_ds() {
    local name=$1
    echo "Aguardando $name..."
    kubectl rollout status daemonset/$name -n $NAMESPACE --timeout=120s 2>/dev/null || exit 1
}

# Minikube
minikube status &>/dev/null || minikube start --memory=8192 --cpus=4 --driver=docker
eval $(minikube docker-env)

# Base
kubectl apply -f namespace.yaml
kubectl apply -f configmaps.yaml
kubectl apply -f persistent-volumes.yaml

# Infra
kubectl apply -f postgres.yaml && wait_pod postgres
kubectl apply -f mongodb.yaml && wait_pod mongodb
kubectl apply -f rabbitmq.yaml && wait_pod rabbitmq
kubectl apply -f consul.yaml && wait_pod consul 120
kubectl apply -f zipkin.yaml && wait_pod zipkin 120
kubectl apply -f loki.yaml && wait_pod loki 120
kubectl apply -f promtail.yaml && sleep 3 && wait_ds promtail
kubectl apply -f grafana-dashboards-configmap.yaml
kubectl apply -f grafana.yaml && wait_pod grafana 120

# Build
cd ..
docker build -t api-gateway:latest ./api-gateway
docker build -t books-service:latest ./books-management-api
docker build -t reviews-service:latest ./reviews-service
docker build -t frontend:latest ./books-management-frontend
cd k8s

# Apps
kubectl apply -f books-service.yaml && wait_pod books-service
kubectl apply -f reviews-service.yaml && wait_pod reviews-service
kubectl apply -f api-gateway.yaml && wait_pod api-gateway
kubectl apply -f frontend.yaml && wait_pod frontend 120

# Status
echo ""
kubectl get pods -n $NAMESPACE
echo ""

# URLs
IP=$(minikube ip)

echo "Frontend:    http://$IP:30000"
echo "API Gateway: http://$IP:30080"
echo "Grafana:     http://$IP:30001 (admin/admin)"
echo "Zipkin:      http://$IP:30411"
echo "Consul:      http://$IP:30500"
echo "RabbitMQ:    http://$IP:30672 (admin/admin)"
