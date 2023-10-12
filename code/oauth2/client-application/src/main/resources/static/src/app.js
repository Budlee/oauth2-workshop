async function requestAccessToken() {

    return fetch("http://localhost:7070/myToken", {
        method: 'GET'
    }).then(response => {
        return response.text();
    }).then(strToken => {
        document.getElementById("div-accessToken").innerText = strToken
    });

}

async function requestContacts() {

    return fetch("http://localhost:7070/contacts", {
        method: 'GET'
    }).then(response => {
        return response.text();
    }).then(strContacts => {
        document.getElementById("div-contacts").innerText = strContacts
    });

}
