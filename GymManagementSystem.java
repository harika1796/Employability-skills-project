
import java.util.*;
import java.time.LocalDate;

// Parent class for all records
abstract class GymRecord {
}

// Member class
class Member extends GymRecord {
    int id, age;
    String name, phone, plan, trainer;
    double fee;
    LocalDate startDate, endDate;

    Member(int id, String name, int age, String phone) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.phone = phone;
        plan = "Not Selected";
        trainer = "Not Assigned";
    }
}

// Trainer class
class Trainer extends GymRecord {
    int id;
    String name, specialization;

    Trainer(int id, String name, String specialization) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
    }
}

// Attendance class
class Attendance extends GymRecord {
    int memberId;
    String memberName;
    LocalDate date;

    Attendance(int id, String name) {
        memberId = id;
        memberName = name;
        date = LocalDate.now();
    }
}

// Customized Exceptions
class MemberNotFoundException extends Exception {
    MemberNotFoundException() {
        super("Member not found!");
    }
}

class TrainerNotFoundException extends Exception {
    TrainerNotFoundException() {
        super("Trainer not found!");
    }
}

class DuplicateMemberException extends Exception {
    DuplicateMemberException() {
        super("Member already exists!");
    }
}

class InvalidAgeException extends Exception {
    InvalidAgeException() {
        super("Age must be at least 16!");
    }
}

// Main class
public class GymManagementSystem {

    static Scanner sc = new Scanner(System.in);

    // Only one data structure
    static ArrayList<GymRecord> records = new ArrayList<>();

    static int nextMemberId = 1;
    static int nextTrainerId = 1;

    // Read integer input
    static int readNumber() {
        return Integer.parseInt(sc.nextLine());
    }

    // Search for member
    static Member searchMember(int id)
            throws MemberNotFoundException {

        for (GymRecord r : records) {
            if (r instanceof Member) {
                Member m = (Member) r;

                if (m.id == id) {
                    return m;
                }
            }
        }

        throw new MemberNotFoundException();
    }

    // Search for trainer
    static Trainer searchTrainer(int id)
            throws TrainerNotFoundException {

        for (GymRecord r : records) {
            if (r instanceof Trainer) {
                Trainer t = (Trainer) r;

                if (t.id == id) {
                    return t;
                }
            }
        }

        throw new TrainerNotFoundException();
    }

    // Display membership plans
    static void viewPlans() {

        System.out.println("\n--- MEMBERSHIP PLANS ---");
        System.out.println("1. Monthly     - Rs.1000");
        System.out.println("2. Quarterly   - Rs.2500");
        System.out.println("3. Half Yearly - Rs.4500");
        System.out.println("4. Yearly      - Rs.8000");
    }

    // Get plan details
    static void setPlan(Member m, int choice) {

        switch (choice) {

            case 1:
                m.plan = "Monthly";
                m.fee = 1000;
                break;

            case 2:
                m.plan = "Quarterly";
                m.fee = 2500;
                break;

            case 3:
                m.plan = "Half Yearly";
                m.fee = 4500;
                break;

            case 4:
                m.plan = "Yearly";
                m.fee = 8000;
                break;
        }
    }

    // Get plan duration
    static int getDuration(int choice) {

        switch (choice) {
            case 1: return 30;
            case 2: return 90;
            case 3: return 180;
            case 4: return 365;
            default: return 0;
        }
    }

    // 1. Register member
    static void registerMember()
            throws DuplicateMemberException,
                   InvalidAgeException {

        System.out.print("Enter member name: ");
        String name = sc.nextLine();

        System.out.print("Enter age: ");
        int age = readNumber();

        if (age < 16) {
            throw new InvalidAgeException();
        }

        System.out.print("Enter phone number: ");
        String phone = sc.nextLine();

        for (GymRecord r : records) {
            if (r instanceof Member) {
                Member m = (Member) r;

                if (m.phone.equals(phone)) {
                    throw new DuplicateMemberException();
                }
            }
        }

        Member m = new Member(
            nextMemberId++, name, age, phone
        );

        records.add(m);

        System.out.println("Member registered successfully!");
        System.out.println("Your Member ID: " + m.id);
    }

    // 2. View all members
    static void viewMembers() {

        boolean found = false;

        for (GymRecord r : records) {

            if (r instanceof Member) {
                Member m = (Member) r;
                found = true;

                System.out.println("\n--------------------");
                System.out.println("Member ID: " + m.id);
                System.out.println("Name: " + m.name);
                System.out.println("Age: " + m.age);
                System.out.println("Phone: " + m.phone);
                System.out.println("Plan: " + m.plan);
                System.out.println("Fee: Rs." + m.fee);
                System.out.println("Trainer: " + m.trainer);
                System.out.println("Start Date: " + m.startDate);
                System.out.println("Expiry Date: " + m.endDate);
            }
        }

        if (!found) {
            System.out.println("No members registered!");
        }
    }

    // 3. View membership plans
    // Uses the viewPlans() method above

    // 4. Select membership plan
    static void selectPlan()
            throws MemberNotFoundException {

        System.out.print("Enter Member ID: ");
        int id = readNumber();

        Member m = searchMember(id);

        viewPlans();

        System.out.print("Choose plan: ");
        int choice = readNumber();

        if (choice < 1 || choice > 4) {
            System.out.println("Invalid plan!");
            return;
        }

        setPlan(m, choice);

        m.startDate = LocalDate.now();
        m.endDate = m.startDate.plusDays(
            getDuration(choice)
        );

        System.out.println("Plan selected successfully!");
        System.out.println("Expiry Date: " + m.endDate);
    }

    // 5. Renew membership
    static void renewMembership()
            throws MemberNotFoundException {

        System.out.print("Enter Member ID: ");
        int id = readNumber();

        Member m = searchMember(id);

        viewPlans();

        System.out.print("Choose renewal plan: ");
        int choice = readNumber();

        if (choice < 1 || choice > 4) {
            System.out.println("Invalid plan!");
            return;
        }

        LocalDate today = LocalDate.now();
        LocalDate renewalDate = today;

        if (m.endDate != null &&
                m.endDate.isAfter(today)) {
            renewalDate = m.endDate;
        }

        setPlan(m, choice);

        m.startDate = today;
        m.endDate = renewalDate.plusDays(
            getDuration(choice)
        );

        System.out.println("Membership renewed!");
        System.out.println("New Expiry Date: " + m.endDate);
    }

    // 6. Mark attendance
    static void markAttendance()
            throws MemberNotFoundException {

        System.out.print("Enter Member ID: ");
        int id = readNumber();

        Member m = searchMember(id);

        if (m.endDate == null ||
                m.endDate.isBefore(LocalDate.now())) {
            System.out.println(
                "Membership expired or not selected!"
            );
            return;
        }

        for (GymRecord r : records) {

            if (r instanceof Attendance) {
                Attendance a = (Attendance) r;

                if (a.memberId == id &&
                        a.date.equals(LocalDate.now())) {
                    System.out.println(
                        "Attendance already marked today!"
                    );
                    return;
                }
            }
        }

        records.add(new Attendance(m.id, m.name));

        System.out.println("Attendance marked successfully!");
    }

    // 7. View attendance
    static void viewAttendance() {

        boolean found = false;

        System.out.println("\n--- ATTENDANCE RECORDS ---");

        for (GymRecord r : records) {

            if (r instanceof Attendance) {
                Attendance a = (Attendance) r;
                found = true;

                System.out.println(
                    "Member ID: " + a.memberId
                );

                System.out.println(
                    "Name: " + a.memberName
                );

                System.out.println(
                    "Date: " + a.date
                );

                System.out.println("--------------------");
            }
        }

        if (!found) {
            System.out.println("No attendance records!");
        }
    }

    // 8. Add trainer
    static void addTrainer() {

        System.out.print("Enter trainer name: ");
        String name = sc.nextLine();

        System.out.print("Enter specialization: ");
        String specialization = sc.nextLine();

        Trainer t = new Trainer(
            nextTrainerId++, name, specialization
        );

        records.add(t);

        System.out.println("Trainer added successfully!");
        System.out.println("Trainer ID: " + t.id);
    }

    // 9. Allot trainer
    static void allotTrainer()
            throws MemberNotFoundException,
                   TrainerNotFoundException {

        System.out.print("Enter Member ID: ");
        int id = readNumber();

        Member m = searchMember(id);

        System.out.println("\n--- AVAILABLE TRAINERS ---");

        boolean found = false;

        for (GymRecord r : records) {

            if (r instanceof Trainer) {
                Trainer t = (Trainer) r;
                found = true;

                System.out.println(
                    t.id + ". " + t.name +
                    " - " + t.specialization
                );
            }
        }

        if (!found) {
            System.out.println("No trainers available!");
            return;
        }

        System.out.print("Enter Trainer ID: ");
        int trainerId = readNumber();

        Trainer t = searchTrainer(trainerId);

        m.trainer = t.name;

        System.out.println("Trainer allotted successfully!");
    }

    // 10. View trainers
    static void viewTrainers() {

        boolean found = false;

        for (GymRecord r : records) {

            if (r instanceof Trainer) {
                Trainer t = (Trainer) r;
                found = true;

                System.out.println("\nTrainer ID: " + t.id);
                System.out.println("Name: " + t.name);
                System.out.println(
                    "Specialization: " + t.specialization
                );
            }
        }

        if (!found) {
            System.out.println("No trainers available!");
        }
    }

    // 11. Main menu
    public static void main(String[] args) {

        while (true) {

            System.out.println("\n==============================");
            System.out.println(" GYM MEMBERSHIP MANAGEMENT");
            System.out.println("==============================");

            System.out.println("1. Register New Member");
            System.out.println("2. View All Members");
            System.out.println("3. View Membership Plans");
            System.out.println("4. Select Membership Plan");
            System.out.println("5. Renew Membership");
            System.out.println("6. Mark Attendance");
            System.out.println("7. View Attendance");
            System.out.println("8. Add Trainer");
            System.out.println("9. Allot Trainer");
            System.out.println("10. View Trainers");
            System.out.println("11. Exit");

            System.out.print("\nEnter your choice: ");

            try {

                int choice = readNumber();

                switch (choice) {

                    case 1:
                        registerMember();
                        break;

                    case 2:
                        viewMembers();
                        break;

                    case 3:
                        viewPlans();
                        break;

                    case 4:
                        selectPlan();
                        break;

                    case 5:
                        renewMembership();
                        break;

                    case 6:
                        markAttendance();
                        break;

                    case 7:
                        viewAttendance();
                        break;

                    case 8:
                        addTrainer();
                        break;

                    case 9:
                        allotTrainer();
                        break;

                    case 10:
                        viewTrainers();
                        break;

                    case 11:
                        System.out.println("Thank you!");
                        sc.close();
                        return;

                    default:
                        System.out.println("Invalid choice!");
                }

            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number!");

            } catch (MemberNotFoundException |
                     TrainerNotFoundException |
                     DuplicateMemberException |
                     InvalidAgeException e) {

                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
