-- Q1. Write query to get list of Customers with thair First name, Last Name and Email
select first_name, last_name, email from customer;

-- Q2. Write query to get list of Customers with Last name desceinding order
-- if last name is same then first name should come in ascending 
select first_name,last_name, email from customer
order by last_name desc, first_name desc;
--OR
select first_name,last_name, email from customer
order by 2 desc, 1 desc;

--Q3. Write Query to get different prices(col : amount) from Payment 
select distinct amount from payment
order by amount desc;

--Q4. Write Query to get last five transections from Payment 
select * from (
	select * from payment
	order by payment_id desc
	limit 5) a
order by payment_id asc;

-- SELECT LAST CHALLENGES
-- Create list of all the distinct districts customers are from
select distinct district from address;

-- What is the latest rental date
select rental_date from rental order by rental_date desc limit 1;

-- How many films does comapny have
select count(distinct title) from film;

-- How many distinct last name of the customer are there
select count(distinct last_name) from customer;

--Q5. How many payment made by customer wwith customer_id = 100
-- What is the last name of our customer with first name 'ERICA'
select count(*) as Number_Of_Payments from payment 
where customer_id = 100;

select first_name, last_name from customer 
where first_name = 'ERICA';

--Q6. Write Query to retrive how many rental have not been returned yet(return_date null)
select count(*) from rental
where return_date is null;

select payment_id, amount from payment
where amount <= 2;

--Q7. Retrive list of all payments of the cutomer 322,346 & 354 where amount is either less than $2 or greater than $10
--Condition -  customer asc and amount desc 
select * from payment 
where (customer_id = 322 or customer_id = 346 or customer_id = 354) and (amount < 2 or amount > 10)
order by customer_id asc, amount desc;

--Q8. Retrive how many payments have been made on jan 26th and 27th 2020 with an amount between 1.99 and 3.99
select count(*) from payment 
where (payment_date between '2020-01-26' and '2020-01-27 23:59')
and (amount between 1.99 and 3.99);

--Q9. There have been 6 complaints of customers
--customer_id : 12,25,67,93,124,234
--amounts : 4.99,7.99,9.99
--Month : january 2020
select * from payment 
where (customer_id in (12,25,67,93,124,234))
and (amount in (4.99,7.99,9.99))
and (payment_date between '2020-01-01' and '2020-01-31 23:59');

--Q10. How many movies are there that contains the "Documentary" in the description
--How many customers are ther with first name is 3 letters long and either an 'X' or a 'Y' as the last letter of last name?
select count(*) from film 
where description like '%Documentary%';

select count(*) from customer
where first_name like '___' 
and (last_name like '%X' or last_name like '%Y');

--WHERE LAST CHALLENGES
--How many movies that contains 'Saga' in descrp. and title starts either with 'A' or ends with 'R'. use the alias 'no_of_movies'.
select count(*) as no_of_movies from film
where (description like '%Saga%')
and (title like 'A%' or title like '%R');

 --create list of all cust. where first_name conatins 'ER' and has an 'A' as the second letter. Order by last_name desc 
 select first_name, last_name from customer
 where first_name like '%ER%' and first_name like '_A%'
 order by last_name desc;
 
 --How many payments are there where the amount is either 0 or is between 3.99 and 7.99 and in the same time has happened on 2020-05-01
 select count(*) from payment 
 where ((amount = 0) or (amount between 3.99 and 7.99))
 and payment_date between '2020-05-01' and '2020-05-02';
 
 