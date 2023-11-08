 CREATE TABLE user_addresses (
     address_id SERIAL PRIMARY KEY,
     street VARCHAR(255),
     apartment VARCHAR(255),
     city VARCHAR(255),
     pincode VARCHAR(255),
     state VARCHAR(255),
     country VARCHAR(255),
     user_id BIGINT REFERENCES user_information(user_id)
 );