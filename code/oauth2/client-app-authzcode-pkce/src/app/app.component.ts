import {Component, OnDestroy} from '@angular/core';
import {OAuthService} from "angular-oauth2-oidc";
import {authConfig} from "./auth.config";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.sass']
})
export class AppComponent {
  title = 'frontend';
  text = '';
  constructor(private oauthService: OAuthService) {
      this.configure();
    }
  login() {
      this.oauthService.initCodeFlow();
    }
  private configure() {
      this.oauthService.configure(authConfig);
      this.oauthService.loadDiscoveryDocumentAndTryLogin();
    }
  logout() {
      this.oauthService.logOut();
    }

   get name(){
      let claims = this.oauthService.getIdentityClaims();
      if (!claims) return null;
      return claims['name'];
   }

   get token(){
      return this.oauthService.getIdToken();
   }
}
