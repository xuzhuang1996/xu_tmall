<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script>
    $(function () {
        var stock = ${p.stock};
        //在页面的数量那里，点击就会增加或者减少
        $(".productNumberSetting").keyup(function () {
            var num = $(".productNumberSetting").val();
            num = parseInt(num);
            if (isNaN(num))
                num = 1;
            if (num <= 0)
                num = 1;
            if (num > stock)
                num = stock;
            $(".productNumberSetting").val(num);
        });

        $(".increaseNumber").click(function () {
            var num = $(".productNumberSetting").val();
            num++;
            if (num > stock)
                num = stock;
            $(".productNumberSetting").val(num);
        });
        $(".decreaseNumber").click(function () {
            var num = $(".productNumberSetting").val();
            --num;
            if (num <= 0)
                num = 1;
            $(".productNumberSetting").val(num);
        });
        //增加购物车按钮,get的方式确认登录后，回调再来一个get请求。去执行加入购物车操作
        $(".addCartButton").removeAttr("disabled");
        $(".addCartLink").click(function () {
            var page = "forecheckLogin";
            $.get(
                page,
                function (result) {
                    if ("success" == result) {
                        var pid = ${p.id};
                        var num = $(".productNumberSetting").val();
                        var addCartpage = "foreaddCart";
                        $.get(
                            addCartpage,
                            {"pid": pid, "num": num},
                            function (result) {
                                if ("success" == result) {
                                    $(".addCartButton").html("已加入购物车");
                                    $(".addCartButton").attr("disabled", "disabled");
                                    $(".addCartButton").css("background-color", "lightgray")
                                    $(".addCartButton").css("border-color", "lightgray")
                                    $(".addCartButton").css("color", "black")
                                } else {
                                }
                            }
                        );
                    } else {
                        $("#loginModal").modal('show');//需要一种美观的弹出框，js原生的alert，很难满足需求。这里推荐一个bootstrap的弹出框
                    }
                }
            );
            return false;//return false表示超链点击本身的跳转功能取消掉
        });
        $(".buyLink").click(function () {
            var page = "forecheckLogin";
            $.get(
                page,
                function (result) {
                    if ("success" == result) {
                        var num = $(".productNumberSetting").val();
                        location.href = $(".buyLink").attr("href") + "&num=" + num;
                    } else {
                        $("#loginModal").modal('show');
                    }
                }
            );
            return false;
        });
        //当modal出现的时候去登录，点击按钮后。进行登录.小碧池用的class样式loginSubmitButton
        $("button.loginSubmitButton").click(function () {
            var name = $("#name").val();
            var password = $("#password").val();

            if (0 == name.length || 0 == password.length) {
                $("span.errorMessage").html("请输入账号密码");
                $("div.loginErrorMessageDiv").show();
                return false;
            }

            var page = "foreloginAjax";
            $.get(
                page,
                {"name": name, "password": password},
                function (result) {
                    if ("success" == result) {
                        location.reload();//给session加入用户后，重新加载页面
                    } else {
                        $("span.errorMessage").html("账号密码错误");
                        $("div.loginErrorMessageDiv").show();
                    }
                }
            );
            return true;
        });

        $("img.smallImage").mouseenter(function () {
            var bigImageURL = $(this).attr("bigImageURL");
            $("img.bigImg").attr("src", bigImageURL);
        });

        $("img.bigImg").load(
            function () {
                $("img.smallImage").each(function () {
                    var bigImageURL = $(this).attr("bigImageURL");
                    img = new Image();
                    img.src = bigImageURL;
                    img.onload = function () {
                        $("div.img4load").append($(img));
                    };
                });
            }
        );
    });

</script>

<div class="imgAndInfo">
    <div class="imgInimgAndInfo"><!-- 显示本商品的5个单独图片 -->
        <img src="img/productSingle/${p.firstProductImage.id}.jpg" class="bigImg">
        <div class="smallImageDiv">
            <c:forEach items="${p.productSingleImages}" var="pi">
                <img src="img/productSingle_small/${pi.id}.jpg"
                     bigImageURL="img/productSingle/${pi.id}.jpg" class="smallImage">
            </c:forEach>
        </div>
        <div class="img4load hidden"></div>
    </div>

    <div class="infoInimgAndInfo">
        <div class="productTitle">
            ${p.name}
        </div>
        <div class="productSubTitle">
            ${p.subTitle}
        </div>

        <div class="productPrice">
            <div class="juhuasuan">
                <span class="juhuasuanBig">聚划算</span>
                <span>此商品即将参加聚划算，<span class="juhuasuanTime">1天19小时</span>后开始，</span>
            </div>
            <div class="productPriceDiv">
                <div class="gouwujuanDiv"><img height="16px" src="img/site/gouwujuan.png">
                    <span> 全天猫实物商品通用</span>
                </div>
                <div class="originalDiv">
                    <span class="originalPriceDesc">价格</span>
                    <span class="originalPriceYuan">¥</span>
                    <span class="originalPrice">
                        <fmt:formatNumber type="number" value="${p.orignalPrice}"
                                          minFractionDigits="2"/>
                    </span>
                </div>
                <div class="promotionDiv">
                    <span class="promotionPriceDesc">促销价</span>
                    <span class="promotionPriceYuan">¥</span>
                    <span class="promotionPrice">
                        <fmt:formatNumber type="number" value="${p.promotePrice}"
                                          minFractionDigits="2"/>
                    </span>
                </div>
            </div>
        </div>
        <div class="productSaleAndReviewNumber">
            <div>销量 <span class="redColor boldWord"> ${p.saleCount }</span></div>
            <div>累计评价 <span class="redColor boldWord"> ${p.reviewCount}</span></div>
        </div>
        <div class="productNumber">
            <span>数量</span>
            <span>
                <span class="productNumberSettingSpan">
                <label>
                    <input class="productNumberSetting" type="text" value="1">
                </label>
                </span>
                <span class="arrow">
                    <a href="#nowhere" class="increaseNumber">
                    <span class="updown">
                            <img src="img/site/increase.png">
                    </span>
                    </a>
                     
                    <span class="updownMiddle"> </span>
                    <a href="#nowhere" class="decreaseNumber">
                    <span class="updown">
                            <img src="img/site/decrease.png">
                    </span>
                    </a>
                </span>
            件</span>
            <span>库存${p.stock}件</span>
        </div>
        <div class="serviceCommitment">
            <span class="serviceCommitmentDesc">服务承诺</span>
            <span class="serviceCommitmentLink">
                <a href="#nowhere">正品保证</a>
                <a href="#nowhere">极速退款</a>
                <a href="#nowhere">赠运费险</a>
                <a href="#nowhere">七天无理由退换</a>
            </span>
        </div>

        <div class="buyDiv">
            <a class="buyLink" href="forebuyone?pid=${p.id}">
                <button class="buyButton">立即购买</button>
            </a>
            <a href="#nowhere" class="addCartLink">
                <button class="addCartButton"><span
                        class="glyphicon glyphicon-shopping-cart"></span>加入购物车
                </button>
            </a>
        </div>
    </div>
    <div style="clear:both"></div>
</div>