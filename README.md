# Pastebin

## Terms of Reference for Pastebin

## General Description
Develop a Pastebin system that allows users to create and share blocks of text. The system should provide storage of text blocks in Amazon S3, generation of unique and short URLs to access these blocks, and the ability to deactivate and delete blocks after a specified time.

### Basic Requirements

### 1. Uploading and storing text blocks
- [x] A user can create a block of text and upload it to the Amazon S3 system.
- [x] The system must provide secure storage of text blocks in Amazon S3.

### 2. URL generation and transmission
- [x] The system must generate unique and short URLs for each text block.
- [x] A user can send a link to a text block to another user who will see the same block of text when they click on it.
- [x] Ability to generate a hash for the URL in advance.

### 3. Deactivation and deletion of text blocks
- [x] User can specify the time after which a text block should be deactivated and removed from the system.
- [x] The system should automatically deactivate and delete text blocks after the specified time.

### 4. Optional Features
- [x] The system shall support the ability to determine the popularity of posts.
- [x] The system shall take into account the frequency of posts created by different users.

### Technical Details

### 1. Data Storage
- Using Amazon S3 to store text blocks.
- Storing metadata about text blocks (creation time, deactivation time, author, etc.) in a database (e.g. PostgreSQL).

### 2. URL generation
- Use of an algorithm to generate unique and short hashes for URLs.
- Ability to pre-generate hashes for URLs to ensure they are unique and short.

### 3. Deactivating and deleting text blocks
- Use a task scheduling mechanism (e.g. cron) to automatically deactivate and delete text boxes after a specified time.
- Notify the user when a text block is about to be deactivated and deleted.

### 4. Monitoring and statistics
- Implementation of a system for monitoring the popularity of posts.
- Collect and analyze data about the frequency of posts created by different users.


## Technologies used

* Java 17
* Spring: Web, Kafka, Validation
* Spring Boot: (Web, Security)
* Spring Cloud: Config, Eureka, OpenFeign
* Spring Data JPA,
* Flyway: Core, Database (PostgreSQL)
* PostgreSQL JDBC Driver
* ModelMapper, Lombok


## Getting Started

1. **Clone the repository:**

    ```bash
    git clone https://github.com/akorovai/pastebin.git
    ```

2. **Navigate to the repository directory:**

    ```bash
    cd pastebin
    ```

3. **Start Docker containers:**

    ```bash
    docker-compose up -d
    ```

4. **Navigate to the `services` directory:**

    ```bash
    cd services
    ```

5. **Build and run the services in the following order:**

    - **Navigate to the `config` directory:**

        ```bash
        cd config
        mvn clean install
        mvn spring-boot:run
        cd ..
        ```

    - **Navigate to the `discovery` directory:**

        ```bash
        cd discovery
        mvn clean install
        mvn spring-boot:run
        cd ..
        ```

    - **Navigate to and build/run the remaining services in any order:**

        ```bash
        cd [remaining_service_directory]
        mvn clean install
        mvn spring-boot:run
        cd ..
        ```


### Installation and Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/akorovai/pastebin.git
   cd pastebin
   ```

2. Start Docker containers:
   ```bash
   docker-compose up -d
   ```

3. Build and run services:
   ```bash
   cd services
   
   # Config Service
   cd config
   mvn clean install
   mvn spring-boot:run
   cd ..
   
   # Discovery Service
   cd discovery
   mvn clean install
   mvn spring-boot:run
   cd ..
   
   # Build and run remaining services
   for service in auth gateway hash-generator post; do
     cd $service
     mvn clean install
     mvn spring-boot:run &
     cd ..
   done
   ```

## API Endpoints

### Auth Service
- `POST /api/auth/register`: Register a new user
- `POST /api/auth/login`: Authenticate a user

### Post Service
- `POST /api/posts`: Create a new post
- `GET /api/posts/{id}`: Retrieve a post
- `GET /api/posts/rating`: Get post ratings

### Hash-Generator Service
- `POST /api/hash-post`: Create a post with a unique hash
- `GET /api/hash-post/{hash}`: Retrieve a post by hash

## Example

- created `requests.http` file in main folder for example requests
- 
## Configuration

- Active profile: `native` for `config`
- Config Server: Loads configurations from a native location
- Eureka Server: Runs on port 8761
- Database and Kafka configurations are defined in respective service configurations

## Security

- JWT-based authentication
- Spring Security for endpoint protection

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.