# Define the Gradle wrapper command
GRADLEW = ./gradlew

# Default target
all: format_and_test build

# Target to run ktlint format, ktlint check, and then tests
format_and_test: format test

# Target to run ktlint format and check
format:
	$(GRADLEW) ktlintFormat && $(GRADLEW) ktlintCheck

# Target to run tests
test:
	$(GRADLEW) test

# Target to build the project
build:
	$(GRADLEW) build

# Clean the project
clean:
	$(GRADLEW) clean

# Phony targets
.PHONY: all format_and_test format test build clean