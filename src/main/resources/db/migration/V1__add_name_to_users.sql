-- First add the name column as nullable if it doesn't exist
ALTER TABLE users ADD COLUMN IF NOT EXISTS name VARCHAR(255);

-- Update existing users to have a name based on their email
UPDATE users SET name = CONCAT('User - ', SPLIT_PART(email, '@', 1)) WHERE name IS NULL;

-- Now make the column NOT NULL
ALTER TABLE users ALTER COLUMN name SET NOT NULL;
