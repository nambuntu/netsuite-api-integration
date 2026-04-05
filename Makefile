SHELL := /bin/sh

PROJECT_DIR := integrate-netsuite-api-code
ZIP_FILE := $(PROJECT_DIR).zip
MAVEN_DIRS := $(sort $(patsubst %/,%,$(dir $(wildcard */pom.xml))))

.PHONY: clean pack

clean:
	@set -e; \
	for dir in $(MAVEN_DIRS); do \
		echo "Running mvn clean in $$dir"; \
		(cd "$$dir" && mvn clean); \
	done

pack: clean
	@echo "Creating ../$(ZIP_FILE)"
	@rm -f "../$(ZIP_FILE)"
	@cd .. && zip -rq "$(ZIP_FILE)" "$(PROJECT_DIR)" -x "$(PROJECT_DIR)/sample/*"
