
<?php
function caesarEncrypt($plaintext, $key) {
    // Convert the key to a valid range (0 to 25)
    $key = $key % 26;
    $encryptedMessage = "";

    // Iterate through each character in the plaintext message
    for ($i = 0; $i < strlen($plaintext); $i++) {
        $char = $plaintext[$i];
        $ascii = ord($char);

        // Encrypt alphabetic characters (A-Z and a-z)
        if (ctype_alpha($char)) {
            // Determine the case (uppercase or lowercase) of the character
            $isUpperCase = ($ascii >= ord('A') && $ascii <= ord('Z'));
            $isLowerCase = ($ascii >= ord('a') && $ascii <= ord('z'));

            if ($isUpperCase) {
                $start = ord('A');
            } elseif ($isLowerCase) {
                $start = ord('a');
            }
            
            // Shift the character by the key value and handle wrapping around the alphabet
            $encryptedAscii = ($ascii - $start + $key) % 26 + $start;
            $encryptedChar = chr($encryptedAscii);
            $encryptedMessage .= $encryptedChar;
        } else {
            // For non-alphabetic characters, handle encryption of integers
            $encryptedMessage .= encryptInteger($char, $key);
        }
    }

    return $encryptedMessage;
}

function encryptInteger($digit, $key) {
      // Convert the character to its numeric value (integer)
    $digit = intval($digit);

    // Encrypt integer value (digit) using a simple shift cipher
    $digit = ($digit + $key) % 10;
    return $digit;
}

$conn = mysqli_connect("localhost", "root", "", "evoting");

$name = $_POST['name'];
$party = $_POST['party'];
$uid = $_POST['uid'];

// Encrypt the data before storing it in the database
$encryptedName = caesarEncrypt($name, 3); // Use a key of your choice (e.g., 3)
$encryptedParty = caesarEncrypt($party, 3); // Use the same key for consistency
$encryptedUid = caesarEncrypt($uid, 3); // Use the same key for consistency

// Check if the user has already voted for the specific party using that UID
$checkVoteQuery = "SELECT * FROM vote WHERE uid = '$encryptedUid'";
$checkVoteResult = mysqli_query($conn, $checkVoteQuery);

if (mysqli_num_rows($checkVoteResult) > 0) {
    echo "You have already voted for the party '$party'. You cannot vote again.";
} else {
    $qry = "INSERT INTO `vote` (`name`, `party`, `uid`) VALUES ('$encryptedName', '$encryptedParty', '$encryptedUid')";
    $res = mysqli_query($conn, $qry);

    if ($res) {
        echo "Vote inserted successfully";
    } else {
        echo "Error: Unable to insert vote";
    }
}
?>
