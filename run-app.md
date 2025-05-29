cd back-end && mvn spring-boot:run

* Configure the frontend
  * Open the [script.js](front-end/script.js) file and set the `apiURL` to the backend URL
    ```js
    const apiURL = 'http://localhost:8080';
    const apiURL = 'https://captainfedo1-8080.theiadockernext-0-labs-prod-theiak8s-4-tor01.proxy.cognitiveclass.ai';
    ```
  * Open the [reviews.html](front-end/reviews.html) file and set the URL in the `function getReviews(storeId, productId)` method to the backend URL as well.
    Just change the URL as you still need the remaining part of the variable.
    ```js
    url = `http://localhost:8080/reviews/${storeId}/${productId}`;
    ```
* Run the frontend
  ```shell
  
  cd front-end && python3 -m http.server
  ```
  This should start a Python server and serve the front end files on port 8000.
