function getQueryParams() {
    const params = new URLSearchParams(window.location.search);
    return {
        userName: params.get('userName') || '',
        userId: params.get('userId') || ''
    };
}

document.addEventListener('DOMContentLoaded', () => {
    const { userName, userId } = getQueryParams();
    if (userName) document.getElementById('userName').value = userName;
    if (userId) document.getElementById('userId').value = userId;
});

async function checkUser() {
    const userName = document.getElementById('userName').value.trim();
    const userId = document.getElementById('userId').value.trim();

    if (userName === '' || userId === '') {
        return false;
    }

    const data = { userName, userId };

    try {
        const response = await axios.post('/check/userInfo', data );
        if (response.data.exists) {
            return true;
        } else {
            alert('일치하는 아이디가 존재하지 않습니다.');
            return false;
        }
    } catch (error) {
        console.error("유저 정보 조회 에러 발생:", error.message);
        alert('서버 오류가 발생했습니다. 다시 시도해주세요.');
        return false;
    }
}

async function findPw() {

    const userConfirmed = confirm('기존 비밀번호가 임시비밀번호로 초기화됩니다. \n임시비밀번호 발급을 진행하시겠습니까?');
    if (!userConfirmed) {
        return;
    }

    const userName = document.getElementById('userName').value.trim();
    const userId = document.getElementById('userId').value;

    const data = { userName, userId };

    try {
        const response = await axios.post("/user/find-pw", data);
        if (response.status === 200) {
            alert(response.data + '\n회원정보페이지에서 비밀번호를 변경해주세요.');
            window.location.href = '/user/login';
        } else if (response.status === 400) {
            alert(response.data + '입력하신 아이디를 다시 확인해주세요.');
        } else {
            alert('서버 오류가 발생했습니다. 다시 시도해주세요.');
        }
    } catch(error) {
        console.error("비밀번호 찾기 에러 발생:", error.message);
        alert('서버 오류가 발생했습니다. 다시 시도해주세요.');
    }
}

document.querySelector('form').addEventListener('submit', async function(e) {
    e.preventDefault();
    const isUserValid = await checkUser();
    if (!isUserValid) { return; }
    findPw();
});

document.querySelector('.homeBtn').addEventListener('click', () => location.href = '/user/login');