<?php
$id = $_POST['id'];
$uid = $_POST['uid'];
$username = $_POST['username'];
$contact = $_POST['contact'];
$address = $_POST['address'];
$password = $_POST['password'];
$dob = $_POST['dob'];
$gender = $_POST['gender'];
$img = $_POST['upload'];
$filename='./images/'.date("d-m-y").'-'.time().'-'.rand(10000,1000000).'.jpeg';
	   file_put_contents($filename,base64_decode($img));
$conn = mysqli_connect('localhost', 'root', '', 'evoting');
$sql = "UPDATE adduser SET uid='$uid', username='$username', contact='$contact', address='$address', password='$password', dob='$dob', gender='$gender', image='$filename' WHERE id='$id'";
$result = $conn->query($sql);
if ($result) {
    echo "1";
} else {
    echo "0";
}
$conn->close();
?>
