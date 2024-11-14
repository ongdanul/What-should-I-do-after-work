// MY 회원 탈퇴 확인
function deleteUser(event) {
    event.preventDefault(); // 기본 제출 막기
        if (confirm('정말로 삭제하시겠습니까?')) {
            event.target.submit();
        } else {
    }
}
