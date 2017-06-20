<#import "../display.ftl" as display/>
<html>
<head>
    <title>Product-产品</title>
</head>
<body>
<section>
    <div class="container">
        <div class="row">
            <div class="span5 content_with_right_sidebar">
                <img src="<@display.pictureUrl product.cover!''/>" class="portfolio_item_image" alt="">
            </div>
            <div id="portfolio_item_meta" class="span7">
                <h2 class="column_title_left">${product.name!""}</h2>
                <p>
                    ${product.description!""}
                </p>
                <p class="portfolio_item_view_link">
                    <a href="javascript:void(0)" target="_self">立即购买</a>
                </p>
                <p class="post_meta_share portfolio_share_social">
                    <#list product.pictureList as picture>
                        <a href="javascript:void(0)">
                            <img src="<@display.pictureUrl picture!''/>" />
                        </a>
                    </#list>
                </p>
            </div>
            <div class="clearfix"></div>
            <#if product.detailDO?? && product.detailDO.propertyArr?? && product.detailDO.propertyArr?size gt 0>
                <table class="Product-parameter">
                    <tbody>
                        <tr>
                            <td colspan="3"> <strong>产品参数</strong> </td>
                        </tr>
                        <#list 1..(product.detailDO.propertyArr?size + 2)/3 as i>
                            <tr>
                                <#list 0..2 as j>
                                    <td>
                                        <#if ((i-1)*3 + j) lt product.detailDO.propertyArr?size>
                                            ${product.detailDO.propertyArr[(i-1)*3 + j]!""}
                                        <#else>
                                            &nbsp;
                                        </#if>
                                    </td>
                                </#list>
                            </tr>
                        </#list>
                    </tbody>
                </table>
            </#if>

            <div class="pic-view">
            </div>
        </div>
    </div>
</section>
</body>
</html>