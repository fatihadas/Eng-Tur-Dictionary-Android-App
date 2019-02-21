<?php
$requestid = "Sozluk7812278*";

if (isset($_GET["requestid"])) {
    if ($_GET["requestid"] != $requestid) {
        echo '{"hata":"true"}';
        exit;
    }
} else {
    echo '{"hata":"true"}';
    exit;
}

$source="en";
$target="tr";
$text="";
if(isset($_GET['source']))
	$source=$_GET['source'];
if(isset($_GET['target']))
	$target=$_GET['target'];
if(isset($_GET['text']))
	$text=$_GET['text'];

require_once ('autoload.php');
use \Statickidz\GoogleTranslate;
$hata="true";
$trans = new GoogleTranslate();
$result = $trans->translate($source, $target, $text);

if(strlen($result)>0)
	$hata="false";
$cevap = array("hata" => $hata,"cevap" => $result);

echo json_encode($cevap);
?>