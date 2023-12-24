var lat_value, lon_value;
var user_data = null;

function displayLoginPage() {
    window.open('login.html', '_self');
}

function redirectToPage(page_html) {
    console.log("page_html");
    window.open(page_html + ".html", "_self");
}

function createTableFromJSON(data) {
    var html = "<table><tr><th>Category</th><th>Value</th></tr>";
    for (const x in data) {
        var category = x;
        var value = data[x];
        html += "<tr><td>" + category + "</td><td>" + value + "</td></tr>";
    }
    html += "</table>";
    return html;

}

function defineLabelName(cookie_key) {
    var new_cookie_key = cookie_key.charAt(0).toUpperCase() + cookie_key.slice(1);
    if (new_cookie_key === "Firstname") {
        new_cookie_key = "First Name";
    } else if (new_cookie_key === "Lastname") {
        new_cookie_key = "Last Name";
    } else if (new_cookie_key === "Personalpage") {
        new_cookie_key = "Personal Page";
    } else if (new_cookie_key === "Propertydescription") {
        new_cookie_key = "Property Description";
    } else if (new_cookie_key === "Catkeeper") {
        new_cookie_key = "Cat Keeper";
    } else if (new_cookie_key === "Dogkeeper") {
        new_cookie_key = "Dog Keeper";
    } else if (new_cookie_key === "Catprice") {
        new_cookie_key = "Cat Price";
    } else if (new_cookie_key === "Dogprice") {
        new_cookie_key = "Dog Price";
    }
    return new_cookie_key;
}

function updateValues(){
    const updatedCookies = getAllCookiePairs();
    
    for (const cookie_key in updatedCookies) {
        if (updatedCookies.hasOwnProperty(cookie_key)) {
            const inputElement = document.querySelector(`.${cookie_key} input`);
           
            if (inputElement) {
                var inputValue = inputElement.value;
                if (cookie_key === "catkeeper" || cookie_key === "dogkeeper"){
                   inputValue = inputElement.checked;
                }
                if (updatedCookies[cookie_key] !== inputValue) {
                    updatedCookies[cookie_key] = inputValue;
                }
            }
        }
    }
    cookies = updatedCookies;
    
    var path = 'UpdatePetOwner';
    if(cookies.hasOwnProperty("catkeeper")){
        path = 'UpdatePetKeeper';
    }
    const xhr = new XMLHttpRequest();
        xhr.onload = function () {
            if (xhr.status !== 200) {
                displayErrorMessage("Error " + xhr.status + " - " + xhr.responseText);
            } else {
                var account_data = document.getElementById('account-data');
                account_data.innerHTML ="";
                var buttons = document.getElementById('buttons');
                buttons.innerHTML ="";
                account_data.innerHTML ='<div class="container" style="text-align: center; padding: 20px; margin-top:1%; margin-left:-25%; background-color:white;padding-top:30%;padding-bottom:30%; border: 2px solid rgb(46, 35, 53);">'+
                        '<h3> Your account has been successfully updated.</h3>'+
                       '</div>';
            }
        };
        
        xhr.open('POST', path);
        xhr.setRequestHeader("Accept", "application/json");
        xhr.setRequestHeader("Content-Type", "application/json");
        xhr.send(JSON.stringify(cookies));
    
}
function myAccount() {
    cookies = getAllCookiePairs();
    const disabledKeys = ["username", "email"];
    const dataDiv = document.getElementById('account-data');

    for (const cookie_key in cookies) {
        if (cookies.hasOwnProperty(cookie_key)) {
            const container = document.createElement('div');
            container.style.display = 'flex'; 
            container.classList.add(`${cookie_key}`);
           
            const label = document.createElement('label');
            var label_name = defineLabelName(`${cookie_key}`);

            label.textContent = `${label_name}:`;

            const input = document.createElement('input');
            input.type = 'text';
            var cookie_value = cookies[cookie_key];
            if (label_name === "Address" || label_name === "Property Description") {
                let new_value = cookie_value.replace(/\+/g, '%20');
                cookie_value = decodeURIComponent(new_value);
            }

            if (label_name === 'Birthdate') {
                input.type = "date";
            }
            
            if (label_name === 'Cat Keeper' || label_name === 'Dog Keeper') {
                input.type = "radio";
                if (cookie_value === "true"){
                    input.checked = true;
                }else{
                    input.checked = false;
                }
            }

            input.value = cookie_value;

            if (disabledKeys.includes(cookie_key)) {
                input.disabled = true;
            }

            input.addEventListener('input', (e) => {
                cookies[cookie_key] = e.target.value;
            });
            container.appendChild(label);
            container.appendChild(input);
            dataDiv.appendChild(container);
        }
    }
}


function getAllCookiePairs() {
    const cookieString = document.cookie;
    const cookiePairs = cookieString.split(';');

    const result = {};

    cookiePairs.forEach(pair => {
        const [key, value] = pair.trim().split('=');
        result[key] = value;
    });

    return result;
}

function login() {
    var jsonData = JSON.stringify(
            {
                username: document.getElementById("username").value,
                password: document.getElementById("password").value
            }
    );

    const xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            window.open('welcome_page.html', '_self');
        } else {
            displayErrorMessage("Wrong credentials. Make sure you are registered and check your username and password again.");
        }
    };

    xhr.open("POST", "Login");
    xhr.setRequestHeader("Accept", "application/json");
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send(jsonData);
}

function isLoggedIn() {
    var logform = document.getElementById("loginForm");
    var cookies = getAllCookiePairs();
    if (cookies.hasOwnProperty("username")) {
        window.open('welcome_page.html', '_self');
        return;
    }

    if (logform) {
        logform.addEventListener("submit", (e) => {
            login();
            e.preventDefault();
        });
    } else {
        console.log("error!!!");
    }


}

function logout() {
    var jsonData = JSON.stringify(
            {
            }
    );


    const xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            window.location.href = 'login.html';
            user_data = null;
        } else {
            console.log("error");
        }
    };

    xhr.open("POST", "Logout");
    xhr.setRequestHeader("Accept", "application/json");
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send(jsonData);
}

function config() {
    var regform = document.getElementById("regform");

    if (regform) {
        regform.addEventListener("submit", (e) => {
            validateForm();
            e.preventDefault();

        });
    } else {
        console.log("error!!!");
    }
}
function checkPasswords() {
    const password = document.getElementById("password").value;
    const password_confirmation = document.getElementById(
            "password-confirmation"
            ).value;

    if (password !== password_confirmation) {
        $("#message").css("color", "crimson");
        document.getElementById("confirm_message").innerHTML =
                "Passwords do not match!";
        document.getElementById("message").innerHTML = "Passwords do not match!";
        return;
    }

    document.getElementById("message").innerHTML = "";
    document.getElementById("confirm_message").innerHTML = "";
}

function invalid_password() {
    const words = ["cat", "dog", "gata", "skulos"];
    var password = document.getElementById("password").value;
    for (const word of words) {
        if (password.includes(word)) {
            document.getElementById("message").innerHTML =
                    "Password must not contain the word: " + word + "!";
            return false;
        }
    }
}

function checkStrength() {
    var password = document.getElementById("password").value;
    let specialChars = /[`!@#$%^&*()_\-+=\[\]{};':"\\|,.<>\/?~ ]/.test(password);
    let capitalLetters = /[A-Z]/.test(password);
    let lowercaseLetters = password.toUpperCase() !== password;
    var number = 0;
    for (var i = 0; i < password.length; i++) {
        if (isNaN(password.charAt(i))) {
            continue;
        }
        number = number + 1;
    }
    max_value = password.length / 2;
    if (number >= max_value) {
        document.getElementById("message").innerHTML = "Weak password";
        $("#message").css("color", "red");
        return false;
    }
    if (invalid_password() === "false") {
        return false;
    }

    if (specialChars && capitalLetters && lowercaseLetters && number >= 1) {
        document.getElementById("message").innerHTML = "Strong password";
        $("#message").css("color", "green");
        return true;
    }
    document.getElementById("message").innerHTML = "Medium password";
    $("#message").css("color", "orange");
    return true;
}

function getGender() {
    var gender_checked = document.querySelector('input[name="genre"]:checked');

    if (gender_checked === null) {
        return "null";
    }

    var gender_value = gender_checked.value;
    return gender_value.charAt(0).toUpperCase() + gender_value.slice(1);
}

function getProperty() {
    var property_checked = document.querySelector('input[name="property"]:checked');

    if (property_checked === null) {
        return;
    }

    type = property_checked.value;
    return type;
}

function checkType() {
    var keeper_checked = document.querySelector('input[name="type"]:checked');

    if (keeper_checked === null) {
        return;
    }

    type = keeper_checked.value;
    if (type === "keeper") {
        document.getElementById("pet-keeper").style.display = "block";
        return keeper_checked;
    }

    document.getElementById("pet-keeper").style.display = "none";
    return keeper_checked;
}

function checkKeeperType() {
    var keeper_checked = document.querySelector('input[name="keeper-type"]:checked').value;
    return keeper_checked;
}

function selectPrice() {
    var keeper_type = document.querySelector('input[name = "keeper-type"]:checked');

    if (keeper_type === null) {
        return;
    }

    type = keeper_type.value;
    if (type === "cat-keeper") {
        document.getElementById("catprice").setAttribute("required", "");
        document.getElementById("dogprice").removeAttribute("required");
        return;
    }

    if (type === "dog-keeper") {
        document.getElementById("dogprice").setAttribute("required", "");
        document.getElementById("catprice").removeAttribute("required");
        return;
    }

    document.getElementById("dogprice").setAttribute("required", "");
    document.getElementById("catprice").setAttribute("required", "");
}

function definePet() {
    var property = document.querySelector('input[name = "property"]:checked');

    if (property === null) {
        return;
    }

    property_value = property.value;
    if (property_value === "outdoor") {
        document.getElementById("cat-keeper").style.display = "none";
        document.getElementById("cat-keeper-label").style.display = "none";
        document.getElementById("cat-dog-keeper").style.display = "none";
        document.getElementById("cat-dog-keeper-label").style.display = "none";
        document.getElementById("catprice-label").style.display = "none";
        document.getElementById("catprice").style.display = "none";
        return;
    }

    document.getElementById("cat-keeper").style.display = "flex";
    document.getElementById("cat-keeper-label").style.display = "flex";
    document.getElementById("cat-dog-keeper").style.display = "flex";
    document.getElementById("cat-dog-keeper-label").style.display = "flex";
    document.getElementById("catprice-label").style.display = "flex";
    document.getElementById("catprice").style.display = "flex";
}

function checkLocation() {
    var country = document.querySelector('select[name = "country"]');
    var city = document.querySelector('input[name = "city"]');
    var address = document.querySelector('input[name = "address"]');

    var country_value = country.value;
    var city_value = city.value;
    var address_value = address.value;

    document.getElementById("osm_message").innerHTML = "";

    if (country_value === 'OTH') {
        document.getElementById("country_message").innerHTML = "This service is only available to Greece.";
        return;
    }

    document.getElementById("country_message").innerHTML = "";

    const data = null;

    const xhr = new XMLHttpRequest();
    xhr.withCredentials = true;

    xhr.addEventListener("readystatechange", function () {
        if (this.readyState === this.DONE) {
            const jsonResults = JSON.parse(this.responseText);

            if (jsonResults[0] === undefined) {
                document.getElementById("osm_message").innerHTML = "This location could not be verified.";
                return;
            }

            if (jsonResults[0].display_name.includes('Heraklion Regional Unit') === false) {
                document.getElementById("osm_message").innerHTML = "This service is only available to the region of Heraklion.";
                return;
            }

            document.getElementById("osm_message").innerHTML = "";

            lat_value = jsonResults[0].lat;
            lon_value = jsonResults[0].lon;
        }
    });

    var address = address_value + " " + city_value + " " + country_value;
    xhr.open("GET", "https://forward-reverse-geocoding.p.rapidapi.com/v1/search?q=" + address + "&accept-language=en&polygon_threshold=0.0");

    xhr.setRequestHeader('X-RapidAPI-Key', '04c6c97612mshf9a7ac750c41d05p1793eejsnabd20a0b9528');
    xhr.setRequestHeader('X-RapidAPI-Host', 'forward-reverse-geocoding.p.rapidapi.com');
    xhr.send(data);
}

function validateUsername() {
    var jsonData = JSON.stringify({
        username: document.getElementById("username").value
    });

    const xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.status !== 200) {
            displayErrorMessage("Error " + xhr.status + " - " + xhr.responseText);
        }
    };
    xhr.open('POST', 'checkUsername');
    xhr.setRequestHeader("Accept", "application/json");
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send(jsonData);
}

function displayErrorMessage(errorMessage) {
    // Use SweetAlert to show the error message in a pop-up
    Swal.fire({
        icon: 'error',
        title: 'Oops...',
        text: errorMessage
    });
}
function validateEmail() {
    var jsonData = JSON.stringify({
        email: document.getElementById("email").value
    });

    const xhr = new XMLHttpRequest();

    xhr.onload = function () {
        if (xhr.status !== 200) {
            displayErrorMessage("Error " + xhr.status + " - " + xhr.responseText);
        }
    };

    xhr.open('POST', 'checkEmail');
    xhr.setRequestHeader("Accept", "application/json");
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send(jsonData);
}

function validateForm() {
    var validation = checkStrength();
    if (validation === true) {
        gender_value = getGender();
        var jsonData =
                {
                    username: document.getElementById("username").value,
                    email: document.getElementById("email").value,
                    password: document.getElementById("password").value,
                    firstname: document.getElementById("firstname").value,
                    lastname: document.getElementById("lastname").value,
                    birthdate: document.getElementById("birthdate").value,
                    gender: gender_value,
                    job: document.getElementById("job").value,
                    country: document.getElementById("country").value,
                    city: document.getElementById("city").value,
                    address: document.getElementById("address").value,
                    lat: lat_value,
                    lon: lon_value,
                    telephone: document.getElementById("telephone").value,
                    personalpage: document.getElementById("personalpage").value
                }
        ;
        var path = '';
        var keeper_checked = document.querySelector('input[name="type"]:checked');
        var type = keeper_checked.value;
        if (type === "keeper") {
            path = 'InsertPetKeeper';
            jsonData =
                    {
                        username: document.getElementById("username").value,
                        email: document.getElementById("email").value,
                        password: document.getElementById("password").value,
                        firstname: document.getElementById("firstname").value,
                        lastname: document.getElementById("lastname").value,
                        birthdate: document.getElementById("birthdate").value,
                        gender: gender_value,
                        job: document.getElementById("job").value,
                        country: document.getElementById("country").value,
                        city: document.getElementById("city").value,
                        address: document.getElementById("address").value,
                        lat: lat_value,
                        lon: lon_value,
                        telephone: document.getElementById("telephone").value,
                        personalpage: document.getElementById("personalpage").value,
                        property: getProperty().charAt(0).toUpperCase() + getProperty().slice(1),
                        propertydescription: document.getElementById("propertyDescription").value,
                        catkeeper: "cat-keeper" === checkKeeperType(),
                        dogkeeper: "dog-keeper" === checkKeeperType(),
                        catprice: document.getElementById("catprice").value,
                        dogprice: document.getElementById("dogprice").value
                    };

            if (checkKeeperType() === "cat-dog-keeper") {
                jsonData['catkeeper'] = true;
                jsonData['dogkeeper'] = true;
            }
        } else {
            path = 'InsertPetOwner';
        }

        const xhr = new XMLHttpRequest();
        xhr.onload = function () {
            if (xhr.status !== 200) {
                displayErrorMessage("Error " + xhr.status + " - " + xhr.responseText);
                setTimeout(5000);
                window.open('index.html', '_self');
            } else {
                window.open('success_register.html', '_self');
            }
        };
        xhr.open('POST', path);
        xhr.setRequestHeader("Accept", "application/json");
        xhr.setRequestHeader("Content-Type", "application/json");
        xhr.send(JSON.stringify(jsonData));
    }
    return validation;
}