document.addEventListener('DOMContentLoaded', () => {
    function formatContactInput() {
        const regex = /^[0-9]*$/;
        const contactMiddle = document.getElementById('contactMiddle');
        const contactSuffix = document.getElementById('contactSuffix');

        if (!regex.test(contactMiddle.value)) {
            contactMiddle.value = contactMiddle.value.replace(/[^0-9]/g, '');
        }
        if (!regex.test(contactSuffix.value)) {
            contactSuffix.value = contactSuffix.value.replace(/[^0-9]/g, '');
        }
        document.getElementById('contact').value = `${document.getElementById('contactPrefix').value}-${contactMiddle.value}-${contactSuffix.value}`;
    }

    document.querySelectorAll('#contactPrefix, #contactMiddle, #contactSuffix').forEach(input => input.addEventListener('input', formatContactInput));

    document.querySelector('.cancelBtn').addEventListener('click', () => location.href = '/profile');
});