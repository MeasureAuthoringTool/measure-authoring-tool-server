# Measure Authoring Tool Server


## Installation
```bash
$ mvn install
``` 

## Testing
```bash
# Run Unit Tests
$ mvn test

# Run PMD Linter
$ mvn site
```

## Authentication
### Default Authentication Data
Adding user data for testing purposes can be confusing. Here are two queries to run that will set you up for testing. 

#### Add a client
```bash
# This will create a client with the cilent id test, the client secret user, the redirect uri as http://localhost:3000/oauth2/callback, and autoapprove

CREATE TABLE IF NOT EXISTS Account(id LONG,
                                    isActive BOOLEAN,
                                    password VARCHAR(255),
                                    username VARCHAR(255));

INSERT INTO OAUTH_CLIENT_DETAILS(CLIENT_ID, RESOURCE_IDS, CLIENT_SECRET, SCOPE, AUTHORIZED_GRANT_TYPES, AUTHORITIES, ACCESS_TOKEN_VALIDITY, REFRESH_TOKEN_VALIDITY, WEB_SERVER_REDIRECT_URI, autoapprove)
VALUES ('user', 'api','$2a$10$BurTWIy5NTF9GJJH4magz.9Bd4bBurWYG8tmXxeQh1vs7r/wnCFG2','write', 'authorization_code', 'USER', 10800, 2592000, "http://localhost:3000/oauth2/callback", "true");
```

#### Add a user
```bash
# This will create a user in the account table with the username test and the password password
INSERT INTO `Account` (`id`,`isActive`,`password`,`username`) VALUES (1,'1','$2a$10$f226euUthknnno/VVaIQZ.D.gK0UtMlo5u866jVHUNSAdSqAUzBJ6','test');
```

### Testing with Postman
Use the following settings for Postman OAuth2 Testing

In the Authorization tab, select OAuth 2.0. Then click new access token.

Token Name = Token, Grant Type = Authorization Code,
Callback URL = http://localhost:3000/oauth2/callback
Auth URL = http://localhost:8080/oauth/authorize,
Access Token URL = http://localhost:8080/oauth/token,
Client ID = user,
Client Secret = user,
Scope = write


