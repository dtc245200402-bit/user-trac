<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quản lý User</title>
    <style>
        body{font-family:Arial,sans-serif;background:#f4f7fb;margin:0;padding:40px;color:#222}
        .box{max-width:1050px;margin:auto;background:#fff;padding:28px;border-radius:12px;box-shadow:0 5px 20px rgba(0,0,0,.08)}
        h2{margin-top:0;color:#1b2a7a}
        .toolbar{display:flex;gap:10px;flex-wrap:wrap;align-items:center;margin-bottom:18px}
        .search{display:flex;gap:8px;flex:1;min-width:280px}
        input{padding:9px 11px;border:1px solid #ccd3df;border-radius:6px;flex:1}
        button,.btn{border:0;display:inline-block;padding:9px 13px;border-radius:6px;text-decoration:none;color:white;cursor:pointer;font-size:14px}
        button{background:#1b2a7a}.add{background:#27ae60}.sort{background:#8e44ad}.reset{background:#7f8c8d}.edit{background:#2980b9}.del{background:#c0392b}
        table{width:100%;border-collapse:collapse;margin-top:15px}th,td{padding:12px;border-bottom:1px solid #ddd;text-align:left}th{background:#1b2a7a;color:#fff}tr:hover{background:#f7f9fc}
        .actions{white-space:nowrap}.notice{margin:12px 0;color:#555}
    </style>
</head>
<body>
    <div style="margin: 15px 0; padding: 12px; background: #fff3cd; border-radius: 6px;">
        <strong>Luyện tập JDBC Transaction:</strong>
        <a href="${pageContext.request.contextPath}/users?action=test-add-user-transaction"
           style="margin-left:10px;">Chạy addUserTransaction()</a>
        <c:if test="${not empty requestScope.transactionMessage}">
            <div style="margin-top:8px;">${requestScope.transactionMessage}</div>
        </c:if>
    </div>

<div class="box">
    <h2>Danh sách User</h2>

    <div class="toolbar">
        <form class="search" action="${pageContext.request.contextPath}/users" method="get">
            <input type="hidden" name="action" value="search">
            <input type="text" name="country" value="${searchCountry}" placeholder="Nhập country cần tìm...">
            <button type="submit">Tìm kiếm</button>
        </form>
        <a class="btn sort" href="${pageContext.request.contextPath}/users?action=sort">Sắp xếp theo tên</a>
        <a class="btn reset" href="${pageContext.request.contextPath}/users">Tất cả</a>
        <a class="btn add" href="${pageContext.request.contextPath}/users?action=create">+ Thêm User</a>
    </div>

    <c:if test="${not empty searchCountry}">
        <div class="notice">Kết quả tìm kiếm country: <strong>${searchCountry}</strong></div>
    </c:if>
    <c:if test="${sortMode}">
        <div class="notice">Danh sách đang được sắp xếp theo tên A → Z.</div>
    </c:if>

    <table>
        <tr><th>ID</th><th>Tên</th><th>Email</th><th>Quốc gia</th><th>Hành động</th></tr>
        <c:forEach var="u" items="${listUser}">
            <tr>
                <td>${u.id}</td><td>${u.name}</td><td>${u.email}</td><td>${u.country}</td>
                <td class="actions">
                    <a class="btn edit" href="${pageContext.request.contextPath}/users?action=edit&id=${u.id}">Sửa</a>
                    <a class="btn del" href="${pageContext.request.contextPath}/users?action=delete&id=${u.id}">Xóa</a>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>
    <div style="width:80%;margin:25px auto;text-align:center;">
        <a href="${pageContext.request.contextPath}/users?action=test-without-tran"
           style="display:inline-block;padding:10px 14px;background:#8e44ad;color:white;text-decoration:none;border-radius:5px;font-weight:bold;margin-right:8px;">
            Test không Transaction
        </a>
        <a href="${pageContext.request.contextPath}/users?action=test-use-tran"
           style="display:inline-block;padding:10px 14px;background:#16a085;color:white;text-decoration:none;border-radius:5px;font-weight:bold;">
            Test có Transaction
        </a>
        <p style="font-size:13px;color:#666;">Test có Transaction sẽ cố tình lỗi UPDATE để kiểm tra rollback.</p>
    </div>
</body>
</html>
