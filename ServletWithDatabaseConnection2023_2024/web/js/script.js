var lat_value, lon_value;
var user_data = null;
function displayLoginPage() {
    window.open('login.html', '_self');
}
function addKeeperBookingCards() {
    let container = document.getElementById('card-container');
    let keepers_header = document.createElement('h3');
    keepers_header.textContent = 'Bookings';
    keepers_header.className = 'mt-4';
    container.appendChild(keepers_header);
    var bookings = JSON.parse(localStorage.getItem("bookings"));
    let categoryContainer = document.createElement('div');
    categoryContainer.className = 'category-container';
    bookings.forEach(entry => {
        container.appendChild(createBookingCard(entry));
    });
    container.appendChild(categoryContainer);
}
function getKeeperStats() {
    window.open("keeper_stats.html", "_self");
}

function createTableWithStats() {
    var main_element = document.getElementById("main-content");
    var stats = JSON.parse(localStorage.getItem("keeper-stats"));
    var table = document.createElement("table");
    table.className = "stats";
    var headerRow = table.insertRow(0);
    var headerCell1 = headerRow.insertCell(0);
    var headerCell2 = headerRow.insertCell(1);
    headerCell1.innerHTML = "<b>Total Bookings</b>";
    headerCell2.innerHTML = "<b>Total Days</b>";
    var row1 = table.insertRow(1);
    var row1_cell1 = row1.insertCell(0);
    var row1_cell2 = row1.insertCell(1);
    row1_cell1.innerHTML = stats["Total Bookings"];
    row1_cell2.innerHTML = stats["Total Days"];
    main_element.appendChild(table);
}

function createTableWithReviewStats() {
    var main_element = document.getElementById("main-content");
    var stats = JSON.parse(localStorage.getItem("keeper-review-stats"));
    let reviews_header = document.createElement('h3');
    reviews_header.textContent = 'Reviews';
    main_element.appendChild(reviews_header);
    var table = document.createElement("table");
    table.className = "review-stats";
    var headerRow = table.insertRow(0);
    var headerCell1 = headerRow.insertCell(0);
    var headerCell2 = headerRow.insertCell(1);
    headerCell1.innerHTML = "<b>Review Message</b>";
    headerCell2.innerHTML = "<b>Review Score</b>";
    var count = 0;
    for (var key in stats) {
        if (stats.hasOwnProperty(key)) {
            count++;
        }
    }
    for (var i = 1; i <= count; i++) {
        var row = table.insertRow(i);
        var cell1 = row.insertCell(0);
        var cell2 = row.insertCell(1);
        let review = (stats[(i - 1).toString()]);
//        let jsonReview = JSON.parse(review);
        cell1.innerHTML = review["message"];
        cell2.innerHTML = review["score"];
    }
    main_element.appendChild(table);
}
function retrieveStats() {
    var cookies = getAllCookiePairs();
    var jsonData = JSON.stringify(
            {
                keeper_id: cookies["keeper_id"]
            }
    );
    const xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            localStorage.setItem("keeper-stats", this.responseText);
        }
    };
    xhr.open("POST", "getKeepersBookingDetails");
    xhr.setRequestHeader("Accept", "application/json");
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send(jsonData);

    const xhr2 = new XMLHttpRequest();
    xhr2.onload = function () {
        if (xhr2.readyState === 4 && xhr2.status === 200) {
            localStorage.setItem("keeper-review-stats", xhr2.responseText);
            createTableWithStats();
            createTableWithReviewStats();
        }
    };
    xhr2.open("POST", "getKeeperReviews");
    xhr2.setRequestHeader("Accept", "application/json");
    xhr2.setRequestHeader("Content-Type", "application/json");
    xhr2.send(jsonData);
}
function ask_chatgpt(user_question) {
    var jsonData = JSON.stringify(
            {
                question: user_question
            }
    );
    const xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            console.log(this.responseText);
        }
    };
    xhr.open("POST", "askCHATGPT");
    xhr.setRequestHeader("Accept", "application/json");
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send(jsonData);
}

function askCHATGPT() {
    //ask_chatgpt();
    Swal.fire({
        title: 'Enter your message',
        html: '<input type="text" id="swal-input-field" class="swal2-input">',
        showCancelButton: true,
        confirmButtonText: 'Send',
        cancelButtonText: 'Cancel',
        confirmButtonColor: 'green',
        cancelButtonColor: 'brown',
        showLoaderOnConfirm: true,
        preConfirm: () => {
            const inputValue = document.getElementById('swal-input-field').value;
            return inputValue;
        },
        allowOutsideClick: () => !Swal.isLoading()
    }).then((result) => {
        if (result.isConfirmed) {
            ask_chatgpt(result.value);
        }

    });
}
function sendMessage(booking) {
    Swal.fire({
        title: 'Enter your message',
        html: '<input type="text" id="swal-input-field" class="swal2-input">',
        showCancelButton: true,
        confirmButtonText: 'Send',
        cancelButtonText: 'Cancel',
        confirmButtonColor: 'green',
        cancelButtonColor: 'brown',
        showLoaderOnConfirm: true,
        preConfirm: () => {
            const inputValue = document.getElementById('swal-input-field').value;
            return inputValue;
        },
        allowOutsideClick: () => !Swal.isLoading()
    }).then((result) => {
        if (result.isConfirmed) {
            Swal.fire({
                title: 'Message Sent',
                confirmButtonColor: 'brown',
                icon: 'success'
            });
        }
        var currentDate = new Date();
        var year = currentDate.getFullYear();
        var month = ('0' + (currentDate.getMonth() + 1)).slice(-2);
        var day = ('0' + currentDate.getDate()).slice(-2);
        var hours = ('0' + currentDate.getHours()).slice(-2);
        var minutes = ('0' + currentDate.getMinutes()).slice(-2);
        var seconds = ('0' + currentDate.getSeconds()).slice(-2);
        var formattedDate = year + '-' + month + '-' + day + ' ' + hours + ':' + minutes + ':' + seconds;
        var sender = "owner";
        var path = "sendMessageToKeeper";
        if (`${booking.sender}` === 'owner') {
            sender = "keeper";
            path = "sendMessageToOwner";
        }
        var jsonData = JSON.stringify(
                {
                    booking_id: `${booking.booking_id}`,
                    message: result.value,
                    sender: sender,
                    datetime: formattedDate
                }
        );
        const xhr = new XMLHttpRequest();
        xhr.onload = function () {
            if (xhr.readyState === 4 && xhr.status === 200) {
                ;
            } else {
                displayErrorMessage("Message could not be sent.");
            }
        };
        xhr.open("POST", path);
        xhr.setRequestHeader("Accept", "application/json");
        xhr.setRequestHeader("Content-Type", "application/json");
        xhr.send(jsonData);
    });
}

function createBookingCard(booking) {
    let card = document.createElement('div');
    card.className = 'card my-2';
    let fromdate = document.createElement('p');
    fromdate.textContent = `From Date: ${booking.fromdate}`;
    card.appendChild(fromdate);
    let todate = document.createElement('p');
    todate.textContent = `To Date: ${booking.todate}`;
    card.appendChild(todate);
    let pet_id = document.createElement('p');
    pet_id.textContent = `Pet Id: ${booking.pet_id}`;
    card.appendChild(pet_id);
    let owner_id = document.createElement('p');
    owner_id.textContent = `Owner Id: ${booking.owner_id}`;
    card.appendChild(owner_id);
    let price = document.createElement('p');
    price.textContent = `Price: ${booking.price}`;
    card.appendChild(price);
    let status_cookie = document.createElement('p');
    status_cookie.textContent = `Status: ${booking.status}`;
    console.log(status_cookie.textContent);
    card.appendChild(status_cookie);
    if (status_cookie.textContent === "Status: accepted") {
        let cardButton = document.createElement('button');
        cardButton.className = 'card-button';
        cardButton.textContent = 'Ask CHATGPT';
        cardButton.onclick = function () {
            askCHATGPT();
        };
        card.appendChild(cardButton);
        let cardButton2 = document.createElement('button');
        cardButton2.className = 'card-button-message';
        cardButton2.textContent = 'Message Owner';
        cardButton2.onclick = function () {
            sendMessage(booking);
        };
        card.appendChild(cardButton2);
    }

    if (status_cookie.textContent === "Status: requested") {
        let cardButton = document.createElement('button');
        cardButton.className = 'card-button-accept';
        cardButton.textContent = 'Accept';
        cardButton.onclick = function () {
            updateBooking(`${booking.booking_id}`, "accepted");
        };
        card.appendChild(cardButton);
        let cardButton2 = document.createElement('button');
        cardButton2.className = 'card-button-reject';
        cardButton2.textContent = 'Reject';
        cardButton2.onclick = function () {
            updateBooking(`${booking.booking_id}`, "rejected");
        };
        card.appendChild(cardButton2);
    }

    return card;
}

function updateBooking(booking_id, status) {
    var jsonData = JSON.stringify(
            {
                booking_id: booking_id,
                status: status
            }
    );
    const xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            localStorage.removeItem('bookings');
            getKeeperBookings();
            console.log("success!");
        }
    };
    xhr.open("POST", "updateBooking");
    xhr.setRequestHeader("Accept", "application/json");
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send(jsonData);
}

function redirectToPage(page_html) {
    if (localStorage.getItem("username") === "admin")
        return;
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

function updateValues() {
    const updatedCookies = getAllCookiePairs();
    for (const cookie_key in updatedCookies) {
        if (updatedCookies.hasOwnProperty(cookie_key)) {
            const inputElement = document.querySelector(`.${cookie_key} input`);
            if (inputElement) {
                var inputValue = inputElement.value;
                if (cookie_key === "catkeeper" || cookie_key === "dogkeeper") {
                    inputValue = inputElement.checked;
                }
                if (updatedCookies[cookie_key] !== inputValue) {
                    updatedCookies[cookie_key] = inputValue;
                }
            }
        }
    }
    cookies = updatedCookies;
    var valid_location = checkLocationUpdate(cookies);
    var path = 'UpdatePetOwner';
    if (cookies.hasOwnProperty("catkeeper")) {
        path = 'UpdatePetKeeper';
    }
    const xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.status !== 200) {
            displayErrorMessage("Error " + xhr.status + " - " + xhr.responseText);
        } else if (valid_location === true) {
            var account_data = document.getElementById('account-data');
            account_data.innerHTML = "";
            var buttons = document.getElementById('buttons');
            buttons.innerHTML = "";
            account_data.innerHTML = '<div class="container" style="text-align: center; padding: 20px; margin-top:1%; margin-left:-25%; background-color:white;padding-top:30%;padding-bottom:30%; border: 2px solid rgb(46, 35, 53);">' +
                    '<h3> Your account has been successfully updated.</h3>' +
                    '</div>';
        }
    };
    xhr.open('POST', path);
    xhr.setRequestHeader("Accept", "application/json");
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send(JSON.stringify(cookies));
}

function configInputField(label_name, input) {
    if (label_name === "Password") {
        input.pattern = '^[^\d].{7,}$';
        input.required = true;
    } else if (label_name === "First Name" || label_name === "Last Name") {
        input.pattern = '^[^\d]{3,30}$';
        input.required = true;
    } else if (label_name === "Birthdate") {
        input.type = "date";
        input.min = "1920-01-01";
        input.max = "2007-12-31";
        input.required = true;
    } else if (label_name === "Gender") {
        input.pattern = "(Male|Female|Other)";
        input.required = true;
    } else if (label_name === "City") {
        input.required = true;
        input.name = 'city';
        input.pattern = "^[^\d]{3,30}$";
    } else if (label_name === "Country") {
        input.name = 'country';
        input.required = true;
        input.addEventListener('change', function () {
            if (input.value !== 'GR') {
                displayErrorMessage("This service is only available to Greece. If your country is Greece, please type 'GR'.");
            }
        });
    } else if (label_name === "Address") {
        input.name = 'address';
        input.required = true;
        input.pattern = "^.{10,150}$";
    } else if (label_name === "Job") {
        input.required = true;
        input.pattern = "^[^\d]{3,30}$";
    } else if (label_name === "Personal Page") {
        input.type = 'url';
    } else if (label_name === 'Telephone') {
//         input.pattern = '^\d{10,14}$';
    } else if (label_name === "Cat Price") {
        input.min = "0";
        input.max = "15";
    } else if (label_name === "Dog Price") {
        input.min = "0";
        input.max = "20";
    } else if (label_name === "Property") {
        input.pattern = "(Indoor|Outdoor|Both)";
        input.required = true;
    }
}

function myAccount() {
    cookies = getAllCookiePairs();
    const disabledKeys = ["username", "email"];
    const dataDiv = document.getElementById('account-data');
    for (const cookie_key in cookies) {
        if (cookie_key === "keeper_id" || cookie_key === "owner_id") {
            continue;
        }
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

            if (label_name === "Lat" || label_name === "Lon") {
                continue;
            }

            configInputField(label_name, input);
            if (label_name === 'Cat Keeper' || label_name === 'Dog Keeper') {
                input.type = "radio";
                if (cookie_value === "true") {
                    input.checked = true;
                } else {
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
    var username_value = document.getElementById("username").value;
    var password_value = document.getElementById("password").value;
    if (username_value === 'admin' && password_value === 'admin12*') {
        localStorage.setItem("username", "admin");
        window.open('welcome_page_admin.html', '_self');
        return;
    } else {
        var jsonData = JSON.stringify(
                {
                    username: username_value,
                    password: password_value
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
}

function isLoggedIn() {
    var logform = document.getElementById("loginForm");
    var cookies = getAllCookiePairs();
    if (cookies.hasOwnProperty("username")) {
        window.open('welcome_page.html', '_self');
        return;
    }

    if (localStorage.getItem("username") === 'admin') {
        window.open('welcome_page_admin.html', '_self');
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

function getCatCount() {
    const xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            localStorage.setItem('cat_count', this.responseText);
        }
    };
    xhr.open("GET", "getCatCount");
    xhr.setRequestHeader("Accept", "application/json");
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send();
}

function getDogCount() {
    const xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            localStorage.setItem('dog_count', this.responseText);
        }
    };
    xhr.open("GET", "getDogCount");
    xhr.setRequestHeader("Accept", "application/json");
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send();
}

function getKeeperCount() {
    const xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            localStorage.setItem('keeper_count', this.responseText);
        }
    };
    xhr.open("GET", "getPetKeepersCount");
    xhr.setRequestHeader("Accept", "application/json");
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send();
}

function getOwnerCount() {
    const xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            localStorage.setItem('owner_count', this.responseText);
        }
    };
    xhr.open("GET", "getPetOwnersCount");
    xhr.setRequestHeader("Accept", "application/json");
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send();
}
function addMessageCards() {
    let container = document.getElementById('card-container');
    let messages_header = document.createElement('h3');
    messages_header.textContent = 'Messages';
    messages_header.className = 'mt-4';
    container.appendChild(messages_header);
    var messages = JSON.parse(localStorage.getItem('messages'));
    let categoryContainer = document.createElement('div');
    categoryContainer.className = 'category-container';
    messages.forEach(entry => {
        container.appendChild(createMessageCard(entry));
    });
}

function createMessageCard(entry) {
    let card = document.createElement('div');
    card.className = 'card my-2';
    let booking = document.createElement('p');
    booking.textContent = `Booking Id: ${entry.booking_id}`;
    card.appendChild(booking);
    let message = document.createElement('p');
    message.textContent = `Message: ${entry.message}`;
    card.appendChild(message);
    let sender = document.createElement('p');
    sender.textContent = `Sender: ${entry.sender}`;
    card.appendChild(sender);
    let datetime = document.createElement('p');
    datetime.textContent = `Datetime: ${entry.datetime}`;
    card.appendChild(datetime);
    let cardButton = document.createElement('button');
    cardButton.className = 'card-button';
    cardButton.textContent = 'Reply';
    cardButton.onclick = function () {
        sendMessage(entry);
    };
    card.appendChild(cardButton);
    return card;
}

function getMessages() {
    var cookies = getAllCookiePairs();
    var path, jsonData;
    if (cookies.hasOwnProperty("keeper_id")) {
        console.log("keeper");
        path = 'getMessagesKeeper';
        jsonData = JSON.stringify({
            keeper_id: cookies["keeper_id"]
        });
    } else {
        path = 'getMessagesOwner';
        jsonData = JSON.stringify({
            owner_id: cookies["owner_id"]
        });
    }

    const xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            window.open("messages.html", "_self");
            localStorage.setItem("messages", this.responseText);
        }
    };
    xhr.open("POST", path);
    xhr.setRequestHeader("Accept", "application/json");
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send(jsonData);
}

function getMoneyEarned() {
    const xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            localStorage.setItem("money_earned", this.responseText);
        }
    };
    xhr.open("GET", "getMoneyEarned");
    xhr.setRequestHeader("Accept", "application/json");
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send();
}

function getStatistics() {
    window.open("statistics.html", "_self");
}

function drawCharts() {
    google.charts.load('current', {'packages': ['corechart']});
    google.charts.setOnLoadCallback(drawPetStatistics);
    google.charts.setOnLoadCallback(drawUserStatistics);
    google.charts.setOnLoadCallback(drawEarningsStatistics);
}

function drawPetStatistics() {
    getCatCount();
    getDogCount();
    var data = new google.visualization.DataTable();
    data.addColumn('string', 'Pets');
    data.addColumn('number', 'Slices');
    data.addRows([
        ['Cats', parseInt(localStorage.getItem("cat_count"))],
        ['Dogs', parseInt(localStorage.getItem("dog_count"))]
    ]);
    // Set chart options
    var options = {'title': 'Pet Statistics',
        'width': 400,
        'height': 300};
    // Instantiate and draw our chart, passing in some options.
    var chart = new google.visualization.PieChart(document.getElementById('pet_chart_div'));
    chart.draw(data, options);
}

function drawUserStatistics() {
    getKeeperCount();
    getOwnerCount();
    var data = new google.visualization.DataTable();
    data.addColumn('string', 'User Types');
    data.addColumn('number', 'Slices');
    data.addRows([
        ['Keepers', parseInt(localStorage.getItem("keeper_count"))],
        ['Owners', parseInt(localStorage.getItem("owner_count"))]
    ]);
    // Set chart options
    var options = {'title': 'User Statistics',
        'width': 400,
        'height': 300};
    // Instantiate and draw our chart, passing in some options.
    var chart = new google.visualization.PieChart(document.getElementById('user_chart_div'));
    chart.draw(data, options);
}
function drawEarningsStatistics() {
    getMoneyEarned();
    var earnings = JSON.parse(localStorage.getItem("money_earned"));
    var application_earnings = 0, keepers_earnings = 0;
    for (let earning in earnings) {
        if (earnings.hasOwnProperty(earning)) {
            let money = earnings[earning];
            application_earnings = application_earnings + (parseFloat(money) * 0.15);
            keepers_earnings = keepers_earnings + (parseFloat(money) * 0.85);
        }
    }
    var data = new google.visualization.DataTable();
    data.addColumn('string', 'Money Earned');
    data.addColumn('number', 'Slices');
    data.addRows([
        ['Application', application_earnings],
        ['Keepers', keepers_earnings]
    ]);
    var options = {'title': 'Money Earned Statistics',
        'width': 400,
        'height': 300};
    var chart = new google.visualization.PieChart(document.getElementById('money_chart_div'));
    chart.draw(data, options);
}

function getKeepers() {
    const xhr_keepers = new XMLHttpRequest();
    xhr_keepers.onload = function () {
        if (xhr_keepers.readyState === 4 && xhr_keepers.status === 200) {
            localStorage.removeItem('keepers');
            localStorage.setItem('keepers', this.responseText);
        } else {
            console.log("error");
        }
    };
    xhr_keepers.open("GET", "getKeepers");
    xhr_keepers.setRequestHeader("Accept", "application/json");
    xhr_keepers.setRequestHeader("Content-Type", "application/json");
    xhr_keepers.send();
}

function getUsers() {
    var jsonData = JSON.stringify(
            {
            }
    );
    const xhr_keepers = new XMLHttpRequest();
    xhr_keepers.onload = function () {
        if (xhr_keepers.readyState === 4 && xhr_keepers.status === 200) {
            localStorage.removeItem('keepers');
            localStorage.setItem('keepers', JSON.stringify(JSON.parse(this.responseText)));
        } else {
            console.log("error");
        }
    };
    xhr_keepers.open("GET", "getKeepers");
    xhr_keepers.setRequestHeader("Accept", "application/json");
    xhr_keepers.setRequestHeader("Content-Type", "application/json");
    xhr_keepers.send(jsonData);
    const xhr_owners = new XMLHttpRequest();
    xhr_owners.onload = function () {
        if (xhr_owners.readyState === 4 && xhr_owners.status === 200) {
            localStorage.removeItem('owners');
            localStorage.setItem('owners', JSON.stringify(JSON.parse(this.responseText)));
            window.open('users.html', '_self');
        } else {
            console.log("error");
        }
    };
    xhr_owners.open("GET", "getOwners");
    xhr_owners.setRequestHeader("Accept", "application/json");
    xhr_owners.setRequestHeader("Content-Type", "application/json");
    xhr_owners.send(jsonData);
}

function createUserCard(user, admin) {
    let card = document.createElement('div');
    card.className = 'card my-2';
    let username = document.createElement('p');
    username.textContent = `Username: ${user.username}`;
    card.appendChild(username);
    let firstName = document.createElement('p');
    firstName.textContent = `First Name: ${user.firstname}`;
    card.appendChild(firstName);
    let lastName = document.createElement('p');
    lastName.textContent = `Last Name: ${user.lastname}`;
    card.appendChild(lastName);
    let job = document.createElement('p');
    job.textContent = `Occupation: ${user.job}`;
    card.appendChild(job);
    if (admin) {
        let cardButton = document.createElement('button');
        cardButton.className = 'card-button';
        cardButton.textContent = 'Delete';
        cardButton.onclick = function () {
            if (user.keeper_id) {
                deletePetKeeper(user.keeper_id);
            } else {
                console.log("owner");
                deletePetOwner(user.owner_id);
            }
        };
        card.appendChild(cardButton);
    }
    return card;
}
function getInfoLinks() {
    window.open("guest_info.html", "_self");
}
function deletePetKeeper(keeper_id) {
    var jsonData = JSON.stringify({
        keeper_id: keeper_id
    });
    const xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            console.log('success');
            getUsers();
        } else {
            displayErrorMessage("Could not delete pet owner.");
        }
    };
    xhr.open("POST", "deletePetKeeper");
    xhr.setRequestHeader("Accept", "application/json");
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send(jsonData);
}
function deletePetOwner(owner_id) {
    var jsonData = JSON.stringify({
        owner_id: owner_id
    });
    const xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            console.log('success');
            getUsers();
        } else {
            displayErrorMessage("Could not delete pet owner.");
        }
    };
    xhr.open("POST", "deletePetOwner");
    xhr.setRequestHeader("Accept", "application/json");
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send(jsonData);
}


function addKeeperCardsGuest() {
    let container = document.getElementById('card-container');
    let keepers_header = document.createElement('h3');
    keepers_header.textContent = 'Keepers';
    keepers_header.className = 'mt-4';
    container.appendChild(keepers_header);
    var keepers = JSON.parse(localStorage.getItem('keepers'));
    let categoryContainer = document.createElement('div');
    categoryContainer.className = 'category-container';
    keepers.forEach(entry => {
        container.appendChild(createUserCard(entry, false));
    });
    container.appendChild(categoryContainer);
}

function addUserCards() {
    let container = document.getElementById('card-container');
    let keepers_header = document.createElement('h3');
    keepers_header.textContent = 'Keepers';
    keepers_header.className = 'mt-4';
    container.appendChild(keepers_header);
    var keepers = JSON.parse(localStorage.getItem('keepers'));
    var owners = JSON.parse(localStorage.getItem('owners'));
    let categoryContainer = document.createElement('div');
    categoryContainer.className = 'category-container';
    keepers.forEach(entry => {
        container.appendChild(createUserCard(entry, true));
    });
    container.appendChild(categoryContainer);
    let owners_header = document.createElement('h3');
    owners_header.textContent = 'Owners';
    owners_header.className = 'mt-4';
    container.appendChild(owners_header);
    categoryContainer = document.createElement('div');
    categoryContainer.className = 'd-flex flex-wrap justify-content-start';
    owners.forEach(entry => {
        container.appendChild(createUserCard(entry, true));
    });
    container.appendChild(categoryContainer);
}

function logout() {
    if (localStorage.getItem("username") === 'admin') {
        localStorage.clear();
        window.location.href = 'login.html';
        return;
    }

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

function getKeeperBookings() {
    var cookies = getAllCookiePairs();
    var keeper_id = cookies["keeper_id"];
    var jsonData = JSON.stringify({
        keeper_id: keeper_id
    });
    const xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            localStorage.setItem("bookings", this.responseText);
            window.open("bookings_keeper.html", "_self");
        }
    };
    xhr.open('POST', 'getKeeperBookings');
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

function checkLocationUpdate(cookies) {
    var country = document.querySelector('input[name = "country"]');
    var city = document.querySelector('input[name = "city"]');
    var address = document.querySelector('input[name = "address"]');
    var country_value = country.value;
    var city_value = city.value;
    var address_value = address.value;
    const data = null;
    const xhr = new XMLHttpRequest();
    xhr.withCredentials = true;
    xhr.addEventListener("readystatechange", function () {
        if (this.readyState === this.DONE) {
            const jsonResults = JSON.parse(this.responseText);
            if (jsonResults[0] === undefined || jsonResults[0].display_name.includes('Heraklion Regional Unit') === false) {
                displayErrorMessage("Your location could not be verified. Please try again.");
                return false;
            }

            cookies["lat"] = jsonResults[0].lat;
            cookies["lon"] = jsonResults[0].lon;
        }
    });
    var address = address_value + " " + city_value + " " + country_value;
    xhr.open("GET", "https://forward-reverse-geocoding.p.rapidapi.com/v1/search?q=" + address + "&accept-language=en&polygon_threshold=0.0");
    xhr.setRequestHeader('X-RapidAPI-Key', '04c6c97612mshf9a7ac750c41d05p1793eejsnabd20a0b9528');
    xhr.setRequestHeader('X-RapidAPI-Host', 'forward-reverse-geocoding.p.rapidapi.com');
    xhr.send(data);
    return true;
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
        text: errorMessage,
        confirmButtonColor: 'brown'
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