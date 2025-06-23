BUILD_PATH := ./build/libs

GUI_PATH := $(BUILD_PATH)/app-gui.jar
CLI_PATH := $(BUILD_PATH)/app-cli.jar

IMAGE_PATH := ./build/image/bin

RUNTIME_PATH := $(IMAGE_PATH)/app
GUI_RUNTIME_PATH :=  $(IMAGE_PATH)/app-gui
CLI_RUNTIME_PATH :=  $(IMAGE_PATH)/app-cli

ready/app-gui.jar : $(GUI_PATH)
	mkdir -p ./ready
	cp $(GUI_PATH) ./ready/app-gui.jar
	
ready/app-cli.jar : $(CLI_PATH)
	mkdir -p ./ready
	cp $(CLI_PATH) ./ready/app-cli.jar

.PHONY : run clean gui-testing func-testing unit-testing unit-testing-prev graphics

run : $(GUI_RUNTIME_PATH)
	bash $(GUI_RUNTIME_PATH)

$(GUI_PATH) : 
	./gradlew build

$(CLI_PATH) : 
	./gradlew buildCli

$(GUI_RUNTIME_PATH) :
	./gradlew runtime
	mv $(RUNTIME_PATH) $(GUI_RUNTIME_PATH)

$(CLI_RUNTIME_PATH) :
	./gradlew runtimeCli
	mv $(RUNTIME_PATH) $(CLI_RUNTIME_PATH)

unit-testing-prev : latest_report.json
	mkdir -p ./ready
	cp latest_report.json ./ready/stud-unit-test-report-prev.json

graphics : $(CLI_RUNTIME_PATH)
	./data/collect_data.sh
	python3 ./data/main.py

clean :
	./gradlew clean
	rm -f generated.bmp
	echo OK
