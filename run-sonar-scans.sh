#!/usr/bin/env bash

# -----------------------------------------
# run-sonar-scans.sh
# -----------------------------------------
# Convenience script to execute SonarQube analysis for
# 1) budget-tracker-be (Spring Boot, Maven)
# 2) budget-tracker-automation (Selenium tests, Maven)
# 3) budget-tracker-fe (React/Vite TypeScript, sonar-scanner CLI)
#
# Usage:
#   1. Ensure SONAR_TOKEN is exported or stored in an .env file at the repo root.
#      Optionally set SONAR_HOST (defaults to http://localhost:9000).
#   2. Make sure "sonar-scanner" CLI is installed and accessible in PATH.
#   3. Run: ./run-sonar-scans.sh
#
# The script will abort on the first error (set -e).
# -----------------------------------------

set -euo pipefail

# Load environment variables from .env if present
if [[ -f ".env" ]]; then
  echo "Loading environment variables from ./.env"
  set -a 
  source .env
  set +a
fi

# Validate required variables
if [[ -z "${SONAR_TOKEN:-}" ]]; then
  echo "ERROR: SONAR_TOKEN is not set. Export it or place it in a .env file." >&2
  exit 1
fi
SONAR_HOST="${SONAR_HOST:-http://localhost:9000}"

# Helper function – pretty logging
step() {
  echo -e "\n\033[1;34m▶ $*\033[0m"
}

# 1. Backend
step "Scanning budget-tracker-be (Maven)"
(
  cd budget-tracker-be
  ./mvnw -q clean verify sonar:sonar \
    -Dsonar.host.url="$SONAR_HOST" \
    -Dsonar.login="$SONAR_TOKEN" \
    -Dsonar.projectKey=budget-tracker-be
)

# 2. Automation tests
step "Scanning budget-tracker-automation (Maven)"
(
  cd budget-tracker-automation
  ./mvnw -q clean verify sonar:sonar \
    -Dsonar.host.url="$SONAR_HOST" \
    -Dsonar.login="$SONAR_TOKEN" \
    -Dsonar.projectKey=budget-tracker-automation
)

# 3. Front-end
step "Scanning budget-tracker-fe (sonar-scanner)"
(
  cd budget-tracker-fe
  if command -v sonar-scanner >/dev/null 2>&1; then
    sonar-scanner \
      -Dsonar.host.url="$SONAR_HOST" \
      -Dsonar.login="$SONAR_TOKEN" \
      -Dsonar.projectKey=budget-tracker-fe
  else
    echo "sonar-scanner CLI not found. Running via Docker image..."
    DOCKER_SONAR_HOST="$SONAR_HOST"
    if [[ "$SONAR_HOST" == *"localhost"* ]]; then
      DOCKER_SONAR_HOST="${SONAR_HOST//localhost/host.docker.internal}"
    fi
    docker run --rm \
      -e SONAR_HOST_URL="$DOCKER_SONAR_HOST" \
      -e SONAR_TOKEN="$SONAR_TOKEN" \
      -v "$(pwd):/usr/src" \
      sonarsource/sonar-scanner-cli \
      -Dsonar.projectKey=budget-tracker-fe
  fi
)

step "All SonarQube analyses completed successfully." 