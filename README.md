# AMPOS_RESTAURANT
This is  a simple restaurant management which use only for interview with the AMPOS inc.


Sample Request for using the API.

Path: /api/menu
Method POST - Create Hawaiian Pizza.
{
	"name" : "Hawaiian Pizza",
	"description" : "All-time favourite toppings, Hawaiian pizza in Tropical Hawaii style.",
	"type" : "Pizza",
	"price" : 300,
	"imageLink" : "https://s3-ap-southeast-1.amazonaws.com/interview.ampostech.com/backend/restaurant/menu1.jpg",
	"tags": [
		{ "name": "Italian" },
		{ "name": "Ham" },
		{ "name": "Pineapple" }
	]
}

Path: /api/menu
Method POST - Create Chicken Tom Yum Pizza.
{
	"name" : "Chicken Tom Yum Pizza",
	"description" : "Best marinated chicken with pineapple and mushroom on Spicy Lemon sauce. Enjoy our tasty Thai style pizza.",
	"type" : "Pizza",
	"price" : 350,
	"imageLink" : "https://s3-ap-southeast-1.amazonaws.com/interview.ampostech.com/backend/restaurant/menu2.jpg",
	"tags": [
		{ "name": "Italian" },
		{ "name": "Thai" },
		{ "name": "Chicken" },
		{ "name": "Mushroom" },
		{ "name": "Spicy" }
	]
}

Path: /api/menu
Method POST - Create Kimchi.
{
	"name" : "Kimchi",
	"description" : "Traditional side dish made from salted and fermented vegetables",
	"type" : "Unknow",
	"price" : 50,
	"imageLink" : "https://s3-ap-southeast-1.amazonaws.com/interview.ampostech.com/backend/restaurant/menu4.jpg",
	"tags": [
		{ "name": "Korean" },
		{ "name": "Radish" },
		{ "name": "Cabbage" },
		{ "name": "Hot" },
		{ "name": "Spicy" }
	]
}

Path: /api/menu
Method PATCH - Update Chicken Tom Yum Pizza.
{
	"name" : "Chicken Tom Yum Pizza",
	"description" : "Best marinated chicken with pineapple and mushroom on Spicy Lemon sauce. Enjoy our tasty Thai style pizza.",
	"type" : "Pizza",
	"price" : 350,
	"imageLink" : "https://s3-ap-southeast-1.amazonaws.com/interview.ampostech.com/backend/restaurant/menu2.jpg",
	"tags": [
		{ "name": "Italian" },
		{ "name": "Thai" },
		{ "name": "Chicken" },
		{ "name": "Mushroom" },
		{ "name": "Spicy" },
		{ "name": "Herb" },
		{ "name": "Fusion" }
	]
}

Get all menu - Method GET
Path: /api/menu/page/{page}/size/{size}
Page start with 0, size is result per page

Get all menu - Method GET
Path: /api/menu/page/{page}/size/{size}
Page start with 0, size is result per page

Search menu by keyword - Method Get
Path: /api/menu/search/{keyword}

Delete menu - Method DELETE
Path: /api/menu/{menuName}
Note: menu cannot be deleted when it's associated with bill.

Path: /api/bill
Method Post - Create Bill with associate to menu.
{
	"orderMenus" : [
		{
			"menuName": "Chicken Tom Yum Pizza",
			"quantity": 2
		},
		{
			"menuName": "Hawaiian Pizza",
			"quantity": 1
		}
	]
}

Path: /api/bill
Method Patch - Upadate Bill with associate to menu.
{
	"buid": "e851b40f-caa4-4f61-807a-0a736c754550",
	"orderMenus" : [
		{
			"menuName": "Chicken Tom Yum Pizza",
			"quantity": 4
		},
		{
			"menuName": "Kimchi",
			"quantity": 2
		}
	]
}

Method - GET
Path: /api/bills
Get all bill.

Method - GET
Path: /api/bill/{buid}
Get bill by bill unique id.
