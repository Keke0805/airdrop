<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>swufe快传</title>
    <link rel="stylesheet" href="css/main.css">
    <link rel="stylesheet" href="css/font_1569256_b2vvh3c4xoj.css">
    <script src="js/clipboard.min.js"></script>
</head>

<body>
<%--通过表单上传文件
get:上传文件大小有限制
post:上传文件大小无限制
--%>
<%
    String clipBoard= (String) application.getAttribute("clipBoard");
%>
<div class="_1nK53-"><img src="res/swufelogo.png" alt="logo" class="_3jtTy3">
    <div class="_1nX2tW _1x8lgF">简单安全高效的文件传输服务</div>
    <div class="_3bzYjy"><span class="_1nX2tW _1x8lgF">发送文件</span></div>
</div>

<div class="_2KMkak">
    <div class="_8bl-qe">
        <div class="_3AsA1Y">
            <div class="MQm7zy">上传文件</div>
        </div>
        <form action="${pageContext.request.contextPath}/upload.do" enctype="multipart/form-data" method="post">
            <div class="_2NI-Bu">
                <div class="_38jnea">
                    <input type="text" class="_1ItMl6 _2iTtOQ" id="txtFile" name="txtFile" value="" />
                    <input type="button" class="_1efWOZ _3WTb49" value="开始上传" onclick="checkFile()" />
                    <input type="file" class="input_file" id="file1" style="display:none"  name="file1" onchange="document.getElementById('txtFile').value=this.value" />

                </div>
            </div>
            <input type="submit" class="_1efWOZ _3WTb49">
            <input type="reset" class="_1efWOZ _3WTb49">
        </form>
    </div>
    <div class="_8bl-qe">
        <div class="_3AsA1Y">
            <div class="MQm7zy">共享剪贴板</div>
        </div>
        <div class="_2NI-Bu">
            <div class="_38jnea">
                    <textarea class="_1ItMl6 _2iTtOQ" id="clipboard2" name="content2" style="resize:none" ><%
                        if (clipBoard != null) {
                            out.print(clipBoard);
                        }
                    %></textarea>

            </div>
            <div  class="_38jnea">
                <button class="btn" data-clipboard-target="#clipboard2">复制</button>
            </div>

        </div>
    </div>


</div>
<script>
    var clipboard = new ClipboardJS('.btn');
    clipboard.on('success', function (e) {
        alert("复制成功！");
    });
    clipboard.on('error', function (e) {
        alert("复制失败，请手动复制。")
    });
</script>
<script type="text/javascript">
    function checkFile(){
        document.getElementById("file1").click();
    }
</script>






<form method=post action="process.jsp" id="clipboard_form">
    <input type="hidden" id="clipboard" rows="10" cols="30"  name="content" type ="hidden" value="<%if(clipBoard!=null) {out.print(clipBoard);}%>">
</form>

<script type="text/javascript">
    function copyToClipboard(text,callback) {
        // 实现复制方法一：
        if (navigator.clipboard) {
            navigator.clipboard.writeText(text);
            callback && callback(true);
            return;
        }
        // 实现复制方法二：
        var copyInput = document.getElementById('COPY_INPUT');
        if (!copyInput) {
            copyInput = document.createElement('input');
            copyInput.setAttribute('id', 'COPY_INPUT');
            copyInput.style.position = 'fixed';
            copyInput.style.left = '-100%';
            copyInput.style.top = '0';
            copyInput.style.zIndex = -100;
            copyInput.style.opacity = 0;
            document.body.appendChild(copyInput);
        }
        copyInput.value = text;
        copyInput.focus();
        copyInput.select();
        // document.execCommand 可能会被废弃
        if (document.execCommand('copy')) {
            document.execCommand('copy');
        }
        copyInput.blur();
        callback && callback(true);
    }
    var x = document.getElementById("clipboard2").innerHTML;
    if (x != null || x != "") {
        // alert(x);
        copyToClipboard(x);
    }

</script>
<script type="text/javascript">
    document.addEventListener('copy',function(e){
        // 获得clipboardData 对象
        let clipboardData = e.clipboardData || window.clipboardData;
        var form=document.getElementById("clipboard_form");
        // 如果 未复制或者未剪切，直接 return
        if(!clipboardData) return ;
        // 获得 Selection 对象 表示用户选择的文本范围或光标的当前位置。
        // 将用户输入的剪切或者复制的文本转化为字符串
        let text = window.getSelection().toString();
        if(text){
            // 如果文本存在，首先取消默认行为
            e.preventDefault();
            console.log("可以获取",text);
            document.getElementById("clipboard").value=text;
            alert('复制成功');
            form.submit();
        }
    })

</script>


</body>
</html>
