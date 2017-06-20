<#assign sec=JspTaglibs["/WEB-INF/customTags/security.tld"]>
<div class="page-sidebar sidebar">
    <div class="page-sidebar-inner item ">
        <ul class="menu accordion-menu sidebar-menu" id="child-menu">
            <@sec.authorize action="user-list">
                <li class="nav-line"></li>
                <li  for="privilege-user"> <a id="user" class="waves-effect waves-button" href="${absoluteContextPath}/privilege/user/list">用户管理</a> </li>
            </@sec.authorize>
            <@sec.authorize action="role-operate">
                <li class="nav-line"></li>
                <li  for="privilege-role"> <a id="role" class="waves-effect waves-button" href="${absoluteContextPath}/privilege/role/list" >角色管理</a> </li>
            </@sec.authorize>
            <@sec.authorize action="param-list">
                <li class="nav-line"></li>
                <li  for="param-list"> <a id="param" class="waves-effect waves-button" href="${absoluteContextPath}/param/list" >系统参数</a> </li>
            </@sec.authorize>
        </ul>
    </div>
</div>
<script>
    $.sidebarMenu = function(menu) {
        var animationSpeed = 300;
        $(menu).on('click', 'li a', function(e) {
            var $this = $(this);
            var checkElement = $this.next();

            if (checkElement.is('.sub-menu') && checkElement.is(':visible')) {
                checkElement.slideUp(animationSpeed, function() {
                    checkElement.removeClass('menu-open');
                });
                checkElement.parent("li").removeClass("active");
            }
            //If the menu is not visible
            else if ((checkElement.is('.sub-menu')) && (!checkElement.is(':visible'))) {
                //Get the parent menu
                var parent = $this.parents('ul').first();
                //Close all open menus within the parent
                var ul = parent.find('ul:visible').slideUp(animationSpeed);
                //Remove the menu-open class from the parent
                ul.removeClass('menu-open');
                //Get the parent li
                var parent_li = $this.parent("li");

                //Open the target menu and add the menu-open class
                checkElement.slideDown(animationSpeed, function() {
                    //Add the class active to the parent li
                    checkElement.addClass('menu-open');
                    parent.find('li.active').removeClass('active');
                    parent_li.addClass('active');
                });
            }
            //if this isn't a link, prevent the page from being redirected
            if (checkElement.is('.sub-menu')) {
                e.preventDefault();
            }
        });
    }
    $.sidebarMenu($('.sidebar-menu'))
</script>