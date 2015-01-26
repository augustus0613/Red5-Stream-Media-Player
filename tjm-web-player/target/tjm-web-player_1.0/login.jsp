<%-- 
    Document   : login
    Created on : 2013/10/26, 上午 12:37:45
    Author     : larry
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>登入</title>
        <%-- EasyUI CSS --%>
        <link rel="stylesheet" type="text/css"
              href="http://www.jeasyui.com/easyui/themes/black/easyui.css">
        <link rel="stylesheet" type="text/css"
              href="http://www.jeasyui.com/easyui/themes/icon.css">
        <%-- JQuery --%>
        <script src="http://code.jquery.com/jquery-1.6.min.js"></script>
        <%-- JQuery EasyUI --%>
        <script type="text/javascript"
        src="http://www.jeasyui.com/easyui/jquery.easyui.min.js"></script>
    </head>
    <body style="background-color: #cccccc;">

        <div id="container" style="margin:100px auto;width:300px;height: 300px;">
            <div class="easyui-panel" title="登入" style="padding:0px 75px;"
                 data-options="closable:false,collapsible:false,minimizable:false,maximizable:false,fit:true">

                <div id="right" style="width:145px">
                    <form action="<%=request.getContextPath()%>/login" method="post">
                        <div style="margin: 15px auto;width:130px;padding-top:10px;padding-bottom: 20px;font-weight: bold;">請輸入您的帳號和密碼</div>
                        <div style="margin-bottom:20px">
                            <div class="field">帳 號:</div>
                            <input class="input" name="j_username" type="text" maxlength="10"/>
                        </div>
                        <div>
                            <div class="field">密 碼:</div>
                            <input class="input" name="j_password" type="password" maxlength="10"/>
                        </div>
                        <!--                    <div id="remeber">
                                                <label><input type="checkbox" name="_spring_security_remember_me"/>記住我</label>
                                            </div>-->
                        <br>
                        <div id="message" style="width:115px;margin:auto;">&nbsp;${sessionScope.SPRING_SECURITY_LAST_EXCEPTION.message}</div>	
                        <br>
                        <div style="width: 50px;margin:auto;">
                            <input id="submit" name="submit" type="submit" value="登入"/>
                        </div>
                    </form>
                </div>	


            </div>
        </div>
    </body>
</html>
