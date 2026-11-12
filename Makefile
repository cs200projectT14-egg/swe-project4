.PHONY: all build clean

all: build

build:
	javac src/*.java

clean:
	rm -f src/*.class
