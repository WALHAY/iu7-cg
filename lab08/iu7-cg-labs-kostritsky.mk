.PHONY: clean gui-testing func-testing
clean:
	make clean
	
ready/app-gui.jar :
	make ready/app-gui.jar

ready/app-cli.jar :
	exit 66

ready/stud-unit-test-report-prev.json : 
	make unit-testing-prev 

ready/stud-unit-test-report.json :
	exit 66 

gui-testing :
	exit 66

func-testing :
	exit 66

run : 
	make run
