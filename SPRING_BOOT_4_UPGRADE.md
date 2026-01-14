# Spring Boot 3.5.9 → 4.0.1 Upgrade - COMPLETED ✅

## Summary

Successfully upgraded the project from Spring Boot 3.5.9 to 4.0.1 with Java 25. All compilation and test execution issues have been resolved.

## Final Build Status

```
[INFO] Tests run: 498, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

## Major Changes Made

### 1. Spring Batch 6.0.1 Package Reorganization

**Classes moved to infrastructure package:**
- `JdbcCursorItemReader` → `org.springframework.batch.infrastructure.item.database.JdbcCursorItemReader`
- `JdbcCursorItemReaderBuilder` → `org.springframework.batch.infrastructure.item.database.builder.JdbcCursorItemReaderBuilder`
- `ItemProcessor`, `ItemWriter`, `ItemReader`, `ItemStreamReader` → `org.springframework.batch.infrastructure.item.*`
- `Chunk` → `org.springframework.batch.infrastructure.item.Chunk`
- `RepeatStatus` → `org.springframework.batch.infrastructure.repeat.RepeatStatus`
- `ExecutionContext` → `org.springframework.batch.infrastructure.item.ExecutionContext`

**Classes moved to subpackages:**
- `Job` → `org.springframework.batch.core.job.Job`
- `Step` → `org.springframework.batch.core.step.Step`
- `JobExecution` → `org.springframework.batch.core.job.JobExecution`
- `JobInstance` → `org.springframework.batch.core.job.JobInstance`
- `JobParameters` → `org.springframework.batch.core.job.parameters.JobParameters`
- `JobLauncher` → `org.springframework.batch.core.launch.JobLauncher`
- `JobExecutionAlreadyRunningException` → `org.springframework.batch.core.launch.JobExecutionAlreadyRunningException`

### 2. Jackson Configuration (Jackson 2.x Compatibility)

Spring Boot 4 defaults to Jackson 3.x (`tools.jackson`), but the project uses Jackson 2.x (`com.fasterxml.jackson`).

**Changes:**
- Excluded `spring-boot-starter-jackson` from `spring-boot-starter-web` and `spring-boot-starter-actuator`
- Added explicit Jackson 2.x dependencies:
  - `jackson-databind`
  - `jackson-core`
  - `jackson-annotations`
  - `jackson-datatype-jdk8`
  - `jackson-datatype-jsr310`
  - `jackson-module-parameter-names`
- Created `JacksonConfig.java` to manually configure `ObjectMapper` bean
- Added `JacksonConfig` to `BatchIntegrationTest` context configuration

### 3. Configuration Updates

**BatchConfig.java:**
- Removed `@Override` annotation from `getTransactionManager()` method (no longer overrides parent)

**JOOQConfig.java:**
- Removed `JooqProperties` dependency (removed in Spring Boot 4)
- Manually configured `SQLDialect.POSTGRES`

**FlywayConfig.java:**
- Removed `FlywayMigrationStrategy` bean (removed in Spring Boot 4)
- Implemented conditional Flyway bean creation using `@ConditionalOnProperty(name = "jore.importer.migrate")`
- Flyway migrations now only run when `jore.importer.migrate=true` (default is false)
- Uses `@Bean(initMethod = "migrate")` to automatically run migrations when enabled
- Excluded `FlywayAutoConfiguration` from Spring Boot auto-configuration to prevent conflicts

**MapMatchingApiUrlFactory.java:**
- Changed from `UriComponentsBuilder.fromHttpUrl()` to `UriComponentsBuilder.fromUriString()`

### 4. JobExecution Constructor Changes

Updated all `JobExecution` constructors to include required parameters in Spring Batch 6:
```java
// Old:
new JobExecution(id)

// New:
new JobExecution(id, jobInstance, jobParameters)
```

### 5. Test Configuration Changes

**JobControllerTest.java:**
- Converted from `@SpringBootTest` to `@ExtendWith(MockitoExtension.class)`
- Removed `@MockBean` (deprecated) and used `@Mock` instead
- Created standalone `MockMvc` setup
- Configured `ObjectMapper` with `Jdk8Module` and `JavaTimeModule`

**BatchIntegrationTest.java:**
- Added `JacksonConfig.class` to `@ContextConfiguration`

### 6. Dependencies Added

**pom.xml:**
- `spring-batch-infrastructure` (explicit dependency)
- Jackson 2.x core libraries (explicit)
- Jackson 2.x datatype modules (explicit)

## Files Modified

### Configuration Files
- `pom.xml` - Dependencies and Jackson configuration
- `src/main/java/fi/hsl/jore/importer/config/JacksonConfig.java` - NEW FILE
- `src/main/java/fi/hsl/jore/importer/config/jobs/BatchConfig.java`
- `src/main/java/fi/hsl/jore/importer/config/jooq/JOOQConfig.java`
- `src/main/java/fi/hsl/jore/importer/config/migration/FlywayConfig.java`
- `src/main/java/fi/hsl/jore/importer/config/jobs/JobConfig.java`
- `src/main/java/fi/hsl/jore/importer/ImporterApplication.java`

### Batch Components (100+ files)
- All `*Reader.java` files - Updated imports
- All `*Writer.java` files - Updated imports
- All `*Processor.java` files - Updated imports
- All tasklet files - Updated `RepeatStatus` import

### Test Files
- `src/test/java/fi/hsl/jore/importer/BatchIntegrationTest.java`
- `src/test/java/fi/hsl/jore/importer/feature/api/JobControllerTest.java`
- Multiple reader/writer test files - Updated `ExecutionContext` imports

### Utility Files
- `src/main/java/fi/hsl/jore/importer/feature/mapmatching/service/MapMatchingApiUrlFactory.java`

## Deprecation Warnings

The following deprecation warnings exist but don't affect functionality:
- `chunk(int, PlatformTransactionManager)` method in StepBuilder
- `JobLauncher` in org.springframework.batch.core.launch

These can be addressed in a future update.

## Flyway Migration Control

The `jore.importer.migrate` property now properly controls whether Flyway database migrations run:

**Default Behavior (migrations disabled):**
```properties
# Property not set, or:
jore.importer.migrate=false
```
- No Flyway bean is created
- No migrations run on application startup
- Safe default for development/testing

**Enable Migrations:**
```properties
jore.importer.migrate=true
```
- Flyway bean is created and configured
- Migrations run automatically on application startup
- Logs: "jore.importer.migrate=true - Flyway migrations will run on importer database"

**Implementation:**
- Uses `@ConditionalOnProperty` to conditionally create Flyway bean
- Spring Boot's `FlywayAutoConfiguration` is excluded to prevent conflicts
- Configuration in `FlywayConfig.java` with `@Bean(initMethod = "migrate")`
- Flyway configured with `baselineOnMigrate=true` for existing databases

## Known Issues

None - all tests pass successfully!

## Next Steps (Optional)

1. **Address Deprecation Warnings:** Update to new chunk() API and JobLauncher
2. **Consider Jackson 3.x Migration:** Evaluate migrating to Jackson 3.x in the future
3. **Review Spring Boot 4.0 New Features:** Explore and adopt new Spring Boot 4 features

## Verification

To verify the upgrade:
```bash
mvn clean verify
```

Expected output:
```
[INFO] Tests run: 498, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

## Documentation References

- [Spring Boot 4.0 Release Notes](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-4.0-Release-Notes)
- [Spring Batch 6.0 Migration Guide](https://github.com/spring-projects/spring-batch/wiki/Spring-Batch-6.0-Migration-Guide)
- [Jackson 2.x Documentation](https://github.com/FasterXML/jackson)

---

**Upgrade completed on:** January 16, 2026
**Tests passing:** 498/498 ✅

