ALTER TABLE orders
  ADD COLUMN customer_name varchar(255) NOT NULL;

ALTER TABLE orders
  ADD COLUMN mobile_number varchar NOT NULL;

ALTER TABLE orders
  ADD COLUMN order_date DATE NOT NULL DEFAULT CURRENT_DATE;