jQuery(document).ready(function(){
    //cache DOM elements
    var mainContent = $('.cd-main-content'),
        header = $('.cd-main-header'),
        sidebar = $('.cd-side-nav'),
        sidebarTrigger = $('.cd-nav-trigger'),
        topNavigation = $('.cd-top-nav'),
        searchForm = $('.cd-search'),
        accountInfo = $('.account');
    //on window scrolling - fix sidebar nav
    var scrolling = false;
    checkScrollbarPosition();
    $(window).on('scroll', function(){
        if( !scrolling ) {
            (!window.requestAnimationFrame) ? setTimeout(checkScrollbarPosition, 300) : window.requestAnimationFrame(checkScrollbarPosition);
            scrolling = true;
        }
    });
    //mobile only - open sidebar when user clicks the hamburger menu
    sidebarTrigger.on('click', function(event){
        event.preventDefault();
        $([sidebar, sidebarTrigger]).toggleClass('nav-is-visible');
    });

    function checkMQ() {
        //check if mobile or desktop device
        return window.getComputedStyle(document.querySelector('.cd-main-content'), '::before').getPropertyValue('content').replace(/'/g, "").replace(/"/g, "");
    }

    function moveNavigation(){
        var mq = checkMQ();

        if ( mq == 'mobile' && topNavigation.parents('.cd-side-nav').length == 0 ) {
            detachElements();
            topNavigation.appendTo(sidebar);
            searchForm.removeClass('is-hidden').prependTo(sidebar);
        } else if ( ( mq == 'tablet' || mq == 'desktop') &&  topNavigation.parents('.cd-side-nav').length > 0 ) {
            detachElements();
            searchForm.insertAfter(header.find('.cd-logo'));
            topNavigation.appendTo(header.find('.cd-nav'));
        }
        checkSelected(mq);
        resizing = false;
    }

    function detachElements() {
        topNavigation.detach();
        searchForm.detach();
    }

    function checkSelected(mq) {
        //on desktop, remove selected class from items selected on mobile/tablet version
        if( mq == 'desktop' ) $('.has-children.selected').removeClass('selected');
    }

    function checkScrollbarPosition() {
        var mq = checkMQ();
    }
});