const flashMessageElement = document.getElementById('flashMessage');
if (flashMessageElement && flashMessageElement.textContent) {
    alert(flashMessageElement.textContent);
}

async function login(e) {
    e.preventDefault();

    const username = document.getElementById('username').value;
    const password = document.getElementById('userPw').value;
    const rememberMe = document.getElementById('rememberMe').checked ? 'true' : 'false';

    const data = { username, password, rememberMe };

    try {
        const response = await axios.post("/user/loginProcess", null, { params: data });

        window.location.href = "/board/list";
    } catch (error) {
        if (error.response) {
            if (error.response.status === 403 && error.response.data === "LOGIN_LOCKED") {
                alert("로그인 5회 실패로 계정이 비활성화 되었습니다.\n비밀번호찾기에서 임시비밀번호를 발급하세요.");
            } else if (error.response.status === 401) {
                alert("아이디 또는 비밀번호를 확인하세요.");
            } else {
                alert("알 수 없는 오류가 발생했습니다. 다시 시도해 주세요.");
            }
        } else {
            alert("서버와의 연결에 문제가 발생했습니다.");
        }
        console.error("로그인 에러 발생:", error.message);
    }
}

document.querySelector('.loginBtn').addEventListener('click', login);
document.querySelector('.signUpBtn').addEventListener('click', () => location.href = '/user/sign-up');
