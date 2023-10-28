# Junit Bank App

### Jpa LocalDateTime 자동으로 생성하는 법
- @EnableJpaAuditing (main class)
- @EntityListeners(AuditingEntityListener.class (Entity class))
- @CreatedDate, @LastModifiedDate (column)