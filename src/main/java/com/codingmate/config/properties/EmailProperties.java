package com.codingmate.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 회원가입 이메일 설정을 해줄 프로퍼티
 *
 * @author duskafka
 * */
@ConfigurationProperties(prefix = "register-email")
public record EmailProperties (

        /** 발신자 주소 */
        String username,

        /** 이메일 제목 */
        String subject,

        /** 이메일 인증 URL 앞부분 */
        String url,

        /** 이메일 인증 후 리다이렉트 할 URL*/
        String redirectUrl
) {
}
