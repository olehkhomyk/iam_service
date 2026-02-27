UPDATE users
SET first_name = 'Unknown',
    last_name  = 'Unknown'
WHERE first_name IS NULL
   OR last_name IS NULL;
