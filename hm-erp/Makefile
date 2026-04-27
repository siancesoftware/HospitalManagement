.PHONY: help build clean test run-infra stop-infra run-patient run-all docker-build

MAVEN = ./mvnw

help: ## Show this help
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-25s\033[0m %s\n", $$1, $$2}'

build: ## Build all modules
	$(MAVEN) clean package -DskipTests

build-fast: ## Build without tests (faster)
	$(MAVEN) clean package -DskipTests -q

clean: ## Clean all build artifacts
	$(MAVEN) clean

test: ## Run all tests
	$(MAVEN) test

test-integration: ## Run integration tests
	$(MAVEN) verify -Pintegration-test

# Infrastructure
run-infra: ## Start all infrastructure (Postgres, Kafka, Redis, etc.)
	docker compose up -d

stop-infra: ## Stop all infrastructure
	docker compose down

reset-infra: ## Reset infrastructure (destroy volumes)
	docker compose down -v

logs-infra: ## Show infrastructure logs
	docker compose logs -f

# Individual services
run-config: ## Start config server
	$(MAVEN) -pl hm-config-server spring-boot:run

run-discovery: ## Start discovery server
	$(MAVEN) -pl hm-discovery-server spring-boot:run

run-gateway: ## Start API gateway
	$(MAVEN) -pl hm-gateway spring-boot:run

run-patient: ## Start patient service
	$(MAVEN) -pl hm-patient-service spring-boot:run

run-appointment: ## Start appointment service
	$(MAVEN) -pl hm-appointment-service spring-boot:run

run-opd: ## Start OPD service
	$(MAVEN) -pl hm-opd-service spring-boot:run

run-billing: ## Start billing service
	$(MAVEN) -pl hm-billing-service spring-boot:run

run-masterdata: ## Start master data service
	$(MAVEN) -pl hm-master-data-service spring-boot:run

run-notification: ## Start notification service
	$(MAVEN) -pl hm-notification-service spring-boot:run

# Docker
docker-build: ## Build Docker images for all services
	docker build --build-arg SERVICE_NAME=hm-patient-service -t siance/hm-patient-service:latest .
	docker build --build-arg SERVICE_NAME=hm-appointment-service -t siance/hm-appointment-service:latest .
	docker build --build-arg SERVICE_NAME=hm-opd-service -t siance/hm-opd-service:latest .
	docker build --build-arg SERVICE_NAME=hm-billing-service -t siance/hm-billing-service:latest .
	docker build --build-arg SERVICE_NAME=hm-gateway -t siance/hm-gateway:latest .

# Utilities
db-migrate: ## Run Flyway migrations for all services
	$(MAVEN) -pl hm-patient-service flyway:migrate
	$(MAVEN) -pl hm-appointment-service flyway:migrate
	$(MAVEN) -pl hm-opd-service flyway:migrate
	$(MAVEN) -pl hm-billing-service flyway:migrate

swagger: ## Open Swagger UI (patient service)
	open http://localhost:8100/swagger-ui.html
