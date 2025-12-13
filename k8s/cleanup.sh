#!/bin/bash

NAMESPACE="books-system"

kubectl delete -f frontend.yaml --ignore-not-found
kubectl delete -f api-gateway.yaml --ignore-not-found
kubectl delete -f reviews-service.yaml --ignore-not-found
kubectl delete -f books-service.yaml --ignore-not-found
kubectl delete -f grafana.yaml --ignore-not-found
kubectl delete -f grafana-dashboards-configmap.yaml --ignore-not-found
kubectl delete -f promtail.yaml --ignore-not-found
kubectl delete -f loki.yaml --ignore-not-found
kubectl delete -f zipkin.yaml --ignore-not-found
kubectl delete -f consul.yaml --ignore-not-found
kubectl delete -f rabbitmq.yaml --ignore-not-found
kubectl delete -f mongodb.yaml --ignore-not-found
kubectl delete -f postgres.yaml --ignore-not-found
kubectl delete -f persistent-volumes.yaml --ignore-not-found
kubectl delete -f configmaps.yaml --ignore-not-found
kubectl delete -f namespace.yaml --ignore-not-found

echo "Minikube pode ser removido com o comando: minikube delete"
