# AI-Chat-Service.
Integrating Gemini with springboot application

Steps to run application:-

Go to https://aistudio.google.com/app/api-keys

Create API key 

Copy it and paste it in application.properties file

you can view your app usage here https://aistudio.google.com/app/usage?project=gen-lang-client-123431345&timeRange=last-28-days


curl:-
postman request POST 'http://localhost:8080/chat' \
  --header 'Content-Type: application/json' \
  --body '{
  "userId": "user1",
  "message": "What is html"
}'

Url:-
To get updated url for Gemini you can visit https://ai.google.dev/gemini-api/docs 
Go to Gemini API and click Rest you will get the update url.

