## Terms of Reference for Pastebin

## General Description
Develop a Pastebin system that allows users to create and share blocks of text. The system should provide storage of text blocks in Amazon S3, generation of unique and short URLs to access these blocks, and the ability to deactivate and delete blocks after a specified time.

### Basic Requirements

### 1. Uploading and storing text blocks
- [ ] A user can create a block of text and upload it to the Amazon S3 system.
- [ ] The system must provide secure storage of text blocks in Amazon S3.

### 2. URL generation and transmission
- [x] The system must generate unique and short URLs for each text block.
- [x] A user can send a link to a text block to another user who will see the same block of text when they click on it.
- [x] Ability to generate a hash for the URL in advance.

### 3. Deactivation and deletion of text blocks
- [x] User can specify the time after which a text block should be deactivated and removed from the system.
- [x] The system should automatically deactivate and delete text blocks after the specified time.

### 4. Optional Features
- [ ] The system shall support the ability to determine the popularity of posts.
- [ ] The system shall take into account the frequency of posts created by different users.

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


