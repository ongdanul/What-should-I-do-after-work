function logout() {
    const form = document.createElement('form');
    form.method = 'POST';
    form.action = '/logout';
    form.style.display = 'none';

    document.body.appendChild(form);
    form.submit();
}

document.getElementById('logoutButton').addEventListener('click', logout);