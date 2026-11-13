.PHONY: all build clean run

all: build

build:
	@javac src/*.java

clean:
	@rm -f src/*.class

run: src/*.class
	@env -C src java Terminal
