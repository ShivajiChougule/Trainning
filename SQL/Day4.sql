--Q31. Most Frequently chosen seat
-- seat which are never booked
--Which line (A,B,..) has been chosen most frequently
select s.seat_no, count(*) from seats s
left join boarding_passes b
on s.seat_no = b.seat_no
group by s.seat_no
order by 2 desc;

select * from seats s
left join boarding_passes b
on s.seat_no = b.seat_no
where b.ticket_no is null;

select right(s.seat_no,1), count(*) from seats s
left join boarding_passes b
on s.seat_no = b.seat_no
group by right(s.seat_no,1)
order by 2 desc;

--Q32.Find out first_name, last_name and phone no. of customer who are from Texas
--find any addresses that are not related to any customer
select c.first_name, c.last_name, a.phone from customer c
left join address a
on c.address_id = a.address_id 
where a.district = 'Texas';

select * from address a
left join customer c
on a.address_id = c.address_id 
where c.address_id  is null;

--Q33. find out all customer from brazil
select first_name, last_name, email, co.country from customer c
inner join address a
on c.address_id = a.address_id
inner join city ci
on a.city_id = ci.city_id
inner join country co
on ci.country_id = co.country_id
where co.country ='Brazil';


--Q34. Which passenger (passenger_name) has spent most amount in their bookings (total_amount)?
select t.passenger_name, sum(b.total_amount) from tickets t
inner join bookings b 
on t.book_ref = b.book_ref
group by t.passenger_name
order by 2 desc;

 -- Which fare_condition has ALEKSANDR IVANOV used the most? 
 select tf.fare_conditions, count(*) from tickets t
inner join ticket_flights  tf 
on t.ticket_no = tf.ticket_no and t.passenger_name = 'ALEKSANDR IVANOV'
group by tf.fare_conditions
order by 2 desc
limit 1;

--Which title has GEORGE LINTON rented the most often?
select first_name, last_name, title, count(*) from customer cu
inner join rental re
on cu.customer_id = re.customer_id
inner join inventory inv
on inv.inventory_id=re.inventory_id
inner join film fi
on fi.film_id = inv.film_id
where first_name='GEORGE' and last_name='LINTON'
group by title, first_name, last_name
order by 4 desc;

--Q35. Select all the films where the length is longer than avg of all films
select * from film 
where length > (select avg(length) from film);

--Find out all the films that are available in the inventory in store 2 more than 3 times

select f.film_id, title from film f
inner join inventory i
on f.film_id = i.film_id and i.store_id = 2
group by f.film_id
having count(*) > 3
order by 1;


-- Q36. find coustomer first and last name who made payment on '2020-01-25'
select first_name, last_name from customer 
where customer_id in (select customer_id from payment 
where payment_date between '2020-01-25' and '2020-01-26'
group by customer_id);

--
select first_name, email from customer 
where customer_id in (select customer_id from payment 
group by customer_id having sum(amount) > 30);

--Return all customers first and last name that are from California and have spent more than 100 in total
select first_name, last_name from customer 
where (customer_id in (select customer_id from payment 
group by customer_id having sum(amount) > 100))
and(customer_id in (select customer_id from customer c 
					inner join address a on c.address_id = a.address_id
					where a.district = 'California' ));

--Q37.What is the avg total amount spent per day (avg daily revenue)
select round(avg(total_amount),2) as avg_daily_revenue from (
	select sum(amount) as total_amount from payment group by date(payment_date)) sub;
	
--Q38. Show all payments together with how much payment amount is below the maximum payment amount
select *,((select max(amount) from payment)- amount) as diffrence from payment;
	
	
--Q39.Show movie title, film id and replacement cost with lowest replacement cost for each rating category. show also their ratings
select title, film_id, replacement_cost, rating from film f1
where replacement_cost = (select min(replacement_cost) from film f2
						 where f1.rating = f2.rating);
--Show movie title, film id and length that have highest length in each rating category - show rating also
select title, film_id, length, rating from film f1
where length = (select max(length) from film f2
						 where f1.rating = f2.rating);

--Q40.show all the payments plus the total amount for every customer as well as number of payments of each customer
select *,
(select sum(amount) from payment p2 where p1.customer_id=p2.customer_id) as total_amount,
(select count(*) from payment p3 where p1.customer_id=p3.customer_id) as no_of_payment
from payment p1
order by 2;

--select title with highest replacment cost in their rating category plus show the avg replacement cost in their rating category
select title,
replacement_cost,
rating,
(select  avg(replacement_cost) from film f2 where f1.rating=f2.rating)
from film f1
where replacement_cost =(select max(replacement_cost) from film f3 where f1.rating=f3.rating)






















	
					
					