-- SQL Server 2019+; rerunning preserves existing coursework data.
IF DB_ID(N'MVC_24162039') IS NULL CREATE DATABASE MVC_24162039;
GO
USE MVC_24162039;
GO
IF OBJECT_ID(N'dbo.User', N'U') IS NULL
CREATE TABLE dbo.[User] (
    id INT IDENTITY(1,1) PRIMARY KEY,
    email NVARCHAR(255) NOT NULL UNIQUE,
    username NVARCHAR(50) NOT NULL UNIQUE,
    fullname NVARCHAR(100) NOT NULL,
    password NVARCHAR(255) NOT NULL,
    avatar NVARCHAR(255) NULL,
    roleid INT NOT NULL DEFAULT 5 CHECK (roleid IN (1,2,5)),
    phone VARCHAR(15) NOT NULL UNIQUE,
    createddate DATE NOT NULL DEFAULT CAST(GETDATE() AS DATE)
);
GO
IF OBJECT_ID(N'dbo.Category', N'U') IS NULL
CREATE TABLE dbo.Category (
    cate_id INT IDENTITY(1,1) PRIMARY KEY,
    cate_name NVARCHAR(255) NOT NULL,
    icons NVARCHAR(255) NULL
);
GO
IF OBJECT_ID(N'dbo.RememberToken', N'U') IS NULL
CREATE TABLE dbo.RememberToken (
    token_hash CHAR(64) PRIMARY KEY,
    user_id INT NOT NULL REFERENCES dbo.[User](id) ON DELETE CASCADE,
    expires_at DATETIME2 NOT NULL
);
GO
-- Demo accounts are seeded separately by seed.sql.
