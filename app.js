const BASE_URL = "http://localhost:8080/api";
let token = localStorage.getItem("token");

// 🔹 On Load
window.onload = async () => {
  if (token) {
    try {
      await loadUser();
      document.getElementById("auth-modal").classList.remove("active");
    } catch {
      logout();
    }
  } else {
    openModal("auth-modal");
  }
};

// 🔹 Login
// 🔹 Login
async function login() {
  const username = val("username");
  const password = val("password");

  try {
    const res = await fetch(`${BASE_URL}/auth/login`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username, password })
    });

    // Log full response for debugging
    const text = await res.text();
    console.log("Login Response:", res.status, text);

    if (!res.ok) {
      showError("auth-error", `Login failed: ${text}`);
      return;
    }

    const data = JSON.parse(text);
    token = data.token;
    localStorage.setItem("token", token);
    closeModal("auth-modal");
    await loadUser();
    showToast(`✅ Welcome back, ${data.username}!`);
  } catch (err) {
    console.error("Login Error:", err);
    showError("auth-error", "Network error. Try again.");
  }
}

// 🔹 Registration
async function register() {
  const username = val("username");
  const password = val("password");
  const email = val("email");

  try {
    const res = await fetch(`${BASE_URL}/auth/register`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username, password, email })
    });

    const text = await res.text();
    console.log("Register Response:", res.status, text);

    if (!res.ok) {
      showError("auth-error", `Registration failed: ${text}`);
      return;
    }

    showToast(`🎉 Registered successfully! ${text}`);
    toggleAuthMode(); // switch to login mode
  } catch (err) {
    console.error("Registration Error:", err);
    showError("auth-error", "Network error. Try again.");
  }
}


// 🔹 Helpers
function val(id) { return document.getElementById(id).value.trim(); }
function openModal(id) { document.getElementById(id).classList.add("active"); }
function closeModal(id) { document.getElementById(id).classList.remove("active"); }
function showToast(msg) {
  const toast = document.getElementById("toast");
  toast.textContent = msg;
  toast.style.display = "block";
  setTimeout(() => (toast.style.display = "none"), 3000);
}
function showError(id, msg) { document.getElementById(id).textContent = msg; }


let isLoginMode = true;
function toggleAuthMode() {
  isLoginMode = !isLoginMode;
  const email = document.getElementById("email");
  const mainBtn = document.getElementById("auth-main-btn");
  const title = document.getElementById("auth-title");
  const toggleBtn = document.getElementById("auth-toggle-btn");

  if (isLoginMode) {
    email.style.display = "none";
    mainBtn.textContent = "Login";
    mainBtn.onclick = login;
    title.textContent = "Login";
    toggleBtn.textContent = "Need an account? Register";
  } else {
    email.style.display = "block";
    mainBtn.textContent = "Register";
    mainBtn.onclick = register;
    title.textContent = "Register";
    toggleBtn.textContent = "Already have an account? Login";
  }
}

function logout() {
  token = null;
  localStorage.removeItem("token");
  location.reload();
}

// 🔹 Load User + Items
function loadUser() {
  fetch(`${BASE_URL}/auth/me`, {
    headers: { Authorization: `Bearer ${token}` }
  })
    .then(r => r.json())
    .then(user => {
      document.getElementById("user-profile").classList.remove("hidden");
      document.getElementById("app").classList.remove("hidden");
      document.getElementById("username-display").textContent = user.username;
      document.getElementById("points-display").textContent = user.points;
      document.getElementById("welcome-user").textContent = user.username;
      document.getElementById("welcome-points").textContent = user.points;
      loadItems();
    })
    .catch(() => {
      showToast("Session expired. Please login again.");
      logout();
    });
}
function loadItems() {
  const grid = document.getElementById("items-grid");
  const loading = document.getElementById("loading");
  loading.classList.remove("hidden");
  
  fetch(`${BASE_URL}/items`, {
    headers: { Authorization: `Bearer ${token}` }
  })
  .then(r => r.json())
  .then(items => {
    grid.innerHTML = items.map(i => `
      <div class="card">
        <h3>${i.name}</h3>
        <p>${i.description}</p>
        <small>Owner: ${i.owner?.username || "N/A"}</small>
      </div>
    `).join("");
  })
  .finally(() => loading.classList.add("hidden"));
}


// 🔹 Add Item
function addItem() {
  const name = val("item-name");
  const description = val("item-description");

  fetch(`${BASE_URL}/items`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`
    },
    body: JSON.stringify({ name, description })
  })
    .then(res => {
      if (res.ok) {
        closeModal("add-item-modal");
        showToast("✅ Item added!");
        loadItems();
      } else showError("add-item-error", "Failed to add item");
    })
    .catch(() => showError("add-item-error", "Error adding item"));
}

// 🔹 Helpers
function val(id) { return document.getElementById(id).value.trim(); }
function openModal(id) { document.getElementById(id).classList.add("active"); }
function closeModal(id) { document.getElementById(id).classList.remove("active"); }
function showToast(msg) {
  const toast = document.getElementById("toast");
  toast.textContent = msg;
  toast.style.display = "block";
  setTimeout(() => (toast.style.display = "none"), 3000);
}
function showError(id, msg) { document.getElementById(id).textContent = msg; }
