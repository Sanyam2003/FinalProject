# Home Utilities Tracker

## Overview
Home Utilities Tracker is a full-stack web application designed to help users monitor, manage, and analyze their home utility usage (such as electricity, water, and gas) and associated costs. The application provides a user-friendly interface for logging utility usage, viewing reports, updating user profiles, and exporting data in both Excel and PDF formats. Security is enforced using modern best practices, including password hashing with BCrypt.

## Features
- **User Registration & Login:** Secure registration and authentication with BCrypt password hashing.
- **Profile Management:** Update email and password from the profile page.
- **Utility Usage Tracking:** Add, edit, and delete records for various utilities (electricity, water, gas, etc.) with details like appliance, sub-category, units used, cost, date, and notes.
- **Usage Reports:** View all your usage entries in a sortable, editable table.
- **Data Export:** Download your utility usage data as Excel (.xlsx) or PDF files with a single click.
- **Responsive UI:** Clean, modern, and mobile-friendly design using Thymeleaf and custom CSS.
- **Security:**
  - Spring Security for authentication and authorization
  - BCrypt for password hashing
  - CSRF protection (with exceptions for login/register)
- **Error & Success Feedback:** User-friendly messages for login errors, registration success/failure, and profile updates.

## Tech Stack
- **Backend:**
  - Java 21
  - Spring Boot 3.x
  - Spring Data JPA (Hibernate)
  - Spring Security
  - MySQL (or any JDBC-compatible database)
- **Frontend:**
  - Thymeleaf (server-side rendering)
  - HTML5, CSS3 (custom, responsive styling)
- **Libraries:**
  - Apache POI (Excel export)
  - OpenPDF (PDF export)
  - Lombok (for model boilerplate)
- **Build Tool:**
  - Maven

## Project Structure
```
home-utilities-tracker/
├── src/
│   ├── main/
│   │   ├── java/com/tracker/
│   │   │   ├── config/           # Security configuration
│   │   │   ├── controller/       # MVC controllers (login, register, report, etc.)
│   │   │   ├── model/            # JPA entities (User, UtilityUsage)
│   │   │   ├── repository/       # Spring Data JPA repositories
│   │   │   └── service/          # Business logic
│   │   └── resources/
│   │       ├── static/           # CSS, images
│   │       └── templates/        # Thymeleaf HTML templates
│   └── test/
│       └── java/com/tracker/     # Unit and integration tests
├── pom.xml                       # Maven build file
└── README.md                     # Project documentation
```

## How to Run
1. **Clone the repository:**
   ```
   git clone <repo-url>
   cd home-utilities-tracker
   ```
2. **Configure the database:**
   - Edit `src/main/resources/application.properties` with your MySQL credentials and database name.
3. **Build the project:**
   ```
   mvn clean install
   ```
4. **Run the application:**
   ```
   mvn spring-boot:run
   ```
5. **Access the app:**
   - Open [http://localhost:8080](http://localhost:8080) in your browser.

## Usage
- **Register:** Create a new account from the Register page.
- **Login:** Log in with your email and password.
- **Dashboard:** Navigate to add usage, view reports, or update your profile.
- **Add Usage:** Log new utility usage entries.
- **Edit/Delete Usage:** Use the report page to update or remove entries.
- **Export Data:** Use the buttons on the report page to download your data as Excel or PDF.
- **Profile:** Update your email or password securely.

## Security Notes
- Passwords are never stored in plain text; BCrypt is used for all password storage and validation.
- Only authenticated users can access and modify their own data.
- CSRF protection is enabled except for login/register endpoints.

## Customization
- **Styling:** Edit `src/main/resources/static/style.css` for custom themes.
- **Database:** Switch to another JDBC-compatible database by updating `application.properties`.
- **PDF/Excel Export:** Modify the export logic in `ReportController.java` for custom columns or formatting.

## Contributing
Pull requests are welcome! For major changes, please open an issue first to discuss what you would like to change.

## License
This project is licensed under the MIT License.

## Acknowledgements
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Thymeleaf](https://www.thymeleaf.org/)
- [Apache POI](https://poi.apache.org/)
- [OpenPDF](https://github.com/LibrePDF/OpenPDF)
- [Lombok](https://projectlombok.org/)

---
**Developed by:** Your Name / Team

