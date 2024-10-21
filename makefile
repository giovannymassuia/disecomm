
.PHONY: build_base_dependencies build_app build_single_app build-all run-all clean run-load-test help
.DEFAULT_GOAL := help

ALL_APPS = api-gateway \
		   product-catalog \
		   inventory-management \
		   order-management
TOTAL_APPS = $(words $(ALL_APPS))

build_base_dependencies:
	@echo "📦 Building base dependencies..."
	@start_time=$$(date +%s) \
		&& cd common && ./build_dependencies.sh > /dev/null 2>&1 \
  		&& echo "✅ Base dependencies built successfully" \
  		&& end_time=$$(date +%s) \
  		&& echo "⏱️ Time taken: $$(($$end_time - $$start_time)) seconds" \
		|| (echo "❌ Error building base dependencies"; exit 1)

build-app:
	@$(MAKE) build_base_dependencies
	@echo "🔧 Building app... $(APP-NAME)"
	@$(MAKE) build_single_app

build_single_app:
	@test -n "$(APP-NAME)" || (echo "❌ APP-NAME is not set"; exit 1)
	@tmpfile=$$(mktemp /tmp/build_output_XXXXXX); \
	start_time=$$(date +%s); \
	if ( \
		cd common && \
		./build_app.sh $(APP-NAME) \
	) >$$tmpfile 2>&1; then \
		rm $$tmpfile; \
		echo "✅ $(APP-NAME) built successfully"; \
	else \
		echo "❌ Error building $(APP-NAME):"; \
		cat $$tmpfile; \
		rm $$tmpfile; \
		exit 1; \
	fi \
	&& end_time=$$(date +%s); \
	echo "⏱️ Time taken: $$(($$end_time - $$start_time)) seconds"

build-all:
	@echo "🏗️ Building all apps... \n"
	@$(MAKE) build_base_dependencies
	@echo
	@i=1; \
	for app in $(ALL_APPS); do \
		echo "🔄 Building app $$i/$(TOTAL_APPS): $$app"; \
		$(MAKE) build_single_app APP-NAME=$$app; \
		i=$$(($$i + 1)); \
		echo; \
	done
	@echo "🏗 All apps built successfully"

run-all:
	@echo "🚀 Starting docker containers..."
	@cd local-dev && docker-compose --profile database --profile monitoring --profile application up -d

clean-all:
	@echo "🧹 Shutting down all docker containers..."
	@cd local-dev && docker-compose --profile database --profile monitoring --profile application down -v
	@echo "🧹 Removing docker containers..."
	@cd local-dev && docker-compose --profile database --profile monitoring --profile application rm -f

run-load-test-docker:
	@echo "📈 Running load test in docker..."
	@cd local-dev/k6 && ./run_docker.sh
run-load-test-local:
	@echo "📈 Running load test from local..."
	@cd local-dev/k6 && ./run_local.sh

help:
	@echo "Available targets:"
	@echo "====================================================================="
	@echo " build APP-NAME=<app-name> - 🔧 Build the app"
	@echo " build-all                 - 🏗️ Build all apps"
	@echo " run-all                   - 🚀 Start all resources and services"
	@echo " run-load-test-docker      - 📈 Run load test in docker 🐳"
	@echo " run-load-test-local       - 📈 Run load test from local"
	@echo " clean-all                 - 🧹 Shut down all resources and services"
	@echo "---------------------------------------------------------------------"
