<?php
$uid = $_POST['uid'];
// $password = $_POST['password'];
$conn = mysqli_connect('localhost','root', '', 'evoting');
$sql = "SELECT * FROM adduser WHERE uid = '$uid'";

$res = mysqli_query($conn, $sql);
$asc = mysqli_fetch_assoc($res);
$count = is_countable($asc);
if($count>0)
{
	echo "Logged in successfully";
}
else
{
	echo "User not found";
}

?>