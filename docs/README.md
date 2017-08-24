# Nine Gold API
This is the deployment guide for the Nine Gold API.

## Prerequisites
1. Java 8
2. Maven 3+
3. Postgres 9.3+ that supports 
4. SMTP Server. You may use [FakeSMTP](https://github.com/Nilhcem/FakeSMTP) for development testing purpose
5. Chrome with postman add-on (to verify api only)

## Video links
https://drive.google.com/file/d/0B8ofBd1aoVrqNExQaFJGUHlmZjg/view
https://drive.google.com/file/d/0B8ofBd1aoVrqUEF6MWltSW9jaFU/view

## DB Setup
- Create database schema with `sqls/schema.sql`.
- Create tables in each schema with `sqls/ddl.sql`.
- If you want to drop all tables please run `sqls/drop.sql` on each schema.
- If you want to clean all tables please run `sqls/clear.sql` on each schema.

## API Configuration
### application configuration
Edit file `backend/src/main/resources/application.properties`:
Edit stripe related configs 
- **stripe.apiKey**: api key
- **stripe.plan.name** : plan name
- **stripe.plan.type** : plan type
- **stripe.plan.interval** : plan interval
- **stripe.plan.currency** : plan currency
- **stripe.plan.amount** : plan amount

Edit datasource settings
- **spring.datasource.url**: PG server connection url
- **spring.datasource.username**: PG Server username
- **spring.datasource.password**: PG Server password
- **spring.mail.host**: SMTP Server host
- **spring.mail.port**: SMTP Server port

You can keep the rest of the parameters unchanged.

### email template configuration
Edit file `backend/src/main/resources/templates`:
- **html/__template_name__-body.html**: The email body template
- **text/__template_name__-subject.txt**: the email subject template


### customize configurations using command line parameters or system variables
Please check all property keys in `backend/src/main/resources/application.properties`.
You can customize any of these keys using command line parameters or system variables if you want to.

## Deployment
You can run below mvn command to run application directly, this application will automatically restart if you change code.
``` bash
mvn clean install
cd backend
mvn spring-boot:run
```
You can also run directly from Idea Intellij also.

## Swagger
Open **http://editor.swagger.io/** and copy  `docs/swagger.yaml` to verify.

## Verification
-- Setup DB first by following DB setup step
-- Import Postman collection `docs/NineGold-API.postman_collection.json`.
-- Ensure that a Stripe plan is created using this api - http://localhost:8080/stripe/plan since a plan is mandatory before a customer registration
-- You can check the video link - https://drive.google.com/file/d/0B8ofBd1aoVrqNExQaFJGUHlmZjg/view - to see how you can go about testing using the Stripe dashboard and the postman collection
-- You can use ultrahook to forward requests to localhost in this manner
ultrahook -k RXAblwmHJLXOo597NcFDuOOfLYEjEYlf stripe http://localhost:8080/stripe/handleEvent
-- This video shows how you can directly test the webhooks https://drive.google.com/file/d/0B8ofBd1aoVrqUEF6MWltSW9jaFU/view
-- Webhooks can be tested from stripe end also by API section in the Stripe dashboard


## front end
Refer `frontend/src/main/frontend/README.md` file.
