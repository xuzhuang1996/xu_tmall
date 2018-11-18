<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix='fmt' %>

<html>
<head>
    <script src="js/jquery/2.0.0/jquery.min.js"></script>
    <link href="css/bootstrap/3.3.6/bootstrap.min.css" rel="stylesheet">
    <script src="js/bootstrap/3.3.6/bootstrap.min.js"></script>
    <link href="css/back/style.css" rel="stylesheet">
    <script>
        function checkEmpty(id, name) {
            var value = $("#" + id).val();
            if (value.length === 0) {
                alert(name + "不能为空");
                $("#" + id)[0].focus();
                return false;
            }
            return true;
        }

        function checkNumber(id, name) {
            var value = $("#" + id).val();
            if (value.length === 0) {
                alert(name + "不能为空");
                $("#" + id)[0].focus();
                return false;
            }
            if (isNaN(value)) {
                alert(name + "必须是数字");
                $("#" + id)[0].focus();
                return false;
            }
            return true;
        }

        function checkInt(id, name) {
            var value = $("#" + id).val();
            if (value.length === 0) {
                alert(name + "不能为空");
                $("#" + id)[0].focus();
                return false;
            }
            if (parseInt(value) !== value) {
                alert(name + "必须是整数");
                $("#" + id)[0].focus();
                return false;
            }
            return true;
        }

        $(function () {
            $("a").click(function () {
            	//在删除的链接按钮a下，自定义deleteLink属性，如果没有点击删除按钮，
            	//这里对所有的a都这样处理了，但是不是每一个a都有deleteLink属性的。因此只有有这个属性的，才为true。
                var deleteLink = $(this).attr("deleteLink");
                console.log(deleteLink);//其他的没有这个属性的，点击会打印undefined的
                if ("true" === deleteLink) {
                	//如果用户点击确定按钮，则 confirm() 返回 true。相当于alert，由用户点击。如果点击取消按钮，则 confirm() 返回 false
                    var confirmDelete = confirm("确认要删除");
                    return confirmDelete;
                }
            });
        })
    </script>
</head>
<body>