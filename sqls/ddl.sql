-- -----------------------------------------------------
-- Table user
-- -----------------------------------------------------
CREATE TABLE "user" (
  id SERIAL NOT NULL PRIMARY KEY ,
  password VARCHAR(256) NOT NULL,
  first_name VARCHAR(256) NULL,
  last_name VARCHAR(256) NULL,
  email VARCHAR(256) NOT NULL UNIQUE,
  customer_id VARCHAR(256) NOT NULL UNIQUE,
  is_terminated BOOLEAN NOT NULL DEFAULT FALSE,
  payment_token VARCHAR(256) NULL,
  last_login_on TIMESTAMP,
  created_on TIMESTAMP NOT NULL,
  last_modified_on TIMESTAMP NOT NULL);

-- -----------------------------------------------------
-- Table forgot_password
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS forgot_password (
  id SERIAL NOT NULL PRIMARY KEY,
  token VARCHAR(128) NOT NULL,
  expired_on TIMESTAMP NOT NULL,
  user_id BIGINT NOT NULL);

-- -----------------------------------------------------
-- Table user_subscription
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS user_subscription (
  id SERIAL NOT NULL PRIMARY KEY,
  user_id BIGINT NOT NULL REFERENCES "user",
  subscription_id VARCHAR(255) NOT NULL,
  subscription_type VARCHAR(255) NOT NULL);

CREATE INDEX t_u_fk_idx ON user_subscription USING btree(user_id);