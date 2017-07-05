<#import "../display.ftl" as display/>
<html>
<head>
    <title>Product-产品</title>

    <!-- 配置文件 -->
    <script type="text/javascript" src="${absoluteContextPath}/js/ueditor/ueditor.config.js"></script>
    <!-- 编辑器源码文件 -->
    <script type="text/javascript" src="${absoluteContextPath}/js/ueditor/ueditor.all.min.js"></script>
    <script type="text/javascript" charset="utf-8" src="${absoluteContextPath}/js/ueditor/lang/zh-cn/zh-cn.js"></script>
    <script type="text/javascript" charset="utf-8" src="${absoluteContextPath}/js/ueditor/ueditor.parse.min.js"></script>
    <script type="text/javascript">
        uParse("#productContent",{
            rootPath:"${absoluteContextPath}/js/ueditor/"
        });
    </script>

</head>
<body>
<section>
    <div class="container">
        <div class="row">
            <div class="span5 content_with_right_sidebar">
                <img id="preview" src="<@display.pictureUrl product.pictureList[0]!''/>" class="portfolio_item_image" alt="">
            </div>
            <div id="portfolio_item_meta" class="span7">
                <h2 class="column_title_left">${product.name!""}</h2>
                <p>
                    ${product.description!""}
                </p>
                <#if product.link?? && product.link!="">
                    <p class="portfolio_item_view_link">
                        <a href="${product.link}" target="_self">立即购买</a>
                    </p>
                </#if>
                <p class="post_meta_share portfolio_share_social">
                    <#list product.pictureList as picture>
                        <a href="javascript:void(0)">
                            <img src="<@display.pictureUrl picture!''/>" onclick="$('#preview').attr('src', $(this).attr('src'))"/>
                        </a>
                    </#list>
                </p>
            </div>
            <div class="clearfix"></div>
            <#if product.detailDO?? && product.detailDO.propertyArr?? && product.detailDO.propertyArr?size gt 0>
                <#assign tmpRow = ((product.detailDO.propertyArr?size + 2)/3)?int/>
                <table class="Product-parameter">
                    <tbody>
                        <tr>
                            <td colspan="3"> <strong>产品参数</strong> </td>
                        </tr>
                        <#list 1..tmpRow as i>
                            <tr>
                                <#list 0..2 as j>
                                    <#assign tmpIndex = j * tmpRow + i />
                                    <#if tmpIndex lte product.detailDO.propertyArr?size>
                                        <#assign detail = product.detailDO.propertyArr[tmpIndex-1]/>
                                    <#else>
                                        <#assign detail="&nbsp;"/>
                                    </#if>
                                    <td title="${detail!""}">${detail!""}</td>
                                </#list>
                            </tr>
                        </#list>
                    </tbody>
                </table>
            </#if>
            <div class="pic-view" id="productContent">
                ${product.detailDO.detail!""}
            </div>
        </div>
    </div>
</section>
</body>
</html>