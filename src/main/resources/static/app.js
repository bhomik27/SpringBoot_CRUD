document.addEventListener("DOMContentLoaded", function () {
    const registerForm = document.getElementById("registerForm");
    const loginForm = document.getElementById("loginForm");
    const userInfo = document.getElementById("userInfo");
    const logoutBtn = document.getElementById("logout");

    const BASE_URL = "http://localhost:8080";

    function showLoading(button, text = "Processing...") {
        button.disabled = true;
        button.innerHTML = `<span class="spinner-border spinner-border-sm"></span> ${text}`;
    }

    function hideLoading(button, originalText) {
        button.disabled = false;
        button.innerHTML = originalText;
    }

    if (registerForm) {
        registerForm.addEventListener("submit", async (e) => {
            e.preventDefault();
            const name = document.getElementById("name").value.trim();
            const email = document.getElementById("email").value.trim();
            const password = document.getElementById("password").value;

            console.log("Registering:", { name, email });

            if (!name || !email || !password) {
                alert("All fields are required!");
                return;
            }

            const submitBtn = registerForm.querySelector("button[type='submit']");
            showLoading(submitBtn, "Registering...");

            const response = await fetch(`${BASE_URL}/users/register`, {  
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ name, email, password }),
            });

            hideLoading(submitBtn, "Register");

            console.log("Register Response:", response);

            if (response.ok) {
                alert("Registration successful! Please log in.");
                window.location.href = "login.html";
            } else {
                alert("Registration failed. Try a different email.");
            }
        });
    }

    if (loginForm) {
        loginForm.addEventListener("submit", async (e) => {
            e.preventDefault();
            const email = document.getElementById("loginEmail").value.trim();
            const password = document.getElementById("loginPassword").value;

            console.log("Logging in:", { email });

            if (!email || !password) {
                alert("Please enter email and password!");
                return;
            }

            const submitBtn = loginForm.querySelector("button[type='submit']");
            showLoading(submitBtn, "Logging in...");

            const response = await fetch(`${BASE_URL}/users/login`, {  
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ email, password }),
            });

            hideLoading(submitBtn, "Login");

            console.log("Login Response:", response);

            if (response.ok) {
                const data = await response.json();
                console.log("Login Data:", data);

                localStorage.setItem("token", data.token);
                localStorage.setItem("userName", data.name || "User");  // ✅ Ensure username is stored
                window.location.href = "home.html";
            } else {
                alert("Invalid credentials. Try again.");
            }
        });
    }

    if (userInfo) {
        async function loadUser() {
            const token = localStorage.getItem("token");
            let userName = localStorage.getItem("userName");

            console.log("Loaded from localStorage:", { token, userName });

            if (!token) {
                alert("You are not logged in.");
                window.location.href = "login.html";
                return;
            }

            if (!userName) {
                console.log("Fetching username from backend...");
                const response = await fetch(`${BASE_URL}/users/home`, {  
                    method: "GET",
                    headers: { Authorization: `Bearer ${token}` },
                });

                console.log("Home Page Response:", response);

                if (response.ok) {
                    const data = await response.json();
                    console.log("Home Page Data:", data);

                    userName = data.name || "User";
                    localStorage.setItem("userName", userName);
                } else {
                    alert("Session expired. Please log in again.");
                    localStorage.removeItem("token");
                    window.location.href = "login.html";
                    return;
                }
            }

            console.log("Final Username:", userName);
            userInfo.textContent = `Welcome, ${userName}`;
        }

        loadUser();
    }

    if (logoutBtn) {
        logoutBtn.addEventListener("click", () => {
            console.log("Logging out...");
            if (confirm("Are you sure you want to log out?")) {
                localStorage.removeItem("token");
                localStorage.removeItem("userName");
                window.location.href = "login.html";
            }
        });
    }
});
