<?php
$conn = mysqli_connect("localhost", "root", "");
mysqli_select_db($conn, "evoting");

$election = $_POST['election'];
$startDate = $_POST['startDate'];
$endDate = $_POST['endDate'];

// Check if the election with the same name and dates already exists
$checkQuery = "SELECT * FROM addelection WHERE election = '$election' AND startDate = '$startDate' AND endDate = '$endDate'";
$checkResult = mysqli_query($conn, $checkQuery);

if (mysqli_num_rows($checkResult) > 0) {
    echo "Election with the same name and dates already exists";
} else {
    // If no duplicate, insert the data into the database
    $insertQuery = "INSERT INTO `addelection`(`id`, `election`, `startDate`, `endDate`) VALUES (NULL, '$election', '$startDate', '$endDate')";
    $res = mysqli_query($conn, $insertQuery);

    if ($res == true) {
        echo "Election Data Inserted Successfully";
    } else {
        echo "Could not insert Election Data";
    }
}
?>
