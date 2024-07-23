## Issues and Improvements

### Build Issues
- **Current Issue:** The project currently fails to build in environments other than the local setup.
- **Action Needed:**
    - Improve the connections between microservices to resolve build issues.
    - Create and add Docker Compose files to enable a containerized deployment environment.

### Keycloak Integration
- **Current Issue:** Users are being saved to the PostgreSQL database but not to Keycloak.
- **Action Needed:**
    - Update the project to ensure that users created within the application are also registered in Keycloak.
    - Ensure that role-based authorization is fully functional and synchronized with Keycloak.
