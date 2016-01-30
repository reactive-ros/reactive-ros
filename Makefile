fatJar: $(shell find src -type f)
	@./gradlew fatJar
distribute: $(shell find src -type f)
	@./gradlew distZip
idea: 
	@./gradlew idea
doc:
	@./gradlew javadoc
clean: 
	@./gradlew clean 
