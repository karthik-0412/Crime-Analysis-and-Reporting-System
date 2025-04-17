package com.crimeanalysisandreportsystem.main;

import com.crimeanalysisandreportsystem.dao.*;
import com.crimeanalysisandreportsystem.entity.*;
import com.crimeanalysisandreportsystem.exception.*;
import com.crimeanalysisandreportsystem.service.*;
import com.crimeanalysisandreportsystem.util.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class CrimeApp {
	
    public static void main(String[] args) {
        try (Connection connection = DatabaseConnection.getConnection();
             Scanner scanner = new Scanner(System.in)) {

            IncidentDao incidentDao = new IncidentDaoImpl(connection);
            VictimDao victimDao = new VictimDaoImpl(connection);
            SuspectDao suspectDao = new SuspectDaoImpl(connection);
            AgencyDao agencyDao = new AgencyDaoImpl(connection);
            OfficerDao officerDao = new OfficerDaoImpl(connection);
            EvidenceDao evidenceDao = new EvidenceDaoImpl(connection);
            ReportDao reportDao = new ReportDaoImpl(connection);
            CaseDao caseDao = new CaseDaoImpl(connection);

            boolean running = true;

            while (running) {
                System.out.println("\n--- Crime Analysis and Reporting System ---");
                System.out.println("1. Manage Incidents");
                System.out.println("2. Manage Victims");
                System.out.println("3. Manage Suspects");
                System.out.println("4. Manage Agencies");
                System.out.println("5. Manage Officers");
                System.out.println("6. Manage Evidence");
                System.out.println("7. Manage Reports");
                System.out.println("8. Manage Cases");
                System.out.println("9. Exit");
                System.out.print("Choose an option: ");

                int choice = scanner.nextInt();
                scanner.nextLine(); 

                switch (choice) {
                    case 1:
                        manageIncidents(scanner, incidentDao);
                        break;
                    case 2:
                        manageVictims(scanner, victimDao);
                        break;
                    case 3:
                        manageSuspects(scanner, suspectDao);
                        break;
                    case 4:
                        manageAgencies(scanner, agencyDao);
                        break;
                    case 5:
                        manageOfficers(scanner, officerDao);
                        break;
                    case 6:
                        manageEvidence(scanner, evidenceDao);
                        break;
                    case 7:
                        manageReports(scanner, reportDao);
                        break;
                    case 8: 
                    	manageCases(scanner, caseDao, incidentDao); 
                    	break;
                    case 9:
                        running = false;
                        System.out.println("Exiting application...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }

        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    private static void manageIncidents(Scanner scanner, IncidentDao incidentDao) {
        boolean running = true;
        while (running) {
            System.out.println("\n--- Incident Management ---");
            System.out.println("1. Create Incident");
            System.out.println("2. Get Incident by ID");
            System.out.println("3. Get Incidents in Date Range");
            System.out.println("4. Search Incidents by Type");
            System.out.println("5. Update Incident Status");
            System.out.println("6. Get All Incidents");
            System.out.println("7. Back");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    createIncident(scanner, incidentDao);
                    break;
                case 2:
                    getIncidentById(scanner, incidentDao);
                    break;
                case 3:
                    getIncidentsInDateRange(scanner, incidentDao);
                    break;
                case 4:
                    searchIncidentsByType(scanner, incidentDao);
                    break;
                case 5:
                    updateIncidentStatus(scanner, incidentDao);
                    break;
                case 6:
                    getAllIncidents(incidentDao);
                    break;
                case 7:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void createIncident(Scanner scanner, IncidentDao incidentDao) {
    	try {

    		System.out.print("Enter incidentType: ");
    		String incidentType = scanner.nextLine();

    		System.out.print("Enter incidentDate (yyyy-MM-dd): ");
    		String dateInput = scanner.nextLine();
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    		sdf.setLenient(false);
    		Date incidentDate = sdf.parse(dateInput);

    		System.out.print("Enter latitude: ");
    		BigDecimal latitude = scanner.nextBigDecimal();

    		System.out.print("Enter longitude: ");
    		BigDecimal longitude = scanner.nextBigDecimal();
    		scanner.nextLine(); 

    		System.out.print("Enter description: ");
    		String description = scanner.nextLine();

    		System.out.print("Enter status (OPEN, CLOSED, UNDER_INVESTIGATION): ");
    		String statusInput = scanner.nextLine();
    		IncidentStatus status = IncidentStatus.valueOf(statusInput.toUpperCase().replace(" ", "_"));

    		System.out.print("Enter victimID: ");
    		int victimID = scanner.nextInt();

    		System.out.print("Enter suspectID: ");
    		int suspectID = scanner.nextInt();

    		System.out.print("Enter officerID: ");
    		int officerID = scanner.nextInt();
    		scanner.nextLine();

    		Incident newIncident = new Incident(incidentType, incidentDate, latitude, longitude,
    				description, status, victimID, suspectID, officerID);

    		if (incidentDao.createIncident(newIncident)) {
    			System.out.println("Incident created successfully!");
    		} else {
    			System.out.println("Failed to create incident.");
    		}

    	} catch (ParseException e) {
    		System.out.println("Invalid Date Format. Please enter date in yyyy-MM-dd format.");
    	} catch (IllegalArgumentException e) {
    		System.out.println("Invalid Status. Please enter status like: OPEN, CLOSED, UNDER_INVESTIGATION.");
    	} catch (InputMismatchException e) {
    		System.out.println("Invalid Input Type. Please enter correct data types.");
    		scanner.nextLine(); 
    	} catch (DataAccessException e) {
    		System.out.println("Error creating incident: " + e.getMessage());
    	}
    }



    private static void getIncidentById(Scanner scanner, IncidentDao incidentDao) {
        System.out.print("Enter Incident ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        try {
            Incident incident = incidentDao.getIncidentById(id);
            System.out.println("Incident Details: " + incident);
        } catch (IncidentNumberNotFoundException e) {
            System.out.println("Incident not found: " + e.getMessage());
        } catch (DataAccessException e) {
            System.out.println("Error retrieving incident: " + e.getMessage());
        }
    }

    private static void getIncidentsInDateRange(Scanner scanner, IncidentDao incidentDao) {
        System.out.print("Enter start date (yyyy-MM-dd): ");
        String startDateStr = scanner.nextLine();
        System.out.print("Enter end date (yyyy-MM-dd): ");
        String endDateStr = scanner.nextLine();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
        	Date parsedStartDate = sdf.parse(startDateStr);
        	Date parsedEndDate = sdf.parse(endDateStr);
    	    Date startDate = new Date(parsedStartDate.getTime());
    	    Date endDate = new Date(parsedEndDate.getTime());
            List<Incident> incidents = incidentDao.getIncidentsInDateRange(startDate, endDate);
            System.out.println("\nIncidents in Date Range:");
            for (Incident incident : incidents) {
                System.out.println(incident);
            }
        } catch (DataAccessException e) {
            System.out.println("Error retrieving incidents: " + e.getMessage());
        } catch (ParseException e) {
			e.printStackTrace();
		}
    }

    private static void searchIncidentsByType(Scanner scanner, IncidentDao incidentDao) {
        System.out.print("Enter incident type: ");
        String type = scanner.nextLine();

        try {
            List<Incident> incidents = incidentDao.searchIncidents(type);
            System.out.println("\nIncidents of Type " + type + ":");
            for (Incident incident : incidents) {
                System.out.println(incident);
            }
        } catch (DataAccessException e) {
            System.out.println("Error searching incidents: " + e.getMessage());
        }
    }

    private static void updateIncidentStatus(Scanner scanner, IncidentDao incidentDao) {
        System.out.print("Enter Incident ID to update: ");
        int updateId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter new status (OPEN, CLOSED, UNDER_INVESTIGATION): ");
        String statusInput = scanner.nextLine();

        try {
            IncidentStatus newStatus = IncidentStatus.valueOf(statusInput.toUpperCase().replace(" ", "_"));

            if (incidentDao.updateIncidentStatus(newStatus, updateId)) {
                System.out.println("Incident status updated successfully!");
            } else {
                System.out.println("Failed to update incident status.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid status entered. Please use OPEN, CLOSED, or UNDER_INVESTIGATION.");
        } catch (DataAccessException e) {
            System.out.println("Error updating incident status: " + e.getMessage());
        }
    }
    
    private static void getAllIncidents(IncidentDao incidentDao) {
        try {
            List<Incident> allIncidents = incidentDao.getAllIncidents();
            System.out.println("\n--- All Incidents ---");
            for (Incident incident : allIncidents) {
                System.out.println(incident);
            }
        } catch (DataAccessException e) {
            System.out.println("Error retrieving incidents: " + e.getMessage());
        }
    }


 // Victim Management
    private static void manageVictims(Scanner scanner, VictimDao victimDao) {
        boolean running = true;
        while (running) {
            System.out.println("\n--- Victim Management ---");
            System.out.println("1. Create Victim");
            System.out.println("2. Get Victim by ID");
            System.out.println("3. Get All Victims");
            System.out.println("4. Update Victim");
            System.out.println("5. Delete Victim");
            System.out.println("6. Back");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    createVictim(scanner, victimDao);
                    break;
                case 2:
                    getVictimById(scanner, victimDao);
                    break;
                case 3:
                    getAllVictims(scanner, victimDao);
                    break;
                case 4:
                    updateVictim(scanner, victimDao);
                    break;
                case 5:
                    deleteVictim(scanner, victimDao);
                    break;
                case 6:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void createVictim(Scanner scanner, VictimDao victimDao) {
        System.out.print("Enter firstName: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter lastName: ");
        String lastName = scanner.nextLine();

        System.out.print("Enter dateOfBirth (yyyy-MM-dd): ");
        String dobInput = scanner.nextLine();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dateOfBirth = null;
        try {
            dateOfBirth = sdf.parse(dobInput);
        } catch (ParseException e) {
            System.out.println("Invalid Date Format!");
            return;
        }

        System.out.print("Enter gender: ");
        String genderInput = scanner.nextLine();
        Gender gender = Gender.valueOf(genderInput.toUpperCase().replace(" ", "_"));

        System.out.print("Enter address: ");
        String address = scanner.nextLine();

        String phoneNumber;
        while (true) {
            System.out.print("Enter Phone number (10 digits only): ");
            phoneNumber = scanner.nextLine();
            if (phoneNumber.matches("\\d{10}")) {
                break;
            } else {
                System.out.println("Invalid phone number. Please enter exactly 10 digits.");
            }
        }
        Victim newVictim = new Victim(firstName, lastName, dateOfBirth, gender, address, phoneNumber);

        try {
            if (victimDao.createVictim(newVictim)) {
                System.out.println("Victim created successfully!");
            } else {
                System.out.println("Failed to create victim.");
            }
        } catch (DataAccessException e) {
            System.out.println("Error creating victim: " + e.getMessage());
        }
    }



    private static void getVictimById(Scanner scanner, VictimDao victimDao) {
        System.out.print("Enter Victim ID: ");
        int id = scanner.nextInt();
        scanner.nextLine(); 

        try {
            Victim victim = victimDao.getVictimById(id);
            System.out.println("Victim Details: " + victim.toString());
        } catch (VictimNotFoundException e) {
            System.out.println("Victim not found: " + e.getMessage());
        } catch (DataAccessException e) {
            System.out.println("Error retrieving victim: " + e.getMessage());
        }
    }

    private static void getAllVictims(Scanner scanner, VictimDao victimDao) {
        try {
            List<Victim> victims = victimDao.getAllVictims();
            System.out.println("\nAll Victims:");
            for (Victim victim : victims) {
                System.out.println(victim);
            }
        } catch (DataAccessException e) {
            System.out.println("Error retrieving all victims: " + e.getMessage());
        }
    }

    private static void updateVictim(Scanner scanner, VictimDao victimDao) {
        System.out.print("Enter Victim ID to update: ");
        int updateId = scanner.nextInt();
        scanner.nextLine(); 

        System.out.print("Enter new firstName: ");
        String newFirstName = scanner.nextLine();

        System.out.print("Enter new lastName: ");
        String newLastName = scanner.nextLine();

        System.out.print("Enter new dateOfBirth (yyyy-MM-dd): ");
        String dateInput = scanner.nextLine();
        java.sql.Date newDateOfBirth = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false); 
            java.util.Date parsedDate = sdf.parse(dateInput); 
            newDateOfBirth = new java.sql.Date(parsedDate.getTime()); 
        } catch (ParseException e) {
            System.out.println("Invalid date format. Please enter in yyyy-MM-dd format.");
            return;
        }

        System.out.print("Enter new gender: ");
        String genderInput = scanner.nextLine();
        Gender newGender = Gender.valueOf(genderInput.toUpperCase().replace(" ", "_"));

        System.out.print("Enter new address: ");
        String newAddress = scanner.nextLine();

        String newphoneNumber;
        while (true) {
            System.out.print("Enter Phone number (10 digits only): ");
            newphoneNumber = scanner.nextLine();
            if (newphoneNumber.matches("\\d{10}")) {
                break;
            } else {
                System.out.println("Invalid phone number. Please enter exactly 10 digits.");
            }
        }

        Victim updatedVictim = new Victim(updateId, newFirstName, newLastName, newDateOfBirth, newGender, newAddress, newphoneNumber);

        try {
            if (victimDao.updateVictim(updatedVictim)) {
                System.out.println("Victim updated successfully!");
            } else {
                System.out.println("Failed to update victim.");
            }
        } catch (DataAccessException e) {
            System.out.println("Error updating victim: " + e.getMessage());
        }
    }



    private static void deleteVictim(Scanner scanner, VictimDao victimDao) {
        System.out.print("Enter Victim ID to delete: ");
        int deleteId = scanner.nextInt();
        scanner.nextLine(); 

        try {
            if (victimDao.deleteVictim(deleteId)) {
                System.out.println("Victim deleted successfully!");
            } else {
                System.out.println("Failed to delete victim.");
            }
        } catch (DataAccessException e) {
            System.out.println("Error deleting victim: " + e.getMessage());
        }
    }
    
 // Suspect Management
    private static void manageSuspects(Scanner scanner, SuspectDao suspectDao) {
        boolean running = true;
        while (running) {
            System.out.println("\n--- Suspect Management ---");
            System.out.println("1. Create Suspect");
            System.out.println("2. Get Suspect by ID");
            System.out.println("3. Get All Suspects");
            System.out.println("4. Update Suspect");
            System.out.println("5. Delete Suspect");
            System.out.println("6. Back");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    createSuspect(scanner, suspectDao);
                    break;
                case 2:
                    getSuspectById(scanner, suspectDao);
                    break;
                case 3:
                    getAllSuspects(scanner, suspectDao);
                    break;
                case 4:
                    updateSuspect(scanner, suspectDao);
                    break;
                case 5:
                    deleteSuspect(scanner, suspectDao);
                    break;
                case 6:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void createSuspect(Scanner scanner, SuspectDao suspectDao) {
        try {

            System.out.print("Enter firstName: ");
            String firstName = scanner.nextLine();

            System.out.print("Enter lastName: ");
            String lastName = scanner.nextLine();

            System.out.print("Enter dateOfBirth (yyyy-MM-dd): ");
            String dobInput = scanner.nextLine();

            // Convert String to java.sql.Date
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date parsedDate = sdf.parse(dobInput);
            Date dateOfBirth = new Date(parsedDate.getTime());

            System.out.print("Enter gender: ");
            String genderInput = scanner.nextLine();
            Gender gender = Gender.valueOf(genderInput.toUpperCase().replace(" ", "_"));

            System.out.print("Enter address: ");
            String address = scanner.nextLine();

            String phoneNumber;
            while (true) {
                System.out.print("Enter Phone number (10 digits only): ");
                phoneNumber = scanner.nextLine();
                if (phoneNumber.matches("\\d{10}")) {
                    break;
                } else {
                    System.out.println("Invalid phone number. Please enter exactly 10 digits.");
                }
            }
            
            Suspect newSuspect = new Suspect(firstName, lastName, dateOfBirth, gender, address,phoneNumber);

            if (suspectDao.createSuspect(newSuspect)) {
                System.out.println("Suspect created successfully!");
            } else {
                System.out.println("Failed to create suspect.");
            }
        } catch (DataAccessException e) {
            System.out.println("Error creating suspect: " + e.getMessage());
        } catch (ParseException e) {
            System.out.println("Invalid date format. Please enter date as yyyy-MM-dd.");
        }
    }

    private static void getSuspectById(Scanner scanner, SuspectDao suspectDao) {
        System.out.print("Enter Suspect ID: ");
        int id = scanner.nextInt();
        scanner.nextLine(); 

        try {
            Suspect suspect = suspectDao.getSuspectById(id);
            System.out.println("Suspect Details: " + suspect.toString());
        } catch (SuspectNotFoundException e) {
            System.out.println("Suspect not found: " + e.getMessage());
        } catch (DataAccessException e) {
            System.out.println("Error retrieving suspect: " + e.getMessage());
        }
    }

    private static void getAllSuspects(Scanner scanner, SuspectDao suspectDao) {
        try {
            List<Suspect> suspects = suspectDao.getAllSuspects();
            System.out.println("\nAll Suspects:");
            for (Suspect suspect : suspects) {
                System.out.println(suspect.toString());
            }
        } catch (DataAccessException e) {
            System.out.println("Error retrieving all suspects: " + e.getMessage());
        }
    }

    private static void updateSuspect(Scanner scanner, SuspectDao suspectDao) {
        System.out.print("Enter Suspect ID to update: ");
        int updateId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter new firstName: ");
        String newFirstName = scanner.nextLine();
        System.out.print("Enter new lastName: ");
        String newLastName = scanner.nextLine();
        System.out.print("Enter new dateOfBirth (yyyy-MM-dd): ");
        String newDateOfBirth = scanner.nextLine();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            java.util.Date parsedDate = sdf.parse(newDateOfBirth);
            Date newdateOfBirth = new Date(parsedDate.getTime());

            System.out.print("Enter new gender: ");
            String newGenderIn = scanner.nextLine();
            Gender newGender = Gender.valueOf(newGenderIn.toUpperCase().replace(" ", "_"));
            System.out.print("Enter new Address: ");
            String newAddress = scanner.nextLine();
            String newphoneNumber;
            while (true) {
                System.out.print("Enter New Phone number (10 digits only): ");
                newphoneNumber = scanner.nextLine();
                if (newphoneNumber.matches("\\d{10}")) {
                    break;
                } else {
                    System.out.println("Invalid phone number. Please enter exactly 10 digits.");
                }
            }

            Suspect updatedSuspect = new Suspect(updateId, newFirstName, newLastName, newdateOfBirth, newGender, newAddress, newphoneNumber);

            if (suspectDao.updateSuspect(updatedSuspect)) {
                System.out.println("Suspect updated successfully!");
            } else {
                System.out.println("Failed to update suspect.");
            }
        } catch (ParseException e) {
            System.out.println("Invalid date format. Please enter the date as yyyy-MM-dd.");
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid gender entered.");
        } catch (DataAccessException e) {
            System.out.println("Error updating suspect: " + e.getMessage());
        }

    }

    private static void deleteSuspect(Scanner scanner, SuspectDao suspectDao) {
        System.out.print("Enter Suspect ID to delete: ");
        int deleteId = scanner.nextInt();
        scanner.nextLine(); 

        try {
            if (suspectDao.deleteSuspect(deleteId)) {
                System.out.println("Suspect deleted successfully!");
            } else {
                System.out.println("Failed to delete suspect.");
            }
        } catch (DataAccessException e) {
            System.out.println("Error deleting suspect: " + e.getMessage());
        }
    }
    
    private static void manageAgencies(Scanner scanner, AgencyDao agencyDao) {
        boolean running = true;
        while (running) {
            System.out.println("\n--- Agency Management ---");
            System.out.println("1. Create Agency");
            System.out.println("2. Get Agency by ID");
            System.out.println("3. Get All Agencies");
            System.out.println("4. Update Agency");
            System.out.println("5. Delete Agency");
            System.out.println("6. Back");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    createAgency(scanner, agencyDao);
                    break;
                case 2:
                    getAgencyById(scanner, agencyDao);
                    break;
                case 3:
                    getAllAgencies(scanner, agencyDao);
                    break;
                case 4:
                    updateAgency(scanner, agencyDao);
                    break;
                case 5:
                    deleteAgency(scanner, agencyDao);
                    break;
                case 6:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void createAgency(Scanner scanner, AgencyDao agencyDao) {

        System.out.print("Enter agencyName: ");
        String agencyName = scanner.nextLine();
        System.out.print("Enter jurisdiction: ");
        String jurisdiction = scanner.nextLine();
        String contactInfo;
        String regex = ".*@.*"; // Ensures '@' is present

        while (true) {
            System.out.print("Enter contactInfo (must contain '@'): ");
            contactInfo = scanner.nextLine();

            if (contactInfo.matches(regex)) {
                break;
            } else {
                System.out.println("Invalid contactInfo. It must contain '@'.");
            }
        }

        Agency newAgency = new Agency(agencyName, jurisdiction, contactInfo);
        try {
            if (agencyDao.createAgency(newAgency)) {
                System.out.println("Agency created successfully!");
            } else {
                System.out.println("Failed to create agency.");
            }
        } catch (DataAccessException e) {
            System.out.println("Error creating agency: " + e.getMessage());
        }
    }

    private static void getAgencyById(Scanner scanner, AgencyDao agencyDao) {
        System.out.print("Enter Agency ID: ");
        int id = scanner.nextInt();
        scanner.nextLine(); 

        try {
            Agency agency = agencyDao.getAgencyById(id);
            System.out.println("Agency Details: " + agency.toString());
        } catch (AgencyNotFoundException e) {
            System.out.println("Agency not found: " + e.getMessage());
        } catch (DataAccessException e) {
            System.out.println("Error retrieving agency: " + e.getMessage());
        }
    }

    private static void getAllAgencies(Scanner scanner, AgencyDao agencyDao) {
        try {
            List<Agency> agencies = agencyDao.getAllAgencies();
            System.out.println("\nAll Agencies:");
            for (Agency agency : agencies) {
                System.out.println(agency.toString());
            }
        } catch (DataAccessException e) {
            System.out.println("Error retrieving all agencies: " + e.getMessage());
        }
    }

    private static void updateAgency(Scanner scanner, AgencyDao agencyDao) {
        System.out.print("Enter Agency ID to update: ");
        int updateId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter new agencyName: ");
        String newAgencyName = scanner.nextLine();
        System.out.print("Enter new jurisdiction: ");
        String newJurisdiction = scanner.nextLine();
        String newContactInfo;
        String regex = ".*@.*"; // Ensures '@' is present

        while (true) {
            System.out.print("Enter new contactInfo (must contain '@'): ");
            newContactInfo = scanner.nextLine();

            if (newContactInfo.matches(regex)) {
                break;
            } else {
                System.out.println("Invalid contactInfo. It must contain '@'.");
            }
        }

        Agency updatedAgency = new Agency(updateId, newAgencyName, newJurisdiction, newContactInfo);
        try {
            if (agencyDao.updateAgency(updatedAgency)) {
                System.out.println("Agency updated successfully!");
            } else {
                System.out.println("Failed to update agency.");
            }
        } catch (DataAccessException e) {
            System.out.println("Error updating agency: " + e.getMessage());
        }
    }

    private static void deleteAgency(Scanner scanner, AgencyDao agencyDao) {
        System.out.print("Enter Agency ID to delete: ");
        int deleteId = scanner.nextInt();
        scanner.nextLine(); 

        try {
            if (agencyDao.deleteAgency(deleteId)) {
                System.out.println("Agency deleted successfully!");
            } else {
                System.out.println("Failed to delete agency.");
            }
        } catch (DataAccessException e) {
            System.out.println("Error deleting agency: " + e.getMessage());
        }
    }
    
    
 // Officer Management
    private static void manageOfficers(Scanner scanner, OfficerDao officerDao) {
        boolean running = true;
        while (running) {
            System.out.println("\n--- Officer Management ---");
            System.out.println("1. Create Officer");
            System.out.println("2. Get Officer by ID");
            System.out.println("3. Get All Officers");
            System.out.println("4. Update Officer");
            System.out.println("5. Delete Officer");
            System.out.println("6. Back");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    createOfficer(scanner, officerDao);
                    break;
                case 2:
                    getOfficerById(scanner, officerDao);
                    break;
                case 3:
                    getAllOfficers(scanner, officerDao);
                    break;
                case 4:
                    updateOfficer(scanner, officerDao);
                    break;
                case 5:
                    deleteOfficer(scanner, officerDao);
                    break;
                case 6:
                    running = false;
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void createOfficer(Scanner scanner, OfficerDao officerDao) {

        System.out.print("Enter firstName: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter lastName: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter badgeNumber: ");
        String badgeNumber = scanner.nextLine();
        System.out.print("Enter rank: ");
        String rank = scanner.nextLine();
        String contactInfo;
        String regex = ".*@.*"; 

        while (true) {
            System.out.print("Enter contactInfo: ");
            contactInfo = scanner.nextLine();

            if (Pattern.matches(regex, contactInfo)) {
                break;
            } else {
                System.out.println("Invalid input. It must contain '@'.");
            }
        }
        System.out.print("Enter agencyID: ");
        int agencyID = scanner.nextInt();
        scanner.nextLine();

        Officer newOfficer = new Officer(firstName, lastName, badgeNumber, rank, contactInfo, agencyID);
        try {
            if (officerDao.createOfficer(newOfficer)) {
                System.out.println("Officer created successfully!");
            } else {
                System.out.println("Failed to create officer.");
            }
        } catch (DataAccessException e) {
            System.out.println("Error creating officer: " + e.getMessage());
        }
    }

    private static void getOfficerById(Scanner scanner, OfficerDao officerDao) {
        System.out.print("Enter Officer ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        try {
            Officer officer = officerDao.getOfficerById(id);
            System.out.println("Officer Details: " + officer);
        } catch (OfficerNotFoundException e) {
            System.out.println("Officer not found: " + e.getMessage());
        } catch (DataAccessException e) {
            System.out.println("Error retrieving officer: " + e.getMessage());
        }
    }

    private static void getAllOfficers(Scanner scanner, OfficerDao officerDao) {
        try {
            List<Officer> officers = officerDao.getAllOfficers();
            System.out.println("\nAll Officers:");
            for (Officer officer : officers) {
                System.out.println(officer);
            }
        } catch (DataAccessException e) {
            System.out.println("Error retrieving all officers: " + e.getMessage());
        }
    }

    private static void updateOfficer(Scanner scanner, OfficerDao officerDao) {
        System.out.print("Enter Officer ID to update: ");
        int updateId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter new firstName: ");
        String newFirstName = scanner.nextLine();
        System.out.print("Enter new lastName: ");
        String newLastName = scanner.nextLine();
        System.out.print("Enter new badgeNumber: ");
        String newBadgeNumber = scanner.nextLine();
        System.out.print("Enter new rank: ");
        String newRank = scanner.nextLine();
        String newContactInfo;
        String regex = ".*@.*"; 

        while (true) {
            System.out.print("Enter contactInfo: ");
            newContactInfo = scanner.nextLine();

            if (Pattern.matches(regex, newContactInfo)) {
                break;
            } else {
                System.out.println("Invalid input. It must contain '@'.");
            }
        }
        System.out.print("Enter new agencyID: ");
        int newAgencyID = scanner.nextInt();
        scanner.nextLine();

        Officer updatedOfficer = new Officer(updateId, newFirstName, newLastName, newBadgeNumber, newRank, newContactInfo, newAgencyID);
        try {
            if (officerDao.updateOfficer(updatedOfficer)) {
                System.out.println("Officer updated successfully!");
            } else {
                System.out.println("Failed to update officer.");
            }
        } catch (DataAccessException e) {
            System.out.println("Error updating officer: " + e.getMessage());
        }
    }

    private static void deleteOfficer(Scanner scanner, OfficerDao officerDao) {
        System.out.print("Enter Officer ID to delete: ");
        int deleteId = scanner.nextInt();
        scanner.nextLine();

        try {
            if (officerDao.deleteOfficer(deleteId)) {
                System.out.println("Officer deleted successfully!");
            } else {
                System.out.println("Failed to delete officer.");
            }
        } catch (DataAccessException e) {
            System.out.println("Error deleting officer: " + e.getMessage());
        }
    }
    


// Evidence Management
    private static void manageEvidence(Scanner scanner, EvidenceDao evidenceDao) {
        boolean running = true;
        while (running) {
            System.out.println("\n--- Evidence Management ---");
            System.out.println("1. Create Evidence");
            System.out.println("2. Get Evidence by ID");
            System.out.println("3. Get All Evidence");
            System.out.println("4. Update Evidence");
            System.out.println("5. Delete Evidence");
            System.out.println("6. Back");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    createEvidence(scanner, evidenceDao);
                    break;
                case 2:
                    getEvidenceById(scanner, evidenceDao);
                    break;
                case 3:
                    getAllEvidence(scanner, evidenceDao);
                    break;
                case 4:
                    updateEvidence(scanner, evidenceDao);
                    break;
                case 5:
                    deleteEvidence(scanner, evidenceDao);
                    break;
                case 6:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void createEvidence(Scanner scanner, EvidenceDao evidenceDao) {
        System.out.print("Enter description: ");
        String description = scanner.nextLine();
        System.out.print("Enter locationFound: ");
        String locationFound = scanner.nextLine();
        System.out.print("Enter incidentID: ");
        int incidentID = scanner.nextInt();
        scanner.nextLine();

        Evidence newEvidence = new Evidence(description, locationFound, incidentID);
        try {
            if (evidenceDao.createEvidence(newEvidence)) {
                System.out.println("Evidence created successfully!");
            } else {
                System.out.println("Failed to create evidence.");
            }
        } catch (DataAccessException e) {
            System.out.println("Error creating evidence: " + e.getMessage());
        }
    }

    private static void getEvidenceById(Scanner scanner, EvidenceDao evidenceDao) {
        System.out.print("Enter Evidence ID: ");
        int id = scanner.nextInt();
        scanner.nextLine(); 

        try {
            Evidence evidence = evidenceDao.getEvidenceById(id);
            System.out.println("Evidence Details: " + evidence);
        } catch (EvidenceNotFoundException e) {
            System.out.println("Evidence not found: " + e.getMessage());
        } catch (DataAccessException e) {
            System.out.println("Error retrieving evidence: " + e.getMessage());
        }
    }

    private static void getAllEvidence(Scanner scanner, EvidenceDao evidenceDao) {
        try {
            List<Evidence> evidences = evidenceDao.getAllEvidence();
            System.out.println("\nAll Evidence:");
            for (Evidence evidence : evidences) {
                System.out.println(evidence);
            }
        } catch (DataAccessException e) {
            System.out.println("Error retrieving all evidence: " + e.getMessage());
        }
    }

    private static void updateEvidence(Scanner scanner, EvidenceDao evidenceDao) {
        System.out.print("Enter Evidence ID to update: ");
        int updateId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter new description: ");
        String newDescription = scanner.nextLine();
        System.out.print("Enter new locationFound: ");
        String newLocationFound = scanner.nextLine();
        System.out.print("Enter new incidentID: ");
        int newIncidentID = scanner.nextInt();
        scanner.nextLine();

        Evidence updatedEvidence = new Evidence(updateId, newDescription, newLocationFound, newIncidentID);
        try {
            if (evidenceDao.updateEvidence(updatedEvidence)) {
                System.out.println("Evidence updated successfully!");
            } else {
                System.out.println("Failed to update evidence.");
            }
        } catch (DataAccessException e) {
            System.out.println("Error updating evidence: " + e.getMessage());
        }
    }

    private static void deleteEvidence(Scanner scanner, EvidenceDao evidenceDao) {
        System.out.print("Enter Evidence ID to delete: ");
        int deleteId = scanner.nextInt();
        scanner.nextLine(); 

        try {
            if (evidenceDao.deleteEvidence(deleteId)) {
                System.out.println("Evidence deleted successfully!");
            } else {
                System.out.println("Failed to delete evidence.");
            }
        } catch (DataAccessException e) {
            System.out.println("Error deleting evidence: " + e.getMessage());
        }
    }

 // Report Management
    private static void manageReports(Scanner scanner, ReportDao reportDao) {
        boolean running = true;
        while (running) {
            System.out.println("\n--- Report Management ---");
            System.out.println("1. Create Report");
            System.out.println("2. Get Report by ID");
            System.out.println("3. Get All Reports");
            System.out.println("4. Update Report");
            System.out.println("5. Delete Report");
            System.out.println("6. Back");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    createReport(scanner, reportDao);
                    break;
                case 2:
                    getReportById(scanner, reportDao);
                    break;
                case 3:
                    getAllReports(scanner, reportDao);
                    break;
                case 4:
                    updateReport(scanner, reportDao);
                    break;
                case 5:
                    deleteReport(scanner, reportDao);
                    break;
                case 6:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void createReport(Scanner scanner, ReportDao reportDao) {
        System.out.print("Enter incidentID: ");
        int incidentID = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter reportingOfficerID: ");
        int reportingOfficer = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter reportDate (yyyy-MM-dd): ");
        String reportDate = scanner.nextLine();
        
        System.out.print("Enter reportDetails: ");
        String reportDetails = scanner.nextLine();
        
        System.out.print("Enter status: ");
        String reportStatus = scanner.nextLine();

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date parsedDate = sdf.parse(reportDate);
            Date reportDatef = new Date(parsedDate.getTime());

            ReportStatus status = ReportStatus.valueOf(reportStatus.toUpperCase().replace(" ", "_"));
            Report newReport = new Report(incidentID, reportingOfficer, reportDatef, reportDetails, status);

            if (reportDao.createReport(newReport)) {
                System.out.println("Report created successfully!");
            } else {
                System.out.println("Failed to create report.");
            }
        } catch (Exception e) {
            System.out.println("Error creating report: " + e.getMessage());
        }
    }


    private static void getReportById(Scanner scanner, ReportDao reportDao) {
        System.out.print("Enter Report ID: ");
        int id = scanner.nextInt();
        scanner.nextLine(); 

        try {
            Report report = reportDao.getReportById(id);
            System.out.println("Report Details: " + report);
        } catch (ReportNotFoundException e) {
            System.out.println("Report not found: " + e.getMessage());
        } catch (DataAccessException e) {
            System.out.println("Error retrieving report: " + e.getMessage());
        }
    }

    private static void getAllReports(Scanner scanner, ReportDao reportDao) {
        try {
            List<Report> reports = reportDao.getAllReports();
            System.out.println("\nAll Reports:");
            for (Report report : reports) {
                System.out.println(report);
            }
        } catch (DataAccessException e) {
            System.out.println("Error retrieving all reports: " + e.getMessage());
        }
    }

    private static void updateReport(Scanner scanner, ReportDao reportDao) {
        try {
            System.out.print("Enter Report ID to update: ");
            int updateId = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Enter new incidentID: ");
            int newIncidentID = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Enter new reportingOfficer: ");
            int newReportingOfficer = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Enter new reportDate (yyyy-MM-dd): ");
            String reportDateStr = scanner.nextLine();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date parsedDate = sdf.parse(reportDateStr);
            Date reportDatef = new Date(parsedDate.getTime());

            System.out.print("Enter new reportDetails: ");
            String newReportDetails = scanner.nextLine();

            System.out.print("Enter new status: ");
            String newStatus = scanner.nextLine();
            ReportStatus status = ReportStatus.valueOf(newStatus.toUpperCase().replace(" ", "_"));

            Report updatedReport = new Report(updateId, newIncidentID, newReportingOfficer, reportDatef, newReportDetails, status);

            if (reportDao.updateReport(updatedReport)) {
                System.out.println("Report updated successfully!");
            } else {
                System.out.println("Failed to update report.");
            }
        } catch (Exception e) {
            System.out.println("Error updating report: " + e.getMessage());
        }
    }


    private static void deleteReport(Scanner scanner, ReportDao reportDao) {
        System.out.print("Enter Report ID to delete: ");
        int deleteId = scanner.nextInt();
        scanner.nextLine();

        try {
            if (reportDao.deleteReport(deleteId)) {
                System.out.println("Report deleted successfully!");
            } else {
                System.out.println("Failed to delete report.");
            }
        } catch (DataAccessException e) {
            System.out.println("Error deleting report: " + e.getMessage());
        }
    }
    
//    Case Management
    private static void manageCases(Scanner scanner, CaseDao caseDao, IncidentDao incidentDao) {
        boolean back = false;

        while (!back) {
            System.out.println("\n--- Case Management ---");
            System.out.println("1. Create Case");
            System.out.println("2. View Case Details");
            System.out.println("3. Update Case");
            System.out.println("4. List All Cases");
            System.out.println("5. View Cases Assigned to Top Officer(s)");
            System.out.println("6. Back");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            try {
                switch (choice) {
                    case 1 -> {
                    	OfficerDao officerDao = new OfficerDaoImpl(DatabaseConnection.getConnection());
                        System.out.print("Enter case description: ");
                        String desc = scanner.nextLine();
                        System.out.print("Enter case status: ");
                        String status = scanner.nextLine();
                        System.out.print("Enter assigned officer ID: ");
                        int officerId = scanner.nextInt();
                        scanner.nextLine();
                        try {
                        List<Officer> officers = officerDao.getAllOfficers();
                        System.out.println("Available Officers:");
                        for (Officer officer : officers) {
                            System.out.println(officer.getOfficerID() + ": " + officer.getFirstName() + " " + officer.getLastName());
                        }
                        }catch(DataAccessException e) {
                        	System.out.println("Error retrieving officers: " + e.getMessage());
                            e.printStackTrace();
                        }
                        List<Incident> availableIncidents = incidentDao.getAllIncidents();
                        System.out.println("Available incidents:");
                        for (Incident i : availableIncidents) {
                            System.out.println(i.getIncidentID() + ": " + i.getIncidentType());
                        }

                        List<Incident> selectedIncidents = new ArrayList<>();
                        while (true) {
                            System.out.print("Enter Incident ID to associate (0 to stop): ");
                            int id = scanner.nextInt();
                            if (id == 0) break;
                            Incident incident = incidentDao.getIncidentById(id);
                            if (incident != null) selectedIncidents.add(incident);
                        }

                        Case newCase = caseDao.createCase(desc, status, officerId, selectedIncidents);
                        System.out.println("Case created with ID: " + newCase.getCaseId());
                    }
                    case 2 -> {
                        System.out.print("Enter case ID: ");
                        int id = scanner.nextInt();
                        Case c = caseDao.getCaseDetails(id);
                        System.out.println("Case ID: " + c.getCaseId());
                        System.out.println("Description: " + c.getCaseDescription());
                        System.out.println("Status: " + c.getCaseStatus());
                        System.out.println("Assigned Officer ID: " + c.getAssignedOfficerId());
                        System.out.println("Incidents:");
                        for (Incident i : c.getIncidents()) {
                            System.out.println(" - " + i.getIncidentID() + ": " + i.getIncidentType());
                        }
                    }
                    case 3 -> {
                        System.out.print("Enter case ID to update: ");
                        int id = scanner.nextInt();
                        scanner.nextLine();
                        Case existingCase = caseDao.getCaseDetails(id);

                        System.out.print("New description (leave blank to keep current): ");
                        String newDesc = scanner.nextLine();
                        System.out.print("New status (leave blank to keep current): ");
                        String newStatus = scanner.nextLine();
                        System.out.print("New officer ID (enter -1 to keep current): ");
                        int newOfficerId = scanner.nextInt();
                        scanner.nextLine();

                        List<Incident> updatedIncidents = new ArrayList<>();
                        System.out.println("Enter new incident IDs (0 to stop): ");
                        while (true) {
                            int incidentId = scanner.nextInt();
                            if (incidentId == 0) break;
                            Incident i = incidentDao.getIncidentById(incidentId);
                            if (i != null) updatedIncidents.add(i);
                        }

                        if (!newDesc.isEmpty()) existingCase.setCaseDescription(newDesc);
                        if (!newStatus.isEmpty()) existingCase.setCaseStatus(newStatus);
                        if (newOfficerId != -1) existingCase.setAssignedOfficerId(newOfficerId);
                        existingCase.setIncidents(updatedIncidents);

                        if (caseDao.updateCaseDetails(existingCase)) {
                            System.out.println("Case updated successfully.");
                        } else {
                            System.out.println("Update failed.");
                        }
                    }
                    case 4 -> {
                        List<Case> allCases = caseDao.getAllCases();
                        for (Case c : allCases) {
                            System.out.println("Case ID: " + c.getCaseId() + ", Description: " + c.getCaseDescription());
                        }
                    }
                    case 5 -> {
                        List<Case> topOfficerCases = caseDao.getCasesByTopOfficer();
                        if (topOfficerCases.isEmpty()) {
                            System.out.println("No cases found for top officer(s).");
                        } else {
                            System.out.println("Cases assigned to top officer(s):");
                            for (Case c : topOfficerCases) {
                                System.out.println("Case ID: " + c.getCaseId() +
                                                   ", Description: " + c.getCaseDescription() +
                                                   ", Status: " + c.getCaseStatus() +
                                                   ", Officer ID: " + c.getAssignedOfficerId());
                            }
                        }
                    }

                    case 6 -> back = true;
                    default -> System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

}