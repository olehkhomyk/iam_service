ALTER TABLE users
    ALTER COLUMN first_name SET NOT NULL,
    ALTER COLUMN first_name SET DEFAULT 'Unknown',
    ALTER COLUMN last_name SET NOT NULL,
    ALTER COLUMN last_name SET DEFAULT 'Unknown';