-- 1. 每次启动时，如果表已存在则删除，确保重新创建
-- 这对于 in-memory 的 H2 数据库非常有用，能保证每次启动都是干净的环境
DROP TABLE IF EXISTS organization;

-- 2. 创建 organization 表
CREATE TABLE organization (
    -- ID 作为主键，使用 VARCHAR 存储 String 类型
    id VARCHAR(255) PRIMARY KEY,
    -- name 字段
    name VARCHAR(255) NOT NULL,
    -- parent_id 字段，使用 VARCHAR 存储 String 类型
    parent_id VARCHAR(255),
    -- 创建时间，使用 TIMESTAMP WITH TIME ZONE 存储时间戳
    created_time TIMESTAMP WITH TIME ZONE NOT NULL,
    -- 更新时间
    updated_time TIMESTAMP WITH TIME ZONE NOT NULL
);

-- 3. 为 parent_id 字段创建索引，以优化查询性能
CREATE INDEX idx_organization_parent_id ON organization (parent_id);