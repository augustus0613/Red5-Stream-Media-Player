<%-- 
    Document   : index
    Created on : 2013/8/17, 上午 01:18:28
    Author     : lawrence
--%>
<%@page import="org.tjm.user.security.SecurityUtil"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <%
            request.setCharacterEncoding("UTF-8");
        %>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>EasyUI Media Player</title>
        <%-- EasyUI CSS --%>
        <link rel="stylesheet" type="text/css"
              href="http://www.jeasyui.com/easyui/themes/black/easyui.css">
        <link rel="stylesheet" type="text/css"
              href="http://www.jeasyui.com/easyui/themes/icon.css">
        <%-- JQuery --%>
        <script src="http://code.jquery.com/jquery-1.6.min.js"></script>
        <%-- JQuery EasyUI  --%>
        <script type="text/javascript"
        src="http://www.jeasyui.com/easyui/jquery.easyui.min.js"></script>
        <%-- jwplayer --%>
        <%-- <script src="${pageContext.request.contextPath}/js/jwplayer/jwplayer.js"></script> --%>
        <script
        src="${pageContext.request.contextPath}/js/jwplayer-5/jwplayer.js"></script>

        <script type="text/javascript">
            var contextPath = '${pageContext.request.contextPath}';
            var unchecked_nodes = '';
            $(function() {

                $('.funcBlock').bind({
                    mouseover: function() {
                        $(this).css('text-decoration', 'underline');
                    },
                    mouseout: function() {
                        $(this).css('text-decoration', 'none');
                    }
                });
                $('#header_logout').bind({
                    mouseover: function() {
                        $(this).css('text-decoration', 'underline');
                    },
                    mouseout: function() {
                        $(this).css('text-decoration', 'none');
                    },
                    click: function() {
                        location.href = contextPath + '/logout';
                    }
                });
                $('.home').bind('click', function() {
                    location.href = contextPath;
                });
                $('.auth').bind('click', function() {
                    location.href = contextPath + '/setting/auth/';
                });

                // window 參數紀錄開啟之設定視窗
                var window;

                // 點選 「設定」 中的 「可對外內容」
                $('.visibility').bind('click', function() {
                    $('#win').show();
                    $('#win').window({
                        title: '請勾選「可對外放映」的影片資料夾',
                        shadow: true,
                        width: 600,
                        height: 300,
                        modal: true
                    });
                    window = $('#win').window('open');
                });

                // 設定視窗中點選"OK"
                $('#window_confirm').bind('click', function() {
                    // 取得所有已勾選的節點
                    var nodes = $('#folder_tree').tree('getChecked');
                    var checked_nodes = '';

                    for (var i = 0; i < nodes.length; i++) {
                        if (checked_nodes != '')
                            checked_nodes += ',';
                        checked_nodes += nodes[i].id;
                    }
                    // 儲存設定
                    $.post(contextPath + '/setting/visibility/save', {
                        checked_ids: checked_nodes,
                        unchecked_ids: unchecked_nodes
                    }, function(data) {

                        $.messager.show({
                            title: 'Message',
                            msg: data.message,
                            timeout: 3000,
                            showType: 'slide'
                        });

                    }, 'json');
                    // 關閉視窗            
                    window.window('close');
                    $('#win').hide();
                });

                // 設定視窗中點選"Cancel"
                $('#window_cancel').bind('click', function() {
                    // 關閉視窗
                    window.window('close');
                    $('#win').hide();
                })

            });

            // 影片檔案列表中「點選檔案」
            function onclickCallBack(node) {

                var path = node.id;
                if (!node.attributes.isDirectory) {

                    $
                            .post(
                                    '${pageContext.request.contextPath}/video/stream',
                                    {
                                        path: path
                                    },
                            function(data) {

                                //*************************************//    
                                //                    data[0].file = encodeURI(encodeURI(data[0].file));
                                //*************************************//    

                                console.log(data);
                                jwplayer('jwplayer')
                                        .setup(
                                                {
                                                    flashplayer: '${pageContext.request.contextPath}/js/jwplayer-5/player.swf',
                                                    file: data.file,
                                                    streamer: data.streamer,
                                                    autostart: 'true',
                                                    controlbar: 'bottom',
                                                    width: '848',
                                                    height: '360'
                                                });
                            }, 'json');
                } // if (!node.attributes.isDirectory)

            } // function onclickCallBack(node)

            function isChecked(node) {
                if (node.checked) {
                    console.log(node.id + ' 取消勾選');
                    unchecked_nodes += node.id + ',';

                } else {
                    console.log(node.id + ' 已勾選');
                    var replaced_str = node.id + ',';
                    unchecked_nodes = unchecked_nodes.replace(replaced_str, "");
                }

                console.log('unchecked_nodes = ' + unchecked_nodes);
            }
        </script>
        <style type="text/css">
            .funcBlock {
                float: left;
                width: 80px;
                height: 20px;
                text-align: center;
                color: #B3B3B3;
                font-weight: bold;
                cursor: pointer;
                padding-left: 5px;
                border-right: 1px solid #999;
            }
            #header_left_block{
                width: 900px;
                float: left;
            }
            #header_right_block{
                color: #B3B3B3;
            }
            #header_username{
                float: left;
                width: 120px;
            }
            #header_logout{
                float: left;
                cursor: pointer;
                font-weight: bold;
            }
            .panel-body {
                background-color: #999999;
            }
            .tree-file{
                background:url("icon/video_icon.png") no-repeat scroll 0px 3px rgba(0, 0, 0, 0)
            }
        </style>

    </head>

    <body style="margin: 0px; padding: 0px;">

        <%-- function Row --%>
        <div style="height: 25px; background-color: #3E0470; padding: 15px 30px 0px 30px">
            <div id="header_left_block">
                <div class="funcBlock home">主頁</div>
                <sec:authorize ifAllGranted ="SETTING">
                    <div class="easyui-menubutton funcBlock setting"
                         data-options="menu:'#mm'">設定</div>
                </sec:authorize>
            </div>
            <c:if test="<%=SecurityUtil.getUsername() != null%>">
                <div id="header_right_block" style="float: left;">
                    <div id="header_username">Hello, <%=SecurityUtil.getUsername()%> !</div>
                    <div id="header_logout">登出</div>
                </div>
            </c:if>
            <br style="clear:both;">
        </div>
        <sec:authorize ifAllGranted ="SETTING">
            <div id="mm" style="width: 150px;">
                <div class="auth">權限</div>
                <div class="visibility">可對外內容</div>
            </div>
        </sec:authorize>
        <%-- EasyUI Layout --%>
        <div class="easyui-layout" style="height: 800px;">
            <%--   west region   --%>
            <div data-options="region:'west',title:'檔案列表',split:true"
                 style="width: 250px;">


                <div id="menu" style="width: 250px; height: 450px; float: left;">
                    <ul id="tree" class="easyui-tree"
                        data-options=" 
                        method:'post',
                        url:'<%=request.getContextPath()%>/video/tree',
                        onClick:function(node){
                        onclickCallBack(node);
                        }
                        ">
                    </ul>
                </div>

            </div>

            <%--   center region   --%>
            <div data-options="region:'center',title:'播放器',fit:true"
                 style="padding: 5px;background-color: #cccccc;">

                <%--   jwplayer 播放器區塊  --%>
                <div id="jwplayer"></div>
                <%-- end of jwplayer --%>

            </div>
        </div>
        <%-- end of easyui-layout --%>
        <sec:authorize ifAllGranted ="SETTING">
            <div id="win" style="display: none;">
                <div style="width: 250px; height: 200px;">
                    <ul id="folder_tree" class="easyui-tree"
                        data-options=" 
                        checkbox:true,
                        cascadeCheck:false,
                        method:'post',
                        url:'<%=request.getContextPath()%>/setting/visibility/folder',
                        onCheck:function(node){
                        isChecked(node);
                        },onClick:function(node){
                        console.log(node.id + ',' + node.checked);
                        }
                        ">
                    </ul>
                </div>
                <div style="padding: 5px; text-align: center;">
                    <a href="#" class="easyui-linkbutton" id="window_confirm"
                       icon="icon-ok">Ok</a> 
                    <a href="#" class="easyui-linkbutton" id="window_cancel"
                       icon="icon-cancel">Cancel</a>
                </div>
            </div>
        </sec:authorize>


        <%-- auth setting window --%>
    </body>
</html>
