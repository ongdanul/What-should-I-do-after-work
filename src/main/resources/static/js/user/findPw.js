async function findPw(e) {
    e.preventDefault();

    const userConfirmed = confirm('기존 비밀번호가 임시비밀번호로 초기화됩니다. \n임시비밀번호 발급을 진행하시겠습니까?');
    if (!userConfirmed) {
        return;
    }

    const userName = document.getElementById('userName').value;
    const email = document.getElementById('email').value;

    const data = {userName, email};


    try {
        const response = await axios.post("/user/find-pw", null, { params: data });
        alert('임시비밀번호 발급이 완료되었습니다. \n회원정보페이지에서 비밀번호를 변경해주세요.');
        window.location.href = '/user/login';
    } catch(error) {
        console.error("비밀번호 찾기 에러 발생:", error.message);
        alert('이름 혹은 이메일이 일치하지 않습니다.');
    }
}

document.querySelector('form').addEventListener('submit', findPw);

document.querySelector('.homeBtn').addEventListener('click', () => location.href = '/user/login');