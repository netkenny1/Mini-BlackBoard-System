# Design Document: Mini Blackboard System

## Table of Contents
1. [Architecture Overview](#architecture-overview)
2. [Design Patterns and Principles](#design-patterns-and-principles)
3. [Class Structure and Organization](#class-structure-and-organization)
4. [Data Structure Choices](#data-structure-choices)
5. [File Persistence System](#file-persistence-system)
6. [Design Decisions and Rationale](#design-decisions-and-rationale)

---

## Architecture Overview

The Mini Blackboard System follows a **layered architecture** with clear separation of concerns:

```
┌─────────────────────────────────────┐
│         Views Layer                 │
│  (AdminMenu, TeacherMenu,          │
│   StudentMenu)                     │
├─────────────────────────────────────┤
│         Services Layer              │
│  (AuthenticationService,           │
│   CourseService, GradeService,     │
│   AssignmentService,                │
│   DataPersistenceService)          │
├─────────────────────────────────────┤
│         Models Layer                │
│  (User, Student, Teacher, Admin,    │
│   Course, Assignment, Grade)       │
├─────────────────────────────────────┤
│         Utils Layer                 │
│  (InputValidator)                  │
└─────────────────────────────────────┘
```

This architecture ensures:
- **Separation of Concerns**: Each layer has a distinct responsibility
- **Maintainability**: Changes in one layer don't affect others
- **Testability**: Each layer can be tested independently
- **Scalability**: New features can be added without disrupting existing code

---

## Design Patterns and Principles

### 1. Object-Oriented Programming Principles

#### Inheritance
**Implementation**: The `User` class is an abstract base class that `Student`, `Teacher`, and `Admin` extend.

**Why this design?**
- **Code Reusability**: Common attributes (userId, password, name, role) are defined once in the `User` class
- **Polymorphism**: All user types can be treated uniformly through the `User` reference
- **Maintainability**: Changes to user authentication logic only need to be made in one place
- **Extensibility**: New user types can be added easily by extending `User`

**Example**: When the `AuthenticationService` needs to find a user, it works with `User` objects, not specific student/teacher/admin types. This allows the same methods to work for all user types.

#### Encapsulation
**Implementation**: All model classes use private fields with public getters and setters.

**Why this design?**
- **Data Protection**: Private fields prevent direct access and modification from outside the class
- **Validation**: Setters can include validation logic (e.g., password length checks)
- **Flexibility**: Internal implementation can change without affecting dependent code
- **Control**: Access to data is controlled through methods, enabling logging, validation, or side effects

**Example**: In the `Student` class, the `enrolledCourses` list is private. External code cannot directly manipulate it; they must use `enrollInCourse()` or `getEnrolledCourses()`, ensuring data integrity.

#### Abstraction
**Implementation**: `User` is an abstract class that defines common behavior without being instantiable.

**Why this design?**
- **Conceptual Clarity**: `User` represents a concept that should never exist independently - only specific user types (Student, Teacher, Admin) should be instantiated
- **Contract Definition**: The abstract class defines what all users must have, but allows each subclass to implement specific behaviors
- **Prevents Errors**: Making `User` abstract prevents accidentally creating a generic user object

**Example**: The `toString()` method is defined in `User` but can be overridden in subclasses to provide role-specific string representations.

#### Polymorphism
**Implementation**: Methods throughout the system work with `User` references but can handle `Student`, `Teacher`, or `Admin` objects.

**Why this design?**
- **Flexibility**: The same code can work with different user types
- **Dynamic Behavior**: The correct method is called at runtime based on the actual object type
- **Simplified Logic**: Instead of separate methods for each user type, one method handles all

**Example**: In `Main.java`, the login method returns a `User` object. Based on the user's role, the appropriate menu (AdminMenu, TeacherMenu, or StudentMenu) is displayed, demonstrating polymorphic behavior.

---

## Class Structure and Organization

### Models Package (`com.blackboard.models`)

The models package contains **Plain Old Java Objects (POJOs)** - classes that only hold data and have no business logic.

#### Why Models are POJOs:
1. **Single Responsibility**: Models only represent data structures
2. **Reusability**: Models can be used across different services without coupling
3. **Testability**: Simple data classes are easy to test
4. **Separation**: Business logic lives in services, not in models

#### Class Hierarchy:
```
User (abstract)
├── Student
├── Teacher
└── Admin
```

**Why this hierarchy?**
- All three user types share common attributes (ID, password, name, role)
- Each has unique attributes:
  - `Student`: major, enrolledCourses
  - `Teacher`: department, assignedCourses
  - `Admin`: minimal additional attributes (system administrator)
- The hierarchy allows polymorphism while maintaining type-specific features

#### Course, Assignment, and Grade Models:
These are standalone classes because:
- They don't share a common parent concept
- Each represents a distinct entity in the domain
- They maintain relationships through references (IDs) rather than inheritance

### Services Package (`com.blackboard.services`)

Services contain all business logic and operations on data.

#### Why Services Exist:
1. **Business Logic Separation**: All rules and operations are centralized
2. **Data Access Abstraction**: Models don't need to know how data is stored
3. **Reusability**: Multiple views can use the same services
4. **Testability**: Services can be tested independently of UI

#### Service Responsibilities:

**AuthenticationService**:
- Manages user authentication and authorization
- Handles user CRUD operations
- Maintains the user registry

**CourseService**:
- Manages course operations (create, update, delete)
- Handles course-student relationships (enrollment)
- Manages course-teacher assignments
- Enforces business rules (e.g., capacity limits)

**AssignmentService**:
- Manages assignment operations
- Links assignments to courses
- Provides queries for assignments by course or student

**GradeService**:
- Manages grade operations
- Calculates final grades
- Links grades to students and assignments

**DataPersistenceService**:
- Handles all file I/O operations
- Manages data serialization/deserialization
- Coordinates saving and loading of all entities

### Views Package (`com.blackboard.views`)

Views handle all user interaction through the console.

#### Why Separate Views:
1. **User Experience**: Each role has a different interface
2. **Separation of Concerns**: UI logic is separate from business logic
3. **Maintainability**: UI changes don't affect business logic
4. **Role-Based Access**: Each menu only shows relevant options

#### View Responsibilities:
- **AdminMenu**: Administrative functions (user management, course management)
- **TeacherMenu**: Teaching functions (view courses, create assignments, grade students)
- **StudentMenu**: Student functions (view courses, view assignments and grades)

### Utils Package (`com.blackboard.utils`)

Utility classes provide reusable helper functions.

#### InputValidator:
- **Purpose**: Centralizes input validation logic
- **Why Static Methods**: Validation doesn't require instance state
- **Benefits**: Reusable across all views, consistent validation rules

---

## Data Structure Choices

### ArrayList for Collections

**Why ArrayList?**
- **Dynamic Size**: Unlike arrays, ArrayLists can grow/shrink as needed
- **Order Preservation**: Maintains insertion order (important for displaying lists)
- **Easy Iteration**: Enhanced for-loops work seamlessly
- **Performance**: O(1) access time for get operations
- **Java Standard**: Part of Java Collections Framework, well-understood

**Usage Examples:**
- `Student.enrolledCourses: ArrayList<Course>` - Students can enroll in multiple courses
- `Teacher.assignedCourses: ArrayList<Course>` - Teachers can teach multiple courses
- `AuthenticationService.users: ArrayList<User>` - All users in the system
- `CourseService.courses: ArrayList<Course>` - All courses in the system

**Alternative Considered**: `HashSet`
- **Rejected because**: Order matters for display, and we need to allow duplicate enrollments (though we prevent them through business logic)

### String for IDs

**Why String?**
- **Flexibility**: Can accommodate various ID formats (STUDENT001, T001, CSCI101)
- **Human Readable**: Easy to understand and debug
- **No Constraints**: No need to worry about integer overflow
- **Compatibility**: Works well with file I/O (text-based storage)

**Trade-off**: Less type safety than an enum or custom ID class, but provides needed flexibility.

### double for Points/Grades

**Why double?**
- **Precision**: Can handle decimal values (e.g., 87.5 points)
- **Standard Practice**: Grades often involve partial points
- **Calculation Support**: Needed for percentage calculations

**Consideration**: Could use `BigDecimal` for exact precision, but `double` is sufficient for this application's requirements.

---

## File Persistence System

### Design Overview

The system uses **comma-separated values (CSV)** format stored in `.txt` files for simplicity and human readability.

### File Structure

Each entity type has its own file:
- `users.txt` - All user accounts
- `courses.txt` - All courses
- `assignments.txt` - All assignments
- `grades.txt` - All grades
- `enrollments.txt` - Student-course relationships

### Why Separate Files?

1. **Separation of Concerns**: Each entity type is independent
2. **Easy Debugging**: Can view/edit individual files
3. **Selective Loading**: Can load only needed data
4. **Maintainability**: Changes to one entity type don't affect others

### Data Format

**users.txt Format:**
```
userId,password,name,role,additionalInfo
STUDENT001,pass123,John Doe,STUDENT,Computer Science
TEACHER001,pass456,Dr. Smith,TEACHER,Mathematics
ADMIN001,admin123,System Admin,ADMIN,
```

**courses.txt Format:**
```
courseId,courseName,description,teacherId,capacity
CSCI101,Java Programming,Introduction to Java,T001,30
```

**assignments.txt Format:**
```
assignmentId,courseId,title,description,dueDate,maxPoints
A001,CSCI101,Homework 1,Basic Java concepts,2024-12-15,100.0
```

**grades.txt Format:**
```
gradeId,studentId,assignmentId,points
G001,STUDENT001,A001,85.5
```

**enrollments.txt Format:**
```
studentId,courseId
STUDENT001,CSCI101
```

### Why CSV Format?

1. **Human Readable**: Can be opened in any text editor
2. **Simple Parsing**: Easy to split by commas
3. **No External Dependencies**: No need for JSON/XML libraries
4. **Easy Debugging**: Can manually edit files if needed
5. **Platform Independent**: Works on any operating system

### Persistence Flow

#### Loading Data (Startup):
1. `Main.main()` creates `DataPersistenceService`
2. Calls `loadAllData()` which loads:
   - Users first (needed for other relationships)
   - Courses second
   - Enrollments third (depends on users and courses)
   - Assignments fourth
   - Grades last (depends on assignments and students)

**Why this order?**
- Enrollments depend on both users and courses existing
- Grades depend on assignments and students existing
- Loading order ensures all dependencies are available

#### Saving Data (Shutdown):
1. User logs out or program exits
2. `saveAllData()` is called
3. All data is written to files in the same order
4. Files are overwritten completely (simple approach)

**Why overwrite completely?**
- Simpler than tracking changes
- Ensures data consistency
- Small-scale system doesn't need incremental updates

### Error Handling

**File Not Found:**
- If a file doesn't exist, the system creates it (first run)
- Empty files are handled gracefully (no data to load)

**Corrupted Data:**
- Invalid lines are skipped with error messages
- System continues loading other valid data
- Logs errors to console for debugging

**Why This Approach:**
- **Resilience**: System doesn't crash if one line is corrupted
- **User-Friendly**: Clear error messages help identify issues
- **Graceful Degradation**: System continues functioning with partial data

---

## Design Decisions and Rationale

### 1. Why Abstract User Class Instead of Interface?

**Decision**: Made `User` an abstract class rather than an interface.

**Rationale**:
- **Code Reuse**: Common fields (userId, password, name, role) and methods can be shared
- **Partial Implementation**: Can provide concrete methods (getters/setters) that all subclasses use
- **Default Behavior**: `toString()` method can have a default implementation
- **Flexibility**: Can add new concrete methods later without breaking existing code

**Trade-off**: Java doesn't support multiple inheritance, but interfaces aren't needed here since users don't need to implement multiple roles.

### 2. Why Separate Services Instead of One Large Service?

**Decision**: Split functionality into multiple service classes.

**Rationale**:
- **Single Responsibility Principle**: Each service has one clear purpose
- **Maintainability**: Easier to find and modify code
- **Testability**: Can test each service independently
- **Scalability**: Can add new services without modifying existing ones

**Example**: If we need to add email notifications, we create a `NotificationService` without touching existing code.

### 3. Why Menu Classes Instead of Switch Statements in Main?

**Decision**: Created separate menu classes for each role.

**Rationale**:
- **Separation of Concerns**: UI logic is separate from application logic
- **Readability**: Each menu class is focused and easier to understand
- **Maintainability**: Changes to one menu don't affect others
- **Extensibility**: Easy to add new menu options or modify existing ones

**Alternative Considered**: One large switch statement in `Main.java`
- **Rejected**: Would create a massive, hard-to-maintain file

### 4. Why Store Relationships via IDs Instead of Object References?

**Decision**: Store relationships using String IDs (e.g., `courseId`, `studentId`) rather than direct object references.

**Rationale**:
- **Persistence**: IDs can be easily serialized to text files
- **Loose Coupling**: Objects don't need direct references to each other
- **Flexibility**: Can load objects independently and reconstruct relationships
- **Circular Reference Avoidance**: Prevents issues with serialization

**Example**: `Course` stores `teacherId` (String) instead of `Teacher` object. When loading, we look up the teacher by ID.

**Trade-off**: Slightly more lookups required, but significantly simpler persistence.

### 5. Why Capacity Checking in CourseService Instead of Course Model?

**Decision**: Capacity validation happens in `CourseService.enrollStudentInCourse()` rather than in the `Course` model.

**Rationale**:
- **Separation of Concerns**: Models are POJOs (data only), business logic is in services
- **Centralized Logic**: All course-related operations are in one place
- **Context Awareness**: Service has access to all students to count enrollments
- **Testability**: Business logic can be tested independently of data models

**Example**: `Course` model only stores capacity; `CourseService` checks if enrollment would exceed capacity.

### 6. Why Automatic Student ID Generation?

**Decision**: Automatically generate student IDs (STUDENT001, STUDENT002, etc.) instead of requiring manual input.

**Rationale**:
- **User Experience**: Reduces input errors and simplifies account creation
- **Consistency**: Ensures uniform ID format
- **Uniqueness**: Guarantees unique IDs without manual checking
- **Scalability**: System can handle any number of students

**Implementation**: `AdminMenu.generateStudentId()` counts existing students and generates next sequential ID.

### 7. Why ArrayList of Courses in Student Instead of Separate Enrollment Table?

**Decision**: Store `enrolledCourses` as an `ArrayList<Course>` in the `Student` class.

**Rationale**:
- **Direct Access**: Students can immediately access their courses
- **Performance**: No need to query enrollments separately
- **Object-Oriented**: Follows natural relationship (student "has" courses)
- **Simplicity**: Easier to work with than separate relationship objects

**Trade-off**: When loading, we need to reconstruct this relationship from `enrollments.txt`, but this is acceptable for the simplicity gained.

### 8. Why Calculate Final Grade in GradeService?

**Decision**: Final grade calculation is in `GradeService.calculateFinalGradeForCourse()`.

**Rationale**:
- **Business Logic**: Grade calculation is business logic, not data
- **Reusability**: Can be used by both students and teachers
- **Centralization**: All grade-related operations in one place
- **Testability**: Can test calculation logic independently

**Formula**: `(Total Points Earned / Total Points Possible) * 100`

### 9. Why Private Methods in DataPersistenceService?

**Decision**: Made save/load methods for each entity type private, with only `loadAllData()` and `saveAllData()` public.

**Rationale**:
- **Encapsulation**: Internal implementation details are hidden
- **Consistency**: Ensures data is always loaded/saved in correct order
- **Simplicity**: External code doesn't need to know about individual save/load methods
- **Error Prevention**: Prevents accidentally loading data in wrong order

### 10. Why Static Methods in InputValidator?

**Decision**: All validation methods in `InputValidator` are static.

**Rationale**:
- **No State**: Validation doesn't require instance variables
- **Utility Functions**: Pure functions that only process input
- **Convenience**: Can call without creating an object
- **Memory Efficiency**: No unnecessary object creation

**Example**: `InputValidator.isValidPassword(password)` can be called directly without `new InputValidator()`.

---

## System Flow

### Application Startup:
1. `Main.main()` is called
2. Services are initialized
3. `DataPersistenceService.loadAllData()` loads all data from files
4. Default admin is created if no users exist
5. Main menu is displayed

### User Login:
1. User enters credentials
2. `AuthenticationService.login()` validates credentials
3. Returns `User` object
4. Based on role, appropriate menu is displayed

### Data Modification:
1. User performs action (e.g., create student)
2. View calls appropriate service method
3. Service updates data structures
4. On logout/exit, `DataPersistenceService.saveAllData()` saves all data

### Application Shutdown:
1. User selects logout or exit
2. `saveAllData()` is called
3. All data is written to files
4. Program exits

---

## Conclusion

This design prioritizes:
- **Clarity**: Easy to understand and navigate
- **Maintainability**: Changes can be made without breaking other parts
- **Extensibility**: New features can be added easily
- **Testability**: Each component can be tested independently
- **Simplicity**: No unnecessary complexity

The architecture follows established software engineering principles while remaining practical for the project's scope. The separation of models, services, and views creates a clean, maintainable codebase that demonstrates proper use of Object-Oriented Programming principles.

