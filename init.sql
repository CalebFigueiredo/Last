-- Table for Users/People (Guests, Employees, Administrators)
CREATE TABLE IF NOT EXISTS users (
    user_id SERIAL PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(20) UNIQUE NOT NULL,
    date_of_birth DATE NOT NULL,
    role VARCHAR(50) NOT NULL CHECK (role IN ('Guest', 'Employee', 'Administrator')),
    password_hash VARCHAR(255) NOT NULL
);

-- Table for Rooms
CREATE TABLE IF NOT EXISTS rooms (
    room_id SERIAL PRIMARY KEY,
    room_number VARCHAR(50) UNIQUE NOT NULL,
    room_type VARCHAR(100) NOT NULL,
    daily_price NUMERIC(10, 2) NOT NULL,
    maintenance_status VARCHAR(50) NOT NULL CHECK (maintenance_status IN ('Clean', 'Dirty', 'Under Maintenance')),
    is_occupied BOOLEAN NOT NULL DEFAULT FALSE
);

-- Table for Bookings
CREATE TABLE IF NOT EXISTS bookings (
    booking_id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    room_id INTEGER NOT NULL,
    check_in_date DATE NOT NULL,
    check_out_date DATE NOT NULL,
    booking_status VARCHAR(50) NOT NULL CHECK (booking_status IN ('Confirmed', 'Pending', 'Cancelled', 'Checked-in', 'Checked-out')),
    total_amount NUMERIC(10, 2) NOT NULL,
    payment_status VARCHAR(50) NOT NULL CHECK (payment_status IN ('Paid', 'Pending', 'Partially Paid', 'Refunded')),
    booking_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (user_id),
    FOREIGN KEY (room_id) REFERENCES rooms (room_id)
);