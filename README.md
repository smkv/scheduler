# Scheduler

A simple task scheduler based on database with java agents for executing tasks.


![Image of diagram]
(https://github.com/smkv/scheduler/raw/master/sql/tables_diagram.png)


The main logic, which task have to be executed, is concentrated inside stored procedures, the Java part is only for executing processes by instructions which came from the database.

The database interface are:
* ```get_next_task_to_execute(i_machine VARCHAR)``` - returns a cursor with task which will need to execute (task.id, task.type and task.command)
* ```create_execution(i_task_id INT, o_execution_id INT)``` - creates new execution process (record in executions table)
* ```append_executing_log(i_execution_id INT, i_log_record TEXT)``` - stores execution output for debugging/monitoring purpous
* ```execution_done(i_execution_id INT)``` - marks execution as finished successfully
* ```execution_failed(i_execution_id INT, i_message TEXT)``` - marks execution as failed

## Configuration

The main configuration file is **config.properties** [https://github.com/smkv/scheduler/blob/master/src/main/resources/config.properties]

The file contains '**machine.name**' property to filter tasks for specific installation. There are also two data source configuration: 

1. **scheduler** - schedule engine database which contains tasks description and execution logs
2. **target** - a database where will be executed task of type SQL
 

## Table Tasks

The "tasks" table contains task to be executed. Each task have type of execution and machine name where it must be executed. There are only two type supported at the moment - **SHELL** and **SQL**.

* **SHELL** means execute shell command locally as a terminal program
* **SQL** means execute a some SQL script on *target* database
 
Only active tasks will be executed - **tasks.active=1**

If task execution should depends from execution of another task then user **tasks.parent_id** field to set dependencies. This task will run only after successfully finished parent task.

If your task should be executed at specific time then use **tasks.hour** and **tasks.minute** fields.

**tasks.parent_id** and **tasks.hour** and **tasks.minute** can be used in the same time.

## Table Executions

Contains tasks execution information like execution start time, finish status and error flag. Based on this table the scheduler engin decide about next task execution.

## Table Execution_logs

Contains output of tasks execution. In case of SHELL - it is standard output stream, in case of SQL - warnings and SQL result sets.


## NB!

Current database design supports execute of each task only once per day.


