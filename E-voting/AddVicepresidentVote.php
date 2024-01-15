<?php
function caesarEncrypt($plaintext, $key) {
   
    $key = $key % 26;
    $encryptedMessage = "";

    for ($i = 0; $i < strlen($plaintext); $i++) {
        $char = $plaintext[$i];
        $ascii = ord($char);

        if (ctype_alpha($char)) {
            
            $isUpperCase = ($ascii >= ord('A') && $ascii <= ord('Z'));
            $isLowerCase = ($ascii >= ord('a') && $ascii <= ord('z'));

            if ($isUpperCase) {
                $start = ord('A');
            } elseif ($isLowerCase) {
                $start = ord('a');
            }
            
            
            $encryptedAscii = ($ascii - $start + $key) % 26 + $start;
            $encryptedChar = chr($encryptedAscii);
            $encryptedMessage .= $encryptedChar;
        } else {
            
            $encryptedMessage .= encryptInteger($char, $key);
        }
    }

    return $encryptedMessage;
}

function encryptInteger($digit, $key) {
      
    $digit = intval($digit);

   
    $digit = ($digit + $key) % 10;
    return $digit;
}

$conn = mysqli_connect("localhost", "root", "", "evoting");

$name = $_POST['name'];
$party = $_POST['party'];
$uid = $_POST['uid'];


$encryptedName = caesarEncrypt($name, 3); 
$encryptedParty = caesarEncrypt($party, 3); 
$encryptedUid = caesarEncrypt($uid, 3); 


$checkVoteQuery = "SELECT * FROM vicepresidentvote WHERE uid = '$encryptedUid'";
$checkVoteResult = mysqli_query($conn, $checkVoteQuery);

if (mysqli_num_rows($checkVoteResult) > 0) {
    echo "You have already voted for the party '$party'. You cannot vote again.";
} else {
    $qry = "INSERT INTO `vicepresidentvote` (`name`, `party`, `uid`) VALUES ('$encryptedName', '$encryptedParty', '$encryptedUid')";
    $res = mysqli_query($conn, $qry);

    if ($res) {
        echo "Vote inserted successfully";
    } else {
        echo "Error: Unable to insert vote";
    }
}
?>