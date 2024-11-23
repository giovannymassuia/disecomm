.PHONY: build_base_dependencies build-app build_single_app build-all run-all clean-all run-load-test-docker run-load-test-local help
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
	@test -n "$(APP_NAME)" || (echo "❌ APP_NAME is not set. Use APP_NAME=<app-name>"; exit 1)
	@echo "🔧 Building app... $(APP_NAME) $(ARGS)"
	@$(MAKE) build_single_app ARGS="$(ARGS)" DEBUG="$(DEBUG)" APP_NAME="$(APP_NAME)"

build_single_app:
	@test -n "$(APP_NAME)" || (echo "❌ APP_NAME is not set"; exit 1)
	@tmpfile=$$(mktemp /tmp/build_output_XXXXXX); \
	start_time=$$(date +%s); \
	if ( \
		cd common && \
		./build_app.sh $(APP_NAME) $(ARGS) \
	) >$$tmpfile 2>&1; then \
		if [ "$(DEBUG)" = "true" ]; then \
			cat $$tmpfile; \
		fi; \
		rm $$tmpfile; \
		echo "✅ $(APP_NAME) built successfully"; \
	else \
		echo "❌ Error building $(APP_NAME):"; \
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
		$(MAKE) build_single_app APP_NAME=$$app ARGS="$(ARGS)" DEBUG="$(DEBUG)"; \
		i=$$(($$i + 1)); \
		echo; \
	done
	@echo "🏗 All apps built successfully"

run-all:
	@echo "🚀 Starting docker containers..."
	@cd local-dev && docker-compose --profile database --profile monitoring --profile application up -d

run-db-only:
	@echo "🚀 Starting only database containers..."
	@cd local-dev && docker-compose --profile database up -d

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
	@printf "\nAvailable targets (use variable assignments for options):\n"
	@printf "================================================================================\n"
	@printf "%-30s %-50s\n" "Target" "Description"
	@printf "%-30s %-50s\n" "------" "-----------"
	@printf "%-30s %-50s\n" "build-app" "🔧 Build the app (Requires APP_NAME=<app-name>)"
	@printf "%-30s %-50s\n" "build-all" "🏗️ Build all apps"
	@printf "%-30s %-50s\n" "run-all" "🚀 Start all resources and services"
	@printf "%-30s %-50s\n" "run-db-only" "🚀 Start only database containers"
	@printf "%-30s %-50s\n" "run-load-test-docker" "📈 Run load test in Docker 🐳"
	@printf "%-30s %-50s\n" "run-load-test-local" "📈 Run load test from local"
	@printf "%-30s %-50s\n" "clean-all" "🧹 Shut down all resources and services"
	@printf "%-30s %-50s\n" "help" "📖 Display this help message"
	@printf "\n⚙️ Options (use as variable assignments):\n"
	@printf " %-28s %s\n" "APP_NAME=<app-name>" "Specify the app name (required for build-app)"
	@printf " %-28s %s\n" "DEBUG=true" "Enable debug mode (verbose output)"
	@printf " %-28s %s\n" "ARGS=\"<args>\"" "Arguments passed to scripts or commands"
	@printf "\t Available <args>:\n"
	@printf "\t %-26s %s\n" "build-app" "--no-otel-agent, --push-local"
	@printf "\t %-26s %s\n" "build-all" "--no-otel-agent, --push-local"
	@printf "\n🎨 Examples:\n"
	@printf "  - Build an app: \`make build-app APP_NAME=api-gateway\`\n"
	@printf "  - Build an app with all options: \`make build-app APP_NAME=product-catalog ARGS=\"--no-otel-agent --push-local\" DEBUG=true\`\n"
	@printf "  - Build all apps: \`make build-all\`\n"
	@printf "  - Build all apps without OpenTelemetry agent: \`make build-all ARGS=\"--no-otel-agent\"\`\n"
	@printf "  - Build all apps and push to local registry: \`make build-all ARGS=\"--push-local\"\`\n"
	@printf "  - Start all resources and services: \`make run-all\`\n"
	@printf "  - Shut down all resources and services: \`make clean-all\`\n"
	@printf "================================================================================\n"

# Pattern rule to display help for unknown targets
%:
	@echo "Unknown target '$@'."
	@$(MAKE) help
	@exit 1
