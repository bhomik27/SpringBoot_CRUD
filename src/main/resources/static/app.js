document.addEventListener("DOMContentLoaded", function () {
    const registerForm = document.getElementById("registerForm");
    const loginForm = document.getElementById("loginForm");
    const userInfo = document.getElementById("userInfo");
    const logoutBtn = document.getElementById("logout");

    // Register User
    if (registerForm) {
        registerForm.addEventListener("submit", async (e) => {
            e.preventDefault();
            const name = document.getElementById("name").value;
            const email = document.getElementById("email").value;
            const password = document.getElementById("password").value;

            const response = await fetch("/users/register", {  // ✅ Fix: Corrected API path
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ name, email, password }),
            });

            if (response.ok) {
                alert("Registration successful! Please login.");
                window.location.href = "login.html";
            } else {
                alert("Registration failed. Please try again.");
            }
        });
    }

    // Login User
    if (loginForm) {
        loginForm.addEventListener("submit", async (e) => {
            e.preventDefault();
            const email = document.getElementById("loginEmail").value;
            const password = document.getElementById("loginPassword").value;

            const response = await fetch("/users/login", {  // ✅ Fix: Corrected API path
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ email, password }),
            });

            if (response.ok) {
                const data = await response.json();
                localStorage.setItem("token", data.token);
                window.location.href = "home.html";
            } else {
                alert("Invalid credentials. Please try again.");
            }
        });
    }

    // Fetch User Info on Home Page
    if (userInfo) {
        async function loadUser() {
            const token = localStorage.getItem("token");
            if (!token) {
                alert("You are not logged in.");
                window.location.href = "login.html";
                return;
            }

            const response = await fetch("/users/home", {  // ✅ Fix: Corrected API path
                method: "GET",
                headers: { Authorization: `Bearer ${token}` },
            });

            if (response.ok) {
                const data = await response.json();
                userInfo.textContent = `Hello, ${data.message}`;
            } else {
                alert("Failed to fetch user data. Redirecting to login...");
                window.location.href = "login.html";
            }
        }

        loadUser();
    }

    // Logout
    if (logoutBtn) {
        logoutBtn.addEventListener("click", () => {
            localStorage.removeItem("token");
            window.location.href = "login.html";
        });
    }
});
