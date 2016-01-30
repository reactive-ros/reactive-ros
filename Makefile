intall: $(shell find src -type f)
	@./gradlew install
idea: 
	@./gradlew idea
doc:
	@./gradlew javadoc
clean: 
	@./gradlew clean 
