<?php
$con=mysqli_connect('student.cs.hioa.no','s306631','','s306631');

$in1=$_REQUEST['roomid'];
$roomid=(int)$in1;

$in2=$_REQUEST['buildingid'];
$buildingid=(int)$in2;

$in3=$_REQUEST['name'];
$name=(String)$in3;

$in4=$_REQUEST['fromtime'];
$fromtime=(String)$in4;

$in5=$_REQUEST['totime'];
$totime=(String)$in5;

$in6=$_REQUEST['date'];
$date=(String)$in6;

$sql=mysqli_query($con,"INSERT INTO Bookings (RoomID, BuildingID, BookerName, FromTime, ToTime, Date) values ('$roomid', '$buildingid', '$name', '$fromtime', '$totime', '$date')");
mysqli_close($con);
?>