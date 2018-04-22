<?php
require "vendor/autoload.php";
use PHPHtmlParser\Dom;

$dom = new Dom;
$dom->loadFromUrl('http://dl.spbstu.ru');
$html = $dom->outerHtml;
echo $html;
?>
