.PHONY: all build clean run

all: build

build:
	@javac src/*.java

clean:
	@rm -f src/*.class

purge: clean
	@rm -f src/clients/*.txt src/reports/*.txt

run: src/*.class
	@env -C src java Terminal
