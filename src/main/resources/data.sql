-- Initialize default organizations
INSERT INTO organization (id, name, parent_id, created_time, updated_time) VALUES
('1', 'Headquarters', '-1', NOW(), NOW()),
('2', 'Technology Department', '1', NOW(), NOW()),
('3', 'Sales Department', '1', NOW(), NOW()),
('4', 'Marketing Department', '1', NOW(), NOW()),
('5', 'Human Resources Department', '1', NOW(), NOW());

-- Initialize default users
INSERT INTO users (id, username, full_name, email, org_id, created_time, updated_time) VALUES
('1', 'admin', 'System Administrator', 'admin@example.com', '1', NOW(), NOW()),
('2', 'john_doe', 'John Doe', 'john.doe@example.com', '2', NOW(), NOW()),
('3', 'jane_smith', 'Jane Smith', 'jane.smith@example.com', '3', NOW(), NOW()),
('4', 'marketing_user', 'Marketing User', 'marketing@example.com', '4', NOW(), NOW()),
('5', 'hr_user', 'HR User', 'hr@example.com', '5', NOW(), NOW());