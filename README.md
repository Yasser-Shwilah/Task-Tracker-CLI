# Task Tracker CLI

A simple command-line interface (CLI) application to track and manage tasks. This application allows you to add, update, delete, and list tasks, with all data stored in a JSON file.

## Features

- Add new tasks
- Update existing task descriptions
- Delete tasks
- Mark tasks as in-progress or done
- List all tasks
- Filter tasks by status (todo, in-progress, done)
- Persistent storage using JSON file

## Requirements

- Java 8 or higher

## Installation and Compilation

1. Navigate to the project directory:
   ```bash
   cd TaskTrackerCLI1
   ```

2. Compile the Java files:
   ```bash
   javac src/*.java
   ```

## Usage

Run the application using the following command format:
```bash
java -cp src Main <command> [arguments]
```

### Commands

#### Add a new task
```bash
java -cp src Main add "Buy groceries"
```
Output: `Task added successfully (ID: 1)`

#### Update a task description
```bash
java -cp src Main update 1 "Buy groceries and cook dinner"
```
Output: `Task updated successfully`

#### Delete a task
```bash
java -cp src Main delete 1
```
Output: `Task deleted successfully`

#### Mark a task as in-progress
```bash
java -cp src Main mark-in-progress 1
```
Output: `Task marked as in-progress`

#### Mark a task as done
```bash
java -cp src Main mark-done 1
```
Output: `Task marked as done`

#### List all tasks
```bash
java -cp src Main list
```
Output:
```
All Tasks:
1. Buy groceries and cook dinner [in-progress]
2. Complete project documentation [done]
```

#### List tasks by status
```bash
java -cp src Main list todo
java -cp src Main list in-progress
java -cp src Main list done
```

Output (example for in-progress):
```
Tasks with status 'in-progress':
1. Buy groceries and cook dinner [in-progress]
```

#### Show help
```bash
java -cp src Main
```

## Task Properties

Each task contains the following properties:
- **id**: Unique identifier for the task
- **description**: Short description of the task
- **status**: Current status (todo, in-progress, done)
- **createdAt**: Date and time when the task was created
- **updatedAt**: Date and time when the task was last updated

## Data Storage

Tasks are stored in a `tasks.json` file in the current directory. The file is automatically created when you add your first task. The JSON format is:

```json
[
  {
    "id": 1,
    "description": "Buy groceries and cook dinner",
    "status": "in-progress",
    "createdAt": "2026-05-09 23:06:37",
    "updatedAt": "2026-05-09 23:07:59"
  }
]
```

## Error Handling

The application includes error handling for:
- Invalid command arguments
- Non-existent task IDs
- File I/O errors
- Invalid task ID format

## Project Structure

```
TaskTrackerCLI1/
├── src/
│   ├── Main.java              # Entry point
│   ├── TaskTrackerCLI.java    # Main CLI logic
│   ├── Task.java              # Task data model
│   ├── TaskManager.java       # Task management class
│   └── JsonFileHandler.java   # JSON file operations
├── tasks.json                 # Data storage (created automatically)
└── README.md                  # This file
```

## Implementation Notes

- Uses only native Java libraries (no external dependencies)
- Implements custom JSON parsing to avoid external libraries
- Follows object-oriented design principles
- Includes proper error handling and user feedback
