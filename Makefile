##############################################################################
# Environment variables
# Set locales
#
CITY=$(shell timedatectl | awk '/Time zone/ {print $$3}' | awk -F/ '{print $$2}')
COUNTRY=$(shell echo $$LANG | awk -F. '{print $$1}' | awk -F_ '{print $$2}')
LANGUAGE=$(shell echo $$LANG | awk -F. '{print $$1}' | awk -F_ '{print $$2}')
TIMEZONE=$(shell timedatectl | awk '/Time zone/ {print $$3}')

##############################################################################

.PHONY: help # This help message
help:
	@grep '^.PHONY: .* #' Makefile \
	| sed 's/\.PHONY: \(.*\) # \(.*\)/\1\t\2/' \
	| expand -t20 \
	| sort

##############################################################################

.PHONY: prepare # Generate Dockerfiles from templates
prepare:
	# Sonar: Set locales and proxy
	@sed "s!%%COUNTRY%%!${COUNTRY}! ; \
		s!%%LANGUAGE%%!${LANGUAGE}! ; \
		s!%%TIMEZONE%%!${TIMEZONE}! ; \
		s!%%HTTP_PROXY%%!${HTTP_PROXY}!" \
		sonar/Dockerfile.tmpl > sonar/Dockerfile

	# Jenkins: Set locales and proxy
	@sed "s!%%HTTP_PROXY%%!${HTTP_PROXY}!" \
		jenkins/Dockerfile.tmpl > jenkins/Dockerfile

	@test -z ${HTTP_PROXY} \
		&& sed '/HTTP_PROXY/d' \
			jenkins/Dockerfile.tmpl > jenkins/Dockerfile \
		|| true

	# Nexus: Set proxy
	@sed "s!%%HTTP_PROXY%%!${HTTP_PROXY}!" \
		nexus/Dockerfile.tmpl > nexus/Dockerfile

	@test -z ${HTTP_PROXY} \
		&& sed '/^HTTP_OPTIONS/d' \
			nexus/Dockerfile.tmpl > nexus/Dockerfile \
		|| true

	# docker-compose: Set traefik virtualhost
	@test -z ${TRAEFIK_VIRTUALHOST} \
		&& sed "s/%%TRAEFIK_VIRTUALHOST%%/localhost/" \
			docker-compose.yml.tmpl > docker-compose.yml \
		|| sed "s/%%TRAEFIK_VIRTUALHOST%%/${TRAEFIK_VIRTUALHOST}/" \
			docker-compose.yml.tmpl > docker-compose.yml

.PHONY: clean # Stop and remove temporary files
clean: down
	@docker-compose rm

	@rm -f \
		jenkins/Dockerfile \
		nexus/Dockerfile \
		sonar/Dockerfile \
		docker-compose.yml

##############################################################################

.PHONY: status # Get stack status "docker-compose ps"
status:
	@docker-compose ps

.PHONY: up # Start "docker-compose up"
up: prepare
	@docker-compose up

.PHONY: daemon # Start "docker-compose up -d"
daemon: prepare
	@docker-compose up -d
	
.PHONY: down # Stop the stack "docker-compose down"
down:
	@docker-compose down

.PHONY: rebuild # Rebuild the containers and run
rebuild: prepare
	@docker-compose down --rmi all
	@docker-compose up --build

.PHONY: daemon-rebuild # Rebuild the containers and run
daemon-rebuild: prepare
	@docker-compose down --rmi all
	@docker-compose up --build -d

