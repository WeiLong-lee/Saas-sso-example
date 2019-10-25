<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <title>登录界面</title>
    <link rel="stylesheet" href="../css/reset.css"/>
    <link rel="stylesheet" href="../css/common.css"/>
    <link rel="stylesheet" href="../css/font-awesome.min.css"/>
</head>
<body>
<div class="wrap login_wrap">
    <div class="content">
        <div class="logo"></div>
        <div class="login_box">

            <div class="login_form" style="height: 260px;">
                <div class="login_title">
                    注册
                </div>
                <form >

                    <div class="form_text_ipt">
                        <input id="username" name="username" type="text" placeholder="用户名" >
                    </div>
                    <div class="ececk_warning"><span>手机号/邮箱不能为空</span></div>
                    <div class="_warning"><span>${message!''}</span></div>
                    <div class="form_text_ipt">
                        <input id="password" name="password" type="password" placeholder="密码" >
                    </div>
                    <div class="ececk_warning"><span>密码不能为空</span></div>
                    <div class="form_check_ipt">
                        <div class="left check_left">
<#--                            <label><input name="remember-me" type="checkbox"> 下次自动登录</label>-->
                        </div>
                        <div class="right check_right">
<#--                            <a href="#">忘记密码</a>-->
                        </div>
                    </div>
                    <div class="form_btn">
                        <button type="button" onclick="register()">提交</button>
                    </div>
                    <div class="form_reg_btn">

<#--                        <span>还没有帐号？</span><a href="/register">马上注册</a>-->
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript" src="../js/jquery.min.js"></script>
<script type="text/javascript" src="../js/common.js"></script>

<script type="text/javascript">
    function register(){
        $.ajax({
            type: "POST",
            url: "/auth/token/register/user",
            data:{username:$("#username").val(),password:$("#password").val()},
            dataType: "json",
            success: function(data){
                if(data.code == 0){
                    alert(data.msg);
                    window.location.href = "/auth/token/login";
                }
            },
            error: function(){
                alert("register error");
                $("#username").val("");
                $("#password").val("");
            }

        })
    }

</script>
</body>
</html>
