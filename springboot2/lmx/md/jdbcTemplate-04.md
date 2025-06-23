# `TransactionSynchronizationManager.getResource()`가 null을 반환하는 이유 분석

`TransactionSynchronizationManager.getResource(secondaryDataSource)` 호출 시 null이 반환되는 이유는 다음과 같은 여러 가지 가능성이 있습니다.

## 주요 원인

1. **트랜잭션이 활성화되지 않음**
   - 현재 스레드에 트랜잭션이 활성화되어 있지 않은 경우
   - `@Transactional` 어노테이션이 없거나 트랜잭션 전파 설정에 문제가 있는 경우

2. **DataSource가 트랜잭션에 바인딩되지 않음**
   - `secondaryDataSource`가 현재 트랜잭션에 연결(resource binding)되지 않았음
   - Spring은 트랜잭션 시작 시 DataSource를 바인딩합니다.

3. **잘못된 DataSource 객체 사용**
   - `secondaryDataSource` 객체가 예상과 다른 인스턴스일 수 있음
   - 동일한 빈을 참조하지 않고 있을 가능성

4. **트랜잭션 매니저 설정 문제**
   - `secondaryDataSource`에 대한 트랜잭션 매니저가 제대로 구성되지 않음
   - 다중 데이터소스 환경에서 특정 DataSource에 대한 트랜잭션 관리가 누락된 경우

5. **비동기/새로운 스레드에서 실행**
   - 트랜잭션 정보는 스레드 로컬에 저장되므로, 새로운 스레드에서는 접근 불가

## 해결 방안

1. **트랜잭션 활성화 확인**
   ```java
   boolean isActive = TransactionSynchronizationManager.isActualTransactionActive();
   log.info("Is transaction active? {}", isActive);
   ```

2. **DataSource 바인딩 확인**
   ```java
   Object actualResource = TransactionSynchronizationManager.getResource(secondaryDataSource);
   log.info("Bound resources: {}", TransactionSynchronizationManager.getResourceMap());
   ```

3. **트랜잭션 매니저 구성 확인**
   - `@Transactional`에 `value` 속성으로 올바른 트랜잭션 매니저 지정
   ```java
   @Transactional(value = "secondaryTransactionManager")
   public void someMethod() { ... }
   ```

4. **DataSource 빈 정확성 확인**
   - `secondaryDataSource` 빈이 예상대로 주입되었는지 확인
   - `@Qualifier` 사용 여부 점검

5. **트랜잭션 전파 속성 확인**
   - `REQUIRES_NEW` 등의 전파 속성이 예상대로 동작하는지 확인

이 문제를 해결하기 위해서는 현재 트랜잭션 상태와 DataSource 바인딩 상태를 더 자세히 로깅하여 분석하는 것이 필요합니다.