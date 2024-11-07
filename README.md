# Hlæja Device Data

Classes for the devices, to structure each state, Messages exchanged, as data awaits, Classes for sensors, each with their role, Bound to one purpose: transfer the whole. Through each link they connect, from device to gateway’s call, In the world of services, where data flows for all.

## Properties for deployment

| name                   | required | info                    |
|------------------------|----------|-------------------------|
| spring.profiles.active | *        | Spring Boot environment |

Required: * can be stored as text, and ** need to be stored as secret.  

## Releasing Service

Run `release.sh` script from `master` branch.

## Development Configuration

### Global gradle properties

To authenticate with Gradle to access repositories that require authentication, you can set your user and token in the `gradle.properties` file.

Here's how you can do it:

1. Open or create the `gradle.properties` file in your Gradle user home directory:
   - On Unix-like systems (Linux, macOS), this directory is typically `~/.gradle/`.
   - On Windows, this directory is typically `C:\Users\<YourUsername>\.gradle\`.
2. Add the following lines to the `gradle.properties` file:
    ```properties
    repository.user=your_user
    repository.token=your_token_value
    ```
   or use environment variables `REPOSITORY_USER` and `REPOSITORY_TOKEN`
