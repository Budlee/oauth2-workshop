import {AuthConfig} from 'angular-oauth2-oidc'

export const authConfig: AuthConfig = {
  issuer: 'http://localhost:8080/realms/oauth2-authzcode-demo',
  redirectUri: window.location.origin,
  clientId: 'oauth2-demo-pkce',
  responseType: 'code',
  strictDiscoveryDocumentValidation: true,
  scope: 'openid profile email offline_access',
}
