<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>



<script>
    $(function () {
    	//每次进入时判断，是否有错误信息提示用户存在
        <c:if test="${!empty msg}">
        $("span.errorMessage").html("${msg}");
        $("div.registerErrorMessageDiv").css("visibility", "visible");
        </c:if>
        
        //可选。规定当发生 submit 事件时运行的函数
        $(".registerForm").submit(function () {
            if (0 == $("#name").val().length) {
                $("span.errorMessage").html("请输入用户名");
                $("div.registerErrorMessageDiv").css("visibility", "visible");
                return false;
            }
            if (0 == $("#password").val().length) {
                $("span.errorMessage").html("请输入密码");
                $("div.registerErrorMessageDiv").css("visibility", "visible");
                return false;
            }
            if (0 == $("#repeatpassword").val().length) {
                $("span.errorMessage").html("请输入重复密码");
                $("div.registerErrorMessageDiv").css("visibility", "visible");
                return false;
            }
            if ($("#password").val() != $("#repeatpassword").val()) {
                $("span.errorMessage").html("重复密码不一致");
                $("div.registerErrorMessageDiv").css("visibility", "visible");
                return false;
            }
            return true;
        });
    })
</script>

<form method="post" action="foreregister" class="registerForm">
    <div class="registerDiv"><!-- 出错信息，如果注册的时候使用了已经存在的用户名，就会有这个错误提示。 -->
        <div class="registerErrorMessageDiv">
            <div class="alert alert-danger" role="alert">
                <span class="errorMessage"></span>
            </div>
        </div>

        <table class="registerTable" align="center">
            <tr>
                <td class="registerTip registerTableLeftTD">设置会员名</td>
                <td></td>
            </tr>
            <tr>
                <td class="registerTableLeftTD">登陆名</td>
                <td class="registerTableRightTD">
                    <input id="name" name="name"
                           placeholder="会员名一旦设置成功，无法修改"></td>
            </tr>
            <tr>
                <td class="registerTip registerTableLeftTD">设置登陆密码</td>
                <td class="registerTableRightTD">登陆时验证，保护账号信息</td>
            </tr>
            <tr>
                <td class="registerTableLeftTD">登陆密码</td>
                <td class="registerTableRightTD">
                    <input id="password" name="password"
                           type="password" placeholder="设置你的登陆密码"></td>
            </tr>
            <tr>
                <td class="registerTableLeftTD">密码确认</td>
                <td class="registerTableRightTD">
                    <input id="repeatpassword" type="password"
                           placeholder="请再次输入你的密码"></td>
            </tr>

            <tr>
                <td colspan="2" class="registerButtonTD"><!-- colspan="2"表格单元横跨两列的表格,暂时不知道怎么提交的，这个按钮直接向成功页跳转，同时带走了数据。 -->
                    <a href="registerSuccess.jsp">
                        <button>提 交</button>
                    </a>
                </td>
            </tr>
        </table>
    </div>
</form>