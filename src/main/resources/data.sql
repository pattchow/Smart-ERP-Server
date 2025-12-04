-- Initialize default organizations
INSERT INTO t_organizations (id, name, parent_id, created_time, updated_time) VALUES
('1', 'Headquarters', '-1', NOW(), NOW()),
('2', 'Technology Department', '1', NOW(), NOW()),
('3', 'Sales Department', '1', NOW(), NOW()),
('4', 'Marketing Department', '1', NOW(), NOW()),
('5', 'Human Resources Department', '1', NOW(), NOW());

-- Initialize default users
INSERT INTO t_users (id, username, full_name, email, org_id, created_time, updated_time) VALUES
('1', 'admin', 'System Administrator', 'admin@example.com', '1', NOW(), NOW()),
('2', 'john_doe', 'John Doe', 'john.doe@example.com', '2', NOW(), NOW()),
('3', 'jane_smith', 'Jane Smith', 'jane.smith@example.com', '3', NOW(), NOW()),
('4', 'marketing_user', 'Marketing User', 'marketing@example.com', '4', NOW(), NOW()),
('5', 'hr_user', 'HR User', 'hr@example.com', '5', NOW(), NOW());

-- Initialize sample objectives
INSERT INTO t_objectives (id, owner_id, status, confidence_level, visibility, weight, score, title, description, start_date, due_date, aligned_to, created_time, updated_time) VALUES
('1', '1', 'NOT_STARTED', 'HIGH', 'PUBLIC', '80', '', 'Improve customer satisfaction', 'Increase overall customer satisfaction rating to 4.5 stars', '2023-01-01 00:00:00', '2023-12-31 23:59:59', '', NOW(), NOW()),
('2', '2', 'IN_PROGRESS', 'MEDIUM', 'TEAM_ONLY', '20', '', 'Optimize platform performance', 'Reduce average page load time by 30%', '2023-01-01 00:00:00', '2023-06-30 23:59:59', '1', NOW(), NOW());

-- Initialize sample key results
INSERT INTO t_key_results (id, objective_id, owner_id, title, description, progress, status, confidence_level, start_date, due_date, created_time, updated_time) VALUES
('1', '1', '1', 'Customer survey score', 'Achieve a customer satisfaction score of 4.5 or higher in quarterly surveys', '0', 'NOT_STARTED', 'HIGH', '2023-01-01 00:00:00', '2023-12-31 23:59:59', NOW(), NOW()),
('2', '1', '1', 'Support ticket resolution', 'Reduce average support ticket resolution time to under 2 hours', '0', 'NOT_STARTED', 'HIGH', '2023-01-01 00:00:00', '2023-12-31 23:59:59', NOW(), NOW()),
('3', '2', '2', 'Page load time', 'Optimize page load time to under 2 seconds', '30', 'IN_PROGRESS', 'MEDIUM', '2023-01-01 00:00:00', '2023-06-30 23:59:59', NOW(), NOW()),
('4', '2', '2', 'Server response time', 'Reduce server response time to under 500ms', '0', 'NOT_STARTED', 'MEDIUM', '2023-01-01 00:00:00', '2023-06-30 23:59:59', NOW(), NOW());