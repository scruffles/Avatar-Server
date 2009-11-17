<html>
<body>
Here are the provided images:<br/>

<div style="padding-left:200; width: 530px; height: 300px; overflow: auto; ">

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

</div>

</body>
</html>