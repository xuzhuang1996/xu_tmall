<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@include file="../include/admin/adminHeader.jsp" %>
<%@include file="../include/admin/adminNavigator.jsp" %>

<title>编辑产品属性值</title>

<script>
    $(function () {
        $("input.pvValue").keyup(function () {
            const value = $(this).val();//拿到正在编辑的属性值
            const page = "admin_Product_updatePropertyValue";
            const pvid = $(this).attr("pvid");//拿到正在编辑的框的属性值id
            const parentSpan = $(this).parent("span");//拿到正在编辑的框的父节点。span标签
            parentSpan.css("border", "1px solid yellow");//编辑成功就对父节点的样式进行修改
            //使用 AJAX 的 POST 请求,
            $.post(
                page,//规定将请求发送到哪个 URL。
                {"value": value, "pvid": pvid},//data,规定连同请求发送到服务器的数据
                //data - 包含来自请求的结果数据,如果只有一个参数，那就是你了。
                //status - 包含请求的状态（"success"、"notmodified"、"error"、"timeout"、"parsererror"）
                //xhr - 包含 XMLHttpRequest 对象
                function (result) {
                    if ("success" === result)
                        parentSpan.css("border", "2px solid green");//规定当请求成功时运行的函数
                    else
                    {
                    	//alert(result);
                    	parentSpan.css("border", "2px solid red");
                    }
                        
                }
            );
        });
    });
</script>

<div class="workingArea">
    <ol class="breadcrumb">
        <li><a href="admin_Category_list">所有分类</a></li>
        <li><a href="admin_Product_list?cid=${p.category.id}">${p.category.name}</a></li>
        <li class="active">${p.name}</li>
        <li class="active">编辑产品属性</li>
    </ol>

    <div class="editPVDiv">
        <c:forEach items="${pvs}" var="pv">
            <div class="eachPV">
                <span class="pvName">${pv.property.name}</span>
                <span class="pvValue">
                    <input class="pvValue" pvid="${pv.id}" type="text"
                           value="${pv.value}">
                </span>
            </div>
        </c:forEach>
        <div style="clear:both">
        </div>
    </div>
</div>