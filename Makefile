install: 
	@./gradlew install
update:
	@./gradlew install --refresh-dependencies
test:
	@./gradlew test --tests Tester
idea: 
	@./gradlew idea
doc:
	@./gradlew javadoc
clean: 
	@./gradlew clean 
