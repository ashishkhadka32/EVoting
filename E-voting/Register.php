<?php
	$conn = mysqli_connect('localhost','root', '', 'evoting');
	
		$user = $_POST['user'];
		$email = $_POST['email'];
		$contact = $_POST['contact'];
		$address = $_POST['address'];
		$password = $_POST['password'];
		
		// $Dup_username = mysqli_query($conn, "SELECT * FROM `register` WHERE Email='$email'");
		// if(mysqli_num_rows($Dup_username))
		// {
		// 	echo "
		// 	<script>
		// 		alert('this account is already taken');
		// 		window.location.href= 'register.php';
		// 	</script>";
		// }
		$sql = "INSERT INTO `user`(`user`, `email`, `contact`, `address`, `password`) VALUES ('$user','$email','$contact','$address','$password')";
		$res = mysqli_query($conn, $sql);

		if($res)
		{
			echo "User add successfull";
		}

		else
		{
			echo "Some error occured";
		}
		
	

?>