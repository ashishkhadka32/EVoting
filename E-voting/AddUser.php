<?php
$conn = mysqli_connect("localhost", "root", "");
mysqli_select_db($conn, "evoting");

$uid = $_POST['uid'];
// Check if the UID already exists in the database
$checkQuery = "SELECT * FROM `adduser` WHERE `uid`='$uid'";
$checkResult = mysqli_query($conn, $checkQuery);

if (mysqli_num_rows($checkResult) > 0) {
    // UID already exists, return an error message
    echo "UID already in use";
} else {
    // UID is unique, proceed with the insertion
    $username = $_POST['username'];
    $contact = $_POST['contact'];
    $address = $_POST['address'];
    $password = $_POST['password'];
    $dob = $_POST['dob'];
    $gender = $_POST['gender'];
    $img = $_POST['upload'];

    $filename = './images/' . date("d-m-y") . '-' . time() . '-' . rand(10000, 1000000) . '.jpeg';
    file_put_contents($filename, base64_decode($img));

    $qry = "INSERT INTO `adduser`(`id`, `uid`, `username`, `contact`, `address`, `password`, `dob`, `gender`, `image`) VALUES ('','$uid','$username','$contact','$address','$password','$dob','$gender','$filename')";

    $res = mysqli_query($conn, $qry);

    if ($res == true)
        echo "File Uploaded Successfully";
    else
        echo "Could not upload File";
}
?>
