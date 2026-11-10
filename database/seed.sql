USE MVC_24162039;
GO
IF NOT EXISTS(SELECT 1 FROM dbo.[User] WHERE username=N'admin')
INSERT INTO dbo.[User](username,fullname,email,password,roleid,phone) VALUES(N'admin',N'Quản trị viên',N'admin@example.com',N'pbkdf2$120000$MjQxNjIwMzktZGVtby12MQ==$oBGo4z+RJxFI/WMLd+fRep+OqJBlMVS4/O5PSejeag8=',1,'0900000001');
GO
IF NOT EXISTS(SELECT 1 FROM dbo.[User] WHERE username=N'manager')
INSERT INTO dbo.[User](username,fullname,email,password,roleid,phone) VALUES(N'manager',N'Quản lý',N'manager@example.com',N'pbkdf2$120000$MjQxNjIwMzktZGVtby12MQ==$oBGo4z+RJxFI/WMLd+fRep+OqJBlMVS4/O5PSejeag8=',2,'0900000002');
GO
IF NOT EXISTS(SELECT 1 FROM dbo.[User] WHERE username=N'user')
INSERT INTO dbo.[User](username,fullname,email,password,roleid,phone) VALUES(N'user',N'Thành viên',N'user@example.com',N'pbkdf2$120000$MjQxNjIwMzktZGVtby12MQ==$oBGo4z+RJxFI/WMLd+fRep+OqJBlMVS4/O5PSejeag8=',5,'0900000003');
GO
IF NOT EXISTS(SELECT 1 FROM dbo.Category WHERE cate_name=N'Sách')
INSERT INTO dbo.Category(cate_name) VALUES(N'Sách');
GO
IF NOT EXISTS(SELECT 1 FROM dbo.Category WHERE cate_name=N'Điện thoại')
INSERT INTO dbo.Category(cate_name) VALUES(N'Điện thoại');
GO
IF NOT EXISTS(SELECT 1 FROM dbo.Category WHERE cate_name=N'Máy tính')
INSERT INTO dbo.Category(cate_name) VALUES(N'Máy tính');
GO
