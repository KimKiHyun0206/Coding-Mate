# 문제 발생 경위
오늘 코딩을 하다가 아래와 같은 오류를 마주했다. 처음에는 entityManagerFactory의 의존성 주입 문제인 줄 알았는데 embedded라는 키워드를 보았다.

```
Error creating bean with name 'entityManagerFactory' defined in class path resource [org/springframework/boot/autoconfigure/orm/jpa/HibernateJpaConfiguration.class]: Convert placed on Embedded attribute must define (sub)attributeName
```
# 문제 원인 분석
```
Convert placed on Embedded
```
로그 중 위와 같은 로그가 있다. 이는 `@Embedded`애노테이션과 연관되어있다고 판단함.
# 문제 원인 해결방식
엔티티에 애노테이션을 제거함

# 결론
맞았다. 
앞으로 엔티티를 설계할 때 불필요한 애노테이션을 사용하지 않도록 하자.