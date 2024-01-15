<?php
  $id = $_POST['id'];
  $name = $_POST['name'];
  $address = $_POST['address'];
  $contact = $_POST['contact'];
  $party = $_POST['party'];
  $nominees = $_POST['nominees'];
  $img = $_POST['upload'];
$filename='./images/'.date("d-m-y").'-'.time().'-'.rand(10000,1000000).'.jpeg';
	   file_put_contents($filename,base64_decode($img));
  $conn = mysqli_connect('localhost', 'root', '', 'evoting');
  $sql = "UPDATE addcandidate SET name='$name', address='$address', contact='$contact', party='$party', nominees='$nominees', image='$filename' WHERE id='$id'";
  $result = $conn->query($sql);

  if ($result) {
    echo "1";
  } else {
    echo "0";
  }

  $conn->close();
?>
