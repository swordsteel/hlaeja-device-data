# Hlæja Device Data

Classes for the devices, to structure each state, Messages exchanged, as data awaits, Classes for sensors, each with their role, Bound to one purpose: transfer the whole. Through each link they connect, from device to gateway’s call, In the world of services, where data flows for all.

## Properties for deployment

| name                   | required | info                    |
|------------------------|----------|-------------------------|
| spring.profiles.active | *        | Spring Boot environment |
| influxdb.bucket        |          | InfluxDB bucket         |
| influxdb.org           |          | InfluxDB organization   |
| influxdb.token         | **       | InfluxDB access token   |
| influxdb.url           | *        | InfluxDB host url       |

Required: * can be stored as text, and ** need to be stored as secret.  

## Releasing Service

Run release pipeline from `master` branch.

## Development Configuration

### Global gradle properties

To authenticate with Gradle for services that require authentication, you can set your users and tokens in the `gradle.properties` file.

Here's how you can do it:

1. Open or create the `gradle.properties` file in your Gradle user home directory:
   - On Unix-like systems (Linux, macOS), this directory is typically `~/.gradle/`.
   - On Windows, this directory is typically `C:\Users\<YourUsername>\.gradle\`.
2. Add the following lines to the `gradle.properties` file:
   ```properties
   repository.user=your_user
   repository.token=your_token_value
   
   influxdb.token=your_token_value
   ```
   or use environment variables `REPOSITORY_USER`, `REPOSITORY_TOKEN`, and `INFLUXDB_TOKEN`

### InfluxDB configuration

Create Bucket and Organization in InfluxDB (Local Development)

Log in to InfluxDB (e.g., via the InfluxDB UI at http://localhost:8086).
Click Create Organization, in the field `Organization Name` enter `hlaeja_ltd`, and in field `Bucket Name` enter `device-data`.
