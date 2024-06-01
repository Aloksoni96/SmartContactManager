#  Smart Contact Manager 

Welcome to the Smart Contact Manager project! This application is designed to help users manage their contacts efficiently. It provides functionalities to create, read, update, and delete contacts in a contact book.



## Technologies Used

 - Java
 - Spring MVC
 - thymeleaf
 - Spring Security

 ## Dependencies

- Java Development Kit (JDK)
- Apache Tomcat Server
- Spring MVC framework
- Spring Security framework
- JSP support
- Other dependencies specified in the project's build file (e.g., pom.xml for Maven)


**Request**
```
GET /add-contact
```
**Response**
- HTTP Status: 200 OK (User can add the contact)
    ```
    "Contact is added"
    ```
- HTTP Status: 400 Bad Request (if the user add invalid contact )
    ```
    "Invalid Contact. Check your field again"
    ```

**Request**
```
POST /process-contact
```
**Response**
- HTTP Status: 200 OK (process the add contact form )
    ```
    "Contact is added"
    ```
- HTTP Status: 400 Bad Request (Not able to process check for invalid contact )
    ```
    "Invalid Contact. Check your field again"
    ```

 **Request**
```
GET /show-contacts/{page}
```
**Response**
- HTTP Status: 200 OK (Show Contact current pages & numbeer of item per page )
    ```
    "Contact is added"
    ```
- HTTP Status: 400 Bad Request (invalid pages )
    ```
    "Invalid Contact. Check your field again"
    ```

**Request**
```
GET /{cId}/contact
```
**Response**
- HTTP Status: 200 OK (showing particular contact details )
    ```
    "Contact is added"
    ```
- HTTP Status: 400 Bad Request (invalid contact id )
    ```
    "Invalid Contact. Check your field again"
    ```

**Request**
```
DELETE delete/{cId}
```
**Response**
- HTTP Status: 200 OK (delete contact details )
    ```
    "Contact is added"
    ```
- HTTP Status: 400 Bad Request (invalid contact id )
    ```
    "Invalid Contact. Check your field again"
    ```

**Request**
```
PUT /process-update
```
**Response**
- HTTP Status: 200 OK (Update Contact details )
    ```
    "Contact is added"
    ```
- HTTP Status: 400 Bad Request (invalid contact id )
    ```
    "Invalid Contact. Check your field again"
    ```


    ## Future Enhancements

- Contact search functionality: Add a search feature to allow users to search for specific - contacts based on criteria such as name, phone number, or email.
- Contact categories: Introduce the concept of contact categories or groups to organize - contacts more efficiently.
- Contact sharing: Implement the ability to share contacts with other users through email or other communication channels.
- User profile customization: Allow users to customize their profile settings, such as changing their profile picture or updating personal information.
- Contact import/export: Provide options to import contacts from external sources (e.g., CSV files) or export contacts to various formats (e.g., CSV, vCard).
- Integration with other platforms: Integrate the contact manager with popular platforms like Google Contacts or Microsoft Outlook for seamless synchronization.
- Mobile application: Develop a mobile application version of the Smart Contact Manager to provide users with on-the-go access to their contacts.
- Please note that these are just ideas for future enhancements and may require additional development and integration efforts.

Thank you for using the Smart Contact Manager project! If you have any questions or need further assistance, please don't hesitate to reach out.
