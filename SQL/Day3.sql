--Q21. What is the month with the highest total payment amount
--What is the day of week with the highest total payment amount (0 is sunday)
--what is the highest amount one customer has spent in a week?
select sum(amount), extract(month from payment_date) from payment
group by extract(month from payment_date)
order by 1 desc;

select sum(amount), extract(dow from payment_date) from payment
group by extract(dow from payment_date)
order by 1 desc;

select customer_id, sum(amount), extract(week from payment_date) from payment
group by customer_id, extract(week from payment_date)
order by 2 desc;

--Q22.Sum payments and group with following format
--Dy,DD/MM/2020
--Month,YYYY
--Dy,HH:MI
select sum(amount), to_char(payment_date, 'dy, dd/mm/yyyy') from payment
group by to_char(payment_date, 'dy, dd/mm/yyyy')
order by 1 desc;

select sum(amount), to_char(payment_date, 'mon, yyyy') from payment
group by to_char(payment_date, 'mon, yyyy')
order by 1 desc;

select sum(amount), to_char(payment_date, 'dy, HH24:MI') from payment
group by to_char(payment_date, 'dy, HH24:MI')
order by 1 desc;


--Q23. All reantal duration with customer_id 35
-- Which customer has the longest avg rental duration
select customer_id, return_date-rental_date from rental
where customer_id =35;

select customer_id, avg(return_date-rental_date) from rental
group by customer_id
order by 2 desc;

--Q24. list film, rental rate, replacement cost and rental rate is less tahn 4%
--Film_ids togther with the percentage rounded to 2 decimal places
select film_id, rental_rate, replacement_cost, round((rental_rate/replacement_cost)*100,2) as percentage from film
where round((rental_rate/replacement_cost)*100,2) < 4
order by 4;

--Q25. How many high price tickets sold 
--low : total_amount < 20000
--mid : total_amount between  20000 and 150000
--high : total_amount >=150000
select count(*),
case
when total_amount < 20000 then 'Low Price'
when total_amount between  20000 and 150000 then 'Mid Price'
when total_amount >=150000 then 'High Price'
else 'No Tickets Sold' 
end as ticket_price
from bookings
group by ticket_price
order by 1 desc;

--Flights departed in seasons
--Winter : Dec, Jan, Feb
--Spring : Mar, April, May
--Summer : Jun, July, August
--Fall : Sept, Oct, Nov
select count(*),
case 
when extract(month from scheduled_departure) in (12,1,2) then 'Winter'
when extract(month from scheduled_departure) in (3,4,5) then 'Spring'
when extract(month from scheduled_departure) in (6,7,8) then 'Summer'
when extract(month from scheduled_departure) in (9,10,11) then 'Fall'
else 'No Data vailabe'
end as season
from flights
group by season;

--Create tier list of movies
--1. Rating is 'PG' or 'PG-13' or length is more than 210 min:
--2.Description contains 'Drama' and length more than 90 min
--3.Description contains 'Drama' and length not more than 90 min
--4.Rental_rate less than $1: 'Very Chip'
select title, 
case
when rating in ('PG','PG-13') or (length > 210 ) then 'Great rating or long (tier 1)'
when description like '%Drama%' and (length > 90) then 'Long drama (tier 2)'
when description like '%Drama%' and (length <= 90) then 'Short drama (tier 3)'
when rental_rate < 1 then 'Very Chip (tier 4)'
end as tier
from film

--Filter only those movies appear in one of the 4 tier 
select title, 
case
when rating in ('PG','PG-13') or (length > 210 ) then 'Great rating or long (tier 1)'
when description like '%Drama%' and (length > 90) then 'Long drama (tier 2)'
when description like '%Drama%' and (length <= 90) then 'Short drama (tier 3)'
when rental_rate < 1 then 'Very Chip (tier 4)'
end as tier
from film
where
case
when rating in ('PG','PG-13') or (length > 210 ) then 'Great rating or long (tier 1)'
when description like '%Drama%' and (length > 90) then 'Long drama (tier 2)'
when description like '%Drama%' and (length <= 90) then 'Short drama (tier 3)'
when rental_rate < 1 then 'Very Chip (tier 4)'
end is not null;

--Q26 pivote rating
select 
sum(case when rating = 'G' then 1 else 0 end) as "G",
sum(case when rating = 'R' then 1 else 0 end) as "R",
sum(case when rating = 'PG' then 1 else 0 end) as "PG",
sum(case when rating = 'PG-13' then 1 else 0 end) as "PG-13",
sum(case when rating = 'NC-17' then 1 else 0 end) as "NC-17"
from film;

--Q27. coalsece return_date with not return
select rental_date, coalesce(cast(return_date as varchar),'not returned')
from rental
order by rental_date desc;

--Q28. Replace PG from filght_no col and change its data type to int
select flight_no, cast(replace(flight_no,'PG','') as int) as filght_no from flights;

--Q29. How many people choose seats in the category : Business, Economy, Comfort

select count(b.seat_no), fare_conditions from boarding_passes b 
inner join seats s 
on  b.seat_no = s.seat_no
group by fare_conditions;


--Q30. find the tickets that dont have a boarding pass related to it
select t.ticket_no from boarding_passes b
full outer join tickets t
on b.ticket_no = t.ticket_no
where b.ticket_no is null ;

