<?php
$conn = mysqli_connect('localhost','root', '', 'evoting');

$stmt = $conn->prepare("SELECT id, name, address, contact, party, nominees, image FROM addcandidate;");
$stmt->execute();
$stmt->bind_result($id, $name, $address, $contact, $party, $nominees, $image);
$candidate = array();
while ($stmt->fetch()) {
	$temp = array();
	$temp['id'] = $id;
	$temp['name'] = $name;
	$temp['address'] = $address;
	$temp['contact'] = $contact;
	$temp['party'] = $party;
	$temp['nominees'] = $nominees;
	$temp['image'] = $image;

	array_push($candidate, $temp);

}
echo json_encode($candidate);

?>