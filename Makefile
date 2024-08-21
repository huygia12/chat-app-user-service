DOCKER_USERNAME ?= huygia12
DOCKER_FOLDER ?= ./docker
APPLICATION_NAME ?= chat-app-user-service
GIT_HASH ?= $(shell git log --format="%h" -n 1)
SERVER_PORT ?= 8030
ENV_FILE ?= .env
TLS ?= false

_BUILD_ARGS_TAG ?= ${GIT_HASH}
_BUILD_ARGS_DOCKERFILE ?= Dockerfile

_builder: test
	$(info ==================== building dockerfile ====================)
	docker buildx build --platform linux/amd64 --tag ${DOCKER_USERNAME}/${APPLICATION_NAME}:${_BUILD_ARGS_TAG} -f ${DOCKER_FOLDER}/${_BUILD_ARGS_DOCKERFILE} .

_server:
	docker container run --rm --env-file ${ENV_FILE} -e ID_TLS=${TLS} ${DOCKER_USERNAME}/${APPLICATION_NAME}:${_BUILD_ARGS_TAG}

run:
	ENV_FILE=${ENV_FILE} ID_TLS=${TLS} ./mvnw_wrapper.sh exec:java

server:
	$(MAKE) _server \
		-e ID_TLS=${TLS}

server_%:
	$(MAKE) _server \
		-e _BUILD_ARGS_TAG="$*-${GIT_HASH}" \
		-e _BUILD_ARGS_DOCKERFILE="Dockerfile.$*" \
		-e ID_TLS=${TLS}

build:
	$(MAKE) _builder

build_%: $(MAKE) _builder \
		-e _BUILD_ARGS_TAG="$*-${GIT_HASH}" \
		-e _BUILD_ARGS_DOCKERFILE="Dockerfile.$*"

clean:
	$(info ==================== cleaning project ====================)
	ENV_FILE=${ENV_FILE} ./mvnw_wrapper.sh spring-javaformat:apply

test: clean
	$(info ==================== running tests ====================)
	ENV_FILE=${ENV_FILE} ID_TLS=${TLS} ./mvnw_wrapper.sh clean verify

.PHONY:
	clean test server server_%