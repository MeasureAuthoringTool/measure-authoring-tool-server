create table IF NOT EXISTS oauth_client_details (
                                    client_id VARCHAR(255) PRIMARY KEY,
                                    resource_ids VARCHAR(255),
                                    client_secret VARCHAR(255),
                                    scope VARCHAR(255),
                                    authorized_grant_types VARCHAR(255),
                                    web_server_redirect_uri VARCHAR(255),
                                    authorities VARCHAR(255),
                                    access_token_validity INTEGER,
                                    refresh_token_validity INTEGER,
                                    additional_information VARCHAR(4096),
                                    autoapprove VARCHAR(255)
);

create table IF NOT EXISTS oauth_client_token (
                                  token_id VARCHAR(255),
                                  token LONGBLOB,
                                  authentication_id VARCHAR(255),
                                  user_name VARCHAR(255),
                                  client_id VARCHAR(255)
);

create table IF NOT EXISTS oauth_access_token (
                                  token_id VARCHAR(255),
                                  token LONGBLOB,
                                  authentication_id VARCHAR(255),
                                  user_name VARCHAR(255),
                                  client_id VARCHAR(255),
                                  authentication BLOB,
                                  refresh_token VARCHAR(255)
);

create table IF NOT EXISTS oauth_refresh_token (
                                   token_id VARCHAR(255),
                                   token LONGBLOB,
                                   authentication LONGBLOB
);

create table IF NOT EXISTS oauth_approvals (
                               userId VARCHAR(256),
                               clientId VARCHAR(256),
                               scope VARCHAR(256),
                               status VARCHAR(10),
                               expiresAt DATETIME,
                               lastModifiedAt DATETIME
);


create table IF NOT EXISTS oauth_code (
                          code VARCHAR(255), authentication LONGBLOB
);