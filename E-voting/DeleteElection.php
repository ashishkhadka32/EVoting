<?php
$id = $_POST['id'];
$conn = mysqli_connect('localhost','root','','evoting');
$sql = "DELETE FROM addelection WHERE id = '$id'";
$result = $conn->query($sql);
if($result)
{
    echo "1";
}
else
{
    echo "0";
}
$conn->close();

?>