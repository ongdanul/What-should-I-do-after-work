function showFeedback(element, message, isValid) {
    element.textContent = message;
    element.style.visibility = message ? 'visible' : 'hidden';
    element.classList.toggle('text-success', isValid);
    element.classList.toggle('text-danger', !isValid);
}

async function checkUserId() {
    const userId = document.getElementById('userId').value.trim();
    const feedback = document.getElementById('idFeedback');

    if (userId === '') {
        showFeedback(feedback, '', false);
        return;
    }

    if (userId.length < 4 || userId.length > 10) {
        showFeedback(feedback, '4자 이상 10자 이하로 입력해주세요.', false);
        return;
    }

    try {
        const response = await axios.post('/check/userId', { userId });
        showFeedback(feedback, response.data.exists ? '이미 등록된 아이디입니다.' : '사용 가능한 아이디입니다.', !response.data.exists);
    } catch (error) {
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
    showFeedback(feedback, pw === confirmPw ? '비밀번호가 일치합니다.' : '비밀번호 불일치', pw === confirmPw);
}

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

    const userIdValid = document.getElementById('idFeedback').classList.contains('text-success');
    const pwValid = document.getElementById('pwFeedback').classList.contains('text-success');
    const confirmPwValid = document.getElementById('confirmPwFeedback').classList.contains('text-success');
    const emailValid = document.getElementById('emailFeedback').classList.contains('text-success');

    if (!userIdValid || !pwValid || !confirmPwValid || !emailValid) {
        alert('모든 입력값이 유효해야 회원가입이 가능합니다.');
    } else {
    document.querySelector('form').submit();
    }
}

document.getElementById('userId').addEventListener('input', checkUserId);
document.getElementById('userPw').addEventListener('input', checkPassword);
document.getElementById('confirmPw').addEventListener('input', checkPasswordMatch);
document.querySelectorAll('#contactPrefix, #contactMiddle, #contactSuffix').forEach(input => input.addEventListener('input', formatContactInput));
document.getElementById('email').addEventListener('input', checkEmail);

document.querySelector('form').addEventListener('submit', validateForm);

document.querySelector('.cancelBtn').addEventListener('click', () => location.href = '/user/login');