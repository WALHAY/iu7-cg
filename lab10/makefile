
BUILD_PATH := ./build/libs
GUI_PATH := $(BUILD_PATH)/app-gui.jar
CLI_PATH := $(BUILD_PATH)/app-cli.jar

GRADLE_WRAPPER := ./gradlew

FUNC_PICS = ./func_tests/out/

ready/app-gui.jar : $(GUI_PATH)
	mkdir -p ./ready
	cp $(GUI_PATH) ./ready/app-gui.jar
	
ready/app-cli.jar : $(CLI_PATH)
	mkdir -p ./ready
	cp $(CLI_PATH) ./ready/app-cli.jar

.PHONY : run clean gui-testing func-testing unit-testing unit-testing-prev

run : $(GUI_PATH)
	java -jar $(GUI_PATH)

$(GRADLE_WRAPPER) :
	gradle wrapper --gradle-version 8.13 || echo "Success actually"

$(GUI_PATH) : $(GRADLE_WRAPPER)
	./gradlew build

$(CLI_PATH) : $(GRADLE_WRAPPER)
	./gradlew buildCli

func-testing : $(CLI_PATH) $(GRADLE_WRAPPER)
	mkdir -p ./ready/pics/
	./func_tests/test.sh 
	cp $(FUNC_PICS)/* ./ready/pics/

unit-testing : $(GRADLE_WRAPPER)
	mkdir -p ./ready
	./gradlew test --rerun-tasks
	cp latest_report.json ./ready/stud-unit-test-report.json

latest_report.json : $(GRADLE_WRAPPER)
	./gradlew test

unit-testing-prev : latest_report.json
	mkdir -p ./ready
	cp latest_report.json ./ready/stud-unit-test-report-prev.json

clean : $(GRADLE_WRAPPER)
	./gradlew clean
	rm -f generated.bmp
	rm -rf $(FUNC_PICS)
	echo OK
