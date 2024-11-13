function toggleAdmin(userId, currentRole) {
    var newRole = (currentRole === 'ROLE_ADMIN') ? 'ROLE_USER' : 'ROLE_ADMIN';
    $.ajax({
        url: '/admin/toggleAdmin',
        type: 'POST',
        data: JSON.stringify({ userId: userId, newRole: newRole }),
        contentType: 'application/json; charset=utf-8',
        success: function(response) {
            alert('권한 변경 성공');
            location.reload();
        },
        error: function(error) {
            alert('권한 변경 실패');
            console.log(error);
        }
    });
}

function toggleLoginLock(userId, currentLockState) {
    var newLockStatus = currentLockState ? 'false' : 'true';
    $.ajax({
        url: '/admin/toggleLoginLock',
        type: 'POST',
        data: JSON.stringify({ userId: userId, newLockStatus: newLockStatus }),
        contentType: 'application/json; charset=utf-8',
        success: function(response) {
            alert('로그인 잠금 상태 변경 성공');
            location.reload();
        },
        error: function(error) {
            alert('로그인 잠금 상태 변경 실패');
            console.log(error);
        }
    });
}
