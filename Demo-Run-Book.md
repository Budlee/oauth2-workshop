# Demo scripts

Pre-req: 
Auth-Server is setup and application properties are configured

##  Starting applications

1. Starting the Resource Sever
   ```bash
   ./gradlew :resource-server:bootRun
   ```
2. The resources live at: <http://localhost:9090/emails>
   ```bash
   curl -v http://localhost:9090/emails
   ```
   
## Client Credentials grant

### Retrieve a Token

```bash
CLIENT_CRED_ID=ClientCredentialsApp
CLIENT_CRED_SECRET=C2sBktd5E2yMjF7xu6ysNxYky3lRlOE6
ACCESS_TOKEN=$(curl --location -X POST 'http://localhost:8080/realms/oauth2-workshop/protocol/openid-connect/token' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode "client_id=$CLIENT_CRED_ID" \
--data-urlencode "client_secret=$CLIENT_CRED_SECRET" \
--data-urlencode 'grant_type=client_credentials' \
| jq -r '.access_token')
```

### Inspect token

```bash
echo $ACCESS_TOKEN
```
Visit <https://jwt.io>

### Use a Token

```bash
curl -v --header "Authorization: Bearer $ACCESS_TOKEN" \
http://localhost:9090/emails
```

## Authorization Code Grant

### Start Client Application

```bash
export CLIENT_SECRET=ikrLKCzI2cZDJZZH1TtNiaHA2z6mf0Ab
./gradlew :client-application:bootRun
```

### Visit the client application

Open a browser and open the network tab.
Then visit <http://localhost:7070>.

You can see the network calls and redirects

In the logs you can see the calls made by the application to get Access Token

### Inspect the token

Visit <https://jwt.io>

# Call the email service

Press the email service and see how the token is used in the resource server
