# HackenTestTask

## 1. Preparing
* Install such programs:
- JDK 21
- Maven 3
- MySQL Workbench
- Postman

## 2. Getting Started
* Clone the project repository to your local machine.
* Open MySQL Workbench and run /src/main/resources/sql/db.sql file
* Configure the database connection(your credentials) in the [resources/application.yaml](/src/main/resources/application.yaml)  file.

## 3. Build project

Run `./mvnw clean install`

...or, if you have installed maven:

Run `mvn clean install`

## 4. Run locally
After that, you should go to the target folder and execute the following command using the console:
``` sh

java -jar BookStore-0.0.1-SNAPSHOT.jar
``` 

## Swagger
To access to swagger page go to `http://localhost:8082/swagger-ui/index.html`


## API Endpoints
### Upload CSV file
#### Endpoint: http://localhost:8082/records/upload
##### Method: POST
#### Params:
- `delimiter` (required): The delimiter character used in the CSV file. Example: `.`
#### Request Body:
- `file` (required): The CSV file to upload.
- Example: 
``` sh
Alice Backer.30.Engineer.IT
Bob Black.25.Designer.Marketing
Charlie Jackson.35.Manager.HR
Diana Smith.28.Developer.IT
``` 
#### Response:
- File uploaded and data saved successfully.

### Retrieve Records by Criteria
#### Endpoint: http://localhost:8082/records/search
##### Method: GET
#### Parameters:
- `fullName` (optional): Full name of the record.
- `age` (optional): Age of the record.
- `position` (optional): Position of the record.
- `department` (optional): Department of the record.
- `pageNum` (optional, default: 1): Page number of the results. Minimum value: 1.
- `pageSize` (optional, default: 1): Number of records per page. Minimum value: 1.

#### Response:
- Returns a paginated list of records matching the specified criteria.
    - Example response body:
      ```json
      {
        "content": [
          {
            "fullName": "Alice",
            "age": 30,
            "position": "Designer",
            "department": "IT"
          },
          {
            "fullName": "Bob",
            "age": 30,
            "position": "Designer",
            "department": "Marketing"
          }
        ],
      "pageable": {
        "pageNumber": 0,
        "pageSize": 11,
        "sort": {
            "empty": false,
            "sorted": true,
            "unsorted": false
        },
        "offset": 0,
        "unpaged": false,
        "paged": true
       },
        "last": true,
        "totalElements": 2,
        "totalPages": 1,
        "pageNumber": 1,
        "pageSize": 11,
         "number": 0,
         "sort": {
         "empty": false,
         "sorted": true,
           "unsorted": false
          },
          "numberOfElements": 2,
          "first": true,
         "empty": false
  
      }
      ```

### Search Records by Text
#### Endpoint: http://localhost:8082/records/text
##### Method: GET
#### Parameters:
- `text`: Text to search within records.
- `pageNum` (optional, default: 1): Page number of the results. Minimum value: 1.
- `pageSize` (optional, default: 1): Number of records per page. Minimum value: 1.
### Response:
- Returns a paginated list of records matching the specified criteria.
    - Example response body:
      ```json
      {
        "content": [
          {
            "fullName": "Alice",
            "age": 30,
            "position": "Designer",
            "department": "IT"
          },
          {
            "fullName": "Bob",
            "age": 30,
            "position": "Designer",
            "department": "Marketing"
          }
        ],
      "pageable": {
        "pageNumber": 0,
        "pageSize": 11,
        "sort": {
            "empty": false,
            "sorted": true,
            "unsorted": false
        },
        "offset": 0,
        "unpaged": false,
        "paged": true
       },
        "last": true,
        "totalElements": 2,
        "totalPages": 1,
        "pageNumber": 1,
        "pageSize": 11,
         "number": 0,
         "sort": {
         "empty": false,
         "sorted": true,
           "unsorted": false
          },
          "numberOfElements": 2,
          "first": true,
         "empty": false
  
      }
      ```

