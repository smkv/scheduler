# Scheduler

A simple task scheduler based on database with java agents for executing tasks.


![Image of diagram]
(https://github.com/smkv/scheduler/raw/master/sql/tables_diagram.png)

All operations with table data does thru stored procedures


* ```get_next_task_to_execute(i_machine VARCHAR)``` - returns a cursor with task which will need to execute (task.id, task.type and task.command)
* ```create_execution(i_task_id INT, o_execution_id INT)``` - creates new execution process (record in executions table)
* ```append_executing_log(i_execution_id INT, i_log_record TEXT)``` - stores execution output for debugging/monitoring purpous
* ```execution_done(i_execution_id INT)``` - marks execution as finished successfully
* ```execution_failed(i_execution_id INT, i_message TEXT)``` - marks execution as failed

