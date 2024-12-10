const flashMessageElement = document.getElementById('flashMessage');
if (flashMessageElement && flashMessageElement.textContent) {
    alert(flashMessageElement.textContent);
}

function getCookie(name) {
    const cookies = document.cookie.split("; ");
    for (let i = 0; i < cookies.length; i++) {
        const cookie = cookies[i].split("=");
        if (cookie[0] === name) {
            return decodeURIComponent(cookie[1]);
        }
    }
    return null;
}

document.addEventListener("DOMContentLoaded", function () {
    const encodedUserId = getCookie("remember-id");
    if (encodedUserId) {
        const userId = atob(encodedUserId);
        const usernameInput = document.getElementById("userName");
        if (usernameInput) {
            usernameInput.value = userId;
            document.getElementById("rememberId").checked = true;
        }
    }
});

async function login(e) {
    e.preventDefault();

    const username = document.getElementById('userName').value;
    const password = document.getElementById('userPw').value;
    const rememberId = document.getElementById('rememberId').checked ? 'true' : 'false';
    const rememberMe = document.getElementById('rememberMe').checked ? 'true' : 'false';

    const data = new URLSearchParams();
    data.append("username", username);
    data.append("password", password);
    data.append("rememberId", rememberId);
    data.append("rememberMe", rememberMe);

    //TODO 로그인 관련 기능 완성이후 삭제하기
    console.log("Test - rememberId:", rememberId);
    console.log("Test - rememberMe:", rememberMe);
    console.log("Test - Data to send:", data.toString());

    try {
        const response = await axios.post("/user/loginProcess", data);
        window.location.href = "/board/list";

    } catch (error) {
        if (error.response) {
            if (error.response.status === 403 && error.response.data === "LOGIN_LOCKED") {
                alert("계정이 비활성화 되었습니다.\n비밀번호찾기에서 임시비밀번호를 발급하세요.");
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
document.querySelector('.kakao').addEventListener('click', () => location.href = '/oauth2/authorization/kakao');
document.querySelector('.naver').addEventListener('click', () => location.href = '/oauth2/authorization/naver');
document.querySelector('.google').addEventListener('click', () => location.href = '/oauth2/authorization/google');
