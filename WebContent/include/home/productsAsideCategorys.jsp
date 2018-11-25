<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<script src="js/jquery/2.0.0/jquery.min.js"></script>
<link href="css/bootstrap/3.3.6/bootstrap.min.css" rel="stylesheet">
<script src="js/bootstrap/3.3.6/bootstrap.min.js"></script>
<link href="css/fore/style.css" rel="stylesheet">

<script>
    //这个是用于随机挑选一个产品作为推荐产品，来进行高亮显示。
    $(function () {
        $("div.productsAsideCategorys div.row a").each(function () {
            var v = Math.round(Math.random() * 6);
            if (v === 1)
                $(this).css("color", "#87CEFA");
        });
    });

</script>

<c:forEach items="${cs}" var="c"><!-- 拿到所有的分类，对某分类 遍历-->
    <div cid="${c.id}" class="productsAsideCategorys">
        <c:forEach items="${c.productsByRow}" var="ps"><!-- 拿到某分类的所有产品，已经按行列分好。对行遍历 -->
            <div class="row show1">
                <c:forEach items="${ps}" var="p"><!-- 拿到某一行的所有产品，对某一产品遍历 -->
                    <c:if test="${!empty p.subTitle}">
                        <a href="foreproduct?pid=${p.id}">
                            <c:forEach items="${fn:split(p.subTitle, ' ')}" var="title" varStatus="st">
                                <c:if test="${st.index==0}">
                                    ${title}
                                </c:if>
                            </c:forEach>
                        </a>
                    </c:if>
                </c:forEach>
                <div class="seperator"></div>
            </div>
        </c:forEach>
    
    </div>
</c:forEach>