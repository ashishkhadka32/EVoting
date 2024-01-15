<?php
$conn=mysqli_connect("localhost","root","");
mysqli_select_db($conn,"evoting");
  
	   $name=$_POST['name'];
	   $address=$_POST['address'];
	   $contact=$_POST['contact'];
	   $party=$_POST['party'];
	   $nominees=$_POST['nominees'];	   
	   $img=$_POST['upload'];

                   $filename='./images/'.date("d-m-y").'-'.time().'-'.rand(10000,1000000).'.jpeg';
	   file_put_contents($filename,base64_decode($img));

			$qry = "INSERT INTO `addcandidate`(`id`, `name`, `address`, `contact`, `party`, `nominees`, `image`) VALUES ('','$name','$address','$contact','$party','$nominees','$filename')";

			$res=mysqli_query($conn,$qry);
			
			if($res==true)
			 echo "File Uploaded Successfully";
			else
			 echo "Could not upload File";
?>