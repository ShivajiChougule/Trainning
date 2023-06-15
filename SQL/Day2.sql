--Q12.Write Query to find max,min,avg and sum of replacemnt cost of the films
select max(replacement_cost),min(replacement_cost),round(avg(replacement_cost),2) as average,sum(replacement_cost)
from film;

--Q13.Which of the two is responsible for a higher overall payment
--How do these amount changes if we dont consider amounts to 0
select staff_id,sum(amount) from payment
group by staff_id
order by sum(amount) desc
limit 2;
--We can use column number of output for ordering, still get same result as above
select staff_id,sum(amount) from payment
group by staff_id
order by 2 desc
limit 2;

select staff_id,sum(amount) from payment
where amount > 0
group by staff_id
order by sum(amount) desc
limit 2;

--Q14.Which employee had highest sales amount in a single day
--Which employee had most sales in a single day(not counting payments with anount=0)
select staff_id, sum(amount), date(payment_date) from payment
group by staff_id, date(payment_date)
order by 2 desc
limit 1;

select staff_id, sum(amount), date(payment_date), count(*) as total_transitions from payment
where amount != 0
group by staff_id, date(payment_date)
order by 4 desc
limit 1;

--Q15.Find out what is the average payment amount grouped by customer and day - cosider only days/customers with more than 1 payment(per customer and day)
-- Only April Days - 28,29,30
select customer_id, date(payment_date), round(avg(amount),2) as average, count(*) as total_payment from payment
where date(payment_date) between '2020-04-28' and '2020-05-01'
group by customer_id, date(payment_date)
having count(*)>1
order by 3 desc;

--String agg Function use. Syntax : string_agg(expression, delimiter)
select district, string_agg(postal_code,', ') from address
group by district

--Q16. Return first name, last name and email of customers whos first or last name is greater than 10 character
--also return first name, last name and email in lower case
select lower(first_name), lower(last_name), lower(email) from customer
where length(first_name) > 10 or length(last_name) > 10;

--Q17. Extract last 5 char and dot from email 
select right(email,5), left(right(email,4),1), email from customer;

--Q18. Create anonymized version of email with first char followed ny three '*' and @ onwards
select email, left(email,1) || '***' || right(email,19) as anonymized_version from customer;

--Q19. Extract First name from email address and concat with last name as ' Last name, first name'
select email, last_name,last_name || ', ' || left(email, position('.' in email)-1)   as last_first_name from customer;

--Q20. Anonymized email with '*'
-- only first letter of first name and last name shoudld be visible in email
-- only last letter of first name and last name shoudld be visible in email
select email,
left(email, 1) 
|| '***.' 
||left(substring(email from position('.' in email)+1 for position('@' in email)-1 - position('.' in email)),1) 
|| '***' 
||substring(email from position('@' in email))
from customer;
--simpler version of above from video
select email, 
left(email, 1)
|| '***' 
|| substring(email from position('.' in email) for 2)
|| '***'
||substring(email from position('@' in email))
from customer;

select email, '***' ||
right(left(email, position('.' in email)-1),1) || '.' ||
left(substring(email from position('.' in email)+1 for position('@' in email)-1 - position('.' in email)),1) || '***' ||
substring(email from position('@' in email))
from customer;
--simpler version of above from video
select email, 
'***' 
|| substring(email from position('.' in email)-1 for 3)
|| '***'
||substring(email from position('@' in email))
from customer;