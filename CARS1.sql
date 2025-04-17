CREATE DATABASE CrimeAnalysisDB;
USE CrimeAnalysisDB;

-- Incidents Table
CREATE TABLE Incidents (
    IncidentID INT PRIMARY KEY AUTO_INCREMENT,
    IncidentType VARCHAR(50) NOT NULL,
    IncidentDate DATETIME NOT NULL,
    Latitude DECIMAL(9,6),
    Longitude DECIMAL(9,6),
    Description TEXT,
    Status ENUM('Open', 'Closed', 'Under_Investigation') DEFAULT 'Open',
    VictimID INT,
    SuspectID INT,
    OfficerId INT,
    FOREIGN KEY (VictimID) REFERENCES Victims(VictimID) ON DELETE SET NULL,
    FOREIGN KEY (SuspectID) REFERENCES Suspects(SuspectID) ON DELETE SET NULL,
	FOREIGN KEY (Officerid) REFERENCES officers(officerid) ON DELETE SET NULL
    
);

-- Victims Table
CREATE TABLE Victims (
    VictimID INT PRIMARY KEY AUTO_INCREMENT,
    FirstName VARCHAR(50) NOT NULL,
    LastName VARCHAR(50) NOT NULL,
    DateOfBirth DATE,
    Gender ENUM('Male', 'Female', 'Other') NOT NULL,
    Address TEXT,
    PhoneNumber VARCHAR(20)
);

-- Suspects Table
CREATE TABLE Suspects (
    SuspectID INT PRIMARY KEY AUTO_INCREMENT,
    FirstName VARCHAR(50) NOT NULL,
    LastName VARCHAR(50) NOT NULL,
    DateOfBirth DATE,
    Gender ENUM('Male', 'Female', 'Other') NOT NULL,
    Address TEXT,
    PhoneNumber VARCHAR(20)
);

-- Law Enforcement Agencies Table
CREATE TABLE Agency (
    AgencyID INT PRIMARY KEY AUTO_INCREMENT,
    AgencyName VARCHAR(100) NOT NULL,
    Jurisdiction TEXT,
    ContactInfo VARCHAR(100)
);

-- Officers Table
CREATE TABLE Officers (
    OfficerID INT PRIMARY KEY AUTO_INCREMENT,
    FirstName VARCHAR(50) NOT NULL,
    LastName VARCHAR(50) NOT NULL,
    BadgeNumber VARCHAR(20) UNIQUE NOT NULL,
    `Rank` VARCHAR(50),
    ContactInfo VARCHAR(100),
    AgencyID INT,
    FOREIGN KEY (AgencyID) REFERENCES Agency(AgencyID) ON DELETE CASCADE
);

-- Evidence Table
CREATE TABLE Evidence (
    EvidenceID INT PRIMARY KEY AUTO_INCREMENT,
    Description TEXT NOT NULL,
    LocationFound TEXT,
    IncidentID INT,
    FOREIGN KEY (IncidentID) REFERENCES Incidents(IncidentID) ON DELETE CASCADE
);

-- Reports Table
CREATE TABLE Reports (
    ReportID INT PRIMARY KEY AUTO_INCREMENT,
    IncidentID INT,
    OfficerID INT,
    ReportDate DATETIME DEFAULT CURRENT_TIMESTAMP,
    ReportDetails TEXT,
    Status ENUM('Draft', 'Finalized') DEFAULT 'Draft',
    FOREIGN KEY (IncidentID) REFERENCES Incidents(IncidentID) ON DELETE CASCADE,
    FOREIGN KEY (OfficerID) REFERENCES Officers(OfficerID) ON DELETE SET NULL
);

-- Create the 'cases' table
CREATE TABLE cases (
    caseId INT AUTO_INCREMENT PRIMARY KEY,
    caseDescription TEXT NOT NULL,
    caseStatus VARCHAR(50) NOT NULL,
    assignedOfficerId INT NOT NULL,
    FOREIGN KEY (assignedOfficerId) REFERENCES officers(officerID)
);

-- Create the 'case_incidents' table
CREATE TABLE case_incidents (
    caseId INT NOT NULL,
    incidentId INT NOT NULL,
    PRIMARY KEY (caseId, incidentId),
    FOREIGN KEY (caseId) REFERENCES cases(caseId),
    FOREIGN KEY (incidentId) REFERENCES incidents(incidentID)
);


show tables;
INSERT INTO Victims (FirstName, LastName, DateOfBirth, Gender, Address, PhoneNumber) 
VALUES 
('Ravi', 'Varma', '1990-05-15', 'Male', '123 Main Street', '9876543210');

INSERT INTO Suspects (FirstName, LastName, DateOfBirth, Gender, Address, PhoneNumber) 
VALUES 
('Ram', 'Kumar', '1985-08-20', 'Female', '456 Elm Street', '8765432109');

INSERT INTO Agency (AgencyName, Jurisdiction, ContactInfo) 
VALUES 
('Central Police Department', 'City Jurisdiction', 'centralpd@gmail.com');

INSERT INTO Officers (FirstName, LastName, BadgeNumber, `Rank`, ContactInfo, AgencyID) 
VALUES 
('Mike', 'Kelvin', 'B1234', 'Sergeant', 'mike.johnson@example.com', 1);

select * from incidents;
select * from suspects;
select * from officers;
select * from victims;
delete from incidents;
desc case_incidents;

drop table cases;

INSERT INTO incidents (IncidentType, DateReported, Latitude, Longitude, Description, Status, SuspectID)
VALUES ('Theft', '2025-04-13', '34.0522', '-118.2437', 'Suspected robbery', 'OPEN', 1);

SHOW CREATE TABLE cases;

INSERT INTO Victims (FirstName, LastName, DateOfBirth, Gender, Address, PhoneNumber) 
VALUES 
('Ravi', 'Varma', '1990-05-15', 'Male', '123 Main Street, Mumbai', '9876543210'),
('Anjali', 'Patel', '1985-09-25', 'Female', '456 Park Avenue, Delhi', '8765432109'),
('Vikram', 'Reddy', '1978-04-10', 'Male', '789 Oak Street, Bangalore', '8876543210'),
('Sita', 'Sharma', '1992-12-30', 'Female', '101 Silver Lane, Chennai', '9988776655'),
('Amit', 'Singh', '1989-07-11', 'Male', '202 Golden Road, Kolkata', '9123456789');


INSERT INTO Suspects (FirstName, LastName, DateOfBirth, Gender, Address, PhoneNumber) 
VALUES 
('Ram', 'Kumar', '1985-08-20', 'Male', '456 Elm Street, Jaipur', '8765432109'),
('Neha', 'Gupta', '1990-05-10', 'Female', '23 Banyan Tree Road, Delhi', '9988776655'),
('Rahul', 'Mehta', '1995-03-15', 'Male', '65 Maple Street, Mumbai', '9001234567'),
('Priya', 'Iyer', '1988-10-07', 'Female', '34 Lotus Lane, Hyderabad', '9887654321'),
('Akash', 'Yadav', '1980-02-25', 'Male', '12 Greenfield Road, Pune', '9823456789');

INSERT INTO Agency (AgencyName, Jurisdiction, ContactInfo) 
VALUES 
('Central Police Department', 'City Jurisdiction', 'centralpd@gmail.com'),
('Mumbai Police Department', 'Mumbai City', 'mumbai.police@gmail.com'),
('Bangalore Police Department', 'Bangalore City', 'bangalorepolice@domain.com'),
('Delhi Police', 'Delhi NCR', 'delhipolice@delhi.in'),
('Kolkata Police', 'Kolkata City', 'kolkatapolice@kolkata.in');

INSERT INTO Officers (FirstName, LastName, BadgeNumber, `Rank`, ContactInfo, AgencyID) 
VALUES 
('Mike', 'Kelvin', 'B1434', 'Sergeant', 'mike.johnson@example.com', 1),
('Amit', 'Verma', 'B5678', 'Constable', 'amit.verma@example.com', 6),
('Rajesh', 'Singh', 'B9101', 'Inspector', 'rajesh.singh@example.com', 3),
('Nina', 'Kumar', 'B1122', 'Sergeant', 'nina.kumar@example.com', 4),
('Ravi', 'Sharma', 'B3345', 'Constable', 'ravi.sharma@example.com', 5);

select * from agency;

INSERT INTO Evidence (Description, LocationFound, IncidentID) 
VALUES 
('Black wallet with cash and cards', 'Park Lane, Mumbai', 11),
('Blood-stained shirt', 'Back alley near building 5, Delhi', 12),
('Stolen mobile phone', 'Shopping mall, Bangalore', 13),
('Broken window glass', 'Residential area, Chennai', 14),
('Red bag with documents', 'Market Street, Kolkata', 15);

select * from incidents;

INSERT INTO Reports (IncidentID, OfficerID, ReportDetails, Status) 
VALUES 
(11, 18, 'Victim reports a suspected robbery at the park.', 'Draft'),
(12, 19, 'Victim has reported a mugging incident in the alley.', 'Draft'),
(13, 20, 'Victim claims their mobile was stolen at the shopping mall.', 'Draft'),
(14, 21, 'Victim witnessed a window break-in at her residence.', 'Draft'),
(15, 22, 'Victim found a red bag near the market street.', 'Draft');

select * from officers;

INSERT INTO cases (caseDescription, caseStatus, assignedOfficerId) 
VALUES 
('Suspected robbery in the city park.', 'Open', 18),
('Mugging incident near the back alley.', 'Under Investigation', 19),
('Mobile phone theft at the shopping mall.', 'Open', 20),
('Vandalism and breaking of windows at residential area.', 'Closed', 21),
('Missing red bag near the market street.', 'Open', 22);

select * from cases;

INSERT INTO case_incidents (caseId, incidentId) 
VALUES 
(6, 11),
(2, 12),
(3, 13),
(4, 14),
(5, 15);

INSERT INTO incidents (IncidentType, IncidentDate, Latitude, Longitude, Description, Status, SuspectID) 
VALUES 
('Robbery', '2025-04-13 08:00:00', 19.0760, 72.8777, 'Suspected robbery in the city park.', 'Open', 1),
('Mugging', '2025-04-14 09:30:00', 28.6139, 77.2090, 'Mugging incident near the back alley.', 'Under Investigation', 6),
('Theft', '2025-04-15 12:45:00', 12.9716, 77.5946, 'Mobile phone theft at the shopping mall.', 'Open', 3),
('Vandalism', '2025-04-16 14:00:00', 13.0827, 80.2707, 'Vandalism and breaking of windows at residential area.', 'Closed', 4),
('Theft', '2025-04-16 16:00:00', 22.5726, 88.3639, 'Missing red bag near the market street.', 'Open', 5);

ALTER TABLE Incidents 
MODIFY COLUMN Status ENUM('Open', 'Closed', 'under_investigation', 'Under Investigation') DEFAULT 'Open';

delete from incident where incidentId = 37;
select * from incidents ;
select * from victims;
select * from suspects;