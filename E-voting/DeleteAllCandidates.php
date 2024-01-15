<?php
 $conn = mysqli_connect('localhost', 'root', '', 'evoting');

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $startDate = $_POST['startDate'];
    $endDate = $_POST['endDate'];
    if ($startDate === $endDate) {
        // Perform the deletion of all candidates
        $deleteQuery = "DELETE FROM addcandidate";
        
        if (mysqli_query($connection, $deleteQuery)) {
            echo "All candidates deleted";
        } else {
            echo "Error deleting candidates: " . mysqli_error($connection);
        }
    } else {
        echo "Start date and end date are not equal";
    }
} else {
    echo "Invalid request method";
}
?>
