
--Tao database Mobile
use master
go
if exists(select * from sysdatabases where name='Mobile')
drop database Mobile
go
begin try
	create database Mobile
		on primary (name = 'ems', filename = 'D:\Project 2\MobileManageSytem\moblie.mdf', size = 10MB, maxsize = unlimited, filegrowth = 100MB)
		log on (name = 'ems_log', filename = 'D:\Project 2\MobileManageSytem\moblie_log.ldf', size = 2MB, maxsize = 100MB, filegrowth = 10%)
end try
begin catch
	create database Mobile
end catch
go

--Tao lan luot cac table theo yeu cau do an
use Mobile
go
-- create table

create table Roles(
	RoleID int identity(1,1) primary key,
	RoleName varchar(50) not null,
	RoleEnabled bit not null default(1)
)

create table Functions(
	FunctionID int  identity(1,1) primary key not null,
	FunctionGroup varchar(50) not null,
	FunctionName varchar(50) not null
)

create table Customer(
	CusID int identity(1,1) primary key not null,
	CusName nvarchar(50) not null,	
	CusAddress nvarchar(300) not null,
	CusPhone varchar(20) not null,
	CusEnabled bit default(1)
)

create table Supplier(
	SupID int identity(1,1) primary key not null,
	SupName nvarchar(50) UNIQUE not null,
	SupAddress nvarchar(300) not null,	
	SupEnabled bit default(1)
)

create table Branch(
	BraID int identity(1,1) primary key not null,	
	BraName varchar(50) UNIQUE not null,
	BraEnabled bit default(1) not null
)

create table Status(
	SttID int identity(1,1) primary key not null,
	SttName varchar(50) not null,
	SttType nvarchar(50) not null
)
create table Users(
	ID int identity(1,1) not null,
	UserID varchar(5) primary key not null,	
	UserPassword varchar(30) not null,
	RoleID int CONSTRAINT fk_Users_Role FOREIGN KEY (RoleID) REFERENCES Roles(RoleID) not null,
	UserEnabled bit default(1) not null
)
create table Employee(
	ID int identity(1,1) not null,
	EmpID  AS 'EMP' + RIGHT('000' + CAST(ID AS VARCHAR(6)), 3) PERSISTED primary key not null,
	UserID varchar(5) CONSTRAINT fk_Employee_Users FOREIGN KEY (UserID) REFERENCES Users(UserID) not null,
	EmpName varchar(50) not null,
	EmpPhone varchar(20) not null,
	Birthday date not null,
	BasicSalary float not null,
	Designation nvarchar(30) not null,	
	WorkingStartDate date not null,
	EmpEnabled bit default(1) not null
)
create table EmpSalary(
	EmpSalaryID int identity(1,1) primary key not null,
	EmpID VARCHAR(6) CONSTRAINT fk_EmpSalary_Employee FOREIGN KEY (EmpID) REFERENCES Employee(EmpID) not null,
	TotalSalary float not null,
	MonthNo int not null,
	WorkDay int not null,
	Bonus float not null
)


create table Permission(
	RoleID int CONSTRAINT fk_Permission_Roles FOREIGN KEY (RoleID) REFERENCES Roles(RoleID) not null,
	FunctionID int CONSTRAINT fk_Permission_Functions FOREIGN KEY (FunctionID) REFERENCES Functions(FunctionID) not null,
)

create table Product(
	ProID int identity(1,1) primary key not null,
	BraID int CONSTRAINT fk_Product_Branch FOREIGN KEY (BraID) REFERENCES Branch(BraID) not null,
	ProName varchar(70) not null,
	ProStock int not null,
	ProPrice float not null,
	ProDescr nvarchar(100) not null,
	ProImage varchar(200) NULL,
	ProEnabled bit not null
)

create table Inbound(
	InID int identity(1,1) primary key not null,
	InDate datetime not null,
	SupID int CONSTRAINT fk_Inbound_Supplier FOREIGN KEY (SupID) REFERENCES Supplier(SupID) not null,
	SupInvoiceID varchar(10),
	UserID varchar(5) CONSTRAINT fk_Inbound_Users FOREIGN KEY (UserID) REFERENCES Users(UserID) not null
)

create table InDetails(
	InDetailsID int identity(1,1) primary key not null,
	InID int CONSTRAINT fk_InDetail_Inbound FOREIGN KEY (InID) REFERENCES Inbound(InID) not null ,
	ProID int CONSTRAINT fk_InDetail_Product FOREIGN KEY (ProID) REFERENCES Product(ProID) not null,
	ProQty int not null
)
create table Salesoff(
	SalesOffID int identity(1,1) primary key not null,
	SalesOffName nvarchar(100) not null,
	ProID int CONSTRAINT fk_Salesoff_Product FOREIGN KEY (ProID) REFERENCES Product(ProID) not null,
	SalesOffStartDate datetime not null,
	SalesOffEndDate datetime not null,
	SalesOffAmount float not null,
	SalesoffEnable bit default(0)
)
create table Outbound(
	OutID int identity(1,1) primary key not null,
	OutDate datetime not null,
	TargetID int not null,--la OrdID hoac ServiceID hoac SupInvoiceID
	UserID varchar(5) CONSTRAINT fk_Outbound_Users FOREIGN KEY (UserID) REFERENCES Users(UserID) not null,
	OutContent varchar(50) not null
)

create table OutDetails(
	OutDetailsID int identity(1,1) primary key not null,
	OutID int CONSTRAINT fk_OutDetails_Outbound FOREIGN KEY (OutID) REFERENCES Outbound(OutID) not null,
	ProID int CONSTRAINT fk_OutDetails_Product FOREIGN KEY (ProID) REFERENCES Product(ProID) not null,
	ProQty int not null
)

create table Orders(
	OrdID int identity(1,1) primary key not null,
	CusID int CONSTRAINT fk_Orders_Customer FOREIGN KEY (CusID) REFERENCES Customer(CusID)
 not null,
	UserID varchar(5) CONSTRAINT fk_Orders_Users FOREIGN KEY (UserID) REFERENCES Users(UserID) not null,
	OrdDate date not null,
	OrdCusDiscount float,	
	SttID int CONSTRAINT fk_Orders_Status FOREIGN KEY (SttID) REFERENCES Status(SttID) not null
)

create table OrderDetails(
	OrdDetailsID int identity(1,1) primary key not null,
	OrdID int CONSTRAINT fk_OrderDetails_Order FOREIGN KEY (OrdID) REFERENCES Orders(OrdID) not null,
	ProID int CONSTRAINT fk_OrderDetails_Product FOREIGN KEY (ProID) REFERENCES Product(ProID) not null,
	ProQty int not null,
	ProPrice int not null
)

create table Service(
	ServiceID int identity(1,1) primary key not null,
	UserID varchar(5) CONSTRAINT fk_Service_Users FOREIGN KEY (UserID) REFERENCES Users(UserID) not null,
	CusID int CONSTRAINT fk_Service_Customer FOREIGN KEY (CusID) REFERENCES Customer(CusID) not null,
	ReceiveDate date not null,
	ReturnDate date not null,
	SttID int  CONSTRAINT fk_Service_Status FOREIGN KEY (SttID) REFERENCES Status(SttID) not null
)
create table ServiceDetails(	
	SerDetailsID int identity(1,1) primary key not null,
	ServiceID int CONSTRAINT fk_ServiceDetails_Service FOREIGN KEY (ServiceID) REFERENCES Service(ServiceID) not null,
	ProID int CONSTRAINT fk_ServiceDetails_Product FOREIGN KEY (ProID) REFERENCES Product(ProID) not null,
	ProQty int not null,
	OrdID int CONSTRAINT fk_ServiceDetails_Order FOREIGN KEY (OrdID) REFERENCES Orders(OrdID) not null
)
--ROLE
INSERT INTO [dbo].[Roles] VALUES (N'Admin',1)
INSERT INTO [dbo].[Roles] VALUES (N'Sales',1)
INSERT INTO [dbo].[Roles] VALUES (N'Manager',1)
INSERT INTO [dbo].[Roles] VALUES (N'Accountant',1)
INSERT INTO [dbo].[Roles] VALUES (N'Technicians',1)
INSERT INTO [dbo].[Roles] VALUES (N'Inventory',1)
--FUNCTIONS
INSERT INTO [dbo].[Functions] VALUES('User', N'Insert')
INSERT INTO [dbo].[Functions] VALUES('User', N'Update')
INSERT INTO [dbo].[Functions] VALUES('User', N'Delete')
INSERT INTO [dbo].[Functions] VALUES('Permission', N'Insert')
INSERT INTO [dbo].[Functions] VALUES('Permission', N'Update')
INSERT INTO [dbo].[Functions] VALUES('Product', N'Insert')
INSERT INTO [dbo].[Functions] VALUES('Product', N'Update')
INSERT INTO [dbo].[Functions] VALUES('Product', N'Delete')
INSERT INTO [dbo].[Functions] VALUES('Inbound', N'Insert')
INSERT INTO [dbo].[Functions] VALUES('Inbound', N'Update')
INSERT INTO [dbo].[Functions] VALUES('Inbound', N'Delete')
INSERT INTO [dbo].[Functions] VALUES('Outbound', N'Insert')
INSERT INTO [dbo].[Functions] VALUES('Outbound', N'Update')
INSERT INTO [dbo].[Functions] VALUES('Outbound', N'Delete')
INSERT INTO [dbo].[Functions] VALUES('Order', N'Insert')
INSERT INTO [dbo].[Functions] VALUES('Order', N'Update')
INSERT INTO [dbo].[Functions] VALUES('Order', N'Delete')
INSERT INTO [dbo].[Functions] VALUES('Customer', N'Insert')
INSERT INTO [dbo].[Functions] VALUES('Customer', N'Update')
INSERT INTO [dbo].[Functions] VALUES('Customer', N'Delete')
INSERT INTO [dbo].[Functions] VALUES('Service', N'Insert')
INSERT INTO [dbo].[Functions] VALUES('Service', N'Update')
INSERT INTO [dbo].[Functions] VALUES('Service', N'Delete')
--PERMISSION
--admin
INSERT INTO [dbo].[Permission] VALUES(1, N'1')
INSERT INTO [dbo].[Permission] VALUES(1, N'2')
INSERT INTO [dbo].[Permission] VALUES(1, N'3')
INSERT INTO [dbo].[Permission] VALUES(1, N'4')
INSERT INTO [dbo].[Permission] VALUES(1, N'5')
--sales
INSERT INTO [dbo].[Permission] VALUES(2, N'15')
INSERT INTO [dbo].[Permission] VALUES(2, N'16')
INSERT INTO [dbo].[Permission] VALUES(2, N'17')
INSERT INTO [dbo].[Permission] VALUES(2, N'18')
INSERT INTO [dbo].[Permission] VALUES(2, N'19')
--manager
INSERT INTO [dbo].[Permission] VALUES(3, N'15')
select * from Roles
select * from Functions
select * from Employee,EmpSalary