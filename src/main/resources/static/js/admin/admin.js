function toggleAdmin(userId, currentRole) {
    var newRole = (currentRole === 'ROLE_ADMIN') ? 'ROLE_USER' : 'ROLE_ADMIN';
    $.ajax({
        url: '/admin/toggleAdmin',
        type: 'POST',
        data: JSON.stringify({ userId: userId, newRole: newRole }),
        contentType: 'application/json; charset=utf-8',
        success: function(response) {
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
            location.reload();
        },
        error: function(error) {
            alert('로그인 잠금 상태 변경 실패');
            console.log(error);
        }
    });
}

function deleteUser(userId) {
    if (confirm('정말로 삭제하시겠습니까?')) {
        $.ajax({
            url: '/admin/delete',
            type: 'POST',
            data: { userId: userId },
            success: function(response) {
                alert('삭제 성공');
                location.reload();
            },
            error: function(error) {
                alert('삭제 실패');
                console.log(error);
            }
        });
    }
}

function deleteSelected() {
    var selectedUsers = [];
    $("input[name='select-user']:checked").each(function() {
        selectedUsers.push($(this).val());
    });

    if (selectedUsers.length > 0) {
        if (confirm('정말로 삭제하시겠습니까?')) {
            $.ajax({
                url: '/admin/deleteSelected',
                type: 'POST',
                data: JSON.stringify({ userIds: selectedUsers }),
                contentType: 'application/json; charset=utf-8',
                success: function(response) {
                    alert('일괄 삭제 성공');
                    location.reload();
                },
                error: function(error) {
                    alert('일괄 삭제 실패');
                    console.log(error);
                }
            });
        }
    } else {
        alert('삭제할 사용자를 선택하세요.');
    }
}

// 최상단으로 이동 함수
function scrollToTop() {
    window.scrollTo({ top: 0, behavior: 'smooth' });
}

window.deleteSelected = deleteSelected;




