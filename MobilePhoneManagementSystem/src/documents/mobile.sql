
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
create table Users(
	ID int identity(1,1) not null,
	UserID varchar(5) primary key not null,	
	UserPassword varchar(30) not null,
	RoleID int CONSTRAINT fk_Users_Role FOREIGN KEY (RoleID) REFERENCES Roles(RoleID) not null,
	UserEnabled bit not null
)

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

create table Permission(
	RoleID int CONSTRAINT fk_Permission_Roles FOREIGN KEY (RoleID) REFERENCES Roles(RoleID) not null,
	FunctionID int CONSTRAINT fk_Permission_Functions FOREIGN KEY (FunctionID) REFERENCES Functions(FunctionID) not null,
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
	InID int CONSTRAINT fk_InDetail_Inbound FOREIGN KEY (InID) REFERENCES Inbound(InID) not null ,
	ProID int CONSTRAINT fk_InDetail_Product FOREIGN KEY (ProID) REFERENCES Product(ProID) not null,
	ProQty int not null
)

create table Outbound(
	OutID int identity(1,1) primary key not null,
	OutDate datetime not null,
	TargetID int not null,
	UserID varchar(5) CONSTRAINT fk_Outbound_Users FOREIGN KEY (UserID) REFERENCES Users(UserID) not null,
	OutContent varchar(50) not null
)

create table OutDetails(
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
	OrdID int CONSTRAINT fk_OrderDetails_Order FOREIGN KEY (OrdID) REFERENCES Orders(OrdID) not null,
	ProID int CONSTRAINT fk_OrderDetails_Product FOREIGN KEY (ProID) REFERENCES Product(ProID) not null,
	ProQty int not null,
	ProPrice int not null
)

create table Status(
	SttID int identity(1,1) primary key not null,
	SttName varchar(50) not null,
	SttType nvarchar(50) not null
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
