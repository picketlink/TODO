-- You can use this file to load seed data into the database using SQL statements 
-- Removed test data as it was in the way of testing initial app state

insert into Tag (id, name, style) values (0, 'tag1', 'tag-red1')
insert into Task (id, title, description, date) values (0, 'task1', 'task-description1', '2012-08-09')
insert into Task_Tag(tags_id, task_id) values (0, 0)
--insert into Project (id, version, name, style) values (0, 0, 'tag1', 'tag-red1')
