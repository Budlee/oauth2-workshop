# Client App for "Authorization Code grant with PKCE flow"

### Pre-requisites
- Install node in your PC [NodeJS Download](https://nodejs.org/en/download)
- Start the KeyCloak instance <https://www.keycloak.org/>

This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 16.2.5.
- `node --version && npm --version`
- `npm install && npm start`. Navigate to [localhost:4200](http://localhost:4200/). The application will automatically reload if you change any of the source files.

Follow below steps if you would like to create Single page client app from the scratch, otherwise skip the steps.
```shell
  CLIENT_APP_NAME=client-app-authz-code-pkce
  sudo npm install -g @angular/cli &&
  mkdir ${CLIENT_APP_NAME} &&
  ng new ${CLIENT_APP_NAME} &&
  npm install angular-oauth2-oidc &&
  npm start
```

### Create Realm [KeyCloak](http://localhost:8080)
- `Administration Console -> Create Realm`
- `Realm Settings -> Endpoints -> OpenID Endpoint Configuration -> Note the Issuer URL`

### Create the client and demo user in KeyCloak (Authorization Server)

#### Enable the authorization flow options as specified on below screenshots and select the PKCE Challenge Method (S256)
1. ![Client account for Authorization Code grant with PKCE Flow](images/auth-code-grant-with-pkce.png "PKCE Authorization Code Flow")
2. ![Access Settings](images/auth-code-with-pkce-access-settings.png "Access Settings")
3. ![Advanced Settings](images/auth-code-with-pkce-adv-settings.png "Advanced Settings")

### Create a demo user
- `Administration Console -> Select Realm -> Create User -> `

### Update the SPA with OauthService
#### Update the `auth.config.ts` with clientId and issuer
```shell
import {AuthConfig} from 'angular-oauth2-oidc'
export const authConfig: AuthConfig = {
  issuer: 'http://localhost:8080/realms/oauth2-authzcode-demo',
  redirectUri: window.location.origin,
  clientId: 'oauth2-demo-pkce',
  responseType: 'code',
  strictDiscoveryDocumentValidation: true,
  scope: 'openid profile email offline_access',
}
```
[Access localhost:4200](http://localhost:4200)

![Login](images/auth-code-with-pkce-login.png "Request for Authorization Code")

![Token](images/auth-code-with-pkce-token.png "Token call with auth code")


### Reference
[Angular Oauth2 OIDC](https://github.com/manfredsteyer/angular-oauth2-oidc)

[Steps to create a code challenge](https://datatracker.ietf.org/doc/html/rfc7636#section-4.2)

[Tool to generate code challenge](https://tonyxu-io.github.io/pkce-generator/)

[Oauth2 vs OpenID Connect](https://www.linkedin.com/advice/0/how-do-you-choose-between-oauth2-openid?trk=cah1)
