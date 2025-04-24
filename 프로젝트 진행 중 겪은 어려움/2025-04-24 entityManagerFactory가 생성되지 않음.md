오늘 코딩을 하다가 아래와 같은 오류를 마주했다. 처음에는 entityManagerFactory의 의존성 주입 문제인 줄 알았는데 embedded라는 키워드를 보았다.

```
Error creating bean with name 'entityManagerFactory' defined in class path resource [org/springframework/boot/autoconfigure/orm/jpa/HibernateJpaConfiguration.class]: Convert placed on Embedded attribute must define (sub)attributeName
```

답은 `@Embedded`애노테이션 때문이었다. 애노테이션을 제거하니 제대로 동작하였다.