```JSON
{
	"info": {
		"_postman_id": "96f9a40d-3b80-44a2-adcc-90ba36610235",
		"name": "FinanciaPro API",
		"schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json",
		"_exporter_id": "26117504"
	},
	"item": [
		{
			"name": "User Management",
			"item": [
				{
					"name": "Create User - Success",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Status code is 201\", function () {",
									"    pm.response.to.have.status(201);",
									"});",
									"",
									"pm.test(\"Response has user data\", function () {",
									"    var jsonData = pm.response.json();",
									"    pm.expect(jsonData.firstName).to.eql(\"Jean\");",
									"    pm.expect(jsonData.email).to.eql(\"jean.dupont@email.com\");",
									"    pm.collectionVariables.set(\"userId\", jsonData.id);",
									"});"
								],
								"type": "text/javascript"
							}
						}
					],
					"request": {
						"method": "POST",
						"header": [
							{
								"key": "Content-Type",
								"value": "application/json"
							}
						],
						"body": {
							"mode": "raw",
							"raw": "{\n  \"firstName\": \"Jean\",\n  \"lastName\": \"Dupont\",\n  \"email\": \"jean.dupont@email.com\",\n  \"monthlyIncome\": 3500.00\n}"
						},
						"url": {
							"raw": "{{baseUrl}}/users",
							"host": [
								"{{baseUrl}}"
							],
							"path": [
								"users"
							]
						}
					},
					"response": []
				},
				{
					"name": "Create User - Invalid Email",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Status code is 400\", function () {",
									"    pm.response.to.have.status(400);",
									"});"
								],
								"type": "text/javascript"
							}
						}
					],
					"request": {
						"method": "POST",
						"header": [
							{
								"key": "Content-Type",
								"value": "application/json"
							}
						],
						"body": {
							"mode": "raw",
							"raw": "{\n  \"firstName\": \"Jean\",\n  \"lastName\": \"Dupont\",\n  \"email\": \"invalid-email\",\n  \"monthlyIncome\": 3500.00\n}"
						},
						"url": {
							"raw": "{{baseUrl}}/users",
							"host": [
								"{{baseUrl}}"
							],
							"path": [
								"users"
							]
						}
					},
					"response": []
				},
				{
					"name": "Get User by ID",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Status code is 200\", function () {",
									"    pm.response.to.have.status(200);",
									"});",
									"",
									"pm.test(\"Response has user data\", function () {",
									"    var jsonData = pm.response.json();",
									"    pm.expect(jsonData.id).to.exist;",
									"    pm.expect(jsonData.email).to.exist;",
									"});"
								],
								"type": "text/javascript"
							}
						}
					],
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "{{baseUrl}}/users/{{userId}}",
							"host": [
								"{{baseUrl}}"
							],
							"path": [
								"users",
								"{{userId}}"
							]
						}
					},
					"response": []
				},
				{
					"name": "Get User by ID - Not Found",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Status code is 404\", function () {",
									"    pm.response.to.have.status(404);",
									"});"
								],
								"type": "text/javascript"
							}
						}
					],
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "{{baseUrl}}/users/999",
							"host": [
								"{{baseUrl}}"
							],
							"path": [
								"users",
								"999"
							]
						}
					},
					"response": []
				},
				{
					"name": "Get User by Email",
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "{{baseUrl}}/users/email/{{userEmail}}",
							"host": [
								"{{baseUrl}}"
							],
							"path": [
								"users",
								"email",
								"{{userEmail}}"
							]
						}
					},
					"response": []
				},
				{
					"name": "Get User by API Key",
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "{{baseUrl}}/users/api-key/{{userApiKey}}",
							"host": [
								"{{baseUrl}}"
							],
							"path": [
								"users",
								"api-key",
								"{{userApiKey}}"
							]
						}
					},
					"response": []
				},
				{
					"name": "Update User",
					"request": {
						"method": "PUT",
						"header": [
							{
								"key": "Content-Type",
								"value": "application/json"
							}
						],
						"body": {
							"mode": "raw",
							"raw": "{\n  \"firstName\": \"Jean-Michel\",\n  \"lastName\": \"Dupont\",\n  \"monthlyIncome\": 4000.00\n}"
						},
						"url": {
							"raw": "{{baseUrl}}/users/{{userId}}",
							"host": [
								"{{baseUrl}}"
							],
							"path": [
								"users",
								"{{userId}}"
							]
						}
					},
					"response": []
				}
			]
		},
		{
			"name": "Budget Management",
			"item": [
				{
					"name": "Create Budget Item - Income",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Status code is 201\", function () {",
									"    pm.response.to.have.status(201);",
									"});",
									"",
									"pm.test(\"Response has budget item data\", function () {",
									"    var jsonData = pm.response.json();",
									"    pm.expect(jsonData.type).to.eql(\"INCOME\");",
									"    pm.collectionVariables.set(\"budgetItemId\", jsonData.id);",
									"});"
								],
								"type": "text/javascript"
							}
						}
					],
					"request": {
						"method": "POST",
						"header": [
							{
								"key": "Content-Type",
								"value": "application/json"
							}
						],
						"body": {
							"mode": "raw",
							"raw": "{\n  \"userId\": {{userId}},\n  \"description\": \"Salaire mensuel\",\n  \"amount\": 3500.00,\n  \"type\": \"INCOME\"\n}"
						},
						"url": {
							"raw": "{{baseUrl}}/budget-items",
							"host": [
								"{{baseUrl}}"
							],
							"path": [
								"budget-items"
							]
						}
					},
					"response": []
				},
				{
					"name": "Create Budget Item - Expense",
					"request": {
						"method": "POST",
						"header": [
							{
								"key": "Content-Type",
								"value": "application/json"
							}
						],
						"body": {
							"mode": "raw",
							"raw": "{\n  \"userId\": {{userId}},\n  \"description\": \"Loyer\",\n  \"amount\": 1200.00,\n  \"type\": \"EXPENSE\"\n}"
						},
						"url": {
							"raw": "{{baseUrl}}/budget-items",
							"host": [
								"{{baseUrl}}"
							],
							"path": [
								"budget-items"
							]
						}
					},
					"response": []
				},
				{
					"name": "Create Budget Item - Invalid User",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Status code is 400\", function () {",
									"    pm.response.to.have.status(400);",
									"});"
								],
								"type": "text/javascript"
							}
						}
					],
					"request": {
						"method": "POST",
						"header": [
							{
								"key": "Content-Type",
								"value": "application/json"
							}
						],
						"body": {
							"mode": "raw",
							"raw": "{\n  \"userId\": 999,\n  \"description\": \"Test\",\n  \"amount\": 100.00,\n  \"type\": \"INCOME\"\n}"
						},
						"url": {
							"raw": "{{baseUrl}}/budget-items",
							"host": [
								"{{baseUrl}}"
							],
							"path": [
								"budget-items"
							]
						}
					},
					"response": []
				},
				{
					"name": "Get Budget Items by User",
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "{{baseUrl}}/budget-items/user/{{userId}}",
							"host": [
								"{{baseUrl}}"
							],
							"path": [
								"budget-items",
								"user",
								"{{userId}}"
							]
						}
					},
					"response": []
				},
				{
					"name": "Get Budget Items by User and Type",
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "{{baseUrl}}/budget-items/user/{{userId}}/type/INCOME",
							"host": [
								"{{baseUrl}}"
							],
							"path": [
								"budget-items",
								"user",
								"{{userId}}",
								"type",
								"INCOME"
							]
						}
					},
					"response": []
				},
				{
					"name": "Calculate Total by Type",
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "{{baseUrl}}/budget-items/total/user/{{userId}}/type/EXPENSE",
							"host": [
								"{{baseUrl}}"
							],
							"path": [
								"budget-items",
								"total",
								"user",
								"{{userId}}",
								"type",
								"EXPENSE"
							]
						}
					},
					"response": []
				},
				{
					"name": "Calculate Current Month Balance",
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "{{baseUrl}}/budget-items/balance/user/{{userId}}/current-month",
							"host": [
								"{{baseUrl}}"
							],
							"path": [
								"budget-items",
								"balance",
								"user",
								"{{userId}}",
								"current-month"
							]
						}
					},
					"response": []
				},
				{
					"name": "Get Financial Summary",
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "{{baseUrl}}/budget-items/financial-summary/user/{{userId}}",
							"host": [
								"{{baseUrl}}"
							],
							"path": [
								"budget-items",
								"financial-summary",
								"user",
								"{{userId}}"
							]
						}
					},
					"response": []
				},
				{
					"name": "Calculate Borrowing Capacity",
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "{{baseUrl}}/budget-items/borrowing-capacity/user/{{userId}}",
							"host": [
								"{{baseUrl}}"
							],
							"path": [
								"budget-items",
								"borrowing-capacity",
								"user",
								"{{userId}}"
							]
						}
					},
					"response": []
				},
				{
					"name": "Calculate Lending Capacity",
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "{{baseUrl}}/budget-items/lending-capacity/user/{{userId}}",
							"host": [
								"{{baseUrl}}"
							],
							"path": [
								"budget-items",
								"lending-capacity",
								"user",
								"{{userId}}"
							]
						}
					},
					"response": []
				},
				{
					"name": "Update Budget Item",
					"request": {
						"method": "PUT",
						"header": [
							{
								"key": "Content-Type",
								"value": "application/json"
							}
						],
						"body": {
							"mode": "raw",
							"raw": "{\n  \"description\": \"Salaire mensuel mis à jour\",\n  \"amount\": 3700.00\n}"
						},
						"url": {
							"raw": "{{baseUrl}}/budget-items/{{budgetItemId}}",
							"host": [
								"{{baseUrl}}"
							],
							"path": [
								"budget-items",
								"{{budgetItemId}}"
							]
						}
					},
					"response": []
				},
				{
					"name": "Delete Budget Item",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Status code is 204\", function () {",
									"    pm.response.to.have.status(204);",
									"});"
								],
								"type": "text/javascript"
							}
						}
					],
					"request": {
						"method": "DELETE",
						"header": [],
						"url": {
							"raw": "{{baseUrl}}/budget-items/{{budgetItemId}}",
							"host": [
								"{{baseUrl}}"
							],
							"path": [
								"budget-items",
								"{{budgetItemId}}"
							]
						}
					},
					"response": []
				}
			]
		},
		{
			"name": "Loan Management",
			"item": [
				{
					"name": "Create Loan Request",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Status code is 201\", function () {",
									"    pm.response.to.have.status(201);",
									"});",
									"",
									"pm.test(\"Response has loan request data\", function () {",
									"    var jsonData = pm.response.json();",
									"    pm.expect(jsonData.amount).to.eql(200.00);",
									"    pm.collectionVariables.set(\"loanRequestId\", jsonData.id);",
									"});"
								],
								"type": "text/javascript",
								"packages": {}
							}
						}
					],
					"request": {
						"method": "POST",
						"header": [
							{
								"key": "Content-Type",
								"value": "application/json"
							}
						],
						"body": {
							"mode": "raw",
							"raw": "{\n  \"borrowerId\": {{userId}},\n  \"amount\": 200.00,\n  \"description\": \"Réparation voiture\"\n}"
						},
						"url": {
							"raw": "{{baseUrl}}/loan-requests",
							"host": [
								"{{baseUrl}}"
							],
							"path": [
								"loan-requests"
							]
						}
					},
					"response": []
				},
				{
					"name": "Create Loan Request - Invalid Amount",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Status code is 400\", function () {",
									"    pm.response.to.have.status(400);",
									"});"
								],
								"type": "text/javascript"
							}
						}
					],
					"request": {
						"method": "POST",
						"header": [
							{
								"key": "Content-Type",
								"value": "application/json"
							}
						],
						"body": {
							"mode": "raw",
							"raw": "{\n  \"borrowerId\": {{userId}},\n  \"amount\": -100.00,\n  \"description\": \"Test négatif\"\n}"
						},
						"url": {
							"raw": "{{baseUrl}}/loan-requests",
							"host": [
								"{{baseUrl}}"
							],
							"path": [
								"loan-requests"
							]
						}
					},
					"response": []
				},
				{
					"name": "Get Loan Request by ID",
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "{{baseUrl}}/loan-requests/{{loanRequestId}}",
							"host": [
								"{{baseUrl}}"
							],
							"path": [
								"loan-requests",
								"{{loanRequestId}}"
							]
						}
					},
					"response": []
				},
				{
					"name": "Get Loan Request - Not Found",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Status code is 404\", function () {",
									"    pm.response.to.have.status(404);",
									"});"
								],
								"type": "text/javascript"
							}
						}
					],
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "{{baseUrl}}/loan-requests/999",
							"host": [
								"{{baseUrl}}"
							],
							"path": [
								"loan-requests",
								"999"
							]
						}
					},
					"response": []
				},
				{
					"name": "Accept Loan Request",
					"request": {
						"method": "POST",
						"header": [
							{
								"key": "Content-Type",
								"value": "application/json"
							}
						],
						"body": {
							"mode": "raw",
							"raw": "{\n  \"lenderId\": 2\n}"
						},
						"url": {
							"raw": "{{baseUrl}}/loan-requests/{{loanRequestId}}/accept",
							"host": [
								"{{baseUrl}}"
							],
							"path": [
								"loan-requests",
								"{{loanRequestId}}",
								"accept"
							]
						}
					},
					"response": []
				},
				{
					"name": "Refuse Loan Request",
					"request": {
						"method": "POST",
						"header": [
							{
								"key": "Content-Type",
								"value": "application/json"
							}
						],
						"body": {
							"mode": "raw",
							"raw": "{\n  \"lenderId\": 2\n}"
						},
						"url": {
							"raw": "{{baseUrl}}/loan-requests/{{loanRequestId}}/refuse",
							"host": [
								"{{baseUrl}}"
							],
							"path": [
								"loan-requests",
								"{{loanRequestId}}",
								"refuse"
							]
						}
					},
					"response": []
				},
				{
					"name": "Get Available Loan Requests",
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "{{baseUrl}}/loan-requests/available/2",
							"host": [
								"{{baseUrl}}"
							],
							"path": [
								"loan-requests",
								"available",
								"2"
							]
						}
					},
					"response": []
				},
				{
					"name": "Get Matching Loan Requests",
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "{{baseUrl}}/loan-requests/matching/2",
							"host": [
								"{{baseUrl}}"
							],
							"path": [
								"loan-requests",
								"matching",
								"2"
							]
						}
					},
					"response": []
				},
				{
					"name": "Get Loan Requests by Borrower",
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "{{baseUrl}}/loan-requests/borrower/{{userId}}",
							"host": [
								"{{baseUrl}}"
							],
							"path": [
								"loan-requests",
								"borrower",
								"{{userId}}"
							]
						}
					},
					"response": []
				},
				{
					"name": "Get Loan Requests by Lender",
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "{{baseUrl}}/loan-requests/lender/2",
							"host": [
								"{{baseUrl}}"
							],
							"path": [
								"loan-requests",
								"lender",
								"2"
							]
						}
					},
					"response": []
				},
				{
					"name": "Get Total Borrowed Amount",
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "{{baseUrl}}/loan-requests/stats/borrowed/{{userId}}",
							"host": [
								"{{baseUrl}}"
							],
							"path": [
								"loan-requests",
								"stats",
								"borrowed",
								"{{userId}}"
							]
						}
					},
					"response": []
				},
				{
					"name": "Get Total Lent Amount",
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "{{baseUrl}}/loan-requests/stats/lent/2",
							"host": [
								"{{baseUrl}}"
							],
							"path": [
								"loan-requests",
								"stats",
								"lent",
								"2"
							]
						}
					},
					"response": []
				}
			]
		},
		{
			"name": "Repayment Management",
			"item": [
				{
					"name": "Create Repayment",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Status code is 201\", function () {",
									"    pm.response.to.have.status(201);",
									"});",
									"",
									"pm.test(\"Response has repayment data\", function () {",
									"    var jsonData = pm.response.json();",
									"    pm.expect(jsonData.amount).to.eql(250.00);",
									"});"
								],
								"type": "text/javascript"
							}
						}
					],
					"request": {
						"method": "POST",
						"header": [
							{
								"key": "Content-Type",
								"value": "application/json"
							}
						],
						"body": {
							"mode": "raw",
							"raw": "{\n  \"loanRequestId\": {{loanRequestId}},\n  \"amount\": 250.00\n}"
						},
						"url": {
							"raw": "{{baseUrl}}/repayments",
							"host": [
								"{{baseUrl}}"
							],
							"path": [
								"repayments"
							]
						}
					},
					"response": []
				},
				{
					"name": "Create Repayment - Invalid Amount",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									"pm.test(\"Status code is 400\", function () {",
									"    pm.response.to.have.status(400);",
									"});"
								],
								"type": "text/javascript"
							}
						}
					],
					"request": {
						"method": "POST",
						"header": [
							{
								"key": "Content-Type",
								"value": "application/json"
							}
						],
						"body": {
							"mode": "raw",
							"raw": "{\n  \"loanRequestId\": {{loanRequestId}},\n  \"amount\": -50.00\n}"
						},
						"url": {
							"raw": "{{baseUrl}}/repayments",
							"host": [
								"{{baseUrl}}"
							],
							"path": [
								"repayments"
							]
						}
					},
					"response": []
				},
				{
					"name": "Create Full Repayment",
					"request": {
						"method": "POST",
						"header": [
							{
								"key": "Content-Type",
								"value": "application/json"
							}
						],
						"body": {
							"mode": "raw",
							"raw": "{\n  \"loanRequestId\": {{loanRequestId}}\n}"
						},
						"url": {
							"raw": "{{baseUrl}}/repayments/full",
							"host": [
								"{{baseUrl}}"
							],
							"path": [
								"repayments",
								"full"
							]
						}
					},
					"response": []
				},
				{
					"name": "Get Repayments by Loan Request",
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "{{baseUrl}}/repayments/loan/{{loanRequestId}}",
							"host": [
								"{{baseUrl}}"
							],
							"path": [
								"repayments",
								"loan",
								"{{loanRequestId}}"
							]
						}
					},
					"response": []
				},
				{
					"name": "Get Total Repaid Amount",
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "{{baseUrl}}/repayments/total-repaid/{{loanRequestId}}",
							"host": [
								"{{baseUrl}}"
							],
							"path": [
								"repayments",
								"total-repaid",
								"{{loanRequestId}}"
							]
						}
					},
					"response": []
				},
				{
					"name": "Get Remaining Amount",
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "{{baseUrl}}/repayments/remaining/{{loanRequestId}}",
							"host": [
								"{{baseUrl}}"
							],
							"path": [
								"repayments",
								"remaining",
								"{{loanRequestId}}"
							]
						}
					},
					"response": []
				},
				{
					"name": "Get Repayment Analysis",
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "{{baseUrl}}/repayments/analysis/{{loanRequestId}}",
							"host": [
								"{{baseUrl}}"
							],
							"path": [
								"repayments",
								"analysis",
								"{{loanRequestId}}"
							]
						}
					},
					"response": []
				},
				{
					"name": "Get Total Repaid by User",
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "{{baseUrl}}/repayments/stats/repaid-by-user/{{userId}}",
							"host": [
								"{{baseUrl}}"
							],
							"path": [
								"repayments",
								"stats",
								"repaid-by-user",
								"{{userId}}"
							]
						}
					},
					"response": []
				},
				{
					"name": "Get Total Received by User",
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "{{baseUrl}}/repayments/stats/received-by-user/{{userId}}",
							"host": [
								"{{baseUrl}}"
							],
							"path": [
								"repayments",
								"stats",
								"received-by-user",
								"{{userId}}"
							]
						}
					},
					"response": []
				},
				{
					"name": "Get Repayments by Period",
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "{{baseUrl}}/repayments/period?start=2024-01-01T00:00:00&end=2024-12-31T23:59:59",
							"host": [
								"{{baseUrl}}"
							],
							"path": [
								"repayments",
								"period"
							],
							"query": [
								{
									"key": "start",
									"value": "2024-01-01T00:00:00"
								},
								{
									"key": "end",
									"value": "2024-12-31T23:59:59"
								}
							]
						}
					},
					"response": []
				},
				{
					"name": "Can User Repay Loan",
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "{{baseUrl}}/repayments/can-repay/{{userId}}/{{loanRequestId}}",
							"host": [
								"{{baseUrl}}"
							],
							"path": [
								"repayments",
								"can-repay",
								"{{userId}}",
								"{{loanRequestId}}"
							]
						}
					},
					"response": []
				}
			]
		}
	],
	"variable": [
		{
			"key": "baseUrl",
			"value": "http://localhost:8080/api",
			"type": "string"
		},
		{
			"key": "userId",
			"value": "1",
			"type": "string"
		},
		{
			"key": "budgetItemId",
			"value": "1",
			"type": "string"
		},
		{
			"key": "loanRequestId",
			"value": "1",
			"type": "string"
		},
		{
			"key": "userEmail",
			"value": "jean.dupont@email.com",
			"type": "string"
		},
		{
			"key": "userApiKey",
			"value": "test-api-key",
			"type": "string"
		}
	]
}

```