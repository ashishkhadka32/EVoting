<?php
function caesarDecrypt($ciphertext, $key) {
    $key = $key % 26;
    $decryptedMessage = "";

    for ($i = 0; $i < strlen($ciphertext); $i++) {
        $char = $ciphertext[$i];
        $ascii = ord($char);

        if (ctype_alpha($char)) {
            $isUpperCase = ($ascii >= ord('A') && $ascii <= ord('Z'));
            $isLowerCase = ($ascii >= ord('a') && $ascii <= ord('z'));

            if ($isUpperCase) {
                $start = ord('A');
            } elseif ($isLowerCase) {
                $start = ord('a');
            }

            $decryptedAscii = ($ascii - $start - $key + 26) % 26 + $start;
            $decryptedChar = chr($decryptedAscii);
            $decryptedMessage .= $decryptedChar;
        } else {
            
        }
    }

    return $decryptedMessage;
}

// Database connection
$conn = mysqli_connect("localhost", "root", "", "evoting");


if (!$conn) {
    die("Connection failed: " . mysqli_connect_error());
}

$voteQuery = "SELECT name, COUNT(*) AS count FROM vote GROUP BY name";
$voteResult = mysqli_query($conn, $voteQuery);

$voteCounts = array();
while ($row = mysqli_fetch_assoc($voteResult)) {
    $name = caesarDecrypt($row['name'], 3); // Decrypt party name
    $count = $row['count'];
    $voteCounts[] = array("name" => $name, "count" => $count);
}

// Convert the vote counts to JSON and send the response
header('Content-Type: application/json');
echo json_encode($voteCounts);

mysqli_close($conn);
?>