# oauth2-workshop

## Pre-requisites

* Docker installed and available on your machine

## OAuth2 Roles

There are only four roles in OAuth2 and they are the following, please keep these four roles in mind as you read through the document:

* **resource owner**:
An entity capable of granting access to a protected resource.
When the resource owner is a person, it is referred to as an
end-user.

* **Resource Server**:
The server hosting the protected resources, capable of accepting
and responding to protected resource requests using Access Tokens.

* **client**:
An application making protected resource requests on behalf of the
resource owner and with its authorization.  The term "client" does
not imply any particular implementation characteristics (e.g.,
whether the application executes on a server, a desktop, or other
devices).

* **Authorization Server**:
The server issuing Access Tokens to the client after successfully
authenticating the resource owner and obtaining authorization.

We are going to go through each of these in the workshop to understand how each part works.
Firstly how does OAuth2 work?

## OAuth2 Overview

OAuth2 is an authorization framework, it provides an outline on how clients can be authorizes by a resourcer owner to access their resources.
Applied to a real world interprutation. When I use LinkedIn and it requests to access my GMail email contacts, this is OAuth2. LinkedIn is the client, I am the resource owner, gmail is the Resource Server and Google is the Authorization Server.

OAuth 2 has something called an abstract protocol.
It is an outline of how the OAuth2 grants work.
You can see this here:

![Abstract protocol image](images/abstract-protocol.png)
Taken from https://www.rfc-editor.org/rfc/rfc6749#section-1.2

Let us look at the story of accessing GMail contacts from linkedin.

<ol type="A">
  <li>The Client (LinkedIn) asks the Resource Owner (User) if they can access their GMail contacts, this is the authorization request.</li>
  <li>The Resource Owner (User) either grants or denies access to the Client (LinkedIn)</li>
  <li>The Client (LinkedIn) presents to the Authoirzation Server (Google) that they have been granted access by the Resource Owner (User)</li>
  <li>The Client (LinkedIn) is issued an Access Token by the Authorization Server (Google)</li>
  <li>The Client (LinkedIn) uses the Access Token to request from the Resource Server (GMail) access the the Resource Ownerns (Users) GMail contacts</li>
  <li>The Resource Server (GMail) returns the GMail contacts to the Client (LinkedIn)</li>
</ol>

This is an outline of the steps for OAuth2 grants and how they work.
As was mentioned, this is an abstract protocol, and the implementations of these protocols are called grants.

A key point is that with OAuth2 the different grants each provide different ways to obtain an Access Token. Step E and F are constant throughout all the grants.

### Authorization Code Grant

The Authorization Code Grant (also known as Auth Code Grant) <https://www.rfc-editor.org/rfc/rfc6749#section-4.1> is the most common grant.
It follows the abstract protocol in terms of its mechanism and is used when users are involved, these are the Resource Owners.
The Auth Code Grant is one of the most secure grants within OAuth2. 

We can see in the following image how this works, note that the User-Agent is a browser, this whole grant is based on browser redirects:

![Auth Code Grant image](images/auth-code-grant-design.png)
This image is taken from <https://www.rfc-editor.org/rfc/rfc6749#section-4.1>

This grant starts with the user being directed to the authorization endpoint of the Authorization Server, this includes who the client is and where to send the authoirzation code.The resource owner authorizes (or denies) that the client can access its resources, an Authorization Code is then issued to the the client.
Once the client has the Authorization Code it can then send a request behind the scenes (it is a server so the user will not see it), to the Authorization Servers token endpoint.
The client sends the Authorization Code, a redirect URL, and a client id and secret.
Having an Authorization Code is not enough to prove who you are.
It is also required for the client to authenticate itself to the Authorization Server.
Once the Authorization Server has all this information it can then issue an Access Token


The Authorization Code Grant requires that the client is a confidential client. A confidential client is a client that can maintain a secret and will not leak it.
This means a SPA can not use Auth Code Grant in this form, please check Auth Code Grant + PKCE to achieve this.


### client credentials grant

client credentials Grant is used for machine-to-machine communication.
This grant has not resource owners involved and you can see from the following image:

![client credentials image](images/client-credentials-design.png)
This image is taken from <https://www.rfc-editor.org/rfc/rfc6749#section-4.4>

We see here that this image only shows how to obtain an Access Token.
The client authenticates to the Authorization Server using a secrect (like a password) to request an Access Token.
As the client is requesting access on behalf of itself there is no resource owner needed.
Once the client has the Access Token it can then send a request to the Resource Server.


Now we have seen how OAuth2 works and understand two of the grants (there are more), we should see how this works in practice.
Lets start by creating our Authorization Server.

## Creating an Authorization Server

For our workshop we are going to use Keycloak.
Keycloak is an opensource identity provider (IDP) <https://www.keycloak.org/>.

Lets fire it up.

```shell
docker run -d -p 8080:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:19.0.3 start-dev
``` 

As we have seen, an Authorization Server issues Access Tokens to to the client.
The Authorization Server needs to keep track of the clients, i.e they need to be known to the Authorization Server.
If the Authorization Server does not know who the clients are then how can it issue Access Tokens?

1. First lets create a new realm, click the `master` dropdown can select `Create Realm`
2. Enter the Realm name `oauth2-workshop` and select `Create`

We will use this realm (environment) to create our clients and manage the Authorization Server.

> **_NOTE:_** In your own time explore of the realm and see what Keycloak offers.

Once the realm is created it creates an endpoint that describes what functionality exists from the provider.
You can see it at <localhost:8080/realms/oauth2-workshop/.well-known/openid-configuration> (if you called your realm something different replace the `oauth2-workshop`).
You should see the following endpoints:

* authorization_endpoint
* token_endpoint

These are core to OAuth2, the authorization endpoint is used the obtain a grant from the resource owner.
The token endpoint is used by the client to get an Access Token.

We have our Authorization Server setup.
The first case we will look at is client credentials Grant.
Lets create a Client

### Client creation for client credentials Grant

As we have seen for the client credentials Grant, a Client directly requests Access Tokens.
We need to create the client.

1. Select `Clients` from the sidebar and press `Create client`
2. Enter the following information
  * Client type: `OpenID Connect`
  * Client ID: `ClientCredentialsApp`
  * Name: `client credentials Application`
  * Description: `An OAuth2 client application using the client credentials grant`
3. Select `Next`
4. Select the following configuration:
![client credentials setup image](images/client-credentials-setup.png "client credentials setup")
5. Select `Save`

Success you have created a client application.
Next we need some credentials, select the `Credentials` tab and reveal the `Client secret` and note it down.

We now have everything for a Client Application.

We are going to tidy up a couple of things, so next select the `Client scopes` tab and set everything the following to default:

![Client scopes for client credentials app](images/cleint-credentials-scopes.png)

Lets verify it is working correctly by requesting an Access Token from the token endpoint

```shell
CLIENT_CRED_ID=ClientCredentialsApp
CLIENT_CRED_SECRET=oneM9EujQeFywuv8QBnFRKc87ECnZoBy
curl --location -X POST 'http://localhost:8080/realms/oauth2-workshop/protocol/openid-connect/token' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode "client_id=$CLIENT_CRED_ID" \
--data-urlencode "client_secret=$CLIENT_CRED_SECRET" \
--data-urlencode 'grant_type=client_credentials' \
| jq -r '.access_token'
```

You should get back an Access Token that will look something like:

```shell
eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJETmlGZUdIZ0VUdmNqdGJ4bzZVa0VIR3VfeXJrYlpCUnVJbzBGLWplWmtFIn0.eyJleHAiOjE2NjY4NzE5NzYsImlhdCI6MTY2Njg3MTY3NiwianRpIjoiY2FhNzdlNzAtNTc3Yi00NGRiLWJjYmEtNzI3M2Q1MzMyZTNiIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwL3JlYWxtcy9vYXV0aDItd29ya3Nob3AiLCJzdWIiOiJhYTY5OThjZC1iZTY4LTRkNTktODljYS00ZmJjMzlkM2E0NmYiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJDbGllbnRDcmVkZW50aWFsc0FwcCIsImFjciI6IjEiLCJzY29wZSI6IiIsImNsaWVudEhvc3QiOiIxNzIuMTcuMC4xIiwiY2xpZW50SWQiOiJDbGllbnRDcmVkZW50aWFsc0FwcCIsImNsaWVudEFkZHJlc3MiOiIxNzIuMTcuMC4xIn0.CVprHTzQYneKvONa8HbjSLO1lp9S47mDlvMhTyfCJSKaFYBN84IqnbbjEkV4jLemkVmaXwDfSRFIzSG1JFO0tdH2DYPR-PjKg6SeLK1pp5MFdJra7TjgZe-Kq2PiDY_BcEHV0OyG7O9hnyZbsyujvI17xwBLSeAdU8qTNa85JrOdxfHx9kp_qpcvigLmTQr9BgCO_ckO_lb0ZI1uJOakaDWCewDspOFe6abazdaTgEraZOU_UROR7MYumSMq1xBJC79vuv3eMCzfjm3yKKXam05HuhbwdjCUI5_vIeL8DDtBtWV3FblKepj4K4i3fId_O6sd5XFyum6kVrXvKvFgAw
```

Success you have registered a client for client 

### Client for Authorization Code Grant

> TODO

## Creating a Resource Server

You have seen how to register clients in the Authorization Server.
Next we need to serve up some resources, this if going to be a simple Spring API.

Navigate to <start.spring.io> choose your setup, however, you will need to select the following dependencies:

* Spring Web / Spring Reactive Web
* OAuth2 Resource Server

Download the project and open it up.
You should then create a `controller` package and an Emails controller.
You can see an example of this setup in the sample [Resource Server](code/oauth2/resource-server/).
You will need to set a property for the Resource Server to act accordingly

```shell
spring.security.oauth2.resourceserver.jwt.jwk-set-uri=http://localhost:8080/realms/oauth2-workshop/protocol/openid-connect/certs
```

This tells Spring to retrieve the public keys from the Authorization Server, it can use these public keys to verify the JWT based Access Tokens.
Spring does this automagically so you don't have to.

> **_Note:_** You may need to change the server.port of the application, I've changed it to port 9090

In the controller the Principal is extracted and it is the JWT Access Token.
It can be inspected to check claims in the token to extract information about the client.

```java
@GetMapping
	public List<EmailContact> getEmailContacts(@AuthenticationPrincipal JwtAuthenticationToken principal) {
		var clientEmails = clientEmailStore.get(principal.getToken().getClaimAsString("azp"));
		if (clientEmails == null) {
			throw new AuthorizationServiceException("Not Authorized");
		}
		return clientEmails;
	}
```

Lets try this out using the client application from the client credentials, run the following request:

```shell
# Get the Access Token
CLIENT_CRED_ID=ClientCredentialsApp
CLIENT_CRED_SECRET=oneM9EujQeFywuv8QBnFRKc87ECnZoBy
ACCESS_TOKEN=$(curl --location -X POST 'http://localhost:8080/realms/oauth2-workshop/protocol/openid-connect/token' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode "client_id=$CLIENT_CRED_ID" \
--data-urlencode "client_secret=$CLIENT_CRED_SECRET" \
--data-urlencode 'grant_type=client_credentials' \
| jq -r '.access_token')

# Call the Resource Server as the Client Application

curl --header "Authorization: Bearer $ACCESS_TOKEN" \
http://localhost:9090/emails
```

Success you should now see that your request returns a response.

>__Try it yourself:__
> 1. Try modifying your Access Token does the request still work?
> 2. Wait till your Access Token expires, what happens when you send it to the Resource Server?
> 3. Use a differnt JWKs and send your Access Token against the Resource Server.
> What happens?

