<?php
require "vendor/autoload.php";
use PHPHtmlParser\Dom;

$dom = new Dom;
$dom->loadFromUrl('http://dl.spbstu.ru');
$html = $dom->outerHtml;
echo $html;
?>
The following functions that work with a linked list are compiled without errors. Please identify as many problems in this code as possible and show how they can be fixed.

typedef struct list_s {
        struct list_s *next;  /* last item in a list contains NULL */
        int data;
} list_t;



int get_list_size(const list_t *head) {
	if (head->next) {
			return get_list_size(head->next) + 1;
	} else {
			return 1;
	}
}



void insert_new_item(list_t *preceding_item, int data) {
	/* Add a new item to list after the preceding_item */
	(preceding_item ->next = malloc(sizeof(list_t)))->next = preceding_item ->next;
	preceding_item ->next->data = data;
}



void remove_next_item(list_t *item) {
	 if (item->next) {
			free(item->next);
        			item->next = item->next->next;
     	}
}



char *get_item_data_as_string(const list_t *list)
{
	char buf[12];
	sprintf(buf, "%d", list->data);
	return buf;
}
