insert into authority (authority_name) values ('ROLE_USER');
insert into authority (authority_name) values ('ROLE_ADMIN');

CREATE INDEX idx_email_verification_token_email ON email_verification_token(email);
CREATE INDEX idx_email_verification_token_expiry ON email_verification_token(expiry);
