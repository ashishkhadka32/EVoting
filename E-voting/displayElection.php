<?php
$conn = mysqli_connect('localhost', 'root', '', 'evoting');

$stmt = $conn->prepare("SELECT id, election, startDate, endDate FROM addelection;");
$stmt->execute();
$stmt->bind_result($id, $electionName, $startDate, $endDate);
$elections = array(); // Change the variable name here
while ($stmt->fetch()) {
    $temp = array();
    $temp['id'] = $id;
    $temp['election'] = $electionName; // Change the variable name here
    $temp['startDate'] = $startDate;
    $temp['endDate'] = $endDate;

    array_push($elections, $temp); // Change the variable name here
}
echo json_encode($elections); 

?>