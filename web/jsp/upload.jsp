
<html>

<head>
<title>Welcome</title>
<link rel="stylesheet" href="../css/bootstrap.css">
<link rel="stylesheet" href="../css/bootstrap-theme.css">

<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script src="../js/jquery.js"></script>
<script src="../js/jquery.min.js"></script>
<script src="../js/bootstrap.min.js"></script>

</head>

<script>
$(document).ready(function(){
        $.get("../Upload", function(data, status){
            $("#mytable").append("<thead><tr><th>First Name</th><th>Last Name</th><th>Phone Number</th></tr></thead>");
            for (var i = 0 ; i < data.length ; i++){
            	var d = data[i];
            	$("#mytable").append("<tr><td>" + d.FirstName + "</td><td>" + d.LastName + "</td><td>" + d.PhoneNumber + "</td></tr>");
            }
        });
});
</script>

<div class="container">
	<table id="mytable" class="table table-bordered table-striped">
	</table>
</div>
</body>
</html>
