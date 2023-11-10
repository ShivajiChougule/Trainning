--Mid Project--
/*
Question 1:
Level: Simple
Topic: DISTINCT
Task: Create a list of all the different (distinct) replacement costs of the films.
Question: What's the lowest replacement cost?
Answer: 9.99
*/
select replacement_cost from film
group by replacement_cost
order by 1 asc;

select min(replacement_cost) from film;

/*
Question 2:
Level: Moderate
Topic: CASE + GROUP BY
Task: Write a query that gives an overview of how many films have replacements costs in the following cost ranges
low: 9.99 - 19.99
medium: 20.00 - 24.99
high: 25.00 - 29.99
Question: How many films have a replacement cost in the "low" group?
Answer: 514
*/

select count(*),
case
when replacement_cost between 9.99 and 19.99 then 'low'
when replacement_cost between 20.00 and 24.99 then 'medium'
when replacement_cost between 25.00 and 29.99 then 'high'
end as cost_range
from film
group by cost_range;

select count(*),
case
when replacement_cost between 9.99 and 19.99 then 'low'
end as cost_range
from film
where case when replacement_cost between 9.99 and 19.99 then 'low' end is not null
group by cost_range;

/*
Question 3:
Level: Moderate
Topic: JOIN
Task: Create a list of the film titles including their title, length, and category
name ordered descendingly by length. Filter the results to only the movies in the category 'Drama' or 'Sports'.
Question: In which category is the longest film and how long is it?
Answer: Sports and 184
*/

select length, ca.name from film f
inner join film_category fg
on f.film_id = fg.film_id
inner join category ca
on fg.category_id = ca.category_id and (ca.name = 'Drama' or ca.name = 'Sports')
order by length desc 
limit 1;

/*
Question 4:
Level: Moderate
Topic: JOIN & GROUP BY
Task: Create an overview of how many movies (titles) there are in each category (name).
Question: Which category (name) is the most common among the films?
Answer: Sports with 74 titles
*/

select ca.name , count(title) from film f
inner join film_category fg
on f.film_id = fg.film_id
inner join category ca
on fg.category_id = ca.category_id 
group by ca.name
order by 2 desc
limit 1;

/*
Question 5:
Level: Moderate
Topic: JOIN & GROUP BY
Task: Create an overview of the actors' first and last names and in how many movies they appear in.
Question: Which actor is part of most movies??
Answer: Susan Davis with 54 movies
*/

select first_name, last_name, count(*) from film f1
inner join film_actor fa
on f1.film_id=fa.film_id
inner join actor ac
on fa.actor_id = ac.actor_id
group by first_name,last_name
order by 3 desc
limit 1;

/*
Question 6:
Level: Moderate
Topic: LEFT JOIN & FILTERING
Task: Create an overview of the addresses that are not associated to any customer.
Question: How many addresses are that?
Answer: 4
*/
select count(*) from address a
left join customer c
on a.address_id = c.address_id
where c.address_id is null;

/*
Question 7:
Level: Moderate
Topic: JOIN & GROUP BY
Task: Create an overview of the cities and how much sales (sum of amount) have occurred there.
Question: Which city has the most sales?
Answer: Cape Coral with a total amount of 221.55
*/
--payment : customer id
--custmor : adress id , customer id
--adress : city id, addres id
--city : city id

select city,sum(amount) from payment p
inner join customer cu 
on p.customer_id = cu.customer_id
inner join address a
on cu.address_id = a.address_id
inner join city ci 
on a.city_id = ci.city_id
group by city
order by 2 desc
limit 1;

/*
Question 8:
Level: Moderate to difficult
Topic: JOIN & GROUP BY
Task: Create an overview of the revenue (sum of amount) grouped by a column in the format "country, city".
Question: Which country, city has the least sales?
Answer: United States, Tallahassee with a total amount of 50.85.
*/

select country, city, sum(amount) from payment p
inner join customer cu 
on p.customer_id = cu.customer_id
inner join address a
on cu.address_id = a.address_id
inner join city ci 
on a.city_id = ci.city_id
inner join country co 
on ci.country_id = co.country_id
group by country, city
order by 3
limit 1;

/*
Question 9:
Level: Difficult
Topic: Uncorrelated subquery
Task: Create a list with the average of the sales amount each staff_id has per customer.
Question: Which staff_id makes on average more revenue per customer?
Answer: staff_id 2 with an average revenue of 56.64 per customer.
*/

select staff_id, round(avg(total_amount),2) avg_sale from
(select  sum(amount) as total_amount, customer_id, staff_id from payment group by customer_id, staff_id) s
group by staff_id
order by 2 desc
limit 1;
 
/*
Question 10:
Level: Difficult to very difficult
Topic: EXTRACT + Uncorrelated subquery
Task: Create a query that shows average daily revenue of all Sundays.
Question: What is the daily average revenue of all Sundays?
Answer: 1410.65
*/

select round(avg(total),2)from (
select sum(amount) as total from payment where extract(dow from payment_date)=0
group by date(payment_date)) a;

/*
Question 11:
Level: Difficult to very difficult
Topic: Correlated subquery
Task: Create a list of movies - with their length and their replacement cost -
that are longer than the average length in each replacement cost group.
Question: Which two movies are the shortest on that list and how long are they?
Answer: CELEBRITY HORN and SEATTLE EXPECTATIONS with 110 minutes.
*/

select title, length, replacement_cost from film f
where length > (select avg(length) from film f2 where f.replacement_cost = f2.replacement_cost)
order by 2
limit 2;

/*
Question 12:
Level: Very difficult
Topic: Uncorrelated subquery
Task: Create a list that shows the "average customer lifetime value" grouped by the different districts.
Example:
If there are two customers in "District 1" where one customer has a total (lifetime) spent of $1000
and the second customer has a total spent of $2000 then the "average customer lifetime spent" in this
district is $1500.
So, first, you need to calculate the total per customer and then the average of these totals per district.
Question: Which district has the highest average customer lifetime value?
Answer: Saint-Denis with an average customer lifetime value of 216.54.
*/

select district, round(avg(total),2) as avg_cust_lifetime_spent from(
select first_name, last_name, district, sum(amount) as total from payment p
inner join customer c
on p.customer_id = c.customer_id
inner join address a
on c.address_id = a.address_id
group by district, first_name, last_name
) sub
group by district
order by 2 desc
limit 1;

/*
Question 13:
Level: Very difficult
Topic: Uncorrelated subquery

Task: Create a list that shows all payments including the payment_id, amount, 
and the film category (name) plus the total amount that was made in this category. 
Order the results ascendingly by the category (name) and as second order criterion
by the payment_id ascendingly.

Question: What is the total revenue of the category 'Action' and 
what is the lowest payment_id in that category 'Action'?
Answer: Total revenue in the category 'Action' is 4375.85 and the lowest payment_id in that category is 16055.
*/


SELECT
title,
amount,
name,
payment_id,
(SELECT SUM(amount) FROM payment p
LEFT JOIN rental r
ON r.rental_id=p.rental_id
LEFT JOIN inventory i
ON i.inventory_id=r.inventory_id
LEFT JOIN film f
ON f.film_id=i.film_id
LEFT JOIN film_category fc
ON fc.film_id=f.film_id
LEFT JOIN category c1
ON c1.category_id=fc.category_id
WHERE c1.name=c.name)
FROM payment p
LEFT JOIN rental r
ON r.rental_id=p.rental_id
LEFT JOIN inventory i
ON i.inventory_id=r.inventory_id
LEFT JOIN film f
ON f.film_id=i.film_id
LEFT JOIN film_category fc
ON fc.film_id=f.film_id
LEFT JOIN category c
ON c.category_id=fc.category_id
order by name;

/*
Bonus question 14:
Level: Extremely difficult
Topic: Correlated and uncorrelated subqueries (nested)
Task: Create a list with the top overall revenue of a film title (sum of amount per title)
for each category (name).
Question: Which is the top-performing film in the animation category?
Answer: DOGMA FAMILY with 178.70.
*/

SELECT
title,
name,
SUM(amount) as total
FROM payment p
LEFT JOIN rental r
ON r.rental_id=p.rental_id
LEFT JOIN inventory i
ON i.inventory_id=r.inventory_id
LEFT JOIN film f
ON f.film_id=i.film_id
LEFT JOIN film_category fc
ON fc.film_id=f.film_id
LEFT JOIN category c
ON c.category_id=fc.category_id
GROUP BY name,title
HAVING SUM(amount) = (SELECT MAX(total) FROM 
					  (SELECT title, name,
			          SUM(amount) as total
			          FROM payment p
			          LEFT JOIN rental r
			          ON r.rental_id=p.rental_id
			          LEFT JOIN inventory i
			          ON i.inventory_id=r.inventory_id
				  	  LEFT JOIN film f
				  	  ON f.film_id=i.film_id
				  	  LEFT JOIN film_category fc
				  	  ON fc.film_id=f.film_id
				  	  LEFT JOIN category c1
				  	  ON c1.category_id=fc.category_id
				  	  GROUP BY name,title) sub
			  		  WHERE c.name=sub.name)
					  
