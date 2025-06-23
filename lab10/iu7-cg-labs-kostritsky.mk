.PHONY: clean gui-testing func-testing
clean:
	make clean

ready/app-cli.jar :
	make ready/app-cli.jar
	
ready/app-gui.jar :
	make ready/app-gui.jar

ready/stud-unit-test-report-prev.json : 
	make unit-testing-prev 

ready/stud-unit-test-report.json :
	make unit-testing 

gui-testing :
	exit 66

func-testing :
	make func-testing

run : 
	make run
