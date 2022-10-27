# oauth2-workshop

## Pre-requisites

* Docker installed and available on your machine

## OAuth2 Roles

There are only four roles in OAuth2 and they are the following, please keep these four roles in mind as you read through the document:

* **resource owner**:
An entity capable of granting access to a protected resource.
When the resource owner is a person, it is referred to as an
end-user.

* **resource server**:
The server hosting the protected resources, capable of accepting
and responding to protected resource requests using access tokens.

* **client**:
An application making protected resource requests on behalf of the
resource owner and with its authorization.  The term "client" does
not imply any particular implementation characteristics (e.g.,
whether the application executes on a server, a desktop, or other
devices).

* **authorization server**:
The server issuing access tokens to the client after successfully
authenticating the resource owner and obtaining authorization.

We are going to go through each of these in the workshop to understand how each part works.
Firstly how does OAuth2 work?

## OAuth2 Overview

OAuth2 is an authorization framework, it provides an outline on how clients can be authorizes by a resourcer owner to access their resources.
Applied to a real world interprutation. When I use LinkedIn and it requests to access my GMail email contacts, this is OAuth2. LinkedIn is the client, I am the resource owner, gmail is the resource server and Google is the authorization server.

> Add Abstract protocol

### Client credentials grant

> TODO

### Auth code grant

> TODO

Now we have seen how OAuth2 works and understand two of the grants (there are more), we should see how this works in practice.
Lets start by creating our Authorization server.

## Creating an Authorization Server

For our workshop we are going to use Keycloak.
Keycloak is an opensource identity provider (IDP) <https://www.keycloak.org/>.

Lets fire it up.

```shell
docker run -d -p 8080:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:19.0.3 start-dev
``` 

As we have seen, an authorization server issues access tokens to to the client.
The authorization server needs to keep track of the clients, i.e they need to be known to the authorization server.
If the authorization server does not know who the clients are then how can it issue access tokens?

1. First lets create a new realm, click the `master` dropdown can select `Create Realm`
2. Enter the Realm name `oauth2-workshop` and select `Create`

We will use this realm (environment) to create our clients and manage the authorization server.

> **_NOTE:_** In your own time explore of the realm and see what Keycloak offers.

Once the realm is created it creates an endpoint that describes what functionality exists from the provider.
You can see it at <localhost:8080/realms/oauth2-workshop/.well-known/openid-configuration> (if you called your realm something different replace the `oauth2-workshop`).
You should see the following endpoints:

* authorization_endpoint
* token_endpoint

These are core to OAuth2, the authorization endpoint is used the obtain a grant from the resource owner.
The token endpoint is used by the client to get an access token.

We have our authorization server setup.
The first case we will look at is Client Credentials Grant.
Lets create a Client

### Client creation for Client Credentials Grant

As we have seen for the Client Credentials Grant, a Client directly requests access tokens.
We need to create the client.

1. Select `Clients` from the sidebar and press `Create client`
2. Enter the following information
  * Client type: `OpenID Connect`
  * Client ID: `ClientCredentialsApp`
  * Name: `Client Credentials Application`
  * Description: `An OAuth2 client application using the client credentials grant`
3. Select `Next`
4. Select the following configuration:
![Client credentials setup image](images/client-credentials-setup.png "Client credentials setup")
5. Select `Save`

Success you have created a client application.
Next we need some credentials, select the `Credentials` tab and reveal the `Client secret` and note it down.

We now have everything for a Client Application.

We are going to tidy up a couple of things, so next select the `Client scopes` tab and set everything the following to default:

![Client scopes for client credentials app](images/cleint-credentials-scopes.png)

Lets verify it is working correctly by requesting an access token from the token endpoint

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

You should get back an access token that will look something like:

```shell
eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJETmlGZUdIZ0VUdmNqdGJ4bzZVa0VIR3VfeXJrYlpCUnVJbzBGLWplWmtFIn0.eyJleHAiOjE2NjY4NzE5NzYsImlhdCI6MTY2Njg3MTY3NiwianRpIjoiY2FhNzdlNzAtNTc3Yi00NGRiLWJjYmEtNzI3M2Q1MzMyZTNiIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwL3JlYWxtcy9vYXV0aDItd29ya3Nob3AiLCJzdWIiOiJhYTY5OThjZC1iZTY4LTRkNTktODljYS00ZmJjMzlkM2E0NmYiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJDbGllbnRDcmVkZW50aWFsc0FwcCIsImFjciI6IjEiLCJzY29wZSI6IiIsImNsaWVudEhvc3QiOiIxNzIuMTcuMC4xIiwiY2xpZW50SWQiOiJDbGllbnRDcmVkZW50aWFsc0FwcCIsImNsaWVudEFkZHJlc3MiOiIxNzIuMTcuMC4xIn0.CVprHTzQYneKvONa8HbjSLO1lp9S47mDlvMhTyfCJSKaFYBN84IqnbbjEkV4jLemkVmaXwDfSRFIzSG1JFO0tdH2DYPR-PjKg6SeLK1pp5MFdJra7TjgZe-Kq2PiDY_BcEHV0OyG7O9hnyZbsyujvI17xwBLSeAdU8qTNa85JrOdxfHx9kp_qpcvigLmTQr9BgCO_ckO_lb0ZI1uJOakaDWCewDspOFe6abazdaTgEraZOU_UROR7MYumSMq1xBJC79vuv3eMCzfjm3yKKXam05HuhbwdjCUI5_vIeL8DDtBtWV3FblKepj4K4i3fId_O6sd5XFyum6kVrXvKvFgAw
```

Success you have registered a client for client 

### Client for authorization code grant

> TODO

## Creating a resource server

You have seen how to register clients in the Authorization server.
Next we need to serve up some resources, this if going to be a simple Spring API.

Navigate to <start.spring.io> choose your setup, however, you will need to select the following dependencies:

* Spring Web / Spring Reactive Web
* OAuth2 Resource Server

Download the project and open it up.
You should then create a `controller` package and an Emails controller.
You can see an example of this setup in the sample [resource server](code/oauth2/resource-server/).

> Note you may need to change the server.port of the application, I've changed it to port 9090

You can see in the controller that the Principal is extracted and can be used to check claims in the token to verify information about the client.

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

Lets try this out, run your application and send the following request:

```shell
# Get the access token
CLIENT_CRED_ID=ClientCredentialsApp
CLIENT_CRED_SECRET=oneM9EujQeFywuv8QBnFRKc87ECnZoBy
ACCESS_TOKEN=$(curl --location -X POST 'http://localhost:8080/realms/oauth2-workshop/protocol/openid-connect/token' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode "client_id=$CLIENT_CRED_ID" \
--data-urlencode "client_secret=$CLIENT_CRED_SECRET" \
--data-urlencode 'grant_type=client_credentials' \
| jq -r '.access_token')

# Call the Resource server as the Client Application

curl --header "Authorization: Bearer $ACCESS_TOKEN" \
http://localhost:9090/emails
```

Success you should now see that your request returns a response.

Why not try the following:

1. Try modifying your access token does the request still work?
2. Wait till your access token expires, what happens when you send it to the resource server

