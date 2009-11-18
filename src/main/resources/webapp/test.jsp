<html>
<head>
    <script type="text/javascript">

        function possiblyClearEmailAddress(textElm) {
            textElm.style.color = 'black';
            if (textElm.value == 'someUser@someDomain.com') {
                textElm.value = '';
            }
        }

        selectedImageElm = null

        function imageClicked(imageElm, imageName) {
            if (selectedImageElm != null) {
                selectedImageElm.style.padding = '7px';
                selectedImageElm.style.border = '0px';
                selectedImageElm.style.background = 'none';
            }
            imageElm.style.padding = '5px';
            imageElm.style.border = '2px solid Red';
            imageElm.style.background = 'lightgrey';
            selectedImageElm = imageElm;
            document.getElementById('selectedImage').value = imageName;
            possiblyEnableSubmitButton();
        }
        function possiblyEnableSubmitButton() {
            var emailAddress = document.getElementById('email').value;
            document.getElementById('submit').disabled = !(selectedImageElm != null &&
                                                           emailAddress != 'someUser@someDomain.com')
        }
    </script>
</head>
<body style="margin: 0px;">
<h1 style="background-color: blue; padding: 20px;">AvatarServer</h1>

<div style="font-size: 15pt; padding-left: 40px;">
    <form action="registerUser" method="GET">
        To register, enter your email address:<br/>
        <input id="email" type="text" name="email" value="someUser@someDomain.com"
               style="font-size: 15pt; width: 350px; color: gray;" onfocus="possiblyClearEmailAddress(this)"
               onchange="possiblyEnableSubmitButton();" onkeypress="possiblyEnableSubmitButton()"><br/><br/>
        <input id="selectedImage" name="image" type="hidden" value="none">
        then select an icon:<br/>

        <div style="width: 510px; height: 300px; overflow: auto; border: 1px solid black;">

            <%
                java.net.URL antPic = this.getClass().getResource("/included/ant.jpg");
                String[] images = new java.io.File(antPic.getFile()).getParentFile().list();
                int count = 1;
                for (String image : images) {
            %>
            <img src="/avatars/included/<%=image%>" style="padding:7px;"
                 onclick="imageClicked(this, '<%=image%>')"/>
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
        <br/>
        <input type="submit" id="submit" value="go" style="font-size:20pt;" disabled="true">
    </form>


</div>


</body>
</html>