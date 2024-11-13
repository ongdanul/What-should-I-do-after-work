function showFeedback(element, message, isValid) {
    element.textContent = message;
    element.style.visibility = message ? 'visible' : 'hidden';
    element.classList.toggle('text-success', isValid);
    element.classList.toggle('text-danger', !isValid);
}

async function checkExistingPassword() {
    const userId = document.getElementById('userId').value;
    const pw = document.getElementById('existingPw').value.trim();
    const feedback = document.getElementById('existingPwFeedback');

    if (pw.length < 8 || pw.length > 10 || !/[0-9]/.test(pw) || !/[a-zA-Z]/.test(pw)) {
        showFeedback(feedback, '비밀번호는 영문+숫자 8~10자입니다.', false);
        return;
    }

    try {
        const response = await axios.post('/check/password', { userId, pw });
        showFeedback(feedback, response.data.valid ? '기존 비밀번호와 일치하지 않습니다.' : '비밀번호가 일치합니다.', !response.data.valid);
    } catch (error) {
        showFeedback(feedback, '비밀번호 확인 중 오류가 발생했습니다.', false);
    }
}

function checkPassword() {
    const pw = document.getElementById('userPw').value;
    const feedback = document.getElementById('pwFeedback');
    const regx = /^(?=.*[0-9])(?=.*[a-zA-Z])[0-9a-zA-Z]{8,10}$/;
    showFeedback(feedback, regx.test(pw) ? '' : '비밀번호는 영문+숫자 8~10자입니다.', regx.test(pw));
    checkPasswordMatch();
}

function checkPasswordMatch() {
    const pw = document.getElementById('userPw').value;
    const confirmPw = document.getElementById('confirmPw').value;
    const feedback = document.getElementById('confirmPwFeedback');
    showFeedback(feedback, pw === confirmPw ? '비밀번호 일치합니다.' : '비밀번호 불일치', pw === confirmPw);
}

const currentValues = {
    contactPrefix: document.getElementById('contactPrefix').value,
    contactMiddle: document.getElementById('contactMiddle').value,
    contactSuffix: document.getElementById('contactSuffix').value,
    contact: document.getElementById('contact').value,
    email: document.getElementById('email').value,
};

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

function checkEmail() {
    const email = document.getElementById('email').value;
    const feedback = document.getElementById('emailFeedback');
    const isValid = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(email);
    showFeedback(feedback, isValid ? '' : '이메일 형식으로 입력해주세요.', isValid);
}

function validateForm(e) {
    e.preventDefault();

    const existingPwValid = document.getElementById('existingPwFeedback').classList.contains('text-success');
    const pwValid = document.getElementById('pwFeedback').classList.contains('text-success') || document.getElementById('userPw').value.trim() === '';
    const confirmPwValid = document.getElementById('confirmPwFeedback').classList.contains('text-success') || document.getElementById('confirmPw').value.trim() === '';
    const emailValid = document.getElementById('emailFeedback').classList.contains('text-success');

    if (!existingPwValid) {
        alert('기존 비밀번호가 일치하지 않습니다.');
        return;
    }

    if (!emailValid) {
        alert('이메일이 유효하지 않습니다.');
        return;
    }

    if (pwValid && !confirmPwValid) {
        alert('비밀번호가 일치하지 않습니다.');
        return;
    }

    document.querySelector('form').submit();
}

document.getElementById('existingPw').addEventListener('input', checkExistingPassword);
document.getElementById('userPw').addEventListener('input', checkPassword);
document.getElementById('confirmPw').addEventListener('input', checkPasswordMatch);
document.querySelectorAll('#contactPrefix, #contactMiddle, #contactSuffix').forEach(input => input.addEventListener('input', formatContactInput));
document.getElementById('email').addEventListener('input', checkEmail);

document.querySelector('form').addEventListener('submit', validateForm);

document.querySelector('.cancelBtn').addEventListener('click', () => location.href = '/profile');