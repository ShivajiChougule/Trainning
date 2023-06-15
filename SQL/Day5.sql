--Creating Databas
create database test;

--Commantig
comment on database test is 'This is our testing database for DDL';

comment on table bookings is 'This is table of booking data';


--count 'behind the scenes' in special_features in film table 
select count(*) from film
where 'Behind the Scenes' = any(special_features);

--Creating table with Diff Constraints
create table online_sales(
	transaction_id serial primary key,
	customer_id int references customer(cusomer_id),
	film_id int references film(film_id),
	amouint numeric(5,2) not null,
	promotion_code varchar(10) default 'None'
);

select pg_get_serial_sequence('online_sales', 'transaction_id');

alter sequence public.online_sales_transaction_id_seq restart with 100 increment by 100;

insert into online_sales values(default,20,501,268.90,'BRO10');
insert into online_sales values(default,30,222,968.90,'BRO20');
insert into online_sales values(default,48,276,768.90,'COOL40');
insert into 
online_sales(customer_id,film_id,amouint,promotion_code) 
values
(90,345,660.90,'COOL30'),
(89,101,405.90,'COOL10');

select * from online_sales;

alter table online_sales rename column amouint to amount;

--Updating Rental rate
update film
set rental_rate = 1.99
where rental_rate = 0.99;

--add column initials to customer, update values to F.L. (firstname intial.lastname initial.)
alter table customer 
add column initials varchar(10);

update customer 
set initials = left(first_name,1)||'.'||left(last_name,1)||'.';

--Delete rows in payment table with payment_id 17064 and 17067
delete from payment
where payment_id in (17065,17066)
returning *;

--creating customer_spending table name(first name + last_name) , total_amount
create table customer_spending
as
select first_name||' '||last_name as name ,sum(amount) as total_amount
from customer c
inner join payment p
on c.customer_id = p.customer_id
group by first_name,last_name;

select * from customer_spending;

/*
Create a view called films_category that shows a list of the film titles
including their title, length and category name ordered descendingly by the length.
Filter the results to only the movies in the category 'Action' and 'Comedy'.
*/
create view films_category
as
select title, length, name as category_name from film f
inner join film_category fc
on f.film_id = fc.film_id
inner join category c
on fc.category_id = c.category_id and (name = 'Action' or name='Comedy')
order by 2 desc;

select * from films_category; 


--Avgrage length by category name
select f.film_id,title, length, name as category_name,
round(avg(length) over (partition by name),2) as average_length from film f
inner join film_category fc
on f.film_id = fc.film_id
inner join category c
on fc.category_id = c.category_id
order by 1;

--First Function with excuation
create function first_func(c1 int, c2 int)
	returns int
	language plpgsql
as
$$
declare
c3 int;
begin
select c1 + c2 + 3
into c3;
return c3;
end;
$$

select first_func(10,30) 


--Create fn that expects the customer first and last name and returns the total 
--amount of payments this customer made 
create function total_amount (fname varchar(20),lname varchar(20))
	returns decimal(6,2)
	language plpgsql
as 
$$
declare 
total decimal(6,2);
begin
select sum(amount) into total from payment p inner join customer c 
on p.customer_id = c.customer_id
where first_name = fname and last_name = lname;
return total;
end;
$$

select total_amount('CASSANDRA','WALTERS')


--Transition Example
CREATE TABLE acc_balance (
    id SERIAL PRIMARY KEY,
    first_name TEXT NOT NULL,
	last_name TEXT NOT NULL,
    amount DEC(9,2) NOT NULL    
);

INSERT INTO acc_balance
VALUES 
(1,'Tim','Brown',2500),
(2,'Sandra','Miller',1600)

SELECT * FROM acc_balance;

begin;
update acc_balance
set amount = amount - 100
where id = 1;
update acc_balance
set amount = amount + 100
where id = 2;
commit;




