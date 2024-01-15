<?php
$conn = mysqli_connect('localhost','root', '', 'evoting');

$stmt = $conn->prepare("SELECT id, uid, username, contact, address, dob, gender, image FROM adduser;");
$stmt->execute();
$stmt->bind_result($id, $uid, $username, $contact, $address, $dob, $gender, $image);
$user = array();
while ($stmt->fetch()) {
	$temp = array();
	$temp['id'] = $id;
	$temp['uid'] = $uid;
	$temp['username'] = $username;
	$temp['contact'] = $contact;
	$temp['address'] = $address;
	$temp['dob'] = $dob;
	$temp['gender'] = $gender;
	$temp['image'] = $image;

	array_push($user, $temp);

}
echo json_encode($user);

?>