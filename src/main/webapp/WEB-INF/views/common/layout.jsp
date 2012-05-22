<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href='${contextRoot}/js/dijit/themes/claro/claro.css'>

<script>
    dojoConfig= {
        has: {
            "dojo-firebug": true,
            "dojo-debug-messages": true
        },
        async: true
    };
</script>    
    
<script type="text/javascript"
    src='${contextRoot}/js/dojo/dojo.js'></script>
<title>${param.title}</title>

</head>

<body class="claro">
    <div class="container">
        <div class="content">
            <div class="page-header">
                <h1>
                    <a href='<c:url value="/"/>'>Spring MVC JPA
                        Blank App</a>
                </h1>
            </div>
            <div class="row">${param.body}</div>
        </div>
        <footer>
            <p>&copy; @making 2011</p>
        </footer>
    </div>
</body>
</html>