import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

// Abstract class Person
abstract class Person {
    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    // Abstract method to be implemented by subclasses
    public abstract String getDetails();
}

// Patient class extending Person
class Patient extends Person {
    private String patientId;

    public Patient(String name, int age, String patientId) {
        super(name, age);
        this.patientId = patientId;
    }

    public String getPatientId() {
        return patientId;
    }

    @Override
    public String getDetails() {
        return "Patient ID: " + patientId + ", Name: " + getName() + ", Age: " + getAge();
    }

    @Override
    public String toString() {
        return getDetails();
    }
}

// Doctor class extending Person
class Doctor extends Person {
    private String specialty;

    public Doctor(String name, int age, String specialty) {
        super(name, age);
        this.specialty = specialty;
    }

    public String getSpecialty() {
        return specialty;
    }

    @Override
    public String getDetails() {
        return "Doctor Name: " + getName() + ", Specialty: " + specialty + ", Age: " + getAge();
    }

    @Override
    public String toString() {
        return getDetails();
    }
}

// Appointment class to store appointments
class Appointment {
    Patient patient;
    Doctor doctor;
    Date appointmentDate;

    public Appointment(Patient patient, Doctor doctor, Date appointmentDate) {
        this.patient = patient;
        this.doctor = doctor;
        this.appointmentDate = appointmentDate;
    }

    @Override
    public String toString() {
        return "Appointment for Patient: " + patient.getName() + " with Doctor: " + doctor.getName() +
                " on " + appointmentDate;
    }
}

// Interface for hospital management functions
interface HospitalManagement {
    void addPatient();
    void addDoctor();
    void scheduleAppointment();
    void displayPatients();
    void displayDoctors();
}

// Hospital class implementing the interface
class Hospital implements HospitalManagement {
    private Person[] patients = new Patient[10]; // Using Person array for polymorphism
    private Person[] doctors = new Doctor[10];   // Using Person array for polymorphism
    private Appointment[] appointments = new Appointment[10];
    private int patientCount = 0;
    private int doctorCount = 0;
    private int appointmentCount = 0;
    private Scanner scanner = new Scanner(System.in);

    @Override
    public void addPatient() {
        if (patientCount == patients.length) {
            System.out.println("Patient list is full. Cannot add more patients.");
            return;
        }
        System.out.print("Enter patient name: ");
        String name = scanner.nextLine();
        System.out.print("Enter patient age: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter patient ID: ");
        String patientId = scanner.nextLine();
        patients[patientCount++] = new Patient(name, age, patientId);
        System.out.println("Patient added successfully!");
    }

    @Override
    public void addDoctor() {
        if (doctorCount == doctors.length) {
            System.out.println("Doctor list is full. Cannot add more doctors.");
            return;
        }
        System.out.print("Enter doctor name: ");
        String name = scanner.nextLine();
        System.out.print("Enter doctor age: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter doctor's specialty: ");
        String specialty = scanner.nextLine();
        doctors[doctorCount++] = new Doctor(name, age, specialty);
        System.out.println("Doctor added successfully!");
    }

    @Override
    public void scheduleAppointment() {
        if (patientCount == 0 || doctorCount == 0) {
            System.out.println("Please add patients and doctors first.");
            return;
        }
        if (appointmentCount == appointments.length) {
            System.out.println("Appointment list is full. Cannot schedule more appointments.");
            return;
        }

        System.out.print("Enter patient ID: ");
        String patientId = scanner.nextLine();
        Patient patient = null;
        for (int i = 0; i < patientCount; i++) {
            if (((Patient) patients[i]).getPatientId().equals(patientId)) {
                patient = (Patient) patients[i];
                break;
            }
        }
        if (patient == null) {
            System.out.println("Patient not found!");
            return;
        }

        System.out.print("Enter doctor's name: ");
        String doctorName = scanner.nextLine();
        Doctor doctor = null;
        for (int i = 0; i < doctorCount; i++) {
            if (doctors[i].getName().equalsIgnoreCase(doctorName)) {
                doctor = (Doctor) doctors[i];
                break;
            }
        }
        if (doctor == null) {
            System.out.println("Doctor not found!");
            return;
        }

        System.out.print("Enter appointment date and time (yyyy-MM-dd HH:mm): ");
        String dateString = scanner.nextLine();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date appointmentDate = sdf.parse(dateString);
            appointments[appointmentCount++] = new Appointment(patient, doctor, appointmentDate);
            System.out.println("Appointment scheduled successfully!");
        } catch (ParseException e) {
            System.out.println("Invalid date and time format!");
        }
    }

    public void viewAppointments() {
        if (appointmentCount == 0) {
            System.out.println("No appointments scheduled.");
            return;
        }
        for (int i = 0; i < appointmentCount; i++) {
            System.out.println(appointments[i]);
        }
    }

    public void cancelAppointment() {
        System.out.print("Enter patient ID to cancel appointment: ");
        String patientId = scanner.nextLine();
        boolean found = false;
        for (int i = 0; i < appointmentCount; i++) {
            if (((Patient) appointments[i].patient).getPatientId().equals(patientId)) {
                found = true;
                System.out.println("Appointment cancelled: " + appointments[i]);
                for (int j = i; j < appointmentCount - 1; j++) {
                    appointments[j] = appointments[j + 1];
                }
                appointmentCount--;
                break;
            }
        }
        if (!found) {
            System.out.println("No appointment found for the given patient ID.");
        }
    }

    public void updatePatientInfo() {
        System.out.print("Enter patient ID to update: ");
        String patientId = scanner.nextLine();
        Patient patient = null;

        for (int i = 0; i < patientCount; i++) {
            if (((Patient) patients[i]).getPatientId().equals(patientId)) {
                patient = (Patient) patients[i];
                break;
            }
        }

        if (patient == null) {
            System.out.println("Patient not found!");
            return;
        }

        System.out.print("Enter new name (current: " + patient.getName() + "): ");
        String newName = scanner.nextLine();
        System.out.print("Enter new age (current: " + patient.getAge() + "): ");
        int newAge = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        // Update patient details
        patients[Arrays.asList(patients).indexOf(patient)] = new Patient(
            newName.isEmpty() ? patient.getName() : newName,
            newAge == 0 ? patient.getAge() : newAge,
            patient.getPatientId()
        );
        System.out.println("Patient information updated successfully!");
    }

    @Override
    public void displayPatients() {
        if (patientCount == 0) {
            System.out.println("No patients available.");
            return;
        }
        for (int i = 0; i < patientCount; i++) {
            System.out.println(patients[i].getDetails());
        }
    }

    @Override
    public void displayDoctors() {
        if (doctorCount == 0) {
            System.out.println("No doctors available.");
            return;
        }
        for (int i = 0; i < doctorCount; i++) {
            System.out.println(doctors[i].getDetails());
        }
    }
}

public class HospitalManagementSystem {
    public static void main(String[] args) {
        Hospital hospital = new Hospital();
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\nHospital Management System");
            System.out.println("1. Add Patient");
            System.out.println("2. Add Doctor");
            System.out.println("3. Schedule Appointment");
            System.out.println("4. Display Patients");
            System.out.println("5. Display Doctors");
            System.out.println("6. View Appointments");
            System.out.println("7. Cancel Appointment");
            System.out.println("8. Update Patient Info");
            System.out.println("9. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    hospital.addPatient();
                    break;
                case 2:
                    hospital.addDoctor();
                    break;
                case 3:
                    hospital.scheduleAppointment();
                    break;
                case 4:
                    hospital.displayPatients();
                    break;
                case 5:
                    hospital.displayDoctors();
                    break;
                case 6:
                    hospital.viewAppointments();
                    break;
                case 7:
                    hospital.cancelAppointment();
                    break;
                case 8:
                    hospital.updatePatientInfo();
                    break;
                case 9:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        } while (choice != 9);

        scanner.close();
    }
}
