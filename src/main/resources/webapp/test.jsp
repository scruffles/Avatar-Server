<html>
<body>
Here are the provided images:<br/>
<%
    java.net.URL antPic = this.getClass().getResource("/included/ant.jpg");
    java.io.File dir = new java.io.File(antPic.getFile()).getParentFile();
    String[] images = dir.list();
    int count = 1;
    for (String image : images) {
%>
<img src="/avatars/included/<%=image%>" style="padding:7px;"/>
<%
    if (count % 5 == 0) {
%>
<br/>
<%
        }
        count++;
    }
%>

</body>
</html>