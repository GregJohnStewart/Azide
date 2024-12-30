-- This file allow to write SQL commands that will be emitted in test and dev.
-- The commands are commented as their support depends of the database
-- insert into myentity (id, field) values(1, 'field-1');
-- insert into myentity (id, field) values(2, 'field-2');
-- insert into myentity (id, field) values(3, 'field-3');
-- alter sequence myentity_seq restart with 4;
insert into PriorityMessage (id, title, priority, date, content) values (1, 'Message 1', '1', '12/29/2024', 'Message 1 content');
insert into PriorityMessage (id, title, priority, date, content) values (2, 'Message 2', '2', '12/30/2024', 'Message 2 content');
ALTER SEQUENCE PriorityMessage_SEQ RESTART WITH 3;