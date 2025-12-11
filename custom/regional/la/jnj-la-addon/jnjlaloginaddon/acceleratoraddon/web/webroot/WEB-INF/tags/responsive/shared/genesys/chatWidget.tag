<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ attribute name="hideHeaderLinks" required="false" %>
<script type="text/javascript" charset="utf-8">
  var deploymentId = '${genesysDeploymentKey}'; 
  (function (g, e, n, es, ys) {
    g['_genesysJs'] = e;
    g[e] = g[e] || function () {
      (g[e].q = g[e].q || []).push(arguments)
    };
    g[e].t = 1 * new Date();
    g[e].c = es;
    ys = document.createElement('script'); ys.async = 1; ys.src = n; ys.charset = 'utf-8'; document.head.appendChild(ys);
  })(window, 'Genesys', 'https://apps.mypurecloud.com/genesys-bootstrap/genesys.min.js', {
    environment: 'prod',
    deploymentId: deploymentId
  });
</script>

<c:set var="isMDDTrue" value="${googleUserSector eq 'MDD'}"/>
<c:set var="isPHRTrue" value="${googleUserSector eq 'PHR' || googleUserSector eq 'PHARMA'}"/>
<c:set var="isMDD_PHRTrue" value="${googleUserSector eq 'MDD_PHARMA'}"/>

<c:set var = "email" value = "${user.uid}"/>
<c:if test = "${fn:endsWith(email, '@its.jnj.com')}">
 <c:set var = "UserEmail" value = "Internal"/>
</c:if>
<c:if test = "${!fn:endsWith(email, '@its.jnj.com')}">
   <c:set var = "UserEmail" value = "External"/>
</c:if>

<c:if test="${isMDD_PHRTrue}">
<script type="text/javascript">
        
		
        let languageToSend;
        let currentCountryCode = "${countryCode}";
        let currentLanguageCode = "${currentLanguage.isocode}";

        var countryMapping = {
            "BR": "BRAZIL",
            "MX": "MEXICO",
            "PE": "PERU",
            "AR": "ARGENTINA",
            "CL": "CHILE",
            "CO": "COLOMBIA",
            "CR": "COSTA RICA",
            "EC": "ECUADOR",
            "PA": "CENCA",
            "PR": "PUERTO RICO",
            "UY": "URUGUAY"
        };

        var languageMapping = {
            "en": "ENGLISH",
            "pt": "PORTUGUESE",
            "es": "SPANISH"
        };

        let dynamicLanguage = languageMapping[currentLanguageCode];
        let dynamicCountryName = countryMapping[currentCountryCode];

        if(dynamicCountryName === "BRAZIL"){
            languageToSend = "PORTUGUESE";
        }else if(dynamicCountryName === "CENCA" && dynamicLanguage === "ENGLISH"){
            languageToSend = "ENGLISH";
        }else if(dynamicCountryName === "PUERTO RICO" && dynamicLanguage === "ENGLISH"){
            languageToSend = "ENGLISH";
        }else{
            languageToSend = "SPANISH";
        }


// Variables to change in your deployment
var deploymentId = '${genesysDeploymentKey}';  // Your WebMessenger DeploymentId
var sessionkey = '_'+ deploymentId + ':gcmcsessionActive';

const hexColor = '#0C8CAA';  // Color theme

if(dynamicCountryName === "COLOMBIA"){


function toggleMessenger() {
  Genesys('command', 'Messenger.open', {}, function (o) {
    closeLauncher();
    sessionStorage.setItem('UserEmail', '${UserEmail}');
	  sessionStorage.setItem('languageref', languageToSend);
    sessionStorage.setItem('email', '${user.email}');
    sessionStorage.setItem('googleUserAccountNumber', '${googleUserAccountNumber}');
    sessionStorage.setItem('googleUserSector', '${googleUserSector}');
    sessionStorage.setItem('firstName', '${user.firstName}');
    sessionStorage.setItem('lastName', '${user.lastName}');
    sessionStorage.setItem("Subject", document.getElementById('productphrmdd').value);
    sessionStorage.setItem("Franchise", document.getElementById('hcpphrmdd').value);
    sessionStorage.setItem("Country", document.getElementById('country').value);

    Genesys('command', 'Database.set', {
      messaging: {
        customAttributes: {
		  type: '${UserEmail}',
          Language: languageToSend,
          Account: '${googleUserAccountNumber}',
          Sector: '${googleUserSector}',
          email: '${user.email}',
          FirstName: '${user.firstName}',
          LastName: '${user.lastName}',
          Subject: document.getElementById('productphrmdd').value,
          Franchise: document.getElementById('hcpphrmdd').value,
		  Country: document.getElementById('country').value  // Added Country
        },
        markdown: true
      }
    });
  }, function (o) {
    Genesys('command', 'Messenger.close');
  });
}


let restoredRan = false;
let disconnected = false;

Genesys("subscribe", "MessagingService.conversationDisconnected", function(){
  
   disconnected = true;
  Genesys('command', 'Database.set', {
    messaging: {
       customAttributes: {
		         type: sessionStorage.getItem('UserEmail'),
             Language: sessionStorage.getItem('languageref'),
             Account: sessionStorage.getItem('googleUserAccountNumber'),
             Sector: sessionStorage.getItem('googleUserSector'),
             email: sessionStorage.getItem('email'),
             FirstName: sessionStorage.getItem('firstName'),
             LastName: sessionStorage.getItem('lastName'),
			       Subject:sessionStorage.getItem("Subject"),
             Franchise:sessionStorage.getItem("Franchise"),
             Country:sessionStorage.getItem("Country")
        },
    },
  }) 
     
  });
  
Genesys('subscribe', 'MessagingService.restored', () => {
   if(restoredRan || !disconnected) {
      return;
   }
   Genesys("command", "MessagingService.clearConversation");
   restoredRan = true;
});
 
Genesys('subscribe', 'Conversations.started', () => {
  disconnected = false;
});
  



function closeLauncher() {
  const input = document.getElementById('input');
  input.hidden = true;
  console.log('Hiding...');
}

function openLauncher() {


  console.log("k",sessionkey)
  const session = JSON.parse(localStorage.getItem(sessionkey));
  
  const input = document.getElementById('input');
  console.log(session?.value);
  if (session?.value) {
    console.log('Opening Widget...');
    Genesys('command', 'Messenger.open', {}, function (o) {
      closeLauncher();
    }, function (o) {
      Genesys('command', 'Messenger.close');
    });
  } else {
    console.log('Showing...');
    input.hidden = false;
  }
}


// Create Input Form
const input = document.createElement('div');
const header = document.createElement('div');
const title = document.createElement('p');
const minButton = document.createElement('button');
const closeButton = document.createElement('button'); // Added closeButton
const form = document.createElement('div');

const productL = document.createElement('label');
const productS = document.createElement('select');
const option1 = document.createElement('option');
const option2 = document.createElement('option');
const option3 = document.createElement('option');
const hcpL = document.createElement('label');
const hcpS = document.createElement('select');
const hcp_option1 = document.createElement('option');
const hcp_option2 = document.createElement('option');
const countryL = document.createElement('label');  // Added Country label
const countryS = document.createElement('select');  // Added Country select
const country_option = document.createElement('option');  // Added Country option
const submit = document.createElement('button');
const cancel = document.createElement('button'); // Added Cancel button

input.id = 'input';
input.hidden = true;
input.style = `
  box-shadow: rgba(0, 0, 0, 0.2) 0px 3px 5px -2px, rgba(0, 0, 0, 0.14) 0px 1px 4px 2px, rgba(0, 0, 0, 0.12) 0px 1px 4px 1px;
  position: fixed !important;
  bottom: 30px !important;
  width: inherit;
  height: 315px;
  right: 30px !important;
  background-color: white;
  z-index: 99999;
  margin-right:25px;
  margin-bottom:-35px;
`;
header.style = `
  display: inline-flex;
  background-color: ${hexColor};
  color: white;
  font-size: 1.33929rem;
  line-height: 2.6;
  font-weight: 400;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen-Sans, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', 'sans-serif';
  width: 100%;
  height: 60px;
  background-color: rgb(12, 140, 170);
`;
title.style = `
  margin: 0;
  padding-left: 25px;
  font-size:80%;
`;
title.innerText = 'Web Messenger';
minButton.style = `
  position: absolute;
  width: 50px;
  right: 8px;
  top: 15px;
  cursor: pointer;
  filter: invert(1);
  border: 0;
  background-color: transparent;
`;
minButton.onclick = closeLauncher;
minButton.tabIndex = 0;
minButton.ariaLabel = 'Minimize the Messenger';
minButton.innerHTML = `
  <svg id="svgid" viewBox="0 0 24 24" style="width: 26px; height: 26px; margin-top: -32px;margin-left: -40px;">
    <title>window-minimize</title>
    <path d="M19 13H5v-2h14v2z"></path>
  </svg>`;
header.appendChild(title);
header.appendChild(minButton);
input.appendChild(header);

closeButton.style = `
  position: absolute;
  width: 50px;
  right: 8px;
  top: 15px;
  cursor: pointer;
  filter: invert(1);
  border: 0;
  background-color: transparent;
`;
closeButton.onclick = closeLauncher;
closeButton.tabIndex = 0;
closeButton.ariaLabel = 'Close the Messenger';
closeButton.innerHTML = `
  <svg id="svgid" viewBox="0 0 24 24" style="width: 26px; height: 26px; margin-top: -32px; margin-left: -10px;">
    <title>window-close</title>
    <path d="M18 6L6 18M6 6l12 12"></path>
  </svg>`;
header.appendChild(closeButton); // Append closeButton to header
input.appendChild(header);

form.style = 'padding: 25px; display: flex; flex-direction: column; gap: 10px;';

const productContainer = document.createElement('div');
productContainer.style = 'display: flex; align-items: center; gap: 10px;';
productL.innerText = 'Subject';
productL.style = `margin-right: 15px;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen-Sans, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', 'sans-serif';
`;
productS.id = 'productphrmdd';
productS.style = `
  flex: 1;
  padding: 12px 20px;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
  height: 30px;
`;
option1.value = 'MEDICAL';
option1.innerText = 'MEDTECH';
option2.value = 'PHARMA';
option2.innerText = 'INNOVATIVE MEDICINE';
option3.value = 'CONSIGNACIONES';
option3.innerText = 'CONSIGNACIONES';
productS.appendChild(option1);
productS.appendChild(option2);
productS.appendChild(option3);
productContainer.appendChild(productL);
productContainer.appendChild(productS);

const hcpContainer = document.createElement('div');
hcpContainer.style = 'display: flex; align-items: center; ';
hcpL.innerText = 'Franchise';
hcpL.style = `margin-right: 12px;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen-Sans, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', 'sans-serif';
`;
hcpS.id = 'hcpphrmdd';
hcpS.className = 'franchise-dropdown-co-mdd-phr'; 
hcpS.style = `
  flex: 1;
  padding: 12px 20px;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
`;
hcp_option1.value = 'ETHICON';
hcp_option1.innerText = 'ETHICON';

hcpS.appendChild(hcp_option1);


hcpContainer.appendChild(hcpL);
hcpContainer.appendChild(hcpS);

const countryContainer = document.createElement('div');  // Added Country container
countryContainer.style = 'display: flex; align-items: center; gap: 10px;';
countryL.innerText = 'Country';
countryL.style = ` margin-right: 10px;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen-Sans, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', 'sans-serif';
`;
countryS.id = 'country';
countryS.className = 'country-dropdown';  // Set className for Country select
countryS.style = `
  flex: 1;
  padding: 12px 20px;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
   height: 30px;
`;
country_option.value = dynamicCountryName;
country_option.innerText = dynamicCountryName;
countryS.appendChild(country_option);
countryContainer.appendChild(countryL);
countryContainer.appendChild(countryS);

submit.style = `
  width: 80px;
  background-color: ${hexColor};
  color: white;
  padding: 4px 1px;
  margin: 25px 0 0 0;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  background-color: rgb(12, 140, 170);
`;
submit.innerText = 'Start Chat';
submit.onclick = toggleMessenger;

cancel.style = `
  width: 80px;
  background-color: #f44336;
  color: white;
  padding: 4px 1px;
  margin: 25px 0 0 10px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
`;
cancel.innerText = 'Cancel';
cancel.onclick = closeLauncher;




const buttonContainer = document.createElement('div');
buttonContainer.style = 'display: flex; justify-content: space-between;';
buttonContainer.appendChild(submit);
buttonContainer.appendChild(cancel);

form.appendChild(productContainer);
form.appendChild(hcpContainer);
form.appendChild(countryContainer);  // Append Country container to form
form.appendChild(buttonContainer);  // Append button container to form
input.appendChild(form);

document.body.appendChild(input);

}else if(dynamicCountryName === "BRAZIL" || dynamicCountryName ===  "PERU" || dynamicCountryName === "MEXICO"){

function toggleMessenger() {
  Genesys('command', 'Messenger.open', {}, function (o) {
    closeLauncher();
    sessionStorage.setItem('UserEmail', '${UserEmail}');
	  sessionStorage.setItem('languagebr', languageToSend);
    sessionStorage.setItem('email', '${user.email}');
    sessionStorage.setItem('googleUserAccountNumber', '${googleUserAccountNumber}');
    sessionStorage.setItem('googleUserSector', '${googleUserSector}');
    sessionStorage.setItem('firstName', '${user.firstName}');
    sessionStorage.setItem('lastName', '${user.lastName}');
    sessionStorage.setItem("Subject", document.getElementById('productbrphrmdd').value);
    sessionStorage.setItem("Franchise", document.getElementById('hcpbrphrmdd').value);
    sessionStorage.setItem("Country", document.getElementById('country').value);

    Genesys('command', 'Database.set', {
      messaging: {
        customAttributes: {
		  type: '${UserEmail}',
          Language: languageToSend,
          Account: '${googleUserAccountNumber}',
          Sector: '${googleUserSector}',
          email: '${user.email}',
          FirstName: '${user.firstName}',
          LastName: '${user.lastName}',
          Subject: document.getElementById('productbrphrmdd').value,
          Franchise: document.getElementById('hcpbrphrmdd').value,
		  Country: document.getElementById('country').value  // Added Country
        },
        markdown: true
      }
    });
  }, function (o) {
    Genesys('command', 'Messenger.close');
  });
}


let restoredRan = false;
let disconnected = false;

Genesys("subscribe", "MessagingService.conversationDisconnected", function(){
  
   disconnected = true;
  Genesys('command', 'Database.set', {
    messaging: {
       customAttributes: {
		        type: sessionStorage.getItem('UserEmail'),
             Language: sessionStorage.getItem('languagebr'),
             Account: sessionStorage.getItem('googleUserAccountNumber'),
             Sector: sessionStorage.getItem('googleUserSector'),
             email: sessionStorage.getItem('email'),
             FirstName: sessionStorage.getItem('firstName'),
             LastName: sessionStorage.getItem('lastName'),
			       Subject:sessionStorage.getItem("Subject"),
             Franchise:sessionStorage.getItem("Franchise"),
             Country:sessionStorage.getItem("Country")
        },
    },
  }) 
     
  });
  
Genesys('subscribe', 'MessagingService.restored', () => {
   if(restoredRan || !disconnected) {
      return;
   }
   Genesys("command", "MessagingService.clearConversation");
   restoredRan = true;
});
 
Genesys('subscribe', 'Conversations.started', () => {
  disconnected = false;
});
  
function closeLauncher() {
  const input = document.getElementById('input');
  input.hidden = true;
  console.log('Hiding...');
}

function openLauncher() {


  console.log("k",sessionkey)
  const session = JSON.parse(localStorage.getItem(sessionkey));
  
  const input = document.getElementById('input');
  console.log(session?.value);
  if (session?.value) {
    console.log('Opening Widget...');
    Genesys('command', 'Messenger.open', {}, function (o) {
      closeLauncher();
    }, function (o) {
      Genesys('command', 'Messenger.close');
    });
  } else {
    console.log('Showing...');
    input.hidden = false;
  }
}


// Create Input Form
const input = document.createElement('div');
const header = document.createElement('div');
const title = document.createElement('p');
const minButton = document.createElement('button');
const closeButton = document.createElement('button'); // Added closeButton
const form = document.createElement('div');

const productL = document.createElement('label');
const productS = document.createElement('select');
const option1 = document.createElement('option');
const option2 = document.createElement('option');

const hcpL = document.createElement('label');
const hcpS = document.createElement('select');
const hcp_option1 = document.createElement('option');
const hcp_option2 = document.createElement('option');
const countryL = document.createElement('label');  // Added Country label
const countryS = document.createElement('select');  // Added Country select
const country_option = document.createElement('option');  // Added Country option
const submit = document.createElement('button');
const cancel = document.createElement('button'); // Added Cancel button

input.id = 'input';
input.hidden = true;
input.style = `
  box-shadow: rgba(0, 0, 0, 0.2) 0px 3px 5px -2px, rgba(0, 0, 0, 0.14) 0px 1px 4px 2px, rgba(0, 0, 0, 0.12) 0px 1px 4px 1px;
  position: fixed !important;
  bottom: 30px !important;
  width: inherit;
  height: 315px;
  right: 30px !important;
  background-color: white;
  z-index: 99999;
  margin-right:25px;
  margin-bottom:-35px;
`;
header.style = `
  display: inline-flex;
  background-color: ${hexColor};
  color: white;
  font-size: 1.33929rem;
  line-height: 2.6;
  font-weight: 400;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen-Sans, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', 'sans-serif';
  width: 100%;
  height: 60px;
  background-color: rgb(12, 140, 170);
`;
title.style = `
  margin: 0;
  padding-left: 25px;
  font-size:80%;
`;
title.innerText = 'Web Messenger';
minButton.style = `
  position: absolute;
  width: 50px;
  right: 8px;
  top: 15px;
  cursor: pointer;
  filter: invert(1);
  border: 0;
  background-color: transparent;
`;
minButton.onclick = closeLauncher;
minButton.tabIndex = 0;
minButton.ariaLabel = 'Minimize the Messenger';
minButton.innerHTML = `
  <svg id="svgid" viewBox="0 0 24 24" style="width: 26px; height: 26px; margin-top: -32px;margin-left: -40px;">
    <title>window-minimize</title>
    <path d="M19 13H5v-2h14v2z"></path>
  </svg>`;
header.appendChild(title);
header.appendChild(minButton);
input.appendChild(header);

closeButton.style = `
  position: absolute;
  width: 50px;
  right: 8px;
  top: 15px;
  cursor: pointer;
  filter: invert(1);
  border: 0;
  background-color: transparent;
`;
closeButton.onclick = closeLauncher;
closeButton.tabIndex = 0;
closeButton.ariaLabel = 'Close the Messenger';
closeButton.innerHTML = `
  <svg id="svgid" viewBox="0 0 24 24" style="width: 26px; height: 26px; margin-top: -32px; margin-left: -10px;">
    <title>window-close</title>
    <path d="M18 6L6 18M6 6l12 12"></path>
  </svg>`;
header.appendChild(closeButton); // Append closeButton to header
input.appendChild(header);

form.style = 'padding: 25px; display: flex; flex-direction: column; gap: 10px;';

const productContainer = document.createElement('div');
productContainer.style = 'display: flex; align-items: center; gap: 10px;';
productL.innerText = 'Subject';
productL.style = `margin-right: 15px;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen-Sans, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', 'sans-serif';
`;
productS.id = 'productbrphrmdd';
productS.style = `
  flex: 1;
  padding: 12px 20px;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
  height: 30px;
`;
option1.value = 'MEDICAL';
option1.innerText = 'MEDTECH';
option2.value = 'PHARMA';
option2.innerText = 'INNOVATIVE MEDICINE';


productS.appendChild(option1);
productS.appendChild(option2);

productContainer.appendChild(productL);
productContainer.appendChild(productS);

const hcpContainer = document.createElement('div');
hcpContainer.style = 'display: flex; align-items: center; ';
hcpL.innerText = 'Franchise';
hcpL.style = `margin-right: 12px;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen-Sans, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', 'sans-serif';
`;
hcpS.id = 'hcpbrphrmdd';
hcpS.className = 'franchise-dropdown-br-mdd-phr'; 
hcpS.style = `
  flex: 1;
  padding: 12px 20px;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
`;

hcp_option1.value = 'ETHICON';
hcp_option1.innerText = 'ETHICON';
hcp_option2.value = 'DEPUY';
hcp_option2.innerText = 'DEPUY';
hcpS.appendChild(hcp_option1);
hcpS.appendChild(hcp_option2);
hcpContainer.appendChild(hcpL);
hcpContainer.appendChild(hcpS);

const countryContainer = document.createElement('div');  // Added Country container
countryContainer.style = 'display: flex; align-items: center; gap: 10px;';
countryL.innerText = 'Country';
countryL.style = ` margin-right: 10px;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen-Sans, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', 'sans-serif';
`;
countryS.id = 'country';
countryS.className = 'country-dropdown';  // Set className for Country select
countryS.style = `
  flex: 1;
  padding: 12px 20px;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
   height: 30px;
`;
country_option.value = dynamicCountryName;
country_option.innerText = dynamicCountryName;
countryS.appendChild(country_option);
countryContainer.appendChild(countryL);
countryContainer.appendChild(countryS);

submit.style = `
  width: 80px;
  background-color: ${hexColor};
  color: white;
  padding: 4px 1px;
  margin: 25px 0 0 0;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  background-color: rgb(12, 140, 170);
`;
submit.innerText = 'Start Chat';
submit.onclick = toggleMessenger;

cancel.style = `
  width: 80px;
  background-color: #f44336;
  color: white;
  padding: 4px 1px;
  margin: 25px 0 0 10px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
`;
cancel.innerText = 'Cancel';
cancel.onclick = closeLauncher;




const buttonContainer = document.createElement('div');
buttonContainer.style = 'display: flex; justify-content: space-between;';
buttonContainer.appendChild(submit);
buttonContainer.appendChild(cancel);

form.appendChild(productContainer);
form.appendChild(hcpContainer);
form.appendChild(countryContainer);  // Append Country container to form
form.appendChild(buttonContainer);  // Append button container to form
input.appendChild(form);

document.body.appendChild(input);
// Add CSS for pointer-events: none !important;
const style = document.createElement('style');
style.innerHTML = `
  .country-dropdown {
    pointer-events: none !important;
  }
`;
document.head.appendChild(style);




}else{


function toggleMessenger() {
  Genesys('command', 'Messenger.open', {}, function (o) {
    closeLauncher();
    sessionStorage.setItem('UserEmail', '${UserEmail}');
	  sessionStorage.setItem('languageref', languageToSend);
    sessionStorage.setItem('email', '${user.email}');
    sessionStorage.setItem('googleUserAccountNumber', '${googleUserAccountNumber}');
    sessionStorage.setItem('googleUserSector', '${googleUserSector}');
    sessionStorage.setItem('firstName', '${user.firstName}');
    sessionStorage.setItem('lastName', '${user.lastName}');
    sessionStorage.setItem("Subject", document.getElementById('productphrmddothers').value);
    sessionStorage.setItem("Franchise", document.getElementById('hcpphrmddothers').value);
    sessionStorage.setItem("Country", document.getElementById('country').value);

    Genesys('command', 'Database.set', {
      messaging: {
        customAttributes: {
		  type: '${UserEmail}',
          Language: languageToSend,
          Account: '${googleUserAccountNumber}',
          Sector: '${googleUserSector}',
          email: '${user.email}',
          FirstName: '${user.firstName}',
          LastName: '${user.lastName}',
          Subject: document.getElementById('productphrmddothers').value,
          Franchise: document.getElementById('hcpphrmddothers').value,
		  Country: document.getElementById('country').value  // Added Country
        },
        markdown: true
      }
    });
  }, function (o) {
    Genesys('command', 'Messenger.close');
  });
}


let restoredRan = false;
let disconnected = false;

Genesys("subscribe", "MessagingService.conversationDisconnected", function(){
  
   disconnected = true;
  Genesys('command', 'Database.set', {
    messaging: {
       customAttributes: {
		        type: sessionStorage.getItem('UserEmail'),
             Language: sessionStorage.getItem('languageref'),
             Account: sessionStorage.getItem('googleUserAccountNumber'),
             Sector: sessionStorage.getItem('googleUserSector'),
             email: sessionStorage.getItem('email'),
             FirstName: sessionStorage.getItem('firstName'),
             LastName: sessionStorage.getItem('lastName'),
			       Subject:sessionStorage.getItem("Subject"),
             Franchise:sessionStorage.getItem("Franchise"),
             Country:sessionStorage.getItem("Country")
        },
    },
  }) 
     
  });
  
Genesys('subscribe', 'MessagingService.restored', () => {
   if(restoredRan || !disconnected) {
      return;
   }
   Genesys("command", "MessagingService.clearConversation");
   restoredRan = true;
});
 
Genesys('subscribe', 'Conversations.started', () => {
  disconnected = false;
});
  

function closeLauncher() {
  const input = document.getElementById('input');
  input.hidden = true;
  console.log('Hiding...');
}

function openLauncher() {


  console.log("k",sessionkey)
  const session = JSON.parse(localStorage.getItem(sessionkey));
  
  const input = document.getElementById('input');
  console.log(session?.value);
  if (session?.value) {
    console.log('Opening Widget...');
    Genesys('command', 'Messenger.open', {}, function (o) {
      closeLauncher();
    }, function (o) {
      Genesys('command', 'Messenger.close');
    });
  } else {
    console.log('Showing...');
    input.hidden = false;
  }
}





// Create Input Form
const input = document.createElement('div');
const header = document.createElement('div');
const title = document.createElement('p');
const minButton = document.createElement('button');
const closeButton = document.createElement('button'); // Added closeButton
const form = document.createElement('div');

const productL = document.createElement('label');
const productS = document.createElement('select');
const option1 = document.createElement('option');
const option2 = document.createElement('option');

const hcpL = document.createElement('label');
const hcpS = document.createElement('select');
const hcp_option1 = document.createElement('option');

const countryL = document.createElement('label');  // Added Country label
const countryS = document.createElement('select');  // Added Country select
const country_option = document.createElement('option');  // Added Country option
const submit = document.createElement('button');
const cancel = document.createElement('button'); // Added Cancel button

input.id = 'input';
input.hidden = true;
input.style = `
  box-shadow: rgba(0, 0, 0, 0.2) 0px 3px 5px -2px, rgba(0, 0, 0, 0.14) 0px 1px 4px 2px, rgba(0, 0, 0, 0.12) 0px 1px 4px 1px;
  position: fixed !important;
  bottom: 30px !important;
  width: inherit;
  height: 315px;
  right: 30px !important;
  background-color: white;
  z-index: 99999;
  margin-right:25px;
  margin-bottom:-35px;
`;
header.style = `
  display: inline-flex;
  background-color: ${hexColor};
  color: white;
  font-size: 1.33929rem;
  line-height: 2.6;
  font-weight: 400;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen-Sans, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', 'sans-serif';
  width: 100%;
  height: 60px;
  background-color: rgb(12, 140, 170);
`;
title.style = `
  margin: 0;
  padding-left: 25px;
  font-size:80%;
`;
title.innerText = 'Web Messenger';
minButton.style = `
  position: absolute;
  width: 50px;
  right: 8px;
  top: 15px;
  cursor: pointer;
  filter: invert(1);
  border: 0;
  background-color: transparent;
`;
minButton.onclick = closeLauncher;
minButton.tabIndex = 0;
minButton.ariaLabel = 'Minimize the Messenger';
minButton.innerHTML = `
  <svg id="svgid" viewBox="0 0 24 24" style="width: 26px; height: 26px; margin-top: -32px;margin-left: -40px;">
    <title>window-minimize</title>
    <path d="M19 13H5v-2h14v2z"></path>
  </svg>`;
header.appendChild(title);
header.appendChild(minButton);
input.appendChild(header);

closeButton.style = `
  position: absolute;
  width: 50px;
  right: 8px;
  top: 15px;
  cursor: pointer;
  filter: invert(1);
  border: 0;
  background-color: transparent;
`;
closeButton.onclick = closeLauncher;
closeButton.tabIndex = 0;
closeButton.ariaLabel = 'Close the Messenger';
closeButton.innerHTML = `
  <svg id="svgid" viewBox="0 0 24 24" style="width: 26px; height: 26px; margin-top: -32px; margin-left: -10px;">
    <title>window-close</title>
    <path d="M18 6L6 18M6 6l12 12"></path>
  </svg>`;
header.appendChild(closeButton); // Append closeButton to header
input.appendChild(header);

form.style = 'padding: 25px; display: flex; flex-direction: column; gap: 10px;';

const productContainer = document.createElement('div');
productContainer.style = 'display: flex; align-items: center; gap: 10px;';
productL.innerText = 'Subject';
productL.style = `margin-right: 15px;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen-Sans, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', 'sans-serif';
`;
productS.id = 'productphrmddothers';
productS.style = `
  flex: 1;
  padding: 12px 20px;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
  height: 30px;
`;
option1.value = 'MEDICAL';
option1.innerText = 'MEDTECH';
option2.value = 'PHARMA';
option2.innerText = 'INNOVATIVE MEDICINE';

productS.appendChild(option1);
productS.appendChild(option2);

productContainer.appendChild(productL);
productContainer.appendChild(productS);

const hcpContainer = document.createElement('div');
hcpContainer.style = 'display: flex; align-items: center; ';
hcpL.innerText = 'Franchise';
hcpL.style = `margin-right: 12px;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen-Sans, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', 'sans-serif';
`;
hcpS.id = 'hcpphrmddothers';
hcpS.className = 'franchise-dropdown-mdd-phr-others'; 
hcpS.style = `
  flex: 1;
  padding: 12px 20px;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
`;

hcp_option1.value = 'ETHICON';
hcp_option1.innerText = 'ETHICON';

hcpS.appendChild(hcp_option1);

hcpContainer.appendChild(hcpL);
hcpContainer.appendChild(hcpS);

const countryContainer = document.createElement('div');  // Added Country container
countryContainer.style = 'display: flex; align-items: center; gap: 10px;';
countryL.innerText = 'Country';
countryL.style = ` margin-right: 10px;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen-Sans, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', 'sans-serif';
`;
countryS.id = 'country';
countryS.className = 'country-dropdown';  // Set className for Country select
countryS.style = `
  flex: 1;
  padding: 12px 20px;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
   height: 30px;
`;
country_option.value = dynamicCountryName;
country_option.innerText = dynamicCountryName;
countryS.appendChild(country_option);
countryContainer.appendChild(countryL);
countryContainer.appendChild(countryS);

submit.style = `
  width: 80px;
  background-color: ${hexColor};
  color: white;
  padding: 4px 1px;
  margin: 25px 0 0 0;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  background-color: rgb(12, 140, 170);
`;
submit.innerText = 'Start Chat';
submit.onclick = toggleMessenger;

cancel.style = `
  width: 80px;
  background-color: #f44336;
  color: white;
  padding: 4px 1px;
  margin: 25px 0 0 10px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
`;
cancel.innerText = 'Cancel';
cancel.onclick = closeLauncher;




const buttonContainer = document.createElement('div');
buttonContainer.style = 'display: flex; justify-content: space-between;';
buttonContainer.appendChild(submit);
buttonContainer.appendChild(cancel);

form.appendChild(productContainer);
form.appendChild(hcpContainer);
form.appendChild(countryContainer);  // Append Country container to form
form.appendChild(buttonContainer);  // Append button container to form
input.appendChild(form);

document.body.appendChild(input);
// Add CSS for pointer-events: none !important;
const style = document.createElement('style');
style.innerHTML = `
  .country-dropdown {
    pointer-events: none !important;
  }
`;
document.head.appendChild(style);


}

</script>

</c:if>

<c:if test="${isMDDTrue}">
<script type="text/javascript">
        
		
        let languageToSend;
        let currentCountryCode = "${countryCode}";
        let currentLanguageCode = "${currentLanguage.isocode}";

        var countryMapping = {
            "BR": "BRAZIL",
            "MX": "MEXICO",
            "PE": "PERU",
            "AR": "ARGENTINA",
            "CL": "CHILE",
            "CO": "COLOMBIA",
            "CR": "COSTA RICA",
            "EC": "ECUADOR",
            "PA": "CENCA",
            "PR": "PUERTO RICO",
            "UY": "URUGUAY"
        };

        var languageMapping = {
            "en": "ENGLISH",
            "pt": "PORTUGUESE",
            "es": "SPANISH"
        };

        let dynamicLanguage = languageMapping[currentLanguageCode];
        let dynamicCountryName = countryMapping[currentCountryCode];

        if(dynamicCountryName === "BRAZIL"){
            languageToSend = "PORTUGUESE";
        }else if(dynamicCountryName === "CENCA" && dynamicLanguage === "ENGLISH"){
            languageToSend = "ENGLISH";
        }else if(dynamicCountryName === "PUERTO RICO" && dynamicLanguage === "ENGLISH"){
            languageToSend = "ENGLISH";
        }else{
            languageToSend = "SPANISH";
        }


// Variables to change in your deployment
var deploymentId = '${genesysDeploymentKey}';  // Your WebMessenger DeploymentId
var sessionkey = '_'+ deploymentId + ':gcmcsessionActive';

const hexColor = '#0C8CAA';  // Color theme

function toggleMessenger() {
  Genesys('command', 'Messenger.open', {}, function (o) {
    closeLauncher();
    sessionStorage.setItem('UserEmail', '${UserEmail}');
	  sessionStorage.setItem('languageref', languageToSend);
    sessionStorage.setItem('email', '${user.email}');
    sessionStorage.setItem('googleUserAccountNumber', '${googleUserAccountNumber}');
    sessionStorage.setItem('googleUserSector', '${googleUserSector}');
    sessionStorage.setItem('firstName', '${user.firstName}');
    sessionStorage.setItem('lastName', '${user.lastName}');
    sessionStorage.setItem("Subject", document.getElementById('product').value);
    sessionStorage.setItem("Franchise", document.getElementById('hcp').value);
    sessionStorage.setItem("Country", document.getElementById('country').value);

    Genesys('command', 'Database.set', {
      messaging: {
        customAttributes: {
		  type: '${UserEmail}',
          Language: languageToSend,
          Account: '${googleUserAccountNumber}',
          Sector: '${googleUserSector}',
          email: '${user.email}',
          FirstName: '${user.firstName}',
          LastName: '${user.lastName}',
          Subject: document.getElementById('product').value,
          Franchise: document.getElementById('hcp').value,
          Country: document.getElementById('country').value  // Added Country
        },
        markdown: true
      }
    });
  }, function (o) {
    Genesys('command', 'Messenger.close');
  });
}

let restoredRan = false;
let disconnected = false;

Genesys("subscribe", "MessagingService.conversationDisconnected", function(){
  
   disconnected = true;
  Genesys('command', 'Database.set', {
    messaging: {
       customAttributes: {
		        type: sessionStorage.getItem('UserEmail'),
             Language: sessionStorage.getItem('languageref'),
             Account: sessionStorage.getItem('googleUserAccountNumber'),
             Sector: sessionStorage.getItem('googleUserSector'),
             email: sessionStorage.getItem('email'),
             FirstName: sessionStorage.getItem('firstName'),
             LastName: sessionStorage.getItem('lastName'),
			       Subject:sessionStorage.getItem("Subject"),
             Franchise:sessionStorage.getItem("Franchise"),
             Country:sessionStorage.getItem("Country")
        },
    },
  }) 
     
  });
  
Genesys('subscribe', 'MessagingService.restored', () => {
   if(restoredRan || !disconnected) {
      return;
   }
   Genesys("command", "MessagingService.clearConversation");
   restoredRan = true;
});
 
Genesys('subscribe', 'Conversations.started', () => {
  disconnected = false;
});
  

function closeLauncher() {
  const input = document.getElementById('input');
  input.hidden = true;
  console.log('Hiding...');
}

function openLauncher() {


  console.log("k",sessionkey)
  const session = JSON.parse(localStorage.getItem(sessionkey));
  
  const input = document.getElementById('input');
  console.log(session?.value);
  if (session?.value) {
    console.log('Opening Widget...');
    Genesys('command', 'Messenger.open', {}, function (o) {
      closeLauncher();
    }, function (o) {
      Genesys('command', 'Messenger.close');
    });
  } else {
    console.log('Showing...');
    input.hidden = false;
  }
}

if(dynamicCountryName === "COLOMBIA"){

// Create Input Form
const input = document.createElement('div');
const header = document.createElement('div');
const title = document.createElement('p');
const minButton = document.createElement('button');
const closeButton = document.createElement('button'); // Added closeButton
const form = document.createElement('div');

const productL = document.createElement('label');
const productS = document.createElement('select');
const option1 = document.createElement('option');
const option2 = document.createElement('option');
const hcpL = document.createElement('label');
const hcpS = document.createElement('select');
const hcp_option1 = document.createElement('option');
const hcp_option2 = document.createElement('option');
const countryL = document.createElement('label');  // Added Country label
const countryS = document.createElement('select');  // Added Country select
const country_option = document.createElement('option');  // Added Country option
const submit = document.createElement('button');
const cancel = document.createElement('button'); // Added Cancel button

input.id = 'input';
input.hidden = true;
input.style = `
  box-shadow: rgba(0, 0, 0, 0.2) 0px 3px 5px -2px, rgba(0, 0, 0, 0.14) 0px 1px 4px 2px, rgba(0, 0, 0, 0.12) 0px 1px 4px 1px;
  position: fixed !important;
  bottom: 30px !important;
  width: inherit;
  height: 315px;
  right: 30px !important;
  background-color: white;
  z-index: 99999;
  margin-right:25px;
  margin-bottom:-35px;
`;
header.style = `
  display: inline-flex;
  background-color: ${hexColor};
  color: white;
  font-size: 1.33929rem;
  line-height: 2.6;
  font-weight: 400;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen-Sans, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', 'sans-serif';
  width: 100%;
  height: 60px;
  background-color: rgb(12, 140, 170);
`;
title.style = `
  margin: 0;
  padding-left: 25px;
  font-size:80%;
`;
title.innerText = 'Web Messenger';
minButton.style = `
  position: absolute;
  width: 50px;
  right: 8px;
  top: 15px;
  cursor: pointer;
  filter: invert(1);
  border: 0;
  background-color: transparent;
`;
minButton.onclick = closeLauncher;
minButton.tabIndex = 0;
minButton.ariaLabel = 'Minimize the Messenger';
minButton.innerHTML = `
  <svg id="svgid" viewBox="0 0 24 24" style="width: 26px; height: 26px; margin-top: -32px;margin-left: -40px;">
    <title>window-minimize</title>
    <path d="M19 13H5v-2h14v2z"></path>
  </svg>`;
header.appendChild(title);
header.appendChild(minButton);
input.appendChild(header);

closeButton.style = `
  position: absolute;
  width: 50px;
  right: 8px;
  top: 15px;
  cursor: pointer;
  filter: invert(1);
  border: 0;
  background-color: transparent;
`;
closeButton.onclick = closeLauncher;
closeButton.tabIndex = 0;
closeButton.ariaLabel = 'Close the Messenger';
closeButton.innerHTML = `
  <svg id="svgid" viewBox="0 0 24 24" style="width: 26px; height: 26px; margin-top: -32px; margin-left: -10px;">
    <title>window-close</title>
    <path d="M18 6L6 18M6 6l12 12"></path>
  </svg>`;
header.appendChild(closeButton); // Append closeButton to header
input.appendChild(header);

form.style = 'padding: 25px; display: flex; flex-direction: column; gap: 10px;';

const productContainer = document.createElement('div');
productContainer.style = 'display: flex; align-items: center; gap: 10px;';
productL.innerText = 'Subject';
productL.style = `margin-right: 15px;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen-Sans, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', 'sans-serif';
`;
productS.id = 'product';
productS.style = `
  flex: 1;
  padding: 12px 20px;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
  height: 30px;
`;
option1.value = 'MEDICAL';
option1.innerText = 'MEDTECH';
option2.value = 'CONSIGNACIONES';
option2.innerText = 'CONSIGNACIONES';
productS.appendChild(option1);
productS.appendChild(option2);
productContainer.appendChild(productL);
productContainer.appendChild(productS);

const hcpContainer = document.createElement('div');
hcpContainer.style = 'display: flex; align-items: center; ';
hcpL.innerText = 'Franchise';
hcpL.style = `margin-right: 12px;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen-Sans, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', 'sans-serif';
`;
hcpS.id = 'hcp';
hcpS.className = 'franchise-dropdown-co-mdd'; 
hcpS.style = `
  flex: 1;
  padding: 12px 20px;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
`;
hcp_option1.value = 'ETHICON';
hcp_option1.innerText = 'ETHICON';

hcpS.appendChild(hcp_option1);


hcpContainer.appendChild(hcpL);
hcpContainer.appendChild(hcpS);

const countryContainer = document.createElement('div');  // Added Country container
countryContainer.style = 'display: flex; align-items: center; gap: 10px;';
countryL.innerText = 'Country';
countryL.style = ` margin-right: 10px;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen-Sans, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', 'sans-serif';
`;
countryS.id = 'country';
countryS.className = 'country-dropdown';  // Set className for Country select
countryS.style = `
  flex: 1;
  padding: 12px 20px;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
   height: 30px;
`;
country_option.value = dynamicCountryName;
country_option.innerText = dynamicCountryName;
countryS.appendChild(country_option);
countryContainer.appendChild(countryL);
countryContainer.appendChild(countryS);

submit.style = `
  width: 80px;
  background-color: ${hexColor};
  color: white;
  padding: 4px 1px;
  margin: 25px 0 0 0;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  background-color: rgb(12, 140, 170);
`;
submit.innerText = 'Start Chat';
submit.onclick = toggleMessenger;

cancel.style = `
  width: 80px;
  background-color: #f44336;
  color: white;
  padding: 4px 1px;
  margin: 25px 0 0 10px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
`;
cancel.innerText = 'Cancel';
cancel.onclick = closeLauncher;




const buttonContainer = document.createElement('div');
buttonContainer.style = 'display: flex; justify-content: space-between;';
buttonContainer.appendChild(submit);
buttonContainer.appendChild(cancel);

form.appendChild(productContainer);
form.appendChild(hcpContainer);
form.appendChild(countryContainer);  // Append Country container to form
form.appendChild(buttonContainer);  // Append button container to form
input.appendChild(form);

document.body.appendChild(input);

}else if(dynamicCountryName === "BRAZIL" || dynamicCountryName ===  "PERU" || dynamicCountryName === "MEXICO"){


// Create Input Form
const input = document.createElement('div');
const header = document.createElement('div');
const title = document.createElement('p');
const minButton = document.createElement('button');
const closeButton = document.createElement('button'); // Added closeButton
const form = document.createElement('div');

const productL = document.createElement('label');
const productS = document.createElement('select');
const option1 = document.createElement('option');

const hcpL = document.createElement('label');
const hcpS = document.createElement('select');
const hcp_option1 = document.createElement('option');
const hcp_option2 = document.createElement('option');
const countryL = document.createElement('label');  // Added Country label
const countryS = document.createElement('select');  // Added Country select
const country_option = document.createElement('option');  // Added Country option
const submit = document.createElement('button');
const cancel = document.createElement('button'); // Added Cancel button

input.id = 'input';
input.hidden = true;
input.style = `
  box-shadow: rgba(0, 0, 0, 0.2) 0px 3px 5px -2px, rgba(0, 0, 0, 0.14) 0px 1px 4px 2px, rgba(0, 0, 0, 0.12) 0px 1px 4px 1px;
  position: fixed !important;
  bottom: 30px !important;
  width: inherit;
  height: 315px;
  right: 30px !important;
  background-color: white;
  z-index: 99999;
  margin-right:25px;
  margin-bottom:-35px;
`;
header.style = `
  display: inline-flex;
  background-color: ${hexColor};
  color: white;
  font-size: 1.33929rem;
  line-height: 2.6;
  font-weight: 400;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen-Sans, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', 'sans-serif';
  width: 100%;
  height: 60px;
  background-color: rgb(12, 140, 170);
`;
title.style = `
  margin: 0;
  padding-left: 25px;
  font-size:80%;
`;
title.innerText = 'Web Messenger';
minButton.style = `
  position: absolute;
  width: 50px;
  right: 8px;
  top: 15px;
  cursor: pointer;
  filter: invert(1);
  border: 0;
  background-color: transparent;
`;
minButton.onclick = closeLauncher;
minButton.tabIndex = 0;
minButton.ariaLabel = 'Minimize the Messenger';
minButton.innerHTML = `
  <svg id="svgid" viewBox="0 0 24 24" style="width: 26px; height: 26px; margin-top: -32px;margin-left: -40px;">
    <title>window-minimize</title>
    <path d="M19 13H5v-2h14v2z"></path>
  </svg>`;
header.appendChild(title);
header.appendChild(minButton);
input.appendChild(header);

closeButton.style = `
  position: absolute;
  width: 50px;
  right: 8px;
  top: 15px;
  cursor: pointer;
  filter: invert(1);
  border: 0;
  background-color: transparent;
`;
closeButton.onclick = closeLauncher;
closeButton.tabIndex = 0;
closeButton.ariaLabel = 'Close the Messenger';
closeButton.innerHTML = `
  <svg id="svgid" viewBox="0 0 24 24" style="width: 26px; height: 26px; margin-top: -32px; margin-left: -10px;">
    <title>window-close</title>
    <path d="M18 6L6 18M6 6l12 12"></path>
  </svg>`;
header.appendChild(closeButton); // Append closeButton to header
input.appendChild(header);

form.style = 'padding: 25px; display: flex; flex-direction: column; gap: 10px;';

const productContainer = document.createElement('div');
productContainer.style = 'display: flex; align-items: center; gap: 10px;';
productL.innerText = 'Subject';
productL.style = `margin-right: 15px;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen-Sans, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', 'sans-serif';
`;
productS.id = 'product';
productS.style = `
  flex: 1;
  padding: 12px 20px;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
  height: 30px;
`;
option1.value = 'MEDICAL';
option1.innerText = 'MEDTECH';

productS.appendChild(option1);

productContainer.appendChild(productL);
productContainer.appendChild(productS);

const hcpContainer = document.createElement('div');
hcpContainer.style = 'display: flex; align-items: center; ';
hcpL.innerText = 'Franchise';
hcpL.style = `margin-right: 12px;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen-Sans, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', 'sans-serif';
`;
hcpS.id = 'hcp';
hcpS.style = `
  flex: 1;
  padding: 12px 20px;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
`;

hcp_option1.value = 'ETHICON';
hcp_option1.innerText = 'ETHICON';
hcp_option2.value = 'DEPUY';
hcp_option2.innerText = 'DEPUY';
hcpS.appendChild(hcp_option1);
hcpS.appendChild(hcp_option2);
hcpContainer.appendChild(hcpL);
hcpContainer.appendChild(hcpS);

const countryContainer = document.createElement('div');  // Added Country container
countryContainer.style = 'display: flex; align-items: center; gap: 10px;';
countryL.innerText = 'Country';
countryL.style = ` margin-right: 10px;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen-Sans, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', 'sans-serif';
`;
countryS.id = 'country';
countryS.className = 'country-dropdown';  // Set className for Country select
countryS.style = `
  flex: 1;
  padding: 12px 20px;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
   height: 30px;
`;
country_option.value = dynamicCountryName;
country_option.innerText = dynamicCountryName;
countryS.appendChild(country_option);
countryContainer.appendChild(countryL);
countryContainer.appendChild(countryS);

submit.style = `
  width: 80px;
  background-color: ${hexColor};
  color: white;
  padding: 4px 1px;
  margin: 25px 0 0 0;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  background-color: rgb(12, 140, 170);
`;
submit.innerText = 'Start Chat';
submit.onclick = toggleMessenger;

cancel.style = `
  width: 80px;
  background-color: #f44336;
  color: white;
  padding: 4px 1px;
  margin: 25px 0 0 10px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
`;
cancel.innerText = 'Cancel';
cancel.onclick = closeLauncher;




const buttonContainer = document.createElement('div');
buttonContainer.style = 'display: flex; justify-content: space-between;';
buttonContainer.appendChild(submit);
buttonContainer.appendChild(cancel);

form.appendChild(productContainer);
form.appendChild(hcpContainer);
form.appendChild(countryContainer);  // Append Country container to form
form.appendChild(buttonContainer);  // Append button container to form
input.appendChild(form);

document.body.appendChild(input);
// Add CSS for pointer-events: none !important;
const style = document.createElement('style');
style.innerHTML = `
  .country-dropdown {
    pointer-events: none !important;
  }
`;
document.head.appendChild(style);

}else{

// Create Input Form
const input = document.createElement('div');
const header = document.createElement('div');
const title = document.createElement('p');
const minButton = document.createElement('button');
const closeButton = document.createElement('button'); // Added closeButton
const form = document.createElement('div');

const productL = document.createElement('label');
const productS = document.createElement('select');
const option1 = document.createElement('option');

const hcpL = document.createElement('label');
const hcpS = document.createElement('select');
const hcp_option1 = document.createElement('option');

const countryL = document.createElement('label');  // Added Country label
const countryS = document.createElement('select');  // Added Country select
const country_option = document.createElement('option');  // Added Country option
const submit = document.createElement('button');
const cancel = document.createElement('button'); // Added Cancel button

input.id = 'input';
input.hidden = true;
input.style = `
  box-shadow: rgba(0, 0, 0, 0.2) 0px 3px 5px -2px, rgba(0, 0, 0, 0.14) 0px 1px 4px 2px, rgba(0, 0, 0, 0.12) 0px 1px 4px 1px;
  position: fixed !important;
  bottom: 30px !important;
  width: inherit;
  height: 315px;
  right: 30px !important;
  background-color: white;
  z-index: 99999;
  margin-right:25px;
  margin-bottom:-35px;
`;
header.style = `
  display: inline-flex;
  background-color: ${hexColor};
  color: white;
  font-size: 1.33929rem;
  line-height: 2.6;
  font-weight: 400;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen-Sans, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', 'sans-serif';
  width: 100%;
  height: 60px;
  background-color: rgb(12, 140, 170);
`;
title.style = `
  margin: 0;
  padding-left: 25px;
  font-size:80%;
`;
title.innerText = 'Web Messenger';
minButton.style = `
  position: absolute;
  width: 50px;
  right: 8px;
  top: 15px;
  cursor: pointer;
  filter: invert(1);
  border: 0;
  background-color: transparent;
`;
minButton.onclick = closeLauncher;
minButton.tabIndex = 0;
minButton.ariaLabel = 'Minimize the Messenger';
minButton.innerHTML = `
  <svg id="svgid" viewBox="0 0 24 24" style="width: 26px; height: 26px; margin-top: -32px;margin-left: -40px;">
    <title>window-minimize</title>
    <path d="M19 13H5v-2h14v2z"></path>
  </svg>`;
header.appendChild(title);
header.appendChild(minButton);
input.appendChild(header);

closeButton.style = `
  position: absolute;
  width: 50px;
  right: 8px;
  top: 15px;
  cursor: pointer;
  filter: invert(1);
  border: 0;
  background-color: transparent;
`;
closeButton.onclick = closeLauncher;
closeButton.tabIndex = 0;
closeButton.ariaLabel = 'Close the Messenger';
closeButton.innerHTML = `
  <svg id="svgid" viewBox="0 0 24 24" style="width: 26px; height: 26px; margin-top: -32px; margin-left: -10px;">
    <title>window-close</title>
    <path d="M18 6L6 18M6 6l12 12"></path>
  </svg>`;
header.appendChild(closeButton); // Append closeButton to header
input.appendChild(header);

form.style = 'padding: 25px; display: flex; flex-direction: column; gap: 10px;';

const productContainer = document.createElement('div');
productContainer.style = 'display: flex; align-items: center; gap: 10px;';
productL.innerText = 'Subject';
productL.style = `margin-right: 15px;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen-Sans, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', 'sans-serif';
`;
productS.id = 'product';
productS.style = `
  flex: 1;
  padding: 12px 20px;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
  height: 30px;
`;
option1.value = 'MEDICAL';
option1.innerText = 'MEDTECH';

productS.appendChild(option1);

productContainer.appendChild(productL);
productContainer.appendChild(productS);

const hcpContainer = document.createElement('div');
hcpContainer.style = 'display: flex; align-items: center; ';
hcpL.innerText = 'Franchise';
hcpL.style = `margin-right: 12px;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen-Sans, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', 'sans-serif';
`;
hcpS.id = 'hcp';
hcpS.style = `
  flex: 1;
  padding: 12px 20px;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
`;

hcp_option1.value = 'ETHICON';
hcp_option1.innerText = 'ETHICON';

hcpS.appendChild(hcp_option1);

hcpContainer.appendChild(hcpL);
hcpContainer.appendChild(hcpS);

const countryContainer = document.createElement('div');  // Added Country container
countryContainer.style = 'display: flex; align-items: center; gap: 10px;';
countryL.innerText = 'Country';
countryL.style = ` margin-right: 10px;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen-Sans, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', 'sans-serif';
`;
countryS.id = 'country';
countryS.className = 'country-dropdown';  // Set className for Country select
countryS.style = `
  flex: 1;
  padding: 12px 20px;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
   height: 30px;
`;
country_option.value = dynamicCountryName;
country_option.innerText = dynamicCountryName;
countryS.appendChild(country_option);
countryContainer.appendChild(countryL);
countryContainer.appendChild(countryS);

submit.style = `
  width: 80px;
  background-color: ${hexColor};
  color: white;
  padding: 4px 1px;
  margin: 25px 0 0 0;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  background-color: rgb(12, 140, 170);
`;
submit.innerText = 'Start Chat';
submit.onclick = toggleMessenger;

cancel.style = `
  width: 80px;
  background-color: #f44336;
  color: white;
  padding: 4px 1px;
  margin: 25px 0 0 10px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
`;
cancel.innerText = 'Cancel';
cancel.onclick = closeLauncher;




const buttonContainer = document.createElement('div');
buttonContainer.style = 'display: flex; justify-content: space-between;';
buttonContainer.appendChild(submit);
buttonContainer.appendChild(cancel);

form.appendChild(productContainer);
form.appendChild(hcpContainer);
form.appendChild(countryContainer);  // Append Country container to form
form.appendChild(buttonContainer);  // Append button container to form
input.appendChild(form);

document.body.appendChild(input);
// Add CSS for pointer-events: none !important;
const style = document.createElement('style');
style.innerHTML = `
  .country-dropdown {
    pointer-events: none !important;
  }
`;
document.head.appendChild(style);


}

</script>

</c:if>

<c:if test="${isPHRTrue}">
<script type="text/javascript">

        let languageToSend;
        let currentCountryCode = "${countryCode}";
        let currentLanguageCode = "${currentLanguage.isocode}";

        var countryMapping = {
            "BR": "BRAZIL",
            "MX": "MEXICO",
            "PE": "PERU",
            "AR": "ARGENTINA",
            "CL": "CHILE",
            "CO": "COLOMBIA",
            "CR": "COSTA RICA",
            "EC": "ECUADOR",
            "PA": "CENCA",
            "PR": "PUERTO RICO",
            "UY": "URUGUAY"
        };

        var languageMapping = {
            "en": "ENGLISH",
            "pt": "PORTUGUESE",
            "es": "SPANISH"
        };

        let dynamicLanguage = languageMapping[currentLanguageCode];
        let dynamicCountryName = countryMapping[currentCountryCode];

        if(dynamicCountryName === "BRAZIL"){
            languageToSend = "PORTUGUESE";
        }else if(dynamicCountryName === "CENCA" && dynamicLanguage === "ENGLISH"){
            languageToSend = "ENGLISH";
        }else if(dynamicCountryName === "PUERTO RICO" && dynamicLanguage === "ENGLISH"){
            languageToSend = "ENGLISH";
        }else{
            languageToSend = "SPANISH";
        }


// Variables to change in your deployment
var deploymentId = '${genesysDeploymentKey}';  // Your WebMessenger DeploymentId
var sessionkey = '_'+ deploymentId + ':gcmcsessionActive';

const hexColor = '#0C8CAA';  // Color theme

function toggleMessenger() {
  Genesys('command', 'Messenger.open', {}, function (o) {
    closeLauncher();
    sessionStorage.setItem('UserEmail', '${UserEmail}');
	  sessionStorage.setItem('languageref', languageToSend);
    sessionStorage.setItem('email', '${user.email}');
    sessionStorage.setItem('googleUserAccountNumber', '${googleUserAccountNumber}');
    sessionStorage.setItem('googleUserSector', '${googleUserSector}');
    sessionStorage.setItem('firstName', '${user.firstName}');
    sessionStorage.setItem('lastName', '${user.lastName}');
    sessionStorage.setItem("Subject", document.getElementById('product').value);
    sessionStorage.setItem("Franchise", document.getElementById('hcp').value);
    sessionStorage.setItem("Country", document.getElementById('country').value);

    Genesys('command', 'Database.set', {
      messaging: {
        customAttributes: {
		  type: '${UserEmail}',
          Language: languageToSend,
          Account: '${googleUserAccountNumber}',
          Sector: '${googleUserSector}',
          email: '${user.email}',
          FirstName: '${user.firstName}',
          LastName: '${user.lastName}',
          Subject: document.getElementById('product').value,
          Franchise: document.getElementById('hcp').value,
          Country: document.getElementById('country').value  // Added Country
        },
        markdown: true
      }
    });
  }, function (o) {
    Genesys('command', 'Messenger.close');
  });
}


let restoredRan = false;
let disconnected = false;

Genesys("subscribe", "MessagingService.conversationDisconnected", function(){
  
   disconnected = true;
  Genesys('command', 'Database.set', {
    messaging: {
       customAttributes: {
		        type: sessionStorage.getItem('UserEmail'),
             Language: sessionStorage.getItem('languageref'),
             Account: sessionStorage.getItem('googleUserAccountNumber'),
             Sector: sessionStorage.getItem('googleUserSector'),
             email: sessionStorage.getItem('email'),
             FirstName: sessionStorage.getItem('firstName'),
             LastName: sessionStorage.getItem('lastName'),
			       Subject:sessionStorage.getItem("Subject"),
             Franchise:sessionStorage.getItem("Franchise"),
             Country:sessionStorage.getItem("Country")
        },
    },
  }) 
     
  });
  
Genesys('subscribe', 'MessagingService.restored', () => {
   if(restoredRan || !disconnected) {
      return;
   }
   Genesys("command", "MessagingService.clearConversation");
   restoredRan = true;
});
 
Genesys('subscribe', 'Conversations.started', () => {
  disconnected = false;
});
  

function closeLauncher() {
  const input = document.getElementById('input');
  input.hidden = true;
  console.log('Hiding...');
}

function openLauncher() {


  console.log("k",sessionkey)
  const session = JSON.parse(localStorage.getItem(sessionkey));
  
  const input = document.getElementById('input');
  console.log(session?.value);
  if (session?.value) {
    console.log('Opening Widget...');
    Genesys('command', 'Messenger.open', {}, function (o) {
      closeLauncher();
    }, function (o) {
      Genesys('command', 'Messenger.close');
    });
  } else {
    console.log('Showing...');
    input.hidden = false;
  }
}
// Create Input Form
const input = document.createElement('div');
const header = document.createElement('div');
const title = document.createElement('p');
const minButton = document.createElement('button');
const closeButton = document.createElement('button'); // Added closeButton
const form = document.createElement('div');

const productL = document.createElement('label');
const productS = document.createElement('select');
const option1 = document.createElement('option');

const hcpL = document.createElement('label');
const hcpS = document.createElement('select');
const hcp_option1 = document.createElement('option');

const countryL = document.createElement('label');  // Added Country label
const countryS = document.createElement('select');  // Added Country select
const country_option = document.createElement('option');  // Added Country option
const submit = document.createElement('button');
const cancel = document.createElement('button'); // Added Cancel button

input.id = 'input';
input.hidden = true;
input.style = `
  box-shadow: rgba(0, 0, 0, 0.2) 0px 3px 5px -2px, rgba(0, 0, 0, 0.14) 0px 1px 4px 2px, rgba(0, 0, 0, 0.12) 0px 1px 4px 1px;
  position: fixed !important;
  bottom: 30px !important;
  width: inherit;
  height: 315px;
  right: 30px !important;
  background-color: white;
  z-index: 99999;
  margin-right:25px;
  margin-bottom:-35px;
`;
header.style = `
  display: inline-flex;
  background-color: ${hexColor};
  color: white;
  font-size: 1.33929rem;
  line-height: 2.6;
  font-weight: 400;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen-Sans, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', 'sans-serif';
  width: 100%;
  height: 60px;
  background-color: rgb(12, 140, 170);
`;
title.style = `
  margin: 0;
  padding-left: 25px;
  font-size:80%;
`;
title.innerText = 'Web Messenger';
minButton.style = `
  position: absolute;
  width: 50px;
  right: 8px;
  top: 15px;
  cursor: pointer;
  filter: invert(1);
  border: 0;
  background-color: transparent;
`;
minButton.onclick = closeLauncher;
minButton.tabIndex = 0;
minButton.ariaLabel = 'Minimize the Messenger';
minButton.innerHTML = `
  <svg id="svgid" viewBox="0 0 24 24" style="width: 26px; height: 26px; margin-top: -32px;margin-left: -40px;">
    <title>window-minimize</title>
    <path d="M19 13H5v-2h14v2z"></path>
  </svg>`;
header.appendChild(title);
header.appendChild(minButton);
input.appendChild(header);

closeButton.style = `
  position: absolute;
  width: 50px;
  right: 8px;
  top: 15px;
  cursor: pointer;
  filter: invert(1);
  border: 0;
  background-color: transparent;
`;
closeButton.onclick = closeLauncher;
closeButton.tabIndex = 0;
closeButton.ariaLabel = 'Close the Messenger';
closeButton.innerHTML = `
  <svg id="svgid" viewBox="0 0 24 24" style="width: 26px; height: 26px; margin-top: -32px; margin-left: -10px;">
    <title>window-close</title>
    <path d="M18 6L6 18M6 6l12 12"></path>
  </svg>`;
header.appendChild(closeButton); // Append closeButton to header
input.appendChild(header);

form.style = 'padding: 25px; display: flex; flex-direction: column; gap: 10px;';

const productContainer = document.createElement('div');
productContainer.style = 'display: flex; align-items: center; gap: 10px;';
productL.innerText = 'Subject';
productL.style = `margin-right: 15px;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen-Sans, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', 'sans-serif';
`;
productS.id = 'product';
productS.style = `
  flex: 1;
  padding: 12px 20px;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
  height: 30px;
`;
option1.value = 'PHARMA';
option1.innerText = 'INNOVATIVE MEDICINE';

productS.appendChild(option1);

productContainer.appendChild(productL);
productContainer.appendChild(productS);

const hcpContainer = document.createElement('div');
hcpContainer.style = 'display: flex; align-items: center; ';
hcpL.innerText = 'Franchise';
hcpL.style = `margin-right: 12px;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen-Sans, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', 'sans-serif';
`;
hcpS.id = 'hcp';
hcpS.style = `
  flex: 1;
  padding: 12px 20px;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
`;

hcp_option1.value = 'PHARMA';
hcp_option1.innerText = 'INNOVATIVE MEDICINE';

hcpS.appendChild(hcp_option1);

hcpContainer.appendChild(hcpL);
hcpContainer.appendChild(hcpS);

const countryContainer = document.createElement('div');  // Added Country container
countryContainer.style = 'display: flex; align-items: center; gap: 10px;';
countryL.innerText = 'Country';
countryL.style = ` margin-right: 10px;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen-Sans, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', 'sans-serif';
`;
countryS.id = 'country';
countryS.style = `
  flex: 1;
  padding: 12px 20px;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
   height: 30px;
`;
country_option.value = dynamicCountryName;
country_option.innerText = dynamicCountryName;
countryS.appendChild(country_option);
countryContainer.appendChild(countryL);
countryContainer.appendChild(countryS);

submit.style = `
  width: 80px;
  background-color: ${hexColor};
  color: white;
  padding: 4px 1px;
  margin: 25px 0 0 0;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  background-color: rgb(12, 140, 170);
`;
submit.innerText = 'Start Chat';
submit.onclick = toggleMessenger;

cancel.style = `
  width: 80px;
  background-color: #f44336;
  color: white;
  padding: 4px 1px;
  margin: 25px 0 0 10px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
`;
cancel.innerText = 'Cancel';
cancel.onclick = closeLauncher;

const buttonContainer = document.createElement('div');
buttonContainer.style = 'display: flex; justify-content: space-between;';
buttonContainer.appendChild(submit);
buttonContainer.appendChild(cancel);

form.appendChild(productContainer);
form.appendChild(hcpContainer);
form.appendChild(countryContainer);  // Append Country container to form
form.appendChild(buttonContainer);  // Append button container to form
input.appendChild(form);

document.body.appendChild(input);

// Add CSS for pointer-events: none !important;
const style = document.createElement('style');
style.innerHTML = `
  .dropdown.bootstrap-select {
    pointer-events: none !important;
  }
`;
document.head.appendChild(style);



</script>

</c:if>
