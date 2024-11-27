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

async function findId(e) {
    e.preventDefault();

    formatContactInput();

    const userName = document.getElementById('userName').value;
    const contact = document.getElementById('contact').value;

    if (!userName || !contact) {
        alert("이름과 연락처를 모두 입력해 주세요.");
        return;
    }

    const data = { userName, contact };

    try {
        const response = await axios.post("/user/find-id", data);

        const userIds = response.data;

        if (userIds && userIds.length > 0) {
            const userIdDisplay = document.getElementById('userIdDisplay');
            userIdDisplay.style.display = 'flex';
            userIdDisplay.style.flexDirection = 'column';
            userIdDisplay.style.height = '100px';
            userIdDisplay.innerHTML = '';

            userIds.forEach((userId) => {
                const idElement = document.createElement('div');
                idElement.className = 'userIdItem';
                idElement.innerText = `아이디는 ${userId} 입니다.`;
                idElement.style.cursor = 'pointer';
                idElement.onclick = () => {
                    window.location.href = `/user/find-pw?userName=${encodeURIComponent(userName)}&userId=${encodeURIComponent(userId)}`;
                };
                userIdDisplay.appendChild(idElement);
            });
        }
    } catch (error) {
        console.error("아이디 찾기 에러 발생:", error.message);
        alert('일치하는 아이디가 존재하지 않습니다.');
    }
}

document.querySelector('form').addEventListener('submit', findId);

document.querySelector('.homeBtn').addEventListener('click', () => location.href = '/user/login');