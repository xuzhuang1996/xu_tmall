<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script>
    var deleteOrderItem = false;
    var deleteOrderItemid = 0;
    $(function () {
        //点击删除的时候就会弹出来。，id="deleteConfirmModal"在modal.jsp
        $("a.deleteOrderItem").click(function () {
            deleteOrderItem = false;
            var oiid = $(this).attr("oiid")
            deleteOrderItemid = oiid;
            $("#deleteConfirmModal").modal('show');
        });
        $("button.deleteConfirmButton").click(function () {
            deleteOrderItem = true;
            $("#deleteConfirmModal").modal('hide');
        });
        //当弹出的模态框消失的时候接下来回调的函数,就是删除按钮确定的时候deleteOrderItem=true  hidden.bs.modal
        $('#deleteConfirmModal').on('hidden.bs.modal', function (e) {
            if (deleteOrderItem) {
                var page = "foredeleteOrderItem";
                $.post(
                    page,
                    {"oiid": deleteOrderItemid},
                    function (result) {
                        if ("success" == result) {
                            //$("tr.cartProductItemTR[oiid=" + deleteOrderItemid + "]").hide();
                            $("tr.cartProductItemTR[oiid=" + deleteOrderItemid + "]").remove();
                        } else {
                            location.href = "login.jsp";
                        }
                    }
                );
            }
        })

        //给这个类进行绑定点击函数-打勾或去掉产生相应的总价-这里所谓的打勾其实就是图片的切换并不是真的打勾
        $("img.cartProductItemIfSelected").click(function () {
            var selectit = $(this).attr("selectit")
            //如果已经选择后再次点击就是取消选择了 
            if ("selectit" == selectit) {
                $(this).attr("src", "img/site/cartNotSelected.png");
                $(this).attr("selectit", "false")
                $(this).parents("tr.cartProductItemTR").css("background-color", "#fff");
            } else {
                $(this).attr("src", "img/site/cartSelected.png");
                $(this).attr("selectit", "selectit")
                $(this).parents("tr.cartProductItemTR").css("background-color", "#FFF8E1");
            }
            syncSelect();//如果所有的都打勾了，那么selectall将打勾，否则不打勾 
            syncCreateOrderButton();//只要有一个订单项打勾，提交订单的按钮就是可用的，否则不可用
            calcCartSumPriceAndNumber();//计算总价跟总数
        });
        //全选时如果全选图片的属性表示已经全选就全不选-否则全选 
        $("img.selectAllItem").click(function () {
            var selectit = $(this).attr("selectit")
            if ("selectit" == selectit) {
                $("img.selectAllItem").attr("src", "img/site/cartNotSelected.png");
                $("img.selectAllItem").attr("selectit", "false")
                $(".cartProductItemIfSelected").each(function () {
                    $(this).attr("src", "img/site/cartNotSelected.png");
                    $(this).attr("selectit", "false");
                    $(this).parents("tr.cartProductItemTR").css("background-color", "#fff");
                });
            } else {
                $("img.selectAllItem").attr("src", "img/site/cartSelected.png");
                $("img.selectAllItem").attr("selectit", "selectit")
                $(".cartProductItemIfSelected").each(function () {
                    $(this).attr("src", "img/site/cartSelected.png");
                    $(this).attr("selectit", "selectit");
                    $(this).parents("tr.cartProductItemTR").css("background-color", "#FFF8E1");
                });
            }
            syncCreateOrderButton();//对提交订单按钮进行处理-是否可用
            calcCartSumPriceAndNumber();//重新计算价格
        });

        $(".orderItemNumberSetting").keyup(function () {
            var pid = $(this).attr("pid");
            var stock = $("span.orderItemStock[pid=" + pid + "]").text();
            var price = $("span.orderItemPromotePrice[pid=" + pid + "]").text();
            var num = $(".orderItemNumberSetting[pid=" + pid + "]").val();
            num = parseInt(num);
            if (isNaN(num))
                num = 1;
            if (num <= 0)
                num = 1;
            if (num > stock)
                num = stock;
            var oiid = $("tr.cartProductItemTR").attr("oiid");
            syncPrice(pid, num, price, oiid);
        });

        $(".numberPlus").click(function () {
            var pid = $(this).attr("pid");
            var stock = $("span.orderItemStock[pid=" + pid + "]").text();
            var price = $("span.orderItemPromotePrice[pid=" + pid + "]").text();
            var num = $(".orderItemNumberSetting[pid=" + pid + "]").val();
            num++;
            if (num > stock)
                num = stock;
            var oiid = $("tr.cartProductItemTR").attr("oiid");
            syncPrice(pid, num, price, oiid);
        });
        $(".numberMinus").click(function () {
            var pid = $(this).attr("pid");
            var stock = $("span.orderItemStock[pid=" + pid + "]").text();
            var price = $("span.orderItemPromotePrice[pid=" + pid + "]").text();
            var num = $(".orderItemNumberSetting[pid=" + pid + "]").val();
            --num;
            if (num <= 0)
                num = 1;
            var oiid = $("tr.cartProductItemTR").attr("oiid");
            syncPrice(pid, num, price, oiid);//动态改变订单项。就是加减的时候，进行了数据库操作
        });

        //提交订单事件-进入结算页面 
        $("button.createOrderButton").click(function () {
            var params = "";
            $(".cartProductItemIfSelected").each(function () {
                if ("selectit" == $(this).attr("selectit")) {
                    var oiid = $(this).attr("oiid");
                    params += "&oiid=" + oiid;
                }
            });
            params = params.substring(1);
            location.href = "forebuy?" + params;
        });
    })

    function syncCreateOrderButton() {
        var selectAny = false;
        $(".cartProductItemIfSelected").each(function () {
            if ("selectit" == $(this).attr("selectit"))
                selectAny = true;
        });

        if (selectAny) {
            $("button.createOrderButton").css("background-color", "#C40000");
            $("button.createOrderButton").removeAttr("disabled");
        } else {
            $("button.createOrderButton").css("background-color", "#AAAAAA");
            $("button.createOrderButton").attr("disabled", "disabled");
        }
    }

    function syncSelect() {
        var selectAll = true;
        $(".cartProductItemIfSelected").each(function () {
            if ("false" == $(this).attr("selectit"))
                selectAll = false;
        });

        if (selectAll)
            $("img.selectAllItem").attr("src", "img/site/cartSelected.png");
        else
            $("img.selectAllItem").attr("src", "img/site/cartNotSelected.png");
    }

    function calcCartSumPriceAndNumber() {
        var sum = 0;//所有订单项的总价
        var totalNumber = 0;//所有订单项的数目  
        //所有属性为选中状态的订单项 进行计算 
        $("img.cartProductItemIfSelected[selectit='selectit']").each(function () {
            var oiid = $(this).attr("oiid");
            var price = $(".cartProductItemSmallSumPrice[oiid=" + oiid + "]").text();
            price = price.replace(/,/g, "");
            price = price.replace(/￥/g, "");
            sum += new Number(price);
            var num = $(".orderItemNumberSetting[oiid=" + oiid + "]").val();
            totalNumber += new Number(num);
        });

        $("span.cartSumPrice").html("￥" + formatMoney(sum));
        $("span.cartTitlePrice").html("￥" + formatMoney(sum));
        $("span.cartSumNumber").html(totalNumber);
    }

    //动态调整订单项,这里跟博主不一样在于，pid我没用了，直接用的oiid。这样servlet里面实现的简单一点
    function syncPrice(pid, num, price, oiid) {
        $(".orderItemNumberSetting[pid=" + pid + "]").val(num);
        var cartProductItemSmallSumPrice = formatMoney(num * price);
        $(".cartProductItemSmallSumPrice[pid=" + pid + "]").html("￥" + cartProductItemSmallSumPrice);
        calcCartSumPriceAndNumber();

        
        var page = "forechangeOrderItem";
        $.post(
            page,
            {"oiid": oiid, "num": num},
            function (result) {
                if ("success" != result)
                    location.href = "login.jsp";
            }
        );
    }
</script>

<title>购物车</title>
<div class="cartDiv">
    <div class="cartTitle pull-right">
        <span>已选商品  (不含运费)</span>
        <span class="cartTitlePrice">￥0.00</span>
        <button class="createOrderButton" disabled="disabled">结 算</button>
    </div>

    <div class="cartProductList">
        <table class="cartProductTable">
            <thead>
            <tr>
                <th class="selectAndImage">
                    <img selectit="false" class="selectAllItem" src="img/site/cartNotSelected.png">
                    全选
                </th>
                <th>商品信息</th>
                <th>单价</th>
                <th>数量</th>
                <th width="120px">金额</th>
                <th class="operation">操作</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${ois }" var="oi">
                <tr oiid="${oi.id}" class="cartProductItemTR">
                    <td>
                        <img selectit="false" oiid="${oi.id}" class="cartProductItemIfSelected"
                             src="img/site/cartNotSelected.png">
                        <a style="display:none" href="#nowhere">
                            <img src="img/site/cartSelected.png">
                        </a>
                        <img class="cartProductImg"
                             src="img/productSingle_middle/${oi.product.firstProductImage.id}.jpg">
                    </td>
                    <td>
                        <div class="cartProductLinkOutDiv">
                            <a href="foreproduct?pid=${oi.product.id}"
                               class="cartProductLink">${oi.product.name}</a>
                            <div class="cartProductLinkInnerDiv">
                                <img src="img/site/creditcard.png" title="支持信用卡支付">
                                <img src="img/site/7day.png" title="消费者保障服务,承诺7天退货">
                                <img src="img/site/promise.png" title="消费者保障服务,承诺如实描述">
                            </div>
                        </div>
                    </td>
                    <td>
                        <span class="cartProductItemOringalPrice">￥${oi.product.orignalPrice}
                        </span>
                        <span class="cartProductItemPromotionPrice">￥${oi.product.promotePrice}
                        </span>
                    </td>
                    <td>
                        <div class="cartProductChangeNumberDiv">
                            <span class="hidden orderItemStock "
                                  pid="${oi.product.id}">${oi.product.stock}</span>
                            <span class="hidden orderItemPromotePrice "
                                  pid="${oi.product.id}">${oi.product.promotePrice}</span>
                            <a pid="${oi.product.id}" class="numberMinus" href="#nowhere">-</a>
                            <label>
                                <input pid="${oi.product.id}" oiid="${oi.id}"
                                       class="orderItemNumberSetting" autocomplete="off"
                                       value="${oi.number}">
                            </label>
                            <a stock="${oi.product.stock}" pid="${oi.product.id}" class="numberPlus"
                               href="#nowhere">+</a>
                        </div>

                    </td>
                    <td>
							<span class="cartProductItemSmallSumPrice" oiid="${oi.id}"
                                  pid="${oi.product.id}">
							￥<fmt:formatNumber type="number"
                                               value="${oi.product.promotePrice*oi.number}"
                                               minFractionDigits="2"/>
							</span>
                    </td>
                    <td>
                        <a class="deleteOrderItem" oiid="${oi.id}" href="#nowhere">删除</a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>

    <div class="cartFoot">
        <img selectit="false" class="selectAllItem" src="img/site/cartNotSelected.png">
        <span>全选</span>
        <div class="pull-right">
            <span>已选商品 <span class="cartSumNumber">0</span> 件</span>
            <span>合计 (不含运费): </span>
            <span class="cartSumPrice">￥0.00</span>
            <button class="createOrderButton" disabled="disabled">结 算</button>
        </div>
    </div>
</div>