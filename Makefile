install: 
	@./gradlew install
update:
	@./gradlew install --refresh-dependencies
adhoc:
	@./gradlew test --tests Adhoc
test:
	@./gradlew test --tests Tester
idea: 
	@./gradlew idea
doc:
	@./gradlew javadoc
clean: 
	@./gradlew clean 
