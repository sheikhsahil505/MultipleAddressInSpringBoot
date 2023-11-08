CREATE TABLE user_information (
    user_id SERIAL PRIMARY KEY,
    first_name VARCHAR(10) NOT NULL,
    last_name VARCHAR(10) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    contact_number VARCHAR(10),
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,
    dob VARCHAR(255) NOT NULL
);