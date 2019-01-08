# ThoughWorks-HTTP-Hunt

A game sent to me by ThoughWorks as a test completely based on Rest API and calls

In this game, you will have to solve series of programming challenges till you hit the finish line. But there's a bit of a twist to it.

The whole game is driven by HTTP REST APIs. You can read about REST at https://en.wikipedia.org/wiki/Representational_state_transfer ( https://en.wikipedia.org/wiki/Representational_state_transfer )

Few important guidelines:

1. For every API call, pass your UserID as an HTTP header with key "userId". Your userId is "xyz123". If you don't do this step, you will receive an "Invalid user!” error.

2. For every POST API call, you need to pass "content-type" as "application/json" as well.

3. For every API call, use the host as https://host-link/

 

So how should you start playing?

Hit an endpoint GET https://host-link/challenge which would give you further instructions.

Conclusion: Need to make RAPI consumer app which will hit the GET calls for questions and input and POST the JSON as Output


How to Use:

Just import the project in Eclipse, replace the placeholders with the actual credentials (UserID and host IP) and RUN!!!
